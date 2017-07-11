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
    private LayoutInflater inflater =null;

    ArrayList<Footprint> fpList = new ArrayList<Footprint>();
    ArrayList<String> hname= new ArrayList<String>();
    ArrayList<String> hdate= new ArrayList<String>();
    ArrayList<String> hcountry= new ArrayList<String>();
    ArrayList<Integer> himage=new ArrayList<Integer>();

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

        viewh.hname.setText(hname.get(position));
        viewh.hdate.setText(hdate.get(position));
        viewh.hcountry.setText(hcountry.get(position));
        viewh.himage.setImageResource(himage.get(position));

        return view;
    }
    @Override
    public int getCount(){
        return hname.size();
    }

    public void add(Footprint fp)
    {
        fpList.add(fp);

        this.hname.add(fp.getName());
        this.hdate.add(fp.getDate());
        this.hcountry.add(fp.getCountry().toString());
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
        this.himage.add(resID);
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