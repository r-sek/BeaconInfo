package info.redspirit.beaconinfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Global global;
    CardRecyclerView crv;
    CardRecyclerAdapter crva;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressDialog waitDialog;
    private OnFragmentInteractionListener mListener;
    private View v;

    public ItemFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Handler handler = new Handler();
        final LayoutInflater fInrlater = inflater;
        final ViewGroup fcontainer = container;

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        v = fInrlater.inflate(R.layout.fragment_item, fcontainer, false);
//                    }
//                });
//            }
//        });

        v = fInrlater.inflate(R.layout.fragment_item, fcontainer, false);
        return v;
    }

    @Override
    public void onStart() {
        Log.i("fragment","onStart");
        crv = (CardRecyclerView)v.findViewById(R.id.cardRecyclerView1);
        global = (Global) getActivity().getApplication();
        global.GlobalArrayInit();
        HttpResponsAsync hra = new HttpResponsAsync(new AsyncCallback() {
            @Override
            public void onPreExecute() {
                // プログレスダイアログの設定
                waitDialog = new ProgressDialog(getContext());
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
                        String id = eventObj.getString("spot_id");
                        String name = eventObj.getString("spot_name");
                        global.idArray.add(Integer.parseInt(id));
                        global.nameArray.add(name);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                Log.i("ArraySize",String.valueOf(global.nameArray.size()));
                if(global.nameArray.size() != 0){
                    crv.setRecyclerAdapterB(getContext(),global.nameArray,global.idArray);
                }else{
//                    crv.setRecyclerAdapterB(getContext(),global.testArray);
                }
                //プログレスダイアログ消す
                if (waitDialog.isShowing()){waitDialog.dismiss();}

            }

            @Override
            public void onProgressUpdate(int progress) {

            }

            @Override
            public void onCancelled() {

            }
        });
        hra.execute("http://sample-env-2.3p4ikwvwvd.us-west-2.elasticbeanstalk.com/listprocess.php");

        if(global.nameArray.size() != 0){
            crv.setRecyclerAdapterB(getContext(),global.nameArray,global.idArray);
        }else{
//                    crv.setRecyclerAdapterB(getContext(),global.testArray);
        }

        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
