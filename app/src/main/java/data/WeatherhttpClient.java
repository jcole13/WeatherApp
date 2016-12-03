package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import Util.Utils;

/**
 * Created by Jared Cole on 11/27/2016.
 */

public class WeatherhttpClient {
    public String getWeatherData(String place){
        HttpURLConnection connection = null;
        InputStream inputstream = null;

        try {
            connection = (HttpURLConnection) (new URL(Utils.BASE_URL + place)).openConnection();
            connection.addRequestProperty("x-api-key","1023b2d5947227099d9c1043ed22f012");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.connect();

            //Read the response
            StringBuffer stringbuffer = new StringBuffer();
            inputstream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream));
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                stringbuffer.append(line + "\r\n");
            }
            inputstream.close();
            connection.disconnect();
            return stringbuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
