package info.redspirit.beaconinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rj on 2017/02/13.
 */

public class HttpResponsAsync extends AsyncTask<String, Integer, JSONArray> {

    public String process;
    public String sort;
    public String lang;
    HttpURLConnection con = null;
    URL url = null;
    JSONArray ja;

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected JSONArray doInBackground(String... strUrl) {
        try {
            // URLの作成
            url = new URL(strUrl[0]);
            // 接続用HttpURLConnectionオブジェクト作成
            con = (HttpURLConnection)url.openConnection();
            // リクエストメソッドの設定
            con.setRequestMethod("GET");
            // リダイレクトを自動で許可しない設定
            con.setInstanceFollowRedirects(false);
            // URL接続からデータを読み取る場合はtrue
            con.setDoInput(true);
            // URL接続にデータを書き込む場合はtrue
            con.setDoOutput(true);



            // 接続
            con.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            ja = new JSONArray(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ja;
    }

    @Override
    protected void onPostExecute(JSONArray ja) {
        try {
            for (int i = 0; i < ja.length(); i++) {
                JSONObject eventObj = ja.getJSONObject(i);
                String id = eventObj.getString("id");
                String name = eventObj.getString("name");

            }
        }catch (Exception e){

        }


        super.onPostExecute(ja);
    }



    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
