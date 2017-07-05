package india.lg.intern.fit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by WooYong on 2017-07-05.
 */

public class Footprint {
    private String name;
    private String date;
    private Country country;
    private Date start;
    private Date end;
    private ArrayList<Position> posList;
    private ArrayList<Spot> spotList;

    public Footprint(String nm) {
        name = nm;
        date = getCurrentTime();
        country = Country.SOUTH_KOREA;
    }

    public static String getCurrentTime() {
        String time = "";
        Calendar cal = Calendar.getInstance();
        time = String.format("%04d-%02d-%02d-%02d-%02d",
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

        return time;
    }
}
