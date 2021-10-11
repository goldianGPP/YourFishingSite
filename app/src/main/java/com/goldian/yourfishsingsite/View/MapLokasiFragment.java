package com.goldian.yourfishsingsite.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.goldian.yourfishsingsite.Controller.LokasiController;
import com.goldian.yourfishsingsite.Controller.MapBoxController;
import com.goldian.yourfishsingsite.Model.LokasiModel;
import com.goldian.yourfishsingsite.Model.PreferencesModel;
import com.goldian.yourfishsingsite.R;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
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
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class MapLokasiFragment extends Fragment implements PermissionsListener, OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private SymbolManager symbolManager;

    //route
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private Point origin;
    private Point destination;
    private Style style;

    //private Symbol symbol;
    private View v;
    private static final String MAKI_ICON_FISH_PIN = "fish-pin-1";

    FloatingActionButton btnAdd, btnDireksi, btnBersih;
    FloatingActionsMenu floatingManu;
    TextView txtJarak;
    CardView card;
    PreferencesModel preferences;
    DrawerLayout drawerLayout;
    List<LokasiModel> lokasiModels;
    MapBoxController boxController;

    MapboxNavigation navigation;
    DecimalFormat df = new DecimalFormat("#,##");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(getContext(), getString(R.string.access_token));
        v = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize the mapboxMap view
        mapView = v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        navigation = new MapboxNavigation(getContext(),getString(R.string.access_token));

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
        btnDireksi = v.findViewById(R.id.btnDireksi);
        btnBersih = v.findViewById(R.id.btnBersih);
        floatingManu = v.findViewById(R.id.floatingManu);
        floatingManu.expand();
        txtJarak = v.findViewById(R.id.txtJarak);
        drawerLayout = v.findViewById(R.id.drawerLayout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        card = v.findViewById(R.id.card);

        this.style = style;
        style.addImage(
                MAKI_ICON_FISH_PIN,
                BitmapFactory.decodeResource(this.getResources(), R.drawable.img_fish_pin)
        );

        boxController = new MapBoxController(this);
        ImageView hoveringMarker = new ImageView(getContext());
        hoveringMarker.setImageResource(R.drawable.img_fish_pin);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        hoveringMarker.setLayoutParams(params);
        mapView.addView(hoveringMarker);

        preferences = new PreferencesModel(getContext(),getResources().getString(R.string.login));

    }

    private void initSource() {
        style.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[] {
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        style.addSource(iconGeoJsonSource);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initLayers() {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

        // Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#000000"))
        );
        style.addLayer(routeLayer);
    }

    // setting the listener
    private void setListener(){
        btnAdd.setOnClickListener(onClick);
        btnAdd.setOnClickListener(onClick);
        btnDireksi.setOnClickListener(onClick);
        btnBersih.setOnClickListener(onClick);
        symbolManager.addClickListener(symbolClick);
        drawerLayout.addDrawerListener(drawer);
    }

    //setting symbol like pin for location
    public void setSymbol(LokasiModel lokasiModel, int i) {
        String name = lokasiModel.getNama();
        if (name.length() > 10)
            name = name.substring(0,10) + "...";

        symbolManager.create(new SymbolOptions()
                .withTextField(name)
                .withTextColor("#000000")
                .withTextAnchor(String.valueOf(i))
                .withIconOffset(new Float[] {0.0f, 40.0f})
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

    private void clearLayers(){
        if (style.getSource(ROUTE_SOURCE_ID) != null){
            style.removeLayer(ROUTE_LAYER_ID);
            style.removeSource(ICON_SOURCE_ID);
            style.removeSource(ROUTE_SOURCE_ID);
        }
        btnBersih.setVisibility(View.GONE);
        btnDireksi.setVisibility(View.GONE);
    }

    //request or map data
    private void request(){
        new LokasiController(getContext(),this)
                .getLokasi();
    }

    //request for map direaction
    private void requestDirection(){
        boxController.getDirection(origin, destination, getString(R.string.access_token));
    }

    //request for map route
    @SuppressLint({"RtlHardcoded", "LogNotTimber"})
    public void requestRoute(Float Long, Float lat){
        drawerLayout.closeDrawer(Gravity.LEFT);

        LatLng current = new LatLng();
        current.setLongitude(mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude());
        current.setLatitude(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude());
        origin = Point.fromLngLat(current.getLongitude(),current.getLatitude());
        destination = Point.fromLngLat(Long, lat);

        clearLayers();
        initSource();
        initLayers();
        boxController.getRoute(origin, destination, getString(R.string.access_token));
    }

    //result for map data
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

    //result for get streetname
    public void result(LatLng current, String jalan){
        Intent intent = new Intent(getContext(), TambahLokasiActivity.class);

        intent.putExtra("alamat", jalan);
        intent.putExtra("latitude", Double.toString(current.getLatitude()));
        intent.putExtra("longitude", Double.toString(current.getLongitude()));
        startActivityForResult(intent,1);
    }

    //result for map route
    @SuppressLint("SetTextI18n")
    public void result(DirectionsResponse currentRoute){
        double distance = currentRoute.routes().get(0).distance();
        if (distance > 1000)
            txtJarak.setText("jarak : " + df.format((distance/1000)) + "km");
        else
            txtJarak.setText("jarak : " + df.format(distance) + "m");
        card.setVisibility(View.VISIBLE);

        if (mapboxMap != null) {
            mapboxMap.getStyle(style -> {
                GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);
                if (source != null) {
                    source.setGeoJson(LineString.fromPolyline(Objects.requireNonNull(currentRoute.routes().get(0).geometry()), PRECISION_6));
                }
            });
        }

        btnBersih.setVisibility(View.VISIBLE);
        btnDireksi.setVisibility(View.VISIBLE);
    }

    //Listener
    //-----------------
    @SuppressLint("RtlHardcoded")
    View.OnClickListener onClick = view -> {

        LatLng current = mapboxMap.getCameraPosition().target;
        if (view == btnAdd){
            boxController.getJalan(
                    current,
                    getString(R.string.access_token)
            );
        }
        else if (view == btnDireksi){
            requestDirection();
        }
        else if (view == btnBersih){
            clearLayers();
        }
    };

    DrawerLayout.SimpleDrawerListener drawer = new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    };

    //symbol onclick
    @SuppressLint("RtlHardcoded")
    OnSymbolClickListener symbolClick = symbol -> {

        drawerLayout.openDrawer(Gravity.LEFT);

        Bundle bundle = new Bundle();
        Fragment fragment = new DetailLokasiFragment();
        LokasiModel lokasiModel = lokasiModels.get(Integer.parseInt(symbol.getTextAnchor()));
        bundle.putString("latitude", Double.toString(lokasiModel.getLatitude()));
        bundle.putString("longitude", Double.toString(lokasiModel.getLongitude()));
        bundle.putString("nama", lokasiModel.getNama());
        bundle.putString("ikan", lokasiModel.getIkan());
        bundle.putString("alamat", lokasiModel.getAlamat());
        bundle.putString("deskripsi", lokasiModel.getDeskripsi());
        bundle.putString("id_lokasi", lokasiModel.getId_lokasi());
        bundle.putString("img", lokasiModel.getImg());
        bundle.putString("key", lokasiModel.getImg_key());
        fragment.setArguments(bundle);
        setFragment(fragment);
        return true;
    };




















    //SOMETHING FROM MAP / MAPBOX

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
        navigation.onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        navigation.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
        navigation.onDestroy();
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
