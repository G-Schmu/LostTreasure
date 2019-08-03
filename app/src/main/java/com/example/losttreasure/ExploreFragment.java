package com.example.losttreasure;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ExploreFragment extends Fragment {

    private RedLED northLED, southLED, eastLED, westLED, centerLED;
    private int[] goldlocation, silverlocation;

    private OnFragmentInteractionListener mListener;

    private MainActivity mainActivity;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button goldNSCheck = mainActivity.findViewById(R.id.goldNS);
        goldNSCheck.setOnClickListener(new GSSearch());
        Button goldEWCheck = mainActivity.findViewById(R.id.goldEW);
        goldEWCheck.setOnClickListener(new GSSearch());
        Button silverNSCheck = mainActivity.findViewById(R.id.silverNS);
        silverNSCheck.setOnClickListener(new GSSearch());
        Button silverEWCheck = mainActivity.findViewById(R.id.silverEW);
        silverEWCheck.setOnClickListener(new GSSearch());

        northLED = mainActivity.findViewById(R.id.northLED);
        southLED = mainActivity.findViewById(R.id.southLED);
        eastLED = mainActivity.findViewById(R.id.eastLED);
        westLED = mainActivity.findViewById(R.id.westLED);
        centerLED = mainActivity.findViewById(R.id.centerLED);

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void retrieveLocations () {
        Bundle locations = mainActivity.getLocations();
        goldlocation = locations.getIntArray("Gold");
        silverlocation = locations.getIntArray("Silver");
    }

    //gold is 0, silver 1; nsvew just changes which array item to get
    private int checkLocation (int locationcoord[], int gvs, int nsvew) {
        System.out.println(locationcoord[nsvew]+"-"+goldlocation[nsvew]);
        if (gvs == 0)
            return locationcoord[nsvew]-goldlocation[nsvew];
        else
            return locationcoord[nsvew]-silverlocation[nsvew];
    }

    private void compassAnimation (int nsvew, int direction) {
        northLED.turnoff();
        southLED.turnoff();
        eastLED.turnoff();
        westLED.turnoff();
        centerLED.turnoff();
        if (direction == 0)
            centerLED.turnOn();
        else if (nsvew == 0) {
            if (direction > 0)
                northLED.turnOn();
            else if (direction < 0)
                southLED.turnOn();
        }
        else if (nsvew == 1) {
            if (direction > 0)
                eastLED.turnOn();
            else if (direction < 0)
                westLED.turnOn();
        }
    }

    class GSSearch implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int[] test = new int[2];
            TextView input = mainActivity.findViewById(R.id.numpadinput);
            test[0] = Integer.parseInt(input.getText().toString())/10;
            test[1] = Integer.parseInt(input.getText().toString())%10;
            int ns = checkLocation(test,0,0);
            compassAnimation(0,ns);
        }
    }
}
