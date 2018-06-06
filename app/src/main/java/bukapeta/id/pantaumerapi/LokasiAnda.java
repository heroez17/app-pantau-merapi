package bukapeta.id.pantaumerapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class LokasiAnda extends AppCompatActivity {

    private MapView mapView;
    private Location lokasiAnda;
    private Marker marker;
    private MapboxMap map;
    private TextView jarak,koordinat;
    private Boolean stat_style=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_key));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        setContentView(R.layout.activity_lokasi_anda);


        IconFactory iconFactory = IconFactory.getInstance(LokasiAnda.this);
        final Icon icon = iconFactory.fromResource(R.drawable.fire);

        koordinat=findViewById(R.id.textView16);
        jarak=findViewById(R.id.textView15);

        final ImageView btn_style=findViewById(R.id.btn_style);
        btn_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stat_style){
                    stat_style=false;
                    map.setStyle(Style.SATELLITE_STREETS);
                    btn_style.post(new Runnable() {
                        @Override
                        public void run() {
                            btn_style.setImageResource(R.drawable.ic_street_icon);
                        }
                    });
                }else{
                    stat_style=true;
                    map.setStyle(Style.MAPBOX_STREETS);
                    btn_style.post(new Runnable() {
                        @Override
                        public void run() {
                            btn_style.setImageResource(R.drawable.ic_satelit_icon);
                        }
                    });
                }
            }
        });

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.SATELLITE_STREETS);
                map=mapboxMap;
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-7.541292151475886,110.44619982622703),8));
                mapboxMap.addMarker(new MarkerOptions().position(new LatLng(-7.541292151475886,110.44619982622703)).setTitle("Puncak Merapi").icon(icon));
                startService(new Intent(LokasiAnda.this,FusedLocationServices.class));
                // Customize map with markers, polylines, etc.
            }
        });
    }


    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("lokasi_lihatdata"));

    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        stopService(new Intent(LokasiAnda.this,FusedLocationServices.class));
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        private Polyline polyline;
        private Boolean statInfo=true;
        @Override
        public void onReceive(Context context, Intent intent) {
            lokasiAnda=intent.getParcelableExtra("datalokasi");
            if (lokasiAnda!=null){
                if (marker!=null){
                    if (lokasiAnda.getAccuracy() < 20) {
                        marker.setPosition(new LatLng(lokasiAnda.getLatitude(), lokasiAnda.getLongitude()));
                        List<LatLng> daftarlatlng=new ArrayList<>();
                        daftarlatlng.add(new LatLng(-7.541292151475886,110.44619982622703));
                        daftarlatlng.add(new LatLng(lokasiAnda.getLatitude(),lokasiAnda.getLongitude()));
                        polyline.setPoints(daftarlatlng);

                        if (statInfo) {
                            koordinat.setText("L: " + lokasiAnda.getLatitude() + ", B: " + lokasiAnda.getLongitude());

                            LatLng latmerapi = new LatLng(-7.541292151475886,110.44619982622703);
                            Location loc1 = new Location("");
                            loc1.setLatitude(latmerapi.getLatitude());
                            loc1.setLongitude(latmerapi.getLongitude());

                            Location loc2 = new Location("");
                            loc2.setLatitude(lokasiAnda.getLatitude());
                            loc2.setLongitude(lokasiAnda.getLongitude());

                            float distanceInMeters = loc1.distanceTo(loc2);
                            jarak.setText("Jarak "+String.valueOf((int) distanceInMeters / 1000) + " KM");
                            statInfo=false;
                        }

                    }
                }else{
                    marker=map.addMarker(new MarkerOptions().position(new LatLng(lokasiAnda.getLatitude(),lokasiAnda.getLongitude())));
                    marker.setTitle("lokasi anda");

                    List<LatLng> daftarlatlng=new ArrayList<>();
                    daftarlatlng.add(new LatLng(-7.541292151475886,110.44619982622703));
                    daftarlatlng.add(new LatLng(lokasiAnda.getLatitude(),lokasiAnda.getLongitude()));
                   polyline= map.addPolyline(new PolylineOptions().addAll(daftarlatlng).color(Color.BLUE).width(5.0f));
                }

            }}
        };
}
