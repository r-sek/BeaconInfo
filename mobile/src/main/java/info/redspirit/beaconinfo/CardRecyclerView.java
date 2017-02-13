package info.redspirit.beaconinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by rj on 2017/01/20.
 */

public class CardRecyclerView extends RecyclerView {


    public CardRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRecyclerAdapter(context);
    }

    public void setRecyclerAdapter(Context context) {

        setLayoutManager(new LinearLayoutManager(context));
        //setAdapter(new CardRecyclerAdapter(context, context.getResources().getStringArray(R.array.dummy)));
        setAdapter(new CardRecyclerAdapter(context));
    }
}
