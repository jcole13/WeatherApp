package model;

/**
 * Created by Jared Cole on 11/27/2016.
 */

public class Weather {
    public Place place;
    public String iconData;
    public CurrentCondition currentcondition = new CurrentCondition();
    public Temperature temperature = new Temperature();
    public Wind wind = new Wind();
    public Clouds clouds = new Clouds();

}
