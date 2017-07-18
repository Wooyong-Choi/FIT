package india.lg.intern.fit;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by WooYong on 2017-07-17.
 */

public class Position implements Serializable, Parcelable {
    private double latitude;
    private double longitude;
    private long time;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
