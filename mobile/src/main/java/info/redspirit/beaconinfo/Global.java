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
    ArrayList<String>testArray;
    ArrayList<Integer>testIdArray;

    public void GlobalAllInit() {
        nameArray = new ArrayList<String>();
        idArray = new ArrayList<Integer>();
        testArray = new ArrayList<String>();

        testArray.add("↓Dummy↓");
        testArray.add("仏日庵");
        testArray.add("東慶寺");
        testArray.add("浄智寺");
        testArray.add("半僧坊大権現");
        testArray.add("明月院");
        testArray.add("妙光院");
        testArray.add("建長寺");
        testArray.add("来迎寺");
        testArray.add("覚園寺");
        testArray.add("瑞泉寺");
        testArray.add("↑Dummy↑");

    }

    public void GlobalArrayInit(){
        nameArray = new ArrayList<String>();
        idArray = new ArrayList<Integer>();
    }

}
