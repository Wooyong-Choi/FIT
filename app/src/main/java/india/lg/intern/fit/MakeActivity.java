package india.lg.intern.fit;

import android.*;
import android.Manifest;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MakeActivity extends AppCompatActivity implements View.OnClickListener {

    private Footprint fp;

    private Date start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make);

        ((Button) findViewById(R.id.collect)).setOnClickListener(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("PosCollector"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        private void findSpot(Footprint fp) {
            String[] proj = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.LATITUDE,
                    MediaStore.Images.Media.LONGITUDE
            };


            String select =  MediaStore.Images.Media.DATE_TAKEN + " > " + fp.getStart().getTime()
                    + " AND " + MediaStore.Images.Media.DATE_TAKEN + " < " + fp.getEnd().getTime();

            Cursor imageCursor = getContentResolver(). query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, select, null, null);

            if (imageCursor.getCount() == 0) return;

            if (imageCursor != null && imageCursor.moveToFirst()) {

                int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int thumbsLatCol = imageCursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
                int thumbsLngCol = imageCursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE);

                do {
                    double lat = Double.parseDouble(imageCursor.getString(thumbsLatCol));
                    double lng = Double.parseDouble(imageCursor.getString(thumbsLngCol));

                    double minVal = Double.MAX_VALUE;
                    int minIdx = 0;
                    for (int i = 1; i < fp.getPosList().size(); i++) {
                        Location loc = fp.getPosList().get(i);

                        Location imageLoc = new Location("");
                        imageLoc.setLatitude(lat);
                        imageLoc.setLongitude(lng);

                        double dist = loc.distanceTo(imageLoc);
                        if (minVal > dist) {
                            minVal = dist;
                            minIdx = i;
                        }
                    }

                    int idx = fp.findSpotIdx(minIdx);
                    Spot spot = null;
                    if (idx == -1 ) {
                        spot = new Spot(minIdx);
                        spot.addImageData(imageCursor.getString(thumbsDataCol));
                        fp.getSpotList().add(spot);
                    } else {
                        spot = fp.getSpotList().get(idx);
                        spot.addImageData(imageCursor.getString(thumbsDataCol));
                    }
                } while (imageCursor.moveToNext());
            }
            imageCursor.close();
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            // Get extra data included in the Intent
            ArrayList<Location> locList = (ArrayList<Location>) intent.getSerializableExtra("Location");
            if (locList == null) {
                Toast.makeText(context, "Cannot find location", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getApplicationContext(), "Complete to collect Position", Toast.LENGTH_SHORT).show();

            fp = new Footprint(getApplicationContext(), "TEST", locList);
            fp.setEnd(Calendar.getInstance().getTime());
            fp.setStart(start);
            findSpot(fp);

            DataAccessor.appendFp(getApplicationContext(), fp);

            intent = new Intent(context, FootprintActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("Footprint", fp);
            intent.putExtra("Bundle", b);
            startActivity(intent);

            LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
            finish();
        }
    };

    @Override
    public void onClick(View v) {
        Button btn = ((Button) v);

        if (btn.getText().equals("Collect")) {
            start = Calendar.getInstance().getTime();

            btn.setText("Stop");

            Toast.makeText(getApplicationContext(), "Start to collect Position", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PosCollector.class);
            startService(intent);

        } else if (btn.getText().equals("Stop")){

            Intent intent = new Intent(this, PosCollector.class);
            stopService(intent);

            btn.setText("Collect");
        }
    }


}
