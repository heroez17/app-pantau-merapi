package bukapeta.id.pantaumerapi;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MediaCenter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_center);

        CardView btnweb=findViewById(R.id.cardView2);
        CardView btntwit=findViewById(R.id.cardView3);

        btnweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.http_www_merapi_bgl_esdm_go_id)));
                startActivity(intent);
            }
        });

        btntwit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse(getString(R.string.https_twitter_com_bpptkg)));
                startActivity(intent);
            }
        });
    }
}
