package bukapeta.id.pantaumerapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.functions.Function;
import com.mapbox.mapboxsdk.style.functions.stops.Stop;
import com.mapbox.mapboxsdk.style.functions.stops.Stops;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import static com.mapbox.mapboxsdk.style.functions.stops.Stop.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;

public class ZonaKrb extends AppCompatActivity {

    private MapView mapView;
    private Location lokasiAnda;
    private Marker marker;
    private MapboxMap map;
    private TextView judulzona,deskripsizona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_key));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        setContentView(R.layout.activity_zona_krb);

        IconFactory iconFactory = IconFactory.getInstance(ZonaKrb.this);
        final Icon icon = iconFactory.fromResource(R.drawable.fire);
        final Icon iconpos = iconFactory.fromResource(R.drawable.placeholder);

        judulzona=findViewById(R.id.judul_zona);
        deskripsizona=findViewById(R.id.deskripsi_zona);

        final Boolean[] stat_style = {false};
        final ImageView btn_style=findViewById(R.id.btn_style);
        btn_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stat_style[0]){
                    stat_style[0] =false;
                    map.removeLayer("zonakrb");
                    map.removeLayer("selectzona");
                    map.setStyle(Style.SATELLITE_STREETS);
                    btn_style.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_style.setImageResource(R.drawable.ic_street_icon);
                            String sb=ReadGeojson();
                            map.addSource(new GeoJsonSource("zonakrb_source",sb));
                            FillLayer zonakrblayer=new FillLayer("zonakrb","zonakrb_source").withProperties(PropertyFactory.fillOutlineColor(Color.WHITE),PropertyFactory.fillOpacity(0.5f), fillColor(Function.property("zona", Stops.categorical(
                                    Stop.stop("3", fillColor(Color.parseColor("#db3236"))),
                                    Stop.stop("2", fillColor(Color.parseColor("#f4c20d"))),
                                    Stop.stop("1", fillColor(Color.parseColor("#3cba54")))
                            ))));
                            map.addLayer(zonakrblayer);
                            tambahLayerCanvas();
                        }
                    },1500);
                }else{
                    stat_style[0] =true;
                    map.removeLayer("zonakrb");
                    map.removeLayer("selectzona");
                    map.setStyle(Style.MAPBOX_STREETS);
                    btn_style.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btn_style.setImageResource(R.drawable.ic_satelit_icon);
                            String sb=ReadGeojson();
                            map.addSource(new GeoJsonSource("zonakrb_source",sb));
                            FillLayer zonakrblayer=new FillLayer("zonakrb","zonakrb_source").withProperties(PropertyFactory.fillOutlineColor(Color.WHITE),PropertyFactory.fillOpacity(0.5f), fillColor(Function.property("zona", Stops.categorical(
                                    Stop.stop("3", fillColor(Color.parseColor("#db3236"))),
                                    Stop.stop("2", fillColor(Color.parseColor("#f4c20d"))),
                                    Stop.stop("1", fillColor(Color.parseColor("#3cba54")))
                            ))));
                            map.addLayer(zonakrblayer);
                            tambahLayerCanvas();
                        }
                    },1500);
                }
            }
        });

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map=mapboxMap;

                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-7.541292151475886,110.44619982622703),8));
                mapboxMap.addMarker(new MarkerOptions().position(new LatLng(-7.541292151475886,110.44619982622703)).setTitle("Puncak Merapi").icon(icon));
                startService(new Intent(ZonaKrb.this,FusedLocationServices.class));
                // Customize map with markers, polylines, etc.

                String sb=ReadGeojson();
                mapboxMap.addSource(new GeoJsonSource("zonakrb_source",sb));
                FillLayer zonakrblayer=new FillLayer("zonakrb","zonakrb_source").withProperties(PropertyFactory.fillOutlineColor(Color.WHITE),PropertyFactory.fillOpacity(0.5f), fillColor(Function.property("zona", Stops.categorical(
                        Stop.stop("3", fillColor(Color.parseColor("#db3236"))),
                        Stop.stop("2", fillColor(Color.parseColor("#f4c20d"))),
                        Stop.stop("1", fillColor(Color.parseColor("#3cba54")))
                ))));
                map.addLayer(zonakrblayer);
                tambahLayerCanvas();

                map.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        final PointF pixel = map.getProjection().toScreenLocation(point);
                        List<Feature> features =map.queryRenderedFeatures(pixel, "zonakrb");

                      if (features.size()!=0) {
                          Feature feature = features.get(features.size()-1);

                          GeoJsonSource source=map.getSourceAs("selectzona_source");
                          if (source!=null){
                              source.setGeoJson(feature);
                          }

                          String judulzonatext = feature.getStringProperty("name");
                          String deszonatext = feature.getStringProperty("description");
                          judulzona.setText(judulzonatext);
                          deskripsizona.setText(deszonatext);
                      }else{
                          judulzona.setText("");
                          deskripsizona.setText("");
                      }
                    }
                });
            }
        });
    }

    private String ReadGeojson() {
        StringBuilder sb = null;
        try {
            InputStream inputStream = getAssets().open("zonakrb.geojson");
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  sb.toString();
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
        stopService(new Intent(ZonaKrb.this,FusedLocationServices.class));
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


    private void tambahLayerCanvas() {
        FeatureCollection emptySource = FeatureCollection.fromFeatures(new Feature[]{});
        Source MarkerSource = new GeoJsonSource("selectzona_source", emptySource);
        map.addSource(MarkerSource);
        FillLayer fillLayer=new FillLayer("selectzona","selectzona_source").withProperties(PropertyFactory.fillColor(Color.TRANSPARENT),PropertyFactory.fillOutlineColor(Color.WHITE));
        map.addLayer(fillLayer);
    }
}
