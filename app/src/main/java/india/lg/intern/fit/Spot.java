package india.lg.intern.fit;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by WooYong on 2017-07-05.
 */

public class Spot implements Parcelable, Serializable {

    private int posIdx;
    private ArrayList<String> imageDataList;

    public Spot() { }

    public Spot(int idx) {
        posIdx = idx;
        imageDataList = new ArrayList<>();
    }

    public int getPosIdx() {
        return posIdx;
    }

    public void setPosIdx(int posIdx) {
        this.posIdx = posIdx;
    }

    public ArrayList<String> getImageDataList() {
        return imageDataList;
    }

    public void setImageDataList(ArrayList<String> imageDataList) {
        this.imageDataList = imageDataList;
    }

    public void addImageData(String imageData) {
        imageDataList.add(imageData);
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
                    sp.imageDataList = in.readArrayList(String.class.getClassLoader());
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
        parcel.writeList(imageDataList);
    }
}
