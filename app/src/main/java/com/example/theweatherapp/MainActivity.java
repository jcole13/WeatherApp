package com.example.theweatherapp;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.DecimalFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

//import org.apache.http.impl.client.HttpClientBuilder



import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Date;

import Util.Utils;
import data.CityPreferences;
import data.JSONWeatherParser;
import data.WeatherhttpClient;
import model.Weather;
//import model.Weather;

public class MainActivity extends AppCompatActivity {
    private TextView cityName;
    private TextView temp;
    private TextView iconView;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;

    private String units;

    Typeface weatherFont;

    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather.ttf");

        cityName = (TextView) findViewById(R.id.cityText);
        iconView = (TextView) findViewById(R.id.thumbnailIcon);
        temp = (TextView) findViewById(R.id.tempText);
        description = (TextView) findViewById(R.id.cloudText);
        humidity = (TextView) findViewById(R.id.humidtext);
        pressure = (TextView) findViewById(R.id.pressureText);
        wind = (TextView) findViewById(R.id.windText);
        sunrise = (TextView) findViewById(R.id.riseText);
        sunset = (TextView) findViewById(R.id.setText);
        updated = (TextView) findViewById(R.id.updateText);

        iconView.setTypeface(weatherFont);

        CityPreferences cityPreference = new CityPreferences(MainActivity.this);

        renderWeatherData(cityPreference.getCity(), "metric");
        units = "metric";









        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void renderWeatherData(String city, String measure) {
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&units=" + measure});
        //change to be able to choose between metric and imperial

    }
    public void toImperial(View view){
        CityPreferences cityPreferences = new CityPreferences(MainActivity.this);
        String newCity = cityPreferences.getCity();
        renderWeatherData(newCity, "imperial");
        units = "imperial";

    }
    public void toMetric(View view){
        CityPreferences cityPreferences = new CityPreferences(MainActivity.this);
        String newCity = cityPreferences.getCity();
        renderWeatherData(newCity, "metric");
        units = "metric";

    }




    private class WeatherTask extends AsyncTask<String, Void, Weather>{
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            DateFormat df = DateFormat.getTimeInstance();

            String sunriseDate = df.format(new Date(weather.place.getSunrise()));
            String sunsetDate = df.format(new Date(weather.place.getSunset()));
            String updateDate = df.format(new Date(weather.place.getLastupdate()));

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            String temperatureformat = decimalFormat.format(weather.currentcondition.getTemperature());

            cityName.setText(weather.place.getCity() + ", " + weather.place.getCountry());
            if(units.equals("metric")) {
                temp.setText(temperatureformat + "℃");
            }
            else{
                temp.setText(temperatureformat + "℉" );
            }
            humidity.setText("Humidity: " + weather.currentcondition.getHumidity() + " %");
            pressure.setText("Pressure: " + weather.currentcondition.getPressure() + " hPa");
            if(units.equals("metric")) {
                wind.setText("Wind: " + weather.wind.getSpeed() + " mps");
            }
            else{
                wind.setText("Wind: " + weather.wind.getSpeed() + " mph");
            }
            sunrise.setText("Sunrise: " + sunriseDate);
            sunset.setText("Sunset: " + sunsetDate);
            updated.setText("Last Updated: " + updateDate);
            description.setText("Condition: " + weather.currentcondition.getCondition() + " (" + weather.currentcondition.getDescription() + ")" );


            String icon = "";
            String code = weather.currentcondition.getIcon();
            if(weather.currentcondition.getIcon().charAt(2) == 'n'){
                if(code.substring(0, 2).equals("13")){
                    icon= getApplicationContext().getString(R.string.weather_snowy);
                }
                else if(code.substring(0, 2).equals("10")){
                    icon= getApplicationContext().getString(R.string.weather_drizzle);
                }
                else if(code.substring(0, 2).equals("03") || code.substring(0, 2).equals("04")){
                    icon= getApplicationContext().getString(R.string.weather_cloudy);
                }
                else if(code.substring(0, 2).equals("50")){
                    icon= getApplicationContext().getString(R.string.weather_foggy);
                }
                else{
                    icon= getApplicationContext().getString(R.string.weather_clear_night);
                    //Log.v("Substring: ", code.substring(0, 2));
                }

            }
            else{
                if(code.substring(0, 2).equals("13")){
                    icon= getApplicationContext().getString(R.string.weather_snowy);
                }
                else if(code.substring(0, 2).equals("10")){
                    icon= getApplicationContext().getString(R.string.weather_drizzle);
                }
                else if(code.substring(0, 2).equals("03") || code.substring(0, 2).equals("04")){
                    icon= getApplicationContext().getString(R.string.weather_cloudy);
                }
                else if(code.substring(0, 2).equals("50")){
                    icon= getApplicationContext().getString(R.string.weather_foggy);
                }
                else{
                    icon= getApplicationContext().getString(R.string.weather_sunny);
                    //Log.v("Substring: ", code.substring(0, 2));
                }
            }
            iconView.setText(icon);





        }

        @Override
        protected Weather doInBackground(String... params) {

            String data = ( (new WeatherhttpClient()).getWeatherData(params[0]));

            weather = JSONWeatherParser.getWeather(data);
            //Log.v("Description: ", weather.currentcondition.getDescription());
            //Log.v("Icon Code: ", weather.currentcondition.getIcon());


            return weather;


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_cityId) {
            showInputDialog();
        }

        return super.onOptionsItemSelected(item);
    }
    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change City");

        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Portland,US");
        builder.setView(cityInput);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CityPreferences cityPreferences = new CityPreferences(MainActivity.this);
                cityPreferences.setCity(cityInput.getText().toString());

                String newcity = cityPreferences.getCity();

                renderWeatherData(newcity, "metric");

            }
        });
                builder.show();
    }
}
