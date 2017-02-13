package info.redspirit.beaconinfo;

import android.os.AsyncTask;

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

public class HttpResponsAsync extends AsyncTask<String, Integer, JSONObject> {

    public String process;
    public String sort;
    public String strUrl;
    HttpURLConnection con = null;
    URL url = null;
    InputStream in;
    JSONObject jo;

    @Override
    protected void onPreExecute() {
        if(process.equals("list")){
            strUrl = "http://sample-env-2.3p4ikwvwvd.us-west-2.elasticbeanstalk.com/listprocess.php";
        }else if(process.equals("info")){
            strUrl = "http://sample-env-2.3p4ikwvwvd.us-west-2.elasticbeanstalk.com/infoprocess.php";
        }else if(process.equals("beacon")){
            strUrl = "http://sample-env-2.3p4ikwvwvd.us-west-2.elasticbeanstalk.com/beaconinfoprocess.php";
        }else{
            strUrl = "http://sample-env-2.3p4ikwvwvd.us-west-2.elasticbeanstalk.com/listprocess.php";
        }

        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... strUrl) {
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

            try
            {
                in.close();
            }catch(Exception e){
                e.printStackTrace();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

//    @Override
//    protected JSONObject doInBackground(String URL) {
//
//        try {
//            // URLの作成
//            url = new URL(strUrl);
//            // 接続用HttpURLConnectionオブジェクト作成
//            con = (HttpURLConnection)url.openConnection();
//            // リクエストメソッドの設定
//            con.setRequestMethod("GET");
//            // リダイレクトを自動で許可しない設定
//            con.setInstanceFollowRedirects(false);
//            // URL接続からデータを読み取る場合はtrue
//            con.setDoInput(true);
//            // URL接続にデータを書き込む場合はtrue
//            con.setDoOutput(true);
//
//            // 接続
//            con.connect();
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    @Override
    protected void onPostExecute(JSONObject o) {

        super.onPostExecute(o);
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
}
