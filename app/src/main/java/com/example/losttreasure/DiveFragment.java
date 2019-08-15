package com.example.losttreasure;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class DiveFragment extends Fragment {

    private MainActivity mainActivity;
    private RedLED treasureLEDs[];
    private int[] goldtreasure, silvertreasure, goldlocation, silverlocation;
    private double tiledepths[], oxygenlevel;
    public Random rng = new Random();

    private OnFragmentInteractionListener mListener;

    public DiveFragment() {
        // Required empty public constructor
    }

    public static DiveFragment newInstance() {
        DiveFragment fragment = new DiveFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();

        goldtreasure = setTreasure();
        silvertreasure = setTreasure();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dive, container, false);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void retrieveLocations () {
        Bundle locations = mainActivity.getLocations();
        goldlocation = locations.getIntArray("Gold");
        silverlocation = locations.getIntArray("Silver");
    }

    private boolean fillAir (int locationcoord[]) {
        if (locationcoord[0]==goldlocation[0]&&locationcoord[1]==goldlocation[1]
                ||locationcoord[0]==silverlocation[0]&&locationcoord[1]==silverlocation[1]) {
            oxygenlevel = 30;
            return true;
        }
        else
            return false;
    }

    private void dive () {
        final boolean buttonpressed = mainActivity.findViewById(R.id.numpad1).isPressed();
        if (buttonpressed) {
            final Timer time = new Timer();
            time.scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    int collectedtreasure = 0;
                    int loadedzone = 0;
                    double diverdepth = 0.1;

                    //begin oxygen use
                    while (oxygenlevel >= 0 && diverdepth > 0) {
                        int buttonid = R.id.numpad1;
                        //getloadedzone
                        loadedzone = buttonid;
                        oxygenlevel -= 0.1;
                        while (diverdepth != tiledepths[loadedzone]&&buttonid==loadedzone) {
                            if (diverdepth - tiledepths[loadedzone] < 0) {
                                diverdepth += 0.1;
                                //play descending sound
                            }
                            else {
                                diverdepth -= 0.1;
                                //play ascending sound
                            }
                        }
                        collectedtreasure = goldtreasure[loadedzone];
                        goldtreasure[loadedzone] = 0;
                        loadedzone = 0;
                    }
                    if (oxygenlevel <= 0) {

                    }
                    time.cancel();
                    time.purge();
                }
            }, 0, 100);
        }
    }

    private void setTileDepths () {
        tiledepths[0] = rng.nextDouble()*10+0.1;
        for (int i = 0; i < 9; i++) {
            //depth is +/- 20% of initial depth
            double nextDepth = tiledepths[i-1]+rng.nextDouble()*4-2;
            //Set bounds for depth between 0.1 and 10
            if (nextDepth > 10)
                tiledepths[i] = 10;
            else if (nextDepth <= 0)
                tiledepths[i] = 0.1;
            else
                tiledepths[i] = nextDepth;
        }
    }

    private int[] setTreasure () {
        int[] treasure = new int[9];
        for (int i = 0; i < 6; i++) {
            int tile = rng.nextInt(9);
            treasure[tile]++;
        }
        return treasure;
    }
}
