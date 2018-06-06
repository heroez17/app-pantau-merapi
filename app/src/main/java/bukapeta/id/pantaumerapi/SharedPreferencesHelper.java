package bukapeta.id.pantaumerapi;

import android.content.Context;

/**
 * Created by jimy on 22/09/17.
 */

public class SharedPreferencesHelper {
    private Context context;
    public SharedPreferencesHelper(Context context) {
        this.context=context;
    }


    public void setShared(String title, String key, String value){
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(title, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key,value);
        editor.commit();
    }


    public String getShared(String title, String key){
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(title, Context.MODE_PRIVATE);
        String stat=sharedPref.getString(key,"");
        return stat;
    }

    public void clearShared(String title){
        android.content.SharedPreferences sharedPref = context.getSharedPreferences(title, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().commit();
    }


}
