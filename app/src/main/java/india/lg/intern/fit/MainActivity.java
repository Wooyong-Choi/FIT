package india.lg.intern.fit;

import android.Manifest;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listview;
    private SearchView searchView;
    private HistoryAdapter historyAdapter;
    private ArrayList<Footprint> fpList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        1);
            }
        }

        // Start the thread
        tutThread.start();

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                historyAdapter.clearData();

                fpList = DataAccessor.readFplist(getApplicationContext());
                if (fpList != null)
                    for (Footprint fp : fpList)
                        historyAdapter.add(fp);

                historyAdapter.notifyDataSetChanged();
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                historyAdapter.clearData();
                for (Footprint fp : fpList) {
                    if (fp.getName().contains(query)) {
                        historyAdapter.add(fp);
                    }
                }
                historyAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                historyAdapter.clearData();
                for (Footprint fp : fpList) {
                    if (fp.getName().contains(query)) {
                        historyAdapter.add(fp);
                    }
                }
                historyAdapter.notifyDataSetChanged();
                return true;
            }
        });


        listview = (ListView)findViewById(R.id.historyList);
        historyAdapter = new HistoryAdapter(MainActivity.this, R.layout.history);
        listview.setAdapter(historyAdapter);
        listview.deferNotifyDataSetChanged();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Footprint entry = ((HistoryAdapter) parent.getAdapter()).getFpList().get(position);
                Intent intent = new Intent(MainActivity.this, FootprintActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("Footprint", entry);
                intent.putExtra("Bundle", b);
                startActivity(intent);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

                // Dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));

                // 제목셋팅
                alertDialogBuilder.setTitle("Delete");

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("Do you want to delete this?")
                        .setPositiveButton("Delete",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        DataAccessor.removeFp(getApplicationContext(), pos);
                                        updateFpList();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });

                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();

                // 다이얼로그 보여주기
                alertDialog.show();

                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MakeActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateFpList();
    }

    private void updateFpList() {
        historyAdapter.clearData();

        fpList = DataAccessor.readFplist(getApplicationContext());
        if (fpList != null)
            for (Footprint fp : fpList)
                historyAdapter.add(fp);

        historyAdapter.notifyDataSetChanged();
    }

    /**
     * Thread for Tutorial
     */
    private Thread tutThread = new Thread(new Runnable() {
        @Override
        public void run() {
            //  Initialize SharedPreferences
            SharedPreferences getPrefs = PreferenceManager
                    .getDefaultSharedPreferences(getBaseContext());

            //  Create a new boolean and preference and set it to true
            boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

            //  If the activity has never started before...
            if (true) {

                //  Launch app intro
                final Intent i = new Intent(MainActivity.this, InitActivity.class);

                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        startActivity(i);
                    }
                });

                //  Make a new preferences editor
                SharedPreferences.Editor e = getPrefs.edit();

                //  Edit preference to make it false because we don't want this to run again
                // e.putBoolean("firstStart", false);
                e.putBoolean("firstStart", false);

                //  Apply changes
                e.apply();
            }
        }
    });


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Start the thread
                    tutThread.start();

                }
                return;
            }
        }
    }
}
