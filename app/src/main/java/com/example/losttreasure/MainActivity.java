package com.example.losttreasure;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ExploreFragment.OnFragmentInteractionListener, DiveFragment.OnFragmentInteractionListener  {

    private int goldlocation[], silverlocation[];
    public Random rng = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goldlocation = new int[2];
        silverlocation = new int[2];

        setLocations(0);
        setLocations(1);
        TextView goldlocationtext = findViewById(R.id.goldLocation);
        TextView silverlocationtext = findViewById(R.id.silverLocation);
        goldlocationtext.setText("G: " + String.valueOf(goldlocation[0])+String.valueOf(goldlocation[1]));
        silverlocationtext.setText("S: "+String.valueOf(silverlocation[0])+String.valueOf(silverlocation[1]));


        ExploreFragment exploreFragment = new ExploreFragment();
        DiveFragment diveFragment = new DiveFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragmentcontainer,exploreFragment).commit();

    }

    @Override
    public void onResume(){
        super.onResume();

        View main = findViewById(R.id.main);
        main.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void setLocations (int gvs) {
        int row = rng.nextInt(8)+1;
        int col = rng.nextInt(8)+1;
        //gold = 0 silver = 1
        if (gvs == 0) {
            goldlocation[0] = row;
            goldlocation[1] = col;
        }
        else {
            silverlocation[0] = row;
            silverlocation[1] = col;
        }
    }

    public Bundle getLocations () {
        Bundle locations = new Bundle();
        locations.putIntArray("Gold",goldlocation);
        locations.putIntArray("Silver",silverlocation);
        return locations;
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

}
