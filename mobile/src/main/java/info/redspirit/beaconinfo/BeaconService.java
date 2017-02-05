package info.redspirit.beaconinfo;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

/**
 * Created by rj on 2017/02/05.
 */

public class BeaconService extends Service {

    //ServiceName
    private final String SERVICE_NAME = "BeaconService";

    //iBeacon検知対応
    private static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    //利用するBeaconのUUID(固定)※全て同一
    private static final String UUID = "00000000-5F80-1001-B000-001C4DB646D9";
    private BeaconManager beaconManager;
    //6.0以上ロケーションアクセス許可
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private final IBinder mBinder = new BeaconServiceBinder();

    public class BeaconServiceBinder extends Binder{
        //BeaconService自身を返す
        BeaconService getService(){
            return BeaconService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.i(SERVICE_NAME,"Service Enable");


        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(SERVICE_NAME,"Service Disable");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

}
