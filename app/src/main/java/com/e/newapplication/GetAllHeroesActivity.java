package com.e.newapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Adapter.HeroesAdapter;
import ClientInterface.HeroesAPI;
import model.Heroes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetAllHeroesActivity extends AppCompatActivity {

private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.heroesRecycler);
        getAllHeroes();

    }

    private void getAllHeroes() {

        HeroesAPI heroesAPI = Url.getInstance().create(HeroesAPI.class);

        Call<List<Heroes>> listCall = heroesAPI.getHeroes();

        listCall.enqueue(new Callback<List<Heroes>>() {
            @Override
            public void onResponse(Call<List<Heroes>> call, Response<List<Heroes>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"error " +response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Heroes> heroesList= response.body();

                HeroesAdapter heroesAdapter = new HeroesAdapter(heroesList, GetAllHeroesActivity.this);
                recyclerView.setAdapter(heroesAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(GetAllHeroesActivity.this));
            }

            @Override
            public void onFailure(Call<List<Heroes>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error" +t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

}
