package data;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Jared Cole on 12/2/2016.
 */

public class CityPreferences {
    SharedPreferences prefs;

    public CityPreferences(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);


    }
    public String getCity(){
        return prefs.getString("city", "Spokane,US");
    }
    public void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }
}
