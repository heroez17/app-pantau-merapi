package bukapeta.id.pantaumerapi;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        setContentView(R.layout.activity_home);


        CardView btn_lokasi_anda=findViewById(R.id.btn_lokasi);
        CardView btn_pos_pengamatan=findViewById(R.id.btn_pos_pengamatan);
        CardView btn_pos_pengungsi=findViewById(R.id.btn_pos_pengungsi);
        CardView btn_aktivitas=findViewById(R.id.btn_aktivitas);
        CardView btn_mediacenter=findViewById(R.id.btn_media_center);


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

        SetPermision setPermision=new SetPermision(Home.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setPermision.setPermision();
        }
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }
}
