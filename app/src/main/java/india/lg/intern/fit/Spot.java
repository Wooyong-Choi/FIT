package india.lg.intern.fit;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by WooYong on 2017-07-05.
 */

public class Spot implements Parcelable, Serializable {

    private int posIdx;

    public Spot() { }

    public Spot(int idx) {
        posIdx = idx;
    }

    public int getPosIdx() {
        return posIdx;
    }

    public void setPosIdx(int posIdx) {
        this.posIdx = posIdx;
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
                    sp.posIdx = in.readInt();
                    return sp;
                }

                @Override
                public Spot[] newArray(int size) {
                    return new Spot[size];
                }
            };

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(posIdx);
    }
}
