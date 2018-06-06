package bukapeta.id.pantaumerapi;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Home extends AppCompatActivity {
    private SwipeRefreshLayout btn_refresh;
    private TextView txt_status,txt_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        setContentView(R.layout.activity_home);

        txt_status=findViewById(R.id.textView2);
        txt_info=findViewById(R.id.textView4);
        CardView btn_lokasi_anda=findViewById(R.id.btn_lokasi);
        CardView btn_pos_pengamatan=findViewById(R.id.btn_pos_pengamatan);
        CardView btn_pos_pengungsi=findViewById(R.id.btn_pos_pengungsi);
        CardView btn_aktivitas=findViewById(R.id.btn_aktivitas);
        CardView btn_mediacenter=findViewById(R.id.btn_media_center);
        CardView btn_zona_krb=findViewById(R.id.btn_zona_krb);

        btn_refresh=findViewById(R.id.btn_refresh);



        btn_lokasi_anda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new CekGps(Home.this).isGpsEnabled()) {
                    startActivity(new Intent(Home.this, LokasiAnda.class));
                }else{
                    Toast.makeText(Home.this, "GPS belum diaktifkan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_pos_pengamatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new CekGps(Home.this).isGpsEnabled()) {
                    startActivity(new Intent(Home.this, PosPengamatan.class));
                }else{
                    Toast.makeText(Home.this, "GPS belum diaktifkan", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_pos_pengungsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new CekGps(Home.this).isGpsEnabled()) {
                    startActivity(new Intent(Home.this, PosPengungsi.class));
                }else{
                    Toast.makeText(Home.this, "GPS belum diaktifkan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_aktivitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, AktivitasMerapi.class));
            }
        });

        btn_mediacenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, MediaCenter.class));
            }
        });


        btn_zona_krb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new CekGps(Home.this).isGpsEnabled()) {
                    startActivity(new Intent(Home.this, ZonaKrb.class));
                }else{
                    Toast.makeText(Home.this, "GPS belum diaktifkan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SetPermision setPermision=new SetPermision(Home.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setPermision.setPermision();
        }

        btn_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        btn_refresh.post(new Runnable() {
            @Override
            public void run() {
                btn_refresh.setRefreshing(true);
                getData();
            }
        });
    }

    private void getData() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(GlobalConstant.base_url)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        RequestServices requestServer=retrofit.create(RequestServices.class);
//        Call<ModelDataHome> call=requestServer.datahome();
//        call.enqueue(new Callback<ModelDataHome>() {
//            @Override
//            public void onResponse(Call<ModelDataHome> call, Response<ModelDataHome> response) {
//                btn_refresh.setRefreshing(false);
//                if (response.errorBody()==null){
//                    ModelDataHome modelDataHome=response.body();
//                    txt_status.setText(modelDataHome.getStatus());
//                    txt_info.setText(modelDataHome.getInfo());
//                    SharedPreferencesHelper sharedPreferencesHelper=new SharedPreferencesHelper(Home.this);
//                    sharedPreferencesHelper.setShared("radius","radius", String.valueOf(modelDataHome.getRadius()));
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ModelDataHome> call, Throwable t) {
//                btn_refresh.setRefreshing(false);
//            }
//        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_refresh.setRefreshing(false);
            }
        },1000);

    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }
}
