package india.lg.intern.fit;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by WooYong on 2017-07-05.
 */

public class Spot implements Parcelable {

    private Location pos;

    public Spot() { }

    public Spot(Location ps) {
        pos = ps;
    }

    public Location getPos() {
        return pos;
    }

    public void setPos(Location pos) {
        this.pos = pos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Spot> CREATOR =
            new Parcelable.Creator<Spot>() {
                @Override
                public Spot createFromParcel(Parcel in) {
                    Spot sp = new Spot();
                    sp.pos = in.readParcelable(Location.class.getClassLoader());
                    return sp;
                }

                @Override
                public Spot[] newArray(int size) {
                    return new Spot[size];
                }
            };

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(pos, 0);
    }
}
