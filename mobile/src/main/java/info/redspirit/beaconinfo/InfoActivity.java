package info.redspirit.beaconinfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import static info.redspirit.beaconinfo.R.id.textView;

public class InfoActivity extends AppCompatActivity {

    ImageView iv;
    TextView placeNameTxt;
    TextView infoTxt;
    Spinner spinner;
    String process;
    String[] WORDS;

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

        Button bt = (Button) findViewById(R.id.visitBtn);
        //ボタン
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test0();
            }
        });

    }

    protected void goMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void test0() {
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
        intent.setData(Uri.parse("http://maps.google.com/maps?saddr=" + start + "&daddr=" + destination + "&dirflg=" + dir));
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
