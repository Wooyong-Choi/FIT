package india.lg.intern.fit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FootprintActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private Footprint fp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        fp = (Footprint) getIntent().getBundleExtra("Bundle").getParcelable("Footprint");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MarkerOptions opt = new MarkerOptions();
        MarkerOptions frame = new MarkerOptions();

        opt.position(locToLatLng(fp.getPosList().get(0)));
        mMap.addMarker(opt);
        opt.position(locToLatLng(fp.getPosList().get(fp.getPosList().size()-1)));
        mMap.addMarker(opt);

        int n = 0;
        for (Spot spot : fp.getSpotList()) {
            LatLng pos = locToLatLng(fp.getPosList().get(spot.getPosIdx()));
            opt.position(pos);
            frame.position(pos);
            opt.snippet("" + n++);
            Bitmap bitmap = BitmapFactory.decodeFile(spot.getImageDataList().get(0));
            Bitmap bitmap_frame = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.frame);

            if(spot.getImageDataList().size() < 5) {
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                Bitmap resized_frame = Bitmap.createScaledBitmap(bitmap_frame, 105, 105, true);
                frame.icon(BitmapDescriptorFactory.fromBitmap(resized_frame));
                opt.icon(BitmapDescriptorFactory.fromBitmap(resized));
            }
            else {
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                Bitmap resized_frame = Bitmap.createScaledBitmap(bitmap_frame, 210, 210, true);
                frame.icon(BitmapDescriptorFactory.fromBitmap(resized_frame));
                opt.icon(BitmapDescriptorFactory.fromBitmap(resized));
            }
            mMap.addMarker(opt);
            mMap.addMarker(frame);
        }
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locToLatLng(fp.getPosList().get(0)), 11));
        mMap.setOnMarkerClickListener(this);

        if (fp.getPosList().size() <= 10) {
            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(fp.getPosList());

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        } else {
            int num = fp.getPosList().size() / 10;
            for (int i = 0; i <= num; i++) {
                int firstIdx = 0 + 10*i;
                int lastIdx = (i != num ? firstIdx + 9 : fp.getPosList().size() - 1);
                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(fp.getPosList().subList(firstIdx, lastIdx));

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }
        }


    }

    private String getDirectionsUrl(List<Location> posList) {

        // Origin of route
        String str_origin = "origin="+posList.get(0).getLatitude()+","+posList.get(0).getLongitude();

        // Destination of route
        String str_dest = "destination="+posList.get(posList.size()-1).getLatitude()+","+posList.get(posList.size()-1).getLongitude();


        // Waypoints
        String waypoints = "waypoints=";
        for(int i = 1; i < posList.size()-1; i++) {
            waypoints += posList.get(i).getLatitude() + "," + posList.get(i).getLongitude() + "|";
        }

        // Building the parameters to the web service
        String parameters;
        if (posList.size() == 2)
            parameters = str_origin+"&"+str_dest;
        else
            parameters = str_origin+"&"+str_dest+"&"+waypoints.substring(0, waypoints.length()-1);

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&mode=walking";


        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception", e.toString());
        } finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i = 0; i < result.size(); i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++) {
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getSnippet() != null) {
            Intent intent = new Intent(FootprintActivity.this, SpotActivity.class);
            Bundle b = new Bundle();
            Spot temp = fp.getSpotList().get(Integer.parseInt(marker.getSnippet()));
            b.putParcelable("Spot", temp);
            b.putParcelable("Location", fp.getPosList().get(temp.getPosIdx()));
            intent.putExtra("Bundle", b);
            startActivity(intent);
        }
        return true;
    }

    private LatLng locToLatLng(Location loc) {
        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }

    public void moveThere(double lat, double lng) {//특정위치 기준으로 줌하는 함수
        LatLng latLng = new LatLng(lat, lng);   // 경도,위도, 특정위치 기준으로 지도 띄우기
        // (설정시 moveThere을 통해 바로 값을 던져주어도 되고 처음부터 변수를 지정해주셔도됩니다.
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12)); //16배율 고정
        // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
