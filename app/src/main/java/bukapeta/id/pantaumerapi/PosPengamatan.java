package bukapeta.id.pantaumerapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

public class PosPengamatan extends AppCompatActivity {

    private MapView mapView;
    private Location lokasiAnda;
    private Marker marker;
    private MapboxMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_key));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        setContentView(R.layout.activity_pos_pengamatan);

        IconFactory iconFactory = IconFactory.getInstance(PosPengamatan.this);
        final Icon icon = iconFactory.fromResource(R.drawable.fire);
        final Icon iconpos = iconFactory.fromResource(R.drawable.placeholder);

        final Boolean[] stat_style = {false};
        final ImageView btn_style=findViewById(R.id.btn_style);
        btn_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stat_style[0]){
                    stat_style[0] =false;
                    map.setStyle(Style.SATELLITE_STREETS);
                    btn_style.post(new Runnable() {
                        @Override
                        public void run() {
                            btn_style.setImageResource(R.drawable.ic_street_icon);
                        }
                    });
                }else{
                    stat_style[0] =true;
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
                startService(new Intent(PosPengamatan.this,FusedLocationServices.class));
                // Customize map with markers, polylines, etc.

                mapboxMap.addMarker(new MarkerOptions().position(new LatLng(-7.525941,110.410546)).setSnippet("Pos Pengamatan Babadan").setIcon(iconpos));
                mapboxMap.addMarker(new MarkerOptions().position(new LatLng(-7.497272,110.421646)).setSnippet("Pos Pengamatan Jrakah").setIcon(iconpos));
                mapboxMap.addMarker(new MarkerOptions().position(new LatLng(-7.601044,110.425081)).setSnippet("Pos Pengamatan Kaliurang").setIcon(iconpos));
                mapboxMap.addMarker(new MarkerOptions().position(new LatLng(-7.498934,110.457064)).setSnippet("Pos Pengamatan Selo").setIcon(iconpos));

                mapboxMap.setOnInfoWindowClickListener(new MapboxMap.OnInfoWindowClickListener() {
                    @Override
                    public boolean onInfoWindowClick(@NonNull Marker marker) {
                        String uri = "http://maps.google.com/maps?daddr=" + marker.getPosition().getLatitude() + "," + marker.getPosition().getLongitude() + " (" + marker.getSnippet() + ")";

//                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+marker.getPosition().getLatitude()+","+marker.getPosition().getLongitude());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                        return false;
                    }
                });

            }
        });
    }


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
        stopService(new Intent(PosPengamatan.this,FusedLocationServices.class));
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
        @Override
        public void onReceive(Context context, Intent intent) {
            lokasiAnda=intent.getParcelableExtra("datalokasi");
            if (lokasiAnda!=null){
                if (marker!=null){
                    if (lokasiAnda.getAccuracy() < 20) {
                        marker.setPosition(new LatLng(lokasiAnda.getLatitude(), lokasiAnda.getLongitude()));
                    }
                }else{
                    marker=map.addMarker(new MarkerOptions().position(new LatLng(lokasiAnda.getLatitude(),lokasiAnda.getLongitude())));
                    marker.setTitle("lokasi anda");
                }

            }}
    };
}
