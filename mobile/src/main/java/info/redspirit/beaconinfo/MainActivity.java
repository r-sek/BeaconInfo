package info.redspirit.beaconinfo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ItemFragment.OnFragmentInteractionListener {

    TextView uuidTxt;
    TextView majorTxt;
    TextView minorTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        uuidTxt = (TextView)findViewById(R.id.uuidTxt);
        majorTxt = (TextView)findViewById(R.id.majorTxt);
        minorTxt = (TextView)findViewById(R.id.minorTxt);

        uuidTxt.setText("standby");
        majorTxt.setText("standby");
        minorTxt.setText("standby");

        final BluetoothManager bluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothAdapter.startLeScan(mLeScanCallback);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        String uuid;
        String major;
        String minor;

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            if(scanRecord.length > 30)
            {
                //iBeacon の場合 6 byte 目から、 9 byte 目はこの値に固定されている。
                if((scanRecord[5] == (byte)0x4c) && (scanRecord[6] == (byte)0x00) &&
                        (scanRecord[7] == (byte)0x02) && (scanRecord[8] == (byte)0x15))
                {
                    uuid = IntToHex2(scanRecord[9] & 0xff)
                        + IntToHex2(scanRecord[10] & 0xff)
                        + IntToHex2(scanRecord[11] & 0xff)
                        + IntToHex2(scanRecord[12] & 0xff)
                        + "-"
                        + IntToHex2(scanRecord[13] & 0xff)
                        + IntToHex2(scanRecord[14] & 0xff)
                        + "-"
                        + IntToHex2(scanRecord[15] & 0xff)
                        + IntToHex2(scanRecord[16] & 0xff)
                        + "-"
                        + IntToHex2(scanRecord[17] & 0xff)
                        + IntToHex2(scanRecord[18] & 0xff)
                        + "-"
                        + IntToHex2(scanRecord[19] & 0xff)
                        + IntToHex2(scanRecord[20] & 0xff)
                        + IntToHex2(scanRecord[21] & 0xff)
                        + IntToHex2(scanRecord[22] & 0xff)
                        + IntToHex2(scanRecord[23] & 0xff)
                        + IntToHex2(scanRecord[24] & 0xff);

                    major = IntToHex2(scanRecord[25] & 0xff) + IntToHex2(scanRecord[26] & 0xff);
                    minor = IntToHex2(scanRecord[27] & 0xff) + IntToHex2(scanRecord[28] & 0xff);
                }
            }
            uuidTxt.setText(uuid);
            majorTxt.setText(major);
            minorTxt.setText(minor);
        }
    };

    //intデータを 2桁16進数に変換するメソッド
    public String IntToHex2(int i) {
        char hex_2[] = {Character.forDigit((i>>4) & 0x0f,16),Character.forDigit(i&0x0f, 16)};
        String hex_2_str = new String(hex_2);
        return hex_2_str.toUpperCase();
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

        if (id == R.id.nav_near) {
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
