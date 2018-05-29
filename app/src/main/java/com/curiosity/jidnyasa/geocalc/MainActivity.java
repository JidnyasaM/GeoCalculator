package com.curiosity.jidnyasa.geocalc;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    public static final int Settings_Activity = 1;
    public static String  distanceUnit = "Kilometers";
    public static String bearingUnit = "Degrees";
    public Float distanceInKilometers;
    public Float bearingInDegrees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //EditText
        final EditText latitude_p1 = findViewById(R.id.latitude_p1);
        final EditText longitude_p1 = findViewById(R.id.longitude_p1);
        final EditText latitude_p2 = findViewById(R.id.latitude_p2);
        final EditText longitude_p2 = findViewById(R.id.longitude_p2);

        //TextView
        final TextView distanceAnswer = findViewById(R.id.distanceAnswer);
        final TextView bearingAnswer = findViewById(R.id.bearingAnswer);
        final TextView distanceUnits = findViewById(R.id.distanceUnits);
        final TextView bearingUnits = findViewById(R.id.bearingUnit);

        //Button
        Button calculate = findViewById(R.id.calculate);
        Button clear = findViewById(R.id.clear);


        //If calculate is clicked
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lat1 = latitude_p1.getText().toString();
                String lat2 = latitude_p2.getText().toString();
                String long1 = longitude_p1.getText().toString();
                String long2 = longitude_p2.getText().toString();
                if (lat1.length() == 0 || lat2.length() == 0 || long1.length() == 0 || long2.length() == 0) {
                    //Disappear keypad on button click
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(latitude_p1.getWindowToken(), 0);
                    Toast.makeText(MainActivity.this,R.string.enterValues,Toast.LENGTH_SHORT).show();
                    return;
                }

                if (lat1.length() != 0 || lat2.length() != 0 || long1.length() != 0 || long2.length() != 0) {
                    //Disappear keypad on button click
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(latitude_p1.getWindowToken(), 0);
                }

                //Getting the p1 location
                Location loc1 = new Location("");
                loc1.setLatitude(Double.parseDouble(latitude_p1.getText().toString()));
                loc1.setLongitude(Double.parseDouble(longitude_p1.getText().toString()));

                //Getting the p2 location
                Location loc2 = new Location("");
                loc2.setLatitude(Double.parseDouble(latitude_p2.getText().toString()));
                loc2.setLongitude(Double.parseDouble(longitude_p2.getText().toString()));

                //Calculating the distance between p1 and p2
                Float distanceInMeters = loc1.distanceTo(loc2);
                distanceInKilometers = distanceInMeters / 1000;

                //Calculating the bearing between p1 and p2
                bearingInDegrees = loc1.bearingTo(loc2);

                //Setting the answers to the TextViews
                unitSetter();
            }
        });

        //If clear is clicked
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear EditText
                latitude_p1.getText().clear();
                latitude_p2.getText().clear();
                longitude_p1.getText().clear();
                longitude_p2.getText().clear();

                //Clears the distance and bearing
                if (distanceAnswer.length() != 0 && bearingAnswer.length() != 0) {
                    distanceAnswer.setText("");
                    bearingAnswer.setText("");
                    distanceUnits.setText("");
                    bearingUnits.setText("");
                }

                //Disappear keypad on button click
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(latitude_p1.getWindowToken(), 0);
            }
        });
    }

    private void unitSetter() {
        TextView distanceLabel = findViewById(R.id.distanceUnits);
        TextView distanceAns = findViewById(R.id.distanceAnswer);
        TextView bearingLabel = findViewById(R.id.bearingUnit);
        TextView bearingAns = findViewById(R.id.bearingAnswer);

        if(distanceUnit.equals("Kilometers")) {
            DecimalFormat df = new DecimalFormat("###.##");
            String distInKilometers = String.valueOf(df.format(distanceInKilometers));
            distanceAns.setText(distInKilometers);
            distanceLabel.setText("Kilometers");
        }

        if(distanceUnit.equals("Miles")) {
            Double distanceInMiles = distanceInKilometers * 17.7777777778;
            DecimalFormat df = new DecimalFormat("###.##");
            String distInMiles = String.valueOf(df.format(distanceInMiles));
            distanceAns.setText(distInMiles);
            distanceLabel.setText("Miles");
        }

        if(bearingUnit.equals("Degrees")) {
            DecimalFormat df2 = new DecimalFormat("###.##");
            String bearInDegrees = String.valueOf(df2.format(bearingInDegrees));
            bearingAns.setText(bearInDegrees);
            bearingLabel.setText("Degrees");
        }

        if(bearingUnit.equals("Mils")){
            Double bearingInMils = bearingInDegrees * 0.621371;
            DecimalFormat df2 = new DecimalFormat("###.##");
            String bearInMils = String.valueOf(df2.format(bearingInMils));
            bearingAns.setText(bearInMils);
            bearingLabel.setText("Mils");
        }

    }


    //View Settings om Task bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //When 'settings' are clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent();
                Intent launchNewIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(launchNewIntent,Settings_Activity);
                return true;
        }
        return false;
    }

    //For Distance Spinner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Settings_Activity) {
            if(resultCode == RESULT_OK) {
                //do unit switch
                distanceUnit = data.getStringExtra("Distance Unit");
                bearingUnit = data.getStringExtra("Bearing Unit");
                unitSetter();
            }
        }
    }

}
