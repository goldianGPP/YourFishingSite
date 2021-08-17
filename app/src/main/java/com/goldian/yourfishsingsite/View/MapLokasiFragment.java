package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.goldian.yourfishsingsite.Controller.LokasiController;
import com.goldian.yourfishsingsite.Model.LokasiModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.R;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MapLokasiFragment extends Fragment implements PermissionsListener, OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private SymbolManager symbolManager;
//    private Symbol symbol;
    private View v;
    private static final String MAKI_ICON_FISH_PIN = "fish-pin-1";

    Button btnAdd;
    PreferencesModel preferences;
    DrawerLayout drawerLayout;
    List<LokasiModel> lokasiModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(getContext(), getString(R.string.access_token));
        v = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize the mapboxMap view
        mapView = v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return  v;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setMinZoomPreference(14);
        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
            symbolManager = new SymbolManager(mapView, mapboxMap, style);

            symbolManager.setIconAllowOverlap(true);
            symbolManager.setTextAllowOverlap(true);

            enableLocationPlugin(style);
            init(style);
            setListener();
            request();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            symbolManager.deleteAll();
            request();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //initialize variable
    private void init(@NonNull Style style){
        btnAdd = v.findViewById(R.id.btnAdd);
        drawerLayout = v.findViewById(R.id.drawerLayout);
        style.addImage(
                MAKI_ICON_FISH_PIN,
                BitmapFactory.decodeResource(this.getResources(), R.drawable.img_fish_pin)
        );

        ImageView hoveringMarker = new ImageView(getContext());
        hoveringMarker.setImageResource(R.drawable.img_fish_pin);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        hoveringMarker.setLayoutParams(params);
        mapView.addView(hoveringMarker);

        preferences = new PreferencesModel(getContext(),getResources().getString(R.string.login));
    }

    // setting the listener
    private void setListener(){
        btnAdd.setOnClickListener(onClick);
        btnAdd.setOnClickListener(onClick);
        symbolManager.addClickListener(symbolClick);
    }

    //setting symbol like pin for location
    public void setSymbol(LokasiModel lokasiModel, int i) {
        symbolManager.create(new SymbolOptions()
            .withTextField(lokasiModel.getNama())
            .withTextColor("#ffffff")
            .withTextAnchor(String.valueOf(i))
            .withLatLng(new LatLng(lokasiModel.getLatitude(), lokasiModel.getLongitude()))
            .withIconImage(MAKI_ICON_FISH_PIN)
            .withIconSize(1.0f)
            .withDraggable(false)
        );
    }

    private void setFragment(Fragment fragment){
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_map, fragment)
                .commit();
    }

    private void request(){
        new LokasiController(getContext(),this)
                .getLokasi();
    }

    public void result(List<LokasiModel> lokasiModels){
        if (lokasiModels!=null) {
            this.lokasiModels = lokasiModels;
            int i = 0;
            for (LokasiModel lokasiModel : this.lokasiModels) {
                setSymbol(lokasiModel, i);
                i++;
            }
        }
    }

    //Listener
    //-----------------
    @SuppressLint("RtlHardcoded")
    View.OnClickListener onClick = view -> {
        LatLng current = mapboxMap.getCameraPosition().target;
        if (view == btnAdd){
            Intent intent = new Intent(getContext(), TambahLokasiActivity.class);
            intent.putExtra("latitude", Double.toString(current.getLatitude()));
            intent.putExtra("longitude", Double.toString(current.getLongitude()));
            startActivityForResult(intent,1);
        }
    };

    @SuppressLint("RtlHardcoded")
    OnSymbolClickListener symbolClick = symbol -> {
        LatLng current = mapboxMap.getCameraPosition().target;
        drawerLayout.openDrawer(Gravity.LEFT);
        Bundle bundle = new Bundle();
        Fragment fragment = new DetailLokasiFragment();
        LokasiModel lokasiModel = lokasiModels.get(Integer.parseInt(symbol.getTextAnchor()));
        bundle.putString("latitude", Double.toString(current.getLatitude()));
        bundle.putString("longitude", Double.toString(current.getLongitude()));
        bundle.putString("nama", lokasiModel.getNama());
        bundle.putString("ikan", lokasiModel.getIkan());
        bundle.putString("deskripsi", lokasiModel.getDeskripsi());
        bundle.putString("id_lokasi", lokasiModel.getId_lokasi());
        bundle.putString("img", lokasiModel.getImg());
        bundle.putString("key", lokasiModel.getImg_key());
        fragment.setArguments(bundle);
        setFragment(fragment);
        return true;
    };


    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void onPermissionResult(boolean granted) {
        if (granted && mapboxMap != null) {
            Style style = mapboxMap.getStyle();
            if (style != null) {
                enableLocationPlugin(style);
            }
        } else {
            Toast.makeText(getContext(), "permission not granted", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

            // Get an instance of the component. Adding in LocationComponentOptions is also an optional
            // parameter
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(
                    getContext(), loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), "permission granted", Toast.LENGTH_LONG).show();
    }
}
