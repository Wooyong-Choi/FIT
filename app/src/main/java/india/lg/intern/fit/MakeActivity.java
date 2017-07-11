package india.lg.intern.fit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

    public class MakeActivity extends AppCompatActivity implements View.OnClickListener {

    Footprint fp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make);

        ((Button) findViewById(R.id.collect)).setOnClickListener(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("GPSLocationUpdates"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            ArrayList<Location> locList = (ArrayList<Location>) intent.getSerializableExtra("Location");
            fp = new Footprint("TEST", locList);
        }
    };

    @Override
    public void onClick(View v) {
        Button btn = ((Button) v);

        if (btn.getText().equals("Collect")) {
            fp = new Footprint("TEST");

            btn.setText("Stop");

            Toast.makeText(getApplicationContext(), "Start to collect Position", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PosCollector.class);
            startService(intent);

        } else {
            Intent intent = new Intent(this, PosCollector.class);
            stopService(intent);

            Toast.makeText(getApplicationContext(), "Complete to collect Position", Toast.LENGTH_SHORT).show();

            intent = new Intent(this, FootprintActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("Footprint", fp);
            intent.putExtra("Bundle", b);
            startActivity(intent);
        }
    }
}
