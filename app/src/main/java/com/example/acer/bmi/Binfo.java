package com.example.acer.bmi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class Binfo extends AppCompatActivity {

    TextView tvWeight,tvFeet,tvInch,tvHeight,tvName,tvLoc;
    EditText etWeight;
    Spinner spFeet,spInch;
    Button btnCal,btnView;
    SharedPreferences sp1;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lattitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binfo);

        btnCal = (Button)findViewById(R.id.btnCal);
        btnView = (Button)findViewById(R.id.btnView);
        etWeight = (EditText)findViewById(R.id.etWeight);
        tvWeight = (TextView)findViewById(R.id.tvWeight);
        tvFeet = (TextView)findViewById(R.id.tvFeet);
        tvInch = (TextView)findViewById(R.id.tvInch);
        tvHeight = (TextView)findViewById(R.id.tvHeight);
        spFeet = (Spinner)findViewById(R.id.spFeet);
        spInch = (Spinner)findViewById(R.id.spInch);
        tvName = (TextView)findViewById(R.id.tvName);


        sp1 = getSharedPreferences("Myp1",MODE_PRIVATE);

        final String rn = sp1.getString("n","");
        tvName.setText("Welcome "+rn);

        Integer[] items = new Integer[]{1,2,3,4,5,6,7};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        spFeet.setAdapter(adapter);

        Integer[] items1 = new Integer[]{1,2,3,4,5,6,7,8,9,10,11};
        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items1);
        spInch.setAdapter(adapter1);

        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);


        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DecimalFormat df = new DecimalFormat("##.##");
                df.setRoundingMode(RoundingMode.DOWN);

                Integer feet = (Integer) spFeet.getSelectedItem();
                Integer inch = (Integer) spFeet.getSelectedItem();
                String weight = etWeight.getText().toString();

                if (weight.length() == 0 ){
                    etWeight.setError("Weight is empty");
                    etWeight.requestFocus();
                    return;
                }

                float height = (float) (((feet*12) + inch) *0.0254);


                float bmi = Integer.parseInt(weight)/(height * height);
                Toast.makeText(Binfo.this,"BMI is "+df.format(bmi),Toast.LENGTH_LONG).show();

                SharedPreferences.Editor editor = sp1.edit();
                editor.putString("bmi", String.valueOf(df.format(bmi)));


                editor.commit();



                startActivity(new Intent(Binfo.this, BmiResult.class));
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Binfo.this,ViewHistory.class));
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_LOCATION);

        tvLoc = (TextView)findViewById(R.id.tvLoc);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }


    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(Binfo.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (Binfo.this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Binfo.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                getAddress(latti,longi);

//
//                tvLoc.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                getAddress(latti,longi);

//                tvLoc.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);


            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                getAddress(latti,longi);

//                tvLoc.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);

            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }

        }
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(Binfo.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
//            String add = obj.getAddressLine(0);
//            add = add + "\n" + obj.getAddressLine(1);
//
//            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare();
//tvLoc.setText(add);


//            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();



            updateWeatherData(obj.getSubAdminArea());

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }







    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("no", null).show();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.m1, menu);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about){
            Toast.makeText(this, "Application is developed by Mr.Harshal Bhope", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.website){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://" + "www.harshalbhope.com"));
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }




    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(Binfo.this, city);
                Handler handler = null;
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(Binfo.this, "Json Problem", Toast.LENGTH_SHORT).show();
                        }

                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        try {

            JSONObject main = json.getJSONObject("main");
            tvLoc.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }



}
public class RemoteFetch {

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    @Nullable
    public static JSONObject getJSON(Context context, String city){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("e06358270ff36d40227b0dae53290a5b",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }
}
