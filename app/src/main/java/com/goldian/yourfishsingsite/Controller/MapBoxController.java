package com.goldian.yourfishsingsite.Controller;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.goldian.yourfishsingsite.R;
import com.goldian.yourfishsingsite.View.FragmentActivity;
import com.goldian.yourfishsingsite.View.MapLokasiFragment;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MapBoxController {

    MapLokasiFragment fragment;

    public MapBoxController(MapLokasiFragment fragment) {
        this.fragment = fragment;
    }

    private void result(DirectionsResponse currentRoute){
        fragment.result(currentRoute);
    }
    private void result(LatLng current, String jalan){
        fragment.result(current, jalan);
    }

    //get route
    @SuppressLint({"BinaryOperationInTimber","SetTextI18n"})
    public void getRoute(Point origin, Point destination, String token) {
        MapboxDirections.builder()
            .origin(origin)
            .destination(destination)
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(DirectionsCriteria.PROFILE_DRIVING)
            .accessToken(token)
            .build()
            .enqueueCall(new Callback<DirectionsResponse>() {
                @Override
                public void onResponse(@NonNull Call<DirectionsResponse> call, @NonNull Response<DirectionsResponse> response) {

                    if (response.body().routes().size() < 1) {
                        TastyToast.makeText(fragment.getContext(), "rute tidak ditemukan", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                        return;
                    }
                    result(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<DirectionsResponse> call, @NonNull Throwable throwable) {
                    TastyToast.makeText(fragment.getContext(), fragment.getContext().getResources().getString(R.string.error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            });
    }

    //get diretion
    public void getDirection(Point origin, Point destination, String token){
        NavigationRoute.builder(fragment.getContext())
                .accessToken(token)
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<DirectionsResponse> call, @NonNull Response<DirectionsResponse> response) {

                        if (response.body() != null){
                            DirectionsRoute route = Objects.requireNonNull(response.body()).routes().get(0);

                            NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                    .directionsRoute(route)
                                    .shouldSimulateRoute(true)
                                    .build();

                            NavigationLauncher.startNavigation((FragmentActivity) fragment.getContext(), options);
                        }


                    }

                    @Override
                    public void onFailure(@NonNull Call<DirectionsResponse> call, @NonNull Throwable t) {

                    }
                });
    }



    public void getJalan(LatLng current, String token){
        MapboxGeocoding.builder()
                .accessToken(token)
                .query(Point.fromLngLat(current.getLongitude(), current.getLatitude()))
                .geocodingTypes(
                        GeocodingCriteria.TYPE_NEIGHBORHOOD
                )
                .build()
                .enqueueCall(new Callback<GeocodingResponse>() {
                    @Override
                    public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                        if (response.body().features().size() > 0){
                            CarmenFeature feature = response.body().features().get(0);
                            result(current, feature.placeName());
                        }
                        else
                            TastyToast.makeText(fragment.getContext(), "rute tidak ditemukan", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    }

                    @Override
                    public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                        TastyToast.makeText(fragment.getContext(), fragment.getContext().getResources().getString(R.string.error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }
                });
    }
}
