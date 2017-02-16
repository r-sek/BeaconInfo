package info.redspirit.beaconinfo;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class MainActivity extends AppCompatActivity
        implements BeaconConsumer, NavigationView.OnNavigationItemSelectedListener, TopFragment.OnFragmentInteractionListener, ItemFragment.OnFragmentInteractionListener {


    //iBeacon検知対応
    private static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    //利用するBeaconのUUID(固定)※全て同一
    private static final String UUID = "00000000-5F80-1001-B000-001C4DB646D9";
    Global global;
    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.container, new TopFragment()).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Applicationクラス取得
        global = (Global) this.getApplication();
        //初期化
        global.GlobalAllInit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            /** fine location のリクエストコード（値は他のパーミッションと被らなければ、なんでも良い）*/
            final int requestCode = 1;

            // いずれも得られていない場合はパーミッションのリクエストを要求する
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode );
            return;
        }


        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));

    }

    @Override
    protected void onResume() {
        super.onResume();
        // サービスの開始
        beaconManager.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // サービスの停止
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {

        Identifier uuid = Identifier.parse(UUID);
        final Region mRegion = new Region("unique-id-001", uuid, null, null);

        //スレッド立ち上げ
        new Thread(new Runnable() {
            @Override
            public void run() {
                beaconManager.addMonitorNotifier(new MonitorNotifier() {
                    @Override
                    public void didEnterRegion(Region region) {
                        // 領域侵入
                        try {
                            // レンジング開始
                            beaconManager.startRangingBeaconsInRegion(mRegion);
                        } catch (RemoteException e) {
                            // 例外が発生した場合
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void didExitRegion(Region region) {
                        // 領域退出
                        try {
                            //レンジングの停止
                            beaconManager.stopRangingBeaconsInRegion(mRegion);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void didDetermineStateForRegion(int i, Region region) {
                        // 領域に対する状態が変化

                    }

                });

                try {
                    //ビーコン情報の監視を開始
                    beaconManager.startMonitoringBeaconsInRegion(mRegion);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                beaconManager.addRangeNotifier(new RangeNotifier() {
                    @Override
                    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                        //検出したビーコンの情報
                        for (Beacon beacon : beacons) {
                            // ログの出力
                            Log.d("Beacon", "UUID:" + beacon.getId1() + ", Name:" + beacon.getBluetoothName() + ", major:" + beacon.getId2() + ", minor:" + beacon.getId3() + ", Distance:" + beacon.getDistance() + ",RSSI" + beacon.getRssi());
                            final String bName = beacon.getBluetoothName();

                            //UIスレッドでのUI操作
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Beaconを検知" + bName, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        }).start();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFragmentInteraction(Uri uri) {

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_all) {
            fragmentManager.beginTransaction().replace(R.id.container, ItemFragment.newInstance("hoge", "hoge")).commit();
        } else if (id == R.id.nav_near){
            fragmentManager.beginTransaction().replace(R.id.container, ItemFragment.newInstance("hoge", "hoge")).commit();
        } else if (id == R.id.nav_temple_shrine) {
            fragmentManager.beginTransaction().replace(R.id.container, ItemFragment.newInstance("hoge", "hoge")).commit();
        } else if (id == R.id.nav_buddha) {
            fragmentManager.beginTransaction().replace(R.id.container, ItemFragment.newInstance("hoge", "hoge")).commit();
        } else if (id == R.id.nav_historical_interest_site) {
            fragmentManager.beginTransaction().replace(R.id.container, ItemFragment.newInstance("hoge", "hoge")).commit();
        } else if (id == R.id.nav_beach) {
            fragmentManager.beginTransaction().replace(R.id.container, ItemFragment.newInstance("hoge", "hoge")).commit();
        } else if (id == R.id.nav_history) {
            fragmentManager.beginTransaction().replace(R.id.container, ItemFragment.newInstance("hoge", "hoge")).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
