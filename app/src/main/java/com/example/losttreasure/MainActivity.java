package com.example.losttreasure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private int goldlocation[], silverlocation[], goldtreasure[], silvertreasure[];
    private double tiledepths[], oxygenlevel;
    public Random rng = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goldlocation = new int[2];
        silverlocation = new int[2];

        setLocations(0);
        setLocations(1);
        goldtreasure = setTreasure();
        silvertreasure = setTreasure();
        TextView goldlocationtext = findViewById(R.id.goldLocation);
        TextView silverlocationtext = findViewById(R.id.silverLocation);
        goldlocationtext.setText("Gold: " + String.valueOf(goldlocation[0])+String.valueOf(goldlocation[1]));
        silverlocationtext.setText("Silver: "+String.valueOf(silverlocation[0])+String.valueOf(silverlocation[1]));

        Button goldNSCheck = (Button)findViewById(R.id.goldNS);
        goldNSCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] test = new int[2];
                EditText input = findViewById(R.id.numpadinput);
                test[0] = Integer.parseInt(input.getText().toString())/10;
                test[1] = Integer.parseInt(input.getText().toString())%10;
                int ns = checkLocation(test,0,0);
                TextView compass = findViewById(R.id.compass);
                if (ns > 0)
                    compass.setText(ns + "North");
                else if (ns < 0)
                    compass.setText(ns + "south");
                else
                    compass.setText("Here");
            }
        });
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

    //gold is 0, silver 1; nsvew just changes which array item to get
    private int checkLocation (int locationcoord[], int gvs, int nsvew) {
        System.out.println(locationcoord[nsvew]+"-"+goldlocation[nsvew]);
        if (gvs == 0)
            return locationcoord[nsvew]-goldlocation[nsvew];
        else
            return locationcoord[nsvew]-silverlocation[nsvew];
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
        final boolean buttonpressed = findViewById(R.id.numpad1).isPressed();
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
        for (int i = 0; i < 9; i++) {
            int tile = rng.nextInt(9);
            treasure[tile]++;
        }
        return treasure;
    }



}
