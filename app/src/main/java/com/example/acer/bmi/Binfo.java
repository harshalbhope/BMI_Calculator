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
import android.os.AsyncTask;
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

import org.json.JSONException;
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
    int count = 0;
    SharedPreferences sp1;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lattitude,longitude;
    String OPEN_WEATHER_MAP_API = "e06358270ff36d40227b0dae53290a5b";


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
            String add = obj.getLocality();

            add = add + "," + obj.getPostalCode();
            add = add + "," + obj.getAdminArea();
            add = add + "," + obj.getCountryName()+".";
            tvLoc.setText(add);

            taskLoadUp(obj.getLocality().toString());

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




    public void taskLoadUp(String query) {
        if (Function.isNetworkAvailable(getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            Log.d("city",query);
            task.execute(query);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }



    class DownloadWeather extends AsyncTask< String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loader.setVisibility(View.VISIBLE);


        }

        protected String doInBackground(String...args) {
            Log.d("arg[0]",args[0]);
            String xml = Function.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
//            Log.d("xml",xml);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            if (xml != null){
            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
//                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
//                    DateFormat df = DateFormat.getDateTimeInstance();

//                    cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));
//                    detailsField.setText(details.getString("description").toUpperCase(Locale.US));
                    String add = (String) tvLoc.getText();
                    tvLoc.setText(add+" Temp:"+String.format("%.2f", main.getDouble("temp")) + "°");
//                    humidity_field.setText("Humidity: " + main.getString("humidity") + "%");
//                    pressure_field.setText("Pressure: " + main.getString("pressure") + " hPa");
//                    updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));
//                    weatherIcon.setText(Html.fromHtml(Function.setWeatherIcon(details.getInt("id"),
//                            json.getJSONObject("sys").getLong("sunrise") * 1000,
//                            json.getJSONObject("sys").getLong("sunset") * 1000)));

//                    loader.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }


        }else {
                Toast.makeText(Binfo.this, "xml error", Toast.LENGTH_SHORT).show();
            }

        }



    }









}
