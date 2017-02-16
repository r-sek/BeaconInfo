package info.redspirit.beaconinfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;


public class InfoActivity extends AppCompatActivity implements LocationListener {

    ImageView iv;
    TextView placeNameTxt;
    TextView infoTxt;
    Button bt;
    Spinner spinner;
    private String process;
    private String[] WORDS;
    private String id;
    private String latitude;
    private String longitude;
    private Double nowLatitude;
    private Double nowLongitude;
    private String imageUrl;
    private String name;
    private String info;


    private LocationManager locationManager;
    private ProgressDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        iv = (ImageView) findViewById(R.id.InfoMainImage);
        placeNameTxt = (TextView) findViewById(R.id.placeNameTxt);
        infoTxt = (TextView) findViewById(R.id.infoTxt);

        Intent intent = getIntent();
        int getId = intent.getIntExtra("id",1);


        HttpResponsAsync hra = new HttpResponsAsync(new AsyncCallback() {
            @Override
            public void onPreExecute() {
                // プログレスダイアログの設定
                waitDialog = new ProgressDialog(InfoActivity.this);
                // プログレスダイアログのメッセージを設定します
                waitDialog.setMessage("NOW LOADING...");
                // 円スタイル（くるくる回るタイプ）に設定します
                waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                waitDialog.setIndeterminate(true);
                // プログレスダイアログを表示
                waitDialog.show();
            }

            @Override
            public void onPostExecute(JSONArray ja) {
                try {
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject eventObj = ja.getJSONObject(i);
                        id = eventObj.getString("spot_id");
                        latitude = eventObj.getString("latitude");
                        longitude = eventObj.getString("longitude");
                        imageUrl = eventObj.getString("image_url");
                        name = eventObj.getString("spot_name");
                        info = eventObj.getString("spot_info");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(imageUrl.equals("sample")){

                }else{

                }
                placeNameTxt.setText(name);
                infoTxt.setText(info);

                if (waitDialog.isShowing()){waitDialog.dismiss();}

            }



            @Override
            public void onProgressUpdate(int progress) {

            }

            @Override
            public void onCancelled() {

            }
        });
        hra.execute("http://sample-env-2.3p4ikwvwvd.us-west-2.elasticbeanstalk.com/infoprocess.php?id="+getId);


        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //スピナー選択
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                process = (String)spinner.getSelectedItem();
                Log.i("spinner",process);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //ロケーション取得関連
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        else{
            locationStart();
        }

        bt = (Button) findViewById(R.id.visitBtn);
        //ボタン
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test0();
            }
        });

    }

    private void locationStart(){
        Log.d("debug","locationStart()");

        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            // GPSを設定するように促す
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            Log.d("debug", "gpsEnable, startActivity");
        } else {
            Log.d("debug", "gpsEnabled");
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);

            Log.d("debug", "checkSelfPermission false");
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, this);
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug","checkSelfPermission true");

                locationStart();
                bt.setEnabled(true);
                return;

            } else {
                // それでも拒否された時の対応
                Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
                bt.setEnabled(false);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        nowLatitude = location.getLatitude();
        nowLongitude = location.getLongitude();

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //TODO:自前マップ実装
    protected void goMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


    private void test0() {
//        String srcLatitude = String.valueOf(nowLatitude);
//        String srcLongitude = String.valueOf(nowLongitude);
        String desLatitude = latitude;
        String desLongitude = longitude;

        String start = "新宿駅";
        String destination = "鶴岡八幡宮";
        String dir;

        // 電車:r
        //String dir = "r";
        // 車:d
        //String dir = "d";
        // 歩き:w
        //String dir = "w";
        if(process.equals("Car") || process.equals("車")){
            dir = "d";
        }else if(process.equals("Train") || process.equals("電車")){
            dir = "r";
        }else{
            dir = "w";
        }

        Log.i("dir",dir);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//        intent.setData(Uri.parse("http://maps.google.com/maps?saddr=" + start + "&daddr=" + destination + "&dirflg=" + dir));
        intent.setData(Uri.parse("http://maps.google.com/maps?saddr="+String.valueOf(nowLatitude)+","+String.valueOf(nowLongitude)+"&daddr="+desLatitude+","+desLongitude + "&dirflg=" + dir));
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
