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

    @DELETE("user")
    @FormUrlEncoded
    Call<Boolean> deleteUser(@Field("id_pengguna") String id_pengguna);

    @GET("user")
    Call<List<PenggunaModel>> profileUser();

    //ITEM---------------------------------------------------------------------------------------------------------//
//    @POST("item/detail")
//    @FormUrlEncoded
//    Call<ItemModel> detailItem(@Field("id_item") String id_item);
//
    @GET("item/{id_pengguna}")
    Call<List<ItemModel>> getItems(@Path("id_pengguna") String id_pengguna);

    @GET("item/rated/{id_pengguna}")
    Call<List<ItemModel>> getRatedItems(@Path("id_pengguna") String id_pengguna );

    @POST("item")
    @Multipart
    Call<Boolean> postItem(
            @Part("id_pengguna") RequestBody id_pengguna,
            @Part("nama") RequestBody nama,
            @Part("jenis") RequestBody jenis,
            @Part("deskripsi") RequestBody deskripsi,
            @Part("harga") RequestBody harga,
            @Part("web") RequestBody web,
            @Part MultipartBody.Part file
    );

    @POST("item/rating")
    @FormUrlEncoded
    Call<Boolean> setRating(
            @Field("id_item") String id_item,
            @Field("id_pengguna") String id_pengguna,
            @Field("rating") String rating
    );

    @POST("item/image")
    @Multipart
    Call<Boolean> updateItem(
            @Part("id_item") RequestBody id_item,
            @Part MultipartBody.Part file
    );

    @PUT("item")
    @FormUrlEncoded
    Call<Boolean> updateItem(
            @Field("id_item") String id_item,
            @Field("nama") String nama,
            @Field("jenis") String jenis,
            @Field("deskripsi") String deskripsi,
            @Field("harga") String harga,
            @Field("web") String web
    );

    @DELETE("item")
    @FormUrlEncoded
    Call<Boolean> deleteItem(@Field("id_item") String id_item);

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
    Call<Boolean> postEvent(
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
    Call<Boolean> updateEvent(
            @Field("id_event") String id_event,
            @Field("day") String day,
            @Field("month") String month,
            @Field("year") String year,
            @Field("title") String title,
            @Field("deskripsi") String deskripsi,
            @Field("link") String link
    );

    @POST("event/image")
    @Multipart
    Call<Boolean> updateEvent(
            @Part("id_event") RequestBody id_event,
            @Part MultipartBody.Part file
    );

    @DELETE("event")
    @FormUrlEncoded
    Call<Boolean> deleteEvent(@Field("id_event") String id_event);

    @GET("event")
    Call<List<EventModel>> getEvent();

    //LOKASI---------------------------------------------------------------------------------------------------------//
    @POST("lokasi")
    @Multipart
    Call<Boolean> postLokasi(
            @Part("id_pengguna") RequestBody id_pengguna,
            @Part("nama") RequestBody nama,
            @Part("deskripsi") RequestBody deskripsi,
            @Part("status") RequestBody status,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part file
    );

    @PUT("lokasi")
    @FormUrlEncoded
    Call<Boolean> updateLokasi(
            @Field("id_lokasi") String id_lokasi,
            @Field("nama") String nama,
            @Field("ikan") String ikan,
            @Field("deskripsi") String deskripsi
    );

    @POST("lokasi/image")
    @Multipart
    Call<Boolean> updateLokasi(
            @Part("id_lokasi") RequestBody id_lokasi,
            @Part MultipartBody.Part file
    );

    @DELETE("lokasi")
    @FormUrlEncoded
    Call<Boolean> deleteLokasi(@Field("id_lokasi") String id_lokasi);

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

    @DELETE("comment")
    @FormUrlEncoded
    Call<Boolean> deleteComment(@Field("id_comment") String id_comment);

    @DELETE("comment/reply")
    @FormUrlEncoded
    Call<Boolean> deleteReply(@Field("id_reply") String id_reply);

    @GET("comment/reply/{id_comment}")
    Call<List<CommentModel>> getReply(
            @Path("id_comment") String id_comment
    );
}

