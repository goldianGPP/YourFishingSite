package com.goldian.yourfishsingsite.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

public class ImageModel {

    private Context context;

    public ImageModel() {}

    public ImageModel(Context context) {
        this.context = context;
    }

    public String getBase_url() {
//        main localhost
//        private final String base_url = "http://192.168.100.27:8082/CodeIgniter/fishingsite/images/";

//        phone
        return "http://192.168.43.146:8082/CodeIgniter/fishingsite/images/";
    }

    public RequestBody requestBody(String nama){
        return RequestBody.create( MultipartBody.FORM, nama);
    }

    public MultipartBody.Part multipartBody(Uri uri, String nama){
        String fileLoc = getRealPathFromUri(context, uri);
        File file = new File(fileLoc);
        RequestBody requestFile = RequestBody.create( MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData(nama, file.getName(), requestFile);
    }

    public MultipartBody.Part multipartBody(ContentResolver contentResolver, Bitmap bitmap, String nama){
        String fileLoc = getRealPathFromUri(contentResolver, getImageUri(context, bitmap));
        File file = new File(fileLoc);
        RequestBody requestFile = RequestBody.create( MediaType.parse("multipart/form-data"), file);

        return MultipartBody.Part.createFormData(nama, file.getName(), requestFile);
    }

    public Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "image", null);
        return Uri.parse(path);
    }

    public String getRealPathFromUri(ContentResolver contentResolver, Uri uri) {
        String path = "";
        if (contentResolver != null) {
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = 0;
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
            else
                return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
