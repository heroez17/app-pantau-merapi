package bukapeta.id.pantaumerapi;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by jimy on 18/10/17.
 */

public class CekGps {
   private Context context;

    public CekGps(Context context) {
        this.context = context;
    }

    public Boolean isGpsEnabled(){
        final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            return false;
        }
    return true;
    }

}
