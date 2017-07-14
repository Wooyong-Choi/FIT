package india.lg.intern.fit;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by WooYong on 2017-07-05.
 */

public class Footprint implements Parcelable {
    private String name;
    private String date;
    private Country country;
    private Date start;
    private Date end;
    private ArrayList<Location> posList;
    private ArrayList<Spot> spotList;

    public Footprint() { }
    public Footprint(String nm) {
        name = nm;
        date = getCurrentTime();
        country = Country.SOUTH_KOREA;
        posList = new ArrayList<Location>();
        Location l1 = new Location("");
        l1.setLongitude(37);
        l1.setLatitude(127);
        Location l2 = new Location("");
        l2.setLongitude(37.01);
        l2.setLatitude(127.01);
        Location l3 = new Location("");
        l3.setLongitude(37.02);
        l3.setLatitude(127.02);
        Location l4 = new Location("");
        l4.setLongitude(37.03);
        l4.setLatitude(127.03);
        Location l5 = new Location("");
        l5.setLongitude(37.04);
        l5.setLatitude(127.04);
        posList.add(l1);
        posList.add(l2);
        posList.add(l3);
        posList.add(l4);
        posList.add(l5);
        spotList = new ArrayList<Spot>();
        spotList.add(new Spot(l2));
        spotList.add(new Spot(l4));
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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Footprint> CREATOR =
            new Parcelable.Creator<Footprint>() {
                @Override
                public Footprint createFromParcel(Parcel in) {
                    Footprint fp = new Footprint();
                    fp.name = in.readString();
                    fp.date = in.readString();
                    fp.country = (Country) in.readSerializable();
                    fp.start = (Date) in.readSerializable();
                    fp.end = (Date) in.readSerializable();
                    fp.posList = in.readArrayList(Location.class.getClassLoader());
                    fp.spotList = in.readArrayList(Spot.class.getClassLoader());
                    return fp;
                }

                @Override
                public Footprint[] newArray(int size) {
                    return new Footprint[size];
                }
            };

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(date);
        parcel.writeSerializable(country);
        parcel.writeSerializable(start);
        parcel.writeSerializable(end);
        parcel.writeList(posList);
        parcel.writeList(spotList);
    }
}
