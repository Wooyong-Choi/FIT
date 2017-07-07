package india.lg.intern.fit;

/**
 * Created by WooYong on 2017-07-05.
 */

public enum Country {
    SOUTH_KOREA("South Korea"),
    FRANCE("France"),
    UNITED_KINGDOM("United Kingdom"),
    INDIA("India");

    public String str;

    Country(String str) {
        this.str = str;
    }

    public String toString() {
        return str;
    }
}
