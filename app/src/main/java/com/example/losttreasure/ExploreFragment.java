package com.example.losttreasure;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ExploreFragment extends Fragment {

    private RedLED northLED, southLED, eastLED, westLED, centerLED;
    private Button numpad[];
    private int[] selectedlocation, goldlocation, silverlocation;
    private TextView numpadInput;

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
        selectedlocation = new int[2];
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

        retrieveLocations();

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

        numpadInput = mainActivity.findViewById(R.id.numpadinput);
        selectedlocation[0] = -1;
        selectedlocation[1] = -1;
        refreshNumpadInput();

        numpad = new Button[10];
        numpad[0] = mainActivity.findViewById(R.id.numpadclear);
        numpad[1] = mainActivity.findViewById(R.id.numpad1);
        numpad[2] = mainActivity.findViewById(R.id.numpad2);
        numpad[3] = mainActivity.findViewById(R.id.numpad3);
        numpad[4] = mainActivity.findViewById(R.id.numpad4);
        numpad[5] = mainActivity.findViewById(R.id.numpad5);
        numpad[6] = mainActivity.findViewById(R.id.numpad6);
        numpad[7] = mainActivity.findViewById(R.id.numpad7);
        numpad[8] = mainActivity.findViewById(R.id.numpad8);
        numpad[9] = mainActivity.findViewById(R.id.numpad9);

        NumpadInput numpadInputListener = new NumpadInput();
        for (int i = 0; i < 10; i++) {
            numpad[i].setOnClickListener(numpadInputListener);
        }

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

    //gold is 0, silver 1; nsvew[0] is NS; nsvew[1] is EW
    private int checkLocation (int locationcoord[], int gvs, int nsvew) {
        if (gvs == 0)
            return locationcoord[nsvew]-goldlocation[nsvew];
        else
            return locationcoord[nsvew]-silverlocation[nsvew];
    }

    private void compassAnimation (final int nsvew, final int direction) {
        new CountDownTimer(1800,200) {
            int interval = 0;
            public void onFinish() {
                showDirection(nsvew, direction);
                turnOffCompass();
            }
            public void onTick(long millisUntilFinished) {
                switch (interval % 4) {
                    case 0:
                        northLED.blinkntimes(1,100);
                        break;
                    case 1:
                        eastLED.blinkntimes(1,100);
                        break;
                    case 2:
                        southLED.blinkntimes(1,100);
                        break;
                    case 3:
                        westLED.blinkntimes(1,100);
                        break;
                }
                interval++;
            }
        }.start();
    }

    private void showDirection (int nsvew, int direction) {

        if (direction == 0)
            centerLED.blinkntimes(3,500);
        else if (nsvew == 0) {
            if (direction < 0)
                northLED.blinkntimes(3,500);
            else
                southLED.blinkntimes(3,500);
        }
        else if (nsvew == 1) {
            if (direction < 0)
                eastLED.blinkntimes(3,500);
            else
                westLED.blinkntimes(3,500);
        }
    }

    private void turnOffCompass () {
        northLED.turnOff();
        southLED.turnOff();
        eastLED.turnOff();
        westLED.turnOff();
        centerLED.turnOff();
    }

    private void refreshNumpadInput () {
        String numbers;
        if (selectedlocation[0] != -1)
            numbers = selectedlocation[0]+" ";
        else
            numbers = "_ ";

        if (selectedlocation[1] != -1)
            numbers += selectedlocation[1]+" ";
        else
            numbers += "_";

        numpadInput.setText(numbers);
    }

    class GSSearch implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int gvs, nsvew;
            //g: 0, s: 1, ns: 0, ew: 1
            switch (v.getId()) {
                case R.id.goldNS:
                    gvs = 0;
                    nsvew = 0;
                    break;
                case R.id.goldEW:
                    gvs = 0;
                    nsvew = 1;
                    break;
                case R.id.silverNS:
                    gvs = 1;
                    nsvew = 0;
                    break;
                case R.id.silverEW:
                    gvs = 1;
                    nsvew = 1;
                    break;

                    default:
                        gvs = 0;
                        nsvew = 0;
                        break;
            }
            int direction = checkLocation(selectedlocation,gvs,nsvew);
            compassAnimation(gvs,direction);
            selectedlocation[0] = -1;
            selectedlocation[1] = -1;
        }
    }

    class NumpadInput implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.numpadclear:
                    clearLocationNumbers();
                    break;
                case R.id.numpad1:
                    setLocationNumber(1);
                    break;
                case R.id.numpad2:
                    setLocationNumber(2);
                    break;
                case R.id.numpad3:
                    setLocationNumber(3);
                    break;
                case R.id.numpad4:
                    setLocationNumber(4);
                    break;
                case R.id.numpad5:
                    setLocationNumber(5);
                    break;
                case R.id.numpad6:
                    setLocationNumber(6);
                    break;
                case R.id.numpad7:
                    setLocationNumber(7);
                    break;
                case R.id.numpad8:
                    setLocationNumber(8);
                    break;
                case R.id.numpad9:
                    setLocationNumber(9);
                    break;
            }
        }

        private void setLocationNumber (int num) {
            if (selectedlocation[0] == -1)
                selectedlocation[0] = num;
            else if (selectedlocation[1] == -1)
                selectedlocation[1] = num;
            refreshNumpadInput();
        }

        private void clearLocationNumbers () {
            selectedlocation[0] = -1;
            selectedlocation[1] = -1;
            refreshNumpadInput();
        }
    }
}
