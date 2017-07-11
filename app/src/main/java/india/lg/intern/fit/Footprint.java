package india.lg.intern.fit;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by WooYong on 2017-07-05.
 */

public class Footprint implements Serializable {
    private String name;
    private String date;
    private Country country;
    private Date start;
    private Date end;
    private ArrayList<Location> posList;
    private ArrayList<Spot> spotList;

    public Footprint(String nm) {
        name = nm;
        date = getCurrentTime();
        country = Country.SOUTH_KOREA;
        posList = new ArrayList<Location>();
        Location l1 = new Location("");
        l1.setLongitude(37);
        l1.setLatitude(127);
        Location l2 = new Location("");
        l1.setLongitude(37.5);
        l1.setLatitude(127.5);
        Location l3 = new Location("");
        l1.setLongitude(38);
        l1.setLatitude(128);
        posList.add(l1);
        posList.add(l2);
        posList.add(l3);
        spotList = new ArrayList<Spot>();
        spotList.add(new Spot());
    }

    public Footprint(String nm, ArrayList<Location> locList) {
        name = nm;
        date = getCurrentTime();
        country = Country.SOUTH_KOREA;
        posList = locList;
        spotList = new ArrayList<Spot>();
        spotList.add(new Spot());
    }

    public static String getCurrentTime() {
        String time = "";
        Calendar cal = Calendar.getInstance();
        time = String.format("%04d-%02d-%02d-%02d-%02d",
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

        return time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public ArrayList<Location> getPosList() {
        return posList;
    }

    public void setPosList(ArrayList<Location> posList) {
        this.posList = posList;
    }

    public ArrayList<Spot> getSpotList() {
        return spotList;
    }

    public void setSpotList(ArrayList<Spot> spotList) {
        this.spotList = spotList;
    }
}
