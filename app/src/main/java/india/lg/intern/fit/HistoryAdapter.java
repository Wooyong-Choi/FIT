package india.lg.intern.fit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lg on 2017-07-06.
 */

public class HistoryAdapter extends ArrayAdapter<String> {

    private ViewH viewh = null;
    private LayoutInflater inflater = null;

    private ArrayList<Footprint> fpList = new ArrayList<Footprint>();
    private ArrayList<String> nameList = new ArrayList<String>();
    private ArrayList<String> dateList = new ArrayList<String>();
    private ArrayList<String> countryList = new ArrayList<String>();
    private ArrayList<Integer> imageList = new ArrayList<Integer>();

    //생성자
    public HistoryAdapter(Context context, int resource){
        super(context, resource);
        this.inflater = LayoutInflater.from(context);

        fpList = new ArrayList<Footprint>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            viewh = new ViewH();
            view = inflater.inflate(R.layout.history, null);

            viewh.hname = (TextView) view.findViewById(R.id.hname);
            viewh.hdate = (TextView) view.findViewById(R.id.hdate);
            viewh.hcountry = (TextView) view.findViewById(R.id.hcountry);
            viewh.himage = (ImageView) view.findViewById(R.id.itemimage);

            view.setTag(viewh);

        } else {
            viewh = (ViewH) view.getTag();
        }

        viewh.hname.setText(nameList.get(position));
        viewh.hdate.setText(dateList.get(position));
        viewh.hcountry.setText(countryList.get(position));
        viewh.himage.setImageResource(imageList.get(position));

        return view;
    }
    @Override
    public int getCount(){
        return nameList.size();
    }

    public void add(Footprint fp)
    {
        fpList.add(fp);

        this.nameList.add(fp.getName());
        this.dateList.add(fp.getDate());
        this.countryList.add(fp.getCountry().toString());
        int resID = R.drawable.ic_arrow_back_white;  // 디폴트 이미지
        switch(fp.getCountry()) {
            case SOUTH_KOREA:
                resID = R.drawable.ko;  // 한국 이미지
                break;

            case FRANCE:
                resID = R.drawable.fr;  // 한국 이미지
                break;

            case UNITED_KINGDOM:
                resID = R.drawable.uk;  // 한국 이미지
                break;

            case INDIA:
                resID = R.drawable.in;  // 인도 이미지
                break;
        }
        this.imageList.add(resID);
    }

    public ArrayList<Footprint> getFpList() {
        return fpList;
    }

    public void setFpList(ArrayList<Footprint> fpList) {
        this.fpList = fpList;
    }

    class ViewH {
        public TextView hname = null;
        public TextView hdate = null;
        public TextView hcountry = null;
        public ImageView himage = null;
    }
}