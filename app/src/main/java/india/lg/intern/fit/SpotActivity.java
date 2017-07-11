package india.lg.intern.fit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

public class SpotActivity extends AppCompatActivity {

    private Spot spot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);

        spot = (Spot) getIntent().getSerializableExtra("Spot");
    }
}
