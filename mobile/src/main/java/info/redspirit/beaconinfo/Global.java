package info.redspirit.beaconinfo;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by rj on 2017/02/13.
 */

public class Global extends Application {
    String sort;
    ArrayList<String>nameArray;
    ArrayList<Integer>idArray;

    public void GlobalAllInit() {
        nameArray = new ArrayList<>();
        idArray = new ArrayList<>();
    }

}
