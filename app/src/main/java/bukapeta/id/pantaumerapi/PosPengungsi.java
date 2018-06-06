package bukapeta.id.pantaumerapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Observable;

import rx.Observer;
import rx.schedulers.Schedulers;

public class PosPengungsi extends AppCompatActivity {

    private MapView mapView;
    private Location lokasiAnda;
    private Marker marker;
    private MapboxMap map;
    private Icon iconpos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_key));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        setContentView(R.layout.activity_pos_pengungsi);

        IconFactory iconFactory = IconFactory.getInstance(PosPengungsi.this);
        final Icon icon = iconFactory.fromResource(R.drawable.fire);
        iconpos = iconFactory.fromResource(R.drawable.placeholder);

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

                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-7.541292151475886,110.44619982622703),9));
                mapboxMap.addMarker(new MarkerOptions().position(new LatLng(-7.541292151475886,110.44619982622703)).setTitle("Puncak Merapi").icon(icon));
                startService(new Intent(PosPengungsi.this,FusedLocationServices.class));
                // Customize map with markers, polylines, etc.

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

                getData();

            }
        });
    }

    private void getData() {
        final String[] sb = {null};
        rx.Observable.just(1).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                try {
                    JSONObject jsonObject=new JSONObject(sb[0]);
                    JSONArray jsonArray=jsonObject.getJSONArray("features");

                    rx.Observable.just(jsonArray).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Observer<JSONArray>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(JSONArray jsonArray) {
                            for (int x=0;x<jsonArray.length();x++){
                                try {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(x);
                                    JSONObject jsongeometry=jsonObject1.getJSONObject("geometry");
                                    JSONObject jsonproperty=jsonObject1.getJSONObject("properties");
                                    JSONArray coords = jsongeometry.getJSONArray("coordinates");
                                    LatLng latLng = new LatLng(coords.getDouble(1), coords.getDouble(0));
                                    map.addMarker(new MarkerOptions().position(latLng).setSnippet(jsonproperty.getString("name")).setIcon(iconpos));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
              sb[0] =ReadGeojson();
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
        stopService(new Intent(PosPengungsi.this,FusedLocationServices.class));
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

    private String ReadGeojson() {
        StringBuilder sb = null;
        try {
            InputStream inputStream = getAssets().open("pos_pengungsi.geojson");
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
