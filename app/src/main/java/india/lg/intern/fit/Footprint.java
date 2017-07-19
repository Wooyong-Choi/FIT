package india.lg.intern.fit;

import android.content.Context;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by WooYong on 2017-07-05.
 */

public class Footprint implements Parcelable, Serializable {
    private String name;
    private String date;
    private Country country;
    private Date start;
    private Date end;
    private transient ArrayList<Location> posList;
    private ArrayList<Spot> spotList;

    private String posListStr = "";

    public Footprint() { }

    public Footprint(Context context, String nm, ArrayList<Location> locList) {
        name = nm;
        date = getCurrentTime();
        country = Country.locToCountry(context, locList.get(0));
        posList = locList;
        spotList = new ArrayList<Spot>();
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

    public int findSpotIdx(int idx) {
        for (int i = 0; i < spotList.size(); i++)
            if (spotList.get(i).getPosIdx() == idx)
                return i;

        return -1;
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

    public void serializePosList() {
        for (Location pos : posList) {
            posListStr += pos.getLatitude() + "," +
                    pos.getLongitude() + "," +
                    pos.getTime() + "/";
        }
    }

    public void  deserializePosList() {
        posList = new ArrayList<Location>();

        Location loc = new Location("");
        for (String token : posListStr.split("/")) {
            String[] data = token.split(",");

            loc.setLatitude(Double.parseDouble(data[0]));
            loc.setLongitude(Double.parseDouble(data[1]));
            loc.setTime(Long.parseLong(data[2]));

            posList.add(loc);
        }

        posListStr = "";
    }
}
