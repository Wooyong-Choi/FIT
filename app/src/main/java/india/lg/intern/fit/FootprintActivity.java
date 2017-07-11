package india.lg.intern.fit;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FootprintActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private Footprint fp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        fp = (Footprint) getIntent().getSerializableExtra("Footprint");

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

        Location testLoc = fp.getPosList().get(0);
        LatLng testPos = new LatLng(testLoc.getLongitude(), testLoc.getLatitude());

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(testPos).title(fp.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(testPos));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().equals(fp.getName())) {
            Intent intent = new Intent(FootprintActivity.this, SpotActivity.class);
            intent.putExtra("Spot", new Spot());
            startActivity(intent);
        }

        return true;
    }
}
