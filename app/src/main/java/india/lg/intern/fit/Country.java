package india.lg.intern.fit;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WooYong on 2017-07-05.
 */

public enum Country implements Serializable {
    SOUTH_KOREA("South Korea"),
    FRANCE("France"),
    UNITED_KINGDOM("United Kingdom"),
    INDIA("India");

    public String str;

    Country(String str) {
        this.str = str;
    }

    private static Country codeToCountry(String code) {
        if (code.equals("KO")) {
            return Country.SOUTH_KOREA;
        } else if (code.equals("FR")) {
            return Country.FRANCE;
        } else if (code.equals("UK")) {
            return Country.UNITED_KINGDOM;
        } else if (code.equals("IN")) {
            return Country.INDIA;
        } else
            return null;
    }


    public static Country locToCountry(Context content, Location loc) {
        Geocoder geocoder = new Geocoder(content);
        List<Address> list = null;
        try {
            return codeToCountry((geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 3)).get(0).getCountryCode());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String toString() {
        return str;
    }
}
