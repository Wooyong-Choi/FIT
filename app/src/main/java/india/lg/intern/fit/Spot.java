package india.lg.intern.fit;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by WooYong on 2017-07-05.
 */

public class Spot implements Serializable {

    private Location pos;

    public Spot() { }

    public Spot(Location ps) {
        pos = ps;
    }

}
