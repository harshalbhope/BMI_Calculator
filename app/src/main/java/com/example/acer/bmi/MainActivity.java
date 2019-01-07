package com.example.acer.bmi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    TextView tvPersonal;
    EditText etName,etAge,etPhone;
    RadioButton rbMale,rbFemale;
    Button btnReg;
    RadioGroup rdGender;
    SharedPreferences sp1;
    static MyDbHandler db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPersonal = (TextView)findViewById(R.id.tvPersonal);
        etName = (EditText)findViewById(R.id.etName);
        etAge = (EditText)findViewById(R.id.etAge);
        etPhone = (EditText)findViewById(R.id.etPhone);
        btnReg = (Button)findViewById(R.id.btnReg);
        rbMale = (RadioButton)findViewById(R.id.rbMale);
        rbFemale = (RadioButton)findViewById(R.id.rbFemale);
        db = new MyDbHandler(this);

        sp1 = getSharedPreferences("Myp1",MODE_PRIVATE);

        String FirstTime = sp1.getString("FirstTimeInstall","");

        if (FirstTime.equals("Yes")){
            startActivity(new Intent(MainActivity.this, Binfo.class));
        }
            else {
            SharedPreferences.Editor editor = sp1.edit();
            editor.putString("FirstTimeInstall","Yes");
            editor.apply();
        }





//        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
//                .getBoolean("isFirstRun", true);
//
//        if (isFirstRun) {
//            //show start activity
//
//            startActivity(new Intent(MainActivity.this, Binfo.class));
//            Toast.makeText(MainActivity.this, "First Run", Toast.LENGTH_LONG)
//                    .show();
//        }
//
//
//        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etName.getText().toString();
                if (name.length() == 0){
                    etName.setError("Name is empty");
                    etName.requestFocus();
                    return;
                }

//                int age = Integer.parseInt(etAge.getText().toString());
                String age = etAge.getText().toString();
                if (0 == age.length()){
                    etAge.setError("Age is empty");
                    etAge.requestFocus();
                    return;
                }

//                int phone = Integer.parseInt(etPhone.getText().toString());
                String phone = etPhone.getText().toString();
                if (phone.length() == 0 || phone.length() < 10 || phone.length() > 10){
                    etPhone.setError("Phone has errors");
                    etPhone.requestFocus();
                    return;
                }

                if ((rbMale.isChecked() && rbFemale.isChecked()) || (!rbMale.isChecked() && !rbFemale.isChecked())){
                    rbFemale.setError("Please Check the gender Description");
                    rbFemale.setChecked(false);
                    rbMale.setChecked(false);
                    rbMale.requestFocus();
                    return;

                }
                String Sex = "Male";

                SharedPreferences.Editor editor = sp1.edit();
                        editor.putString("n",name);
                        editor.putString("age",age);
                        editor.putString("phone",phone);
                        if (rbFemale.isChecked())
                        {Sex = "Female" ;}

//                if (radioGroup.getCheckedRadioButtonId() == -1)
//                {
//                    // no radio buttons are checked
//                }
//                else
//                {
//                    // one of the radio buttons is checked
//                }            rbFemale.is
//
                        editor.putString("sex",Sex);
                        editor.commit();

                Toast.makeText(MainActivity.this,"Data Saved",Toast.LENGTH_SHORT).show();
                Intent launchNextActivity;
                launchNextActivity = new Intent(MainActivity.this, Binfo.class);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(launchNextActivity);
                finish();

//                startActivity(new Intent(MainActivity.this, Binfo.class));


            }
        });
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
}