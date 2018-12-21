package com.example.acer.bmi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BmiResult extends AppCompatActivity {

    TextView tvResult,tvIndex1,tvIndex2,tvIndex3,tvIndex4;
    Button btnSave,btnBack,btnShare;
    SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_result);

        tvResult = (TextView)findViewById(R.id.tvResult);
        tvIndex1 = (TextView)findViewById(R.id.tvIndex1);
        tvIndex2 = (TextView)findViewById(R.id.tvIndex2);
        tvIndex3 = (TextView)findViewById(R.id.tvIndex3);
        tvIndex4 = (TextView)findViewById(R.id.tvIndex4);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnBack = (Button)findViewById(R.id.btnBack);
        btnShare = (Button)findViewById(R.id.btnShare);

        sp1 = getSharedPreferences("Myp1",MODE_PRIVATE);


        final String name = sp1.getString("n","");
        final String age = sp1.getString("age","");
        final String bmi = sp1.getString("bmi", "");
        final String phone = sp1.getString("phone","");
//        final String sex = sp1.getString("sex","");


        final String index;



        tvIndex1.setText("Below 18.5 is Underweight");
        tvIndex2.setText("Between 18.5 to 25 is Normal");
        tvIndex3.setText("Between 25 to 30 is Overweight");
        tvIndex4.setText("Above 30 is Obese");


        if(Float.parseFloat(bmi) < 18.5){
            tvIndex1.setTypeface(null, Typeface.BOLD);
            index = "You are Underweight";
        }else if (Float.parseFloat(bmi) < 25){
            tvIndex2.setTypeface(null, Typeface.BOLD);
            index = "You are Normal";
        }
        else if (Float.parseFloat(bmi) < 30){
            tvIndex3.setTypeface(null, Typeface.BOLD);
            index = "You are Overweight";
        }else {
            tvIndex4.setTypeface(null, Typeface.BOLD);
            index = "You are Obese";
        }

        tvResult.setText(" Bmi: "+bmi+" and "+index);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BmiResult.this, Binfo.class));
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                String msg = "Name: "+name+"/n Age: "+age+"/n Phone: "+phone+"Bmi: "+bmi+"/n "+index;
                Toast.makeText(BmiResult.this, msg, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,msg);
                startActivity(intent);






            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                MainActivity.db.addRecord(bmi, name);
     //           etAddRno.setText("");
//                etAddName.setText("");
//                etAddRno.requestFocus();


            }
        });



    }
}
