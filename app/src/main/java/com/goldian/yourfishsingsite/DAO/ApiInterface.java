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
import retrofit2.http.Body;
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
    Call<PenggunaModel> loginUser(
            @Body PenggunaModel penggunaModel
    );

    @POST("user/register")
    Call<Boolean> registerUser(
            @Body PenggunaModel penggunaModel
    );

    @PUT("user")
    Call<Boolean> updateUser(
            @Body PenggunaModel penggunaModel
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

    @DELETE("user/{id_pengguna}")
    @FormUrlEncoded
    Call<Boolean> deleteUser(@Path("id_pengguna") String id_pengguna);

    //ITEM---------------------------------------------------------------------------------------------------------//
//    @POST("item/detail")
//    @FormUrlEncoded
//    Call<ItemModel> detailItem(@Field("id_item") String id_item);
//
    @GET("item/{id_pengguna}")
    Call<List<ItemModel>> getMyItems(@Path("id_pengguna") String id_pengguna);

    @GET("item/items/{jenis}")
    Call<List<ItemModel>> getItems(@Path("jenis") String jenis);

    @GET("item/search/{jenis}")
    Call<List<ItemModel>> getSearchItems(@Path("jenis") String jenis);

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
    Call<Boolean> setRating(
            @Body ItemModel itemModel
    );

    @POST("item/image")
    @Multipart
    Call<Boolean> updateItem(
            @Part("id_item") RequestBody id_item,
            @Part("image") RequestBody image,
            @Part MultipartBody.Part file
    );

    @PUT("item")
    Call<Boolean> updateItem(
            @Body ItemModel itemModel
    );

    @DELETE("item/{id_item}")
    @FormUrlEncoded
    Call<Boolean> deleteItem(@Path("id_item") String id_item);

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
            @Part("tanggal") RequestBody tanggal,
            @Part("link") RequestBody link,
            @Part("deskripsi") RequestBody deskripsi,
            @Part MultipartBody.Part file
    );

    @GET("event/{id_pengguna}")
    Call<List<EventModel>> getEvent(
            @Path("id_pengguna") String id_pengguna
    );

    @PUT("event")
    Call<Boolean> updateEvent(
            @Body EventModel eventModel
    );

    @POST("event/image")
    @Multipart
    Call<Boolean> updateEvent(
            @Part("id_event") RequestBody id_event,
            @Part("id_pengguna") RequestBody id_pengguna,
            @Part("image") RequestBody image,
            @Part MultipartBody.Part file
    );

    @POST("event/delete")
    @FormUrlEncoded
    Call<Boolean> deleteEvent(@Field("id_event") String id_event, @Field("image") String image);

    @GET("event")
    Call<List<EventModel>> getEvent();

    //LOKASI---------------------------------------------------------------------------------------------------------//
    @POST("lokasi")
    @Multipart
    Call<Boolean> postLokasi(
            @Part("id_pengguna") RequestBody id_pengguna,
            @Part("nama") RequestBody nama,
            @Part("ikan") RequestBody ikan,
            @Part("alamat") RequestBody alamat,
            @Part("deskripsi") RequestBody deskripsi,
            @Part("status") RequestBody status,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part file
    );

    @PUT("lokasi")
    Call<Boolean> updateLokasi(
            @Body LokasiModel lokasiModel
    );

    @POST("lokasi/image")
    @Multipart
    Call<Boolean> updateLokasi(
            @Part("id_lokasi") RequestBody id_lokasi,
            @Part("id_pengguna") RequestBody id_pengguna,
            @Part("image") RequestBody image,
            @Part MultipartBody.Part file
    );

    @POST("lokasi/delete")
    @FormUrlEncoded
    Call<Boolean> deleteLokasi(@Field("id_lokasi") String id_lokasi, @Field("image") String image);

    @GET("lokasi/{id_pengguna}")
    Call<List<LokasiModel>> getLokasi(
            @Path("id_pengguna") String id_pengguna
    );

    @GET("lokasi")
    Call<List<LokasiModel>> getLokasi();

    //COMMENT---------------------------------------------------------------------------------------------------------//
    @POST("comment")
    Call<CommentModel> postComment(
            @Body CommentModel commentModel
    );

    @GET("comment/{ids}")
    Call<List<CommentModel>> getComments(
            @Path("ids") String ids
    );

    @POST("comment/reply")
    Call<CommentModel> postReply(
            @Body CommentModel commentModel
    );

    @DELETE("comment/{id_comment}")
    Call<Boolean> deleteComment(@Path("id_comment") String id_comment);

    @DELETE("comment/reply{id_reply}")
    Call<Boolean> deleteReply(@Path("id_reply") String id_reply);

    @GET("comment/reply/{id_comment}")
    Call<List<CommentModel>> getReply(
            @Path("id_comment") String id_comment
    );
}

