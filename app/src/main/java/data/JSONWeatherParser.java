package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Util.Utils;
import model.Place;
import model.Weather;

/**
 * Created by Jared Cole on 11/27/2016.
 */

public class JSONWeatherParser {
    public static Weather getWeather(String data){
        Weather weather = new Weather();
        //create JsonObject from data

        try {
            JSONObject jsonObject = new JSONObject(data);
            Place place = new Place();

            //get the coordinates obj

            JSONObject coorObj = Utils.getObject("coord", jsonObject);
            place.setLat(Utils.getFloat("lat", coorObj));
            place.setLon(Utils.getFloat("lon", coorObj));


            //Get the sys obj

            JSONObject sysObj = Utils.getObject("sys", jsonObject);
            place.setCountry(Utils.getString("country", sysObj));
            place.setLastupdate(Utils.getInt("dt", jsonObject));
            place.setSunrise(Utils.getInt("sunrise", sysObj));
            place.setSunset(Utils.getInt("sunset", sysObj));
            place.setCity(Utils.getString("name", jsonObject));
            weather.place = place;

            //get the weather obj, which is actually an array

            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject jsonweather = jsonArray.getJSONObject(0);
            weather.currentcondition.setWeatherId(Utils.getInt("id", jsonweather));
            weather.currentcondition.setDescription(Utils.getString("description", jsonweather));
            weather.currentcondition.setCondition(Utils.getString("main", jsonweather));
            weather.currentcondition.setIcon(Utils.getString("icon", jsonweather));

            JSONObject mainObj = Utils.getObject("main", jsonObject);

            weather.currentcondition.setHumidity(Utils.getInt("humidity", mainObj));
            weather.currentcondition.setPressure(Utils.getInt("pressure", mainObj));
            weather.currentcondition.setMinTemp(Utils.getFloat("temp_min", mainObj));
            weather.currentcondition.setMaxTemp(Utils.getFloat("temp_max", mainObj));
            weather.currentcondition.setTemperature(Utils.getDouble("temp", mainObj));

            //wind

            JSONObject windObj = Utils.getObject("wind", jsonObject);
            weather.wind.setSpeed((Utils.getFloat("speed", windObj)));
            weather.wind.setDeg(Utils.getFloat("deg", windObj));

            //clouds

            JSONObject cloudObj = Utils.getObject("clouds", jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all", cloudObj));

            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
