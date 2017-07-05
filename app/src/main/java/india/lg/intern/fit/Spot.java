package india.lg.intern.fit;

/**
 * Created by WooYong on 2017-07-05.
 */

public class Spot {

    private Position pos;
    // 썸네일 변수

    public Spot(double lg, double lt) {
        pos = new Position(lg, lt);
        // 썸네일 변수
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}
