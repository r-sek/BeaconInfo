package info.redspirit.beaconinfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class BeaconInfoActivity extends AppCompatActivity {

    int getId;
    TextView titleTxt;
    TextView mainTxt;
    TextView viewTitleTxt;

    private String id;
    private String latitude;
    private String longitude;
    private String imageUrl;
    private String name;
    private String info;
    private ProgressDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleTxt= (TextView)findViewById(R.id.infoMainTxt);
        mainTxt = (TextView)findViewById(R.id.mainTxt);
        viewTitleTxt = (TextView)findViewById(R.id.recommendTitleTxt);

        titleTxt.setText(getResources().getString(R.string.info));

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        getId = intent.getIntExtra("id",1);


        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String url;

        if(language.equals("ja")){
            url = "http://sample-env-2.3p4ikwvwvd.us-west-2.elasticbeanstalk.com/ja/infoprocess.php?id=" + getId;
        }else{
            url = "http://sample-env-2.3p4ikwvwvd.us-west-2.elasticbeanstalk.com/en/infoprocess.php?id=" + getId;
        }

        HttpResponsAsync hra = new HttpResponsAsync(new AsyncCallback() {
            @Override
            public void onPreExecute() {
                // プログレスダイアログの設定
                waitDialog = new ProgressDialog(BeaconInfoActivity.this);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (imageUrl.equals("sample")) {

                } else {

                }
                titleTxt.setText(name);
                mainTxt.setText(info);

                if (waitDialog.isShowing()) {
                    waitDialog.dismiss();
                }
            }

            @Override
            public void onProgressUpdate(int progress) {

            }

            @Override
            public void onCancelled() {

            }
        });
        hra.execute(url);
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
