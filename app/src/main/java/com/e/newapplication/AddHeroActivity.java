package com.e.newapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ClientInterface.HeroesAPI;
import model.Heroes;
import model.ImageResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddHeroActivity extends AppCompatActivity {
    
    private EditText etName,etDesc;
    private ImageView imgHero;
    private Button btnSave;
    String imgPath;
    String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hero);
        
        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);
        imgHero = findViewById(R.id.imgHero);
        btnSave = findViewById(R.id.btnSave);


//        loadFromUrl();
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveImageOnly();
                AddHero();
            }
        });

        imgHero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Browse();
            }
        });
    }

    private void Browse() {
        Intent browseIntent = new Intent(Intent.ACTION_PICK);
        browseIntent.setType("image/*");
        startActivityForResult(browseIntent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode ==RESULT_OK){
            if (data == null) {
                Toast.makeText(this,"Please Select an Image",Toast.LENGTH_SHORT).show();
            }
        }
        Uri uri = data.getData();
        imgPath = getRealDataFromUri(uri);
        previewImage(imgPath);
    }

    private void previewImage(String imgPath) {
        File imgFile = new File(imgPath);
        if (imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgHero.setImageBitmap(bitmap);
        }
    }

    private String getRealDataFromUri(Uri uri) {
        String []projection ={MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(getApplicationContext(),uri,projection,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }

        private void StrictMode()
    {
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    private void SaveImageOnly(){
        File file = new File(imgPath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", file.getName(), requestBody);

        HeroesAPI heroesAPI = Url.getInstance().create(HeroesAPI.class);
        Call<ImageResponse> responseCall = heroesAPI.uploadImage(body);

        StrictMode();

        try {
            Response<ImageResponse> imageResponseResponse = responseCall.execute();
            imageName = imageResponseResponse.body().getFilename();

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

//    private void loadFromUrl() {
//        StrictMode();
//        try
//        {
//            String imgUrl="https://pmcvariety.files.wordpress.com/2019/03/spider-man-shattered-dimensions.png?w=805";
//            URL url = new URL(imgUrl);
//            imgHero.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            Toast.makeText(AddHeroActivity.this, "Error",Toast.LENGTH_SHORT).show();
//        }
//
//
//    }

    private void AddHero() {

        String name = etName.getText().toString();
        String desc = etDesc.getText().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HeroesAPI heroesAPI = retrofit.create(HeroesAPI.class);

        Call<Void> voidCall = heroesAPI.addHero(name,desc,imageName);

        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(AddHeroActivity.this,"Code "+response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(AddHeroActivity.this, "Successfully Added",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                Toast.makeText(AddHeroActivity.this, "Error "+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}
