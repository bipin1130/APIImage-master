package ClientInterface;

import java.util.List;

import model.Heroes;
import model.ImageResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface HeroesAPI {

    @GET("heroes")
    Call<List<Heroes>> getHeroes();


    @FormUrlEncoded
    @POST("heroes")
    Call<Void> addHero(@Field("name") String name, @Field("desc") String desc, @Field("image") String imageName);

    @Multipart
    @POST("upload")
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part img);

    @FormUrlEncoded
    @POST("users/login")
    Call<LoginSignupResponse> checkUser(@Field("username") String username, @Field("password") String password);



}
