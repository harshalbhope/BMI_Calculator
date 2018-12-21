package com.example.acer.bmi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Binfo extends AppCompatActivity {

    TextView tvWeight,tvFeet,tvInch,tvHeight,tvName;
    EditText etWeight;
    Spinner spFeet,spInch;
    Button btnCal,btnView;
    SharedPreferences sp1;

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


    }
}
