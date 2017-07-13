package india.lg.intern.fit;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;


/**
 * Created by LeeJaeYoung on 2016-12-09.
 */

public class PosCollector extends Service implements

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final String TAG = "GoogleMapAPIClient";
    private final IBinder mBinder = new MyBinder();

    private ArrayList<Location> locList;


    public class MyBinder extends Binder {
        public PosCollector GetService() {
            return PosCollector.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * GoogleApiClient.Builder : Google Play 서비스 접근승인 요청
     * addConnectionCallbacks(this) //Google Client Connection Callback 클래스
     * addApi(LocationServices.API); //Fused Location Provider API 사용 요청
     */
    @Override
    public void onCreate() {

        locList = new ArrayList<Location>();

        super.onCreate();

        //Google Api를 사용할 수 있는지 체크
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    /**
     * GoogleApiClient와 connect() 연결이 완료된 후 실행됨 (이 부분에서 다양한 서비스 활용)
     * setFastestInterval : 지도 갱신시간
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d(TAG, "GoogleService onConnected");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(300);
        mLocationRequest.setFastestInterval(150);

        int nper1 = ContextCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int nper2 = ContextCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        String s = String.format("%d %d", nper1, nper2);
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission
                        (this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission
                        (this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(PosCollector.this, s, Toast.LENGTH_LONG).show();
            return;
        }
        startLocationUpdates();
        Log.d(TAG, "Connected Success");
    }


    /**
     * Requests location updates from the FusedLocationApi. , myService
     * Fused Location Provider의 특징은 저전력으로 위치측위의 정확도를 향상시켰고,
     *  기존보다 간편하게 API 호출하여 위치를 측위할 수 있도록 개선되었습니다.
     * Fused Location Provider는 Google Play 서비스를 통해 API형태로 사용할 수 있습니다.
     * Google Play 서비스는 구글에서 제공하는 다양한 서비스들을 손쉽게 사용할 수 있도록
     *  제공하는 클라이언트 라이브러리입니다.
     * 구글 플레이 스토어를 통한 자동 플랫폼 업데이트를 지원하기 때문에 OS버전이나, 통신사 버전에 따른
     * 디바이스 지원에 대한 걱정없이 구글이 제공하는 최신의 기능을 쉽게 빠르게 사용할 수 있습니다.
     */

    //4-2번
    protected void startLocationUpdates() {

        int nper1 = ContextCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int nper2 = ContextCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        String s = String.format("%d %d", nper1, nper2);
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission
                        (this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission
                        (this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(PosCollector.this, s, Toast.LENGTH_LONG).show();
            return;
        }

        //Fused Location Provider를 통한 위치정보
        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (locationAvailability.isLocationAvailable()) {
            LocationServices.FusedLocationApi.requestLocationUpdates
                    (mGoogleApiClient, mLocationRequest, this);
        } else {
            Toast.makeText(this, "Location Unavialable", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        locList.add(location);
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent("PosCollector");
        intent.putExtra("Location", locList);
        sendBroadcast(intent);

        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * 연결이 갑자기 일시적으로 끊어지면 자동으로 연결복구를 시도함.
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    public void connect() {
        mGoogleApiClient.connect();
    }
}