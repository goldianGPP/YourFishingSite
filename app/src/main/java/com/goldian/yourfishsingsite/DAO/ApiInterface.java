package com.goldian.yourfishsingsite.DAO;

import com.goldian.yourfishsingsite.Model.CommentModel;
import com.goldian.yourfishsingsite.Model.EventModel;
import com.goldian.yourfishsingsite.Model.ItemModel;
import com.goldian.yourfishsingsite.Model.LokasiModel;
import com.goldian.yourfishsingsite.Model.PenggunaModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {
    //USER---------------------------------------------------------------------------------------------------------//
    @POST("user/login")
    @FormUrlEncoded
    Call<PenggunaModel> loginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("user/register")
    @FormUrlEncoded
    Call<PenggunaModel> registerUser(
            @Field("email") String email,
            @Field("nama") String nama,
            @Field("username") String username,
            @Field("password") String password
    );

    @PUT("user")
    @FormUrlEncoded
    Call<Boolean> updateUser(
            @Field("id_pengguna") String id_pengguna,
            @Field("nama") String nama,
            @Field("username") String username
    );

    @POST("user/image")
    @Multipart
    Call<Boolean> updateUserImg(
            @Part("id_pengguna") RequestBody id_pengguna,
            @Part MultipartBody.Part image
    );

    @PUT("user/password")
    @FormUrlEncoded
    Call<Boolean> updateUserPassword(
            @Field("id_pengguna") String id_pengguna,
            @Field("password") String password
    );

    @GET("user")
    Call<List<PenggunaModel>> profileUser();

    //ITEM---------------------------------------------------------------------------------------------------------//
//    @POST("item/detail")
//    @FormUrlEncoded
//    Call<ItemModel> detailItem(@Field("id_item") String id_item);
//
    @POST("item")
    @Multipart
    Call<ItemModel> postItem(
            @Part("id_pengguna") RequestBody id_pengguna,
            @Part("nama") RequestBody nama,
            @Part("jenis") RequestBody jenis,
            @Part("deskripsi") RequestBody deskripsi,
            @Part("harga") RequestBody harga,
            @Part("web") RequestBody web,
            @Part MultipartBody.Part file
    );

    @PUT("item")
    @FormUrlEncoded
    Call<ItemModel> updateItem(
            @Field("id_item") String id_item,
            @Field("nama") String nama,
            @Field("jenis") String jenis,
            @Field("deskripsi") String deskripsi,
            @Field("harga") String harga,
            @Field("web") String web
    );

    @POST("item/image")
    @Multipart
    Call<ItemModel> updateItem(
            @Field("id_item") RequestBody id_item,
            @Part MultipartBody.Part file
    );

    @DELETE("item")
    @FormUrlEncoded
    Call<String> deleteItem(@Field("id_item") String id_item);

    @GET("item/{id_pengguna}")
    Call<List<ItemModel>> getItems(@Path("id_pengguna") String id_pengguna);

    @GET("item/rated/{id_pengguna}")
    Call<List<ItemModel>> getRatedItems(@Path("id_pengguna") String id_pengguna );

    //RECOMENDATION---------------------------------------------------------------------------------------------------------//
    @GET("CtrlRecomender/itembased/{id_pengguna}/{id_item}/{jenis}")
    Call<List<ItemModel>> getRecomendation(
            @Path("id_pengguna") String id_pengguna,
            @Path("id_item") String id_item,
            @Path("jenis") String jenis
    );

    @GET("CtrlRecomender/userbased/{id_pengguna}/{jenis}")
    Call<List<ItemModel>> getRecomendation(
            @Path("id_pengguna") String id_pengguna,
            @Path("jenis") String jenis
    );

    @GET("CtrlRecomender/userbasedsearch/{id_pengguna}/{search}")
    Call<List<ItemModel>> getSearchRecomendation(
            @Path("id_pengguna") String id_pengguna,
            @Path("search") String search
    );

    //EVENT---------------------------------------------------------------------------------------------------------//
    @POST("event")
    @Multipart
    Call<EventModel> postEvent(
            @Part("id_pengguna") RequestBody id_pengguna,
            @Part("title") RequestBody title,
            @Part("day") RequestBody day,
            @Part("month") RequestBody month,
            @Part("year") RequestBody year,
            @Part("link") RequestBody link,
            @Part("deskripsi") RequestBody deskripsi,
            @Part MultipartBody.Part file
    );

    @GET("event/{id_pengguna}")
    Call<List<EventModel>> getEvent(
            @Path("id_pengguna") String id_pengguna
    );

    @PUT("event")
    @FormUrlEncoded
    Call<EventModel> updateEvent(
            @Field("id_event") String id_event,
            @Field("title") String title,
            @Field("deskripsi") String deskripsi,
            @Field("link") String link
    );

    @POST("event/image")
    @Multipart
    Call<EventModel> updateEvent(
            @Field("id_event") RequestBody id_event,
            @Part MultipartBody.Part file
    );

    @GET("event")
    Call<List<EventModel>> getEvent();

    //LOKASI---------------------------------------------------------------------------------------------------------//
    @POST("lokasi")
    @Multipart
    Call<LokasiModel> postLokasi(
            @Part("id_pengguna") RequestBody id_pengguna,
            @Part("nama") RequestBody nama,
            @Part("deskripsi") RequestBody deskripsi,
            @Part("status") RequestBody status,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part file
    );

    @PUT("event")
    @FormUrlEncoded
    Call<LokasiModel> updateLokasi(
            @Field("id_lokasi") String id_lokasi,
            @Field("nama") String nama,
            @Field("ikan") String ikan,
            @Field("deskripsi") String deskripsi
    );

    @POST("event/image")
    @Multipart
    Call<LokasiModel> updateLokasi(
            @Field("id_lokasi") RequestBody id_lokasi,
            @Part MultipartBody.Part file
    );

    @GET("lokasi/{id_pengguna}")
    Call<List<LokasiModel>> getLokasi(
            @Path("id_pengguna") String id_pengguna
    );

    @GET("lokasi")
    Call<List<LokasiModel>> getLokasi();

    //COMMENT---------------------------------------------------------------------------------------------------------//
    @POST("comment")
    @FormUrlEncoded
    Call<CommentModel> postComment(
            @Field("id_pengguna") String id_pengguna,
            @Field("ids") String ids,
            @Field("comment") String comment
    );

    @GET("comment/{ids}")
    Call<List<CommentModel>> getComments(
            @Path("ids") String ids
    );

    @POST("comment/reply")
    @FormUrlEncoded
    Call<CommentModel> postReply(
            @Field("id_comment") String id_comment,
            @Field("id_pengguna") String id_pengguna,
            @Field("reply_to") String reply_to,
            @Field("reply") String reply
    );

    @GET("comment/reply/{id_comment}")
    Call<List<CommentModel>> getReply(
            @Path("id_comment") String id_comment
    );
}

