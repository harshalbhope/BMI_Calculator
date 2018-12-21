package com.example.acer.bmi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tvPersonal;
    EditText etName,etAge,etPhone;
    RadioButton rbMale,rbFemale;
    Button btnReg;
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
        db = new MyDbHandler(this);

        sp1 = getSharedPreferences("Myp1",MODE_PRIVATE);

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

//                if ((rbMale.isChecked() && rbFemale.isChecked()){
//                    rbMale.setError("Please Check the gender Description");
//                    rbMale.requestFocus();
//                }
                String Sex = "Male";

                SharedPreferences.Editor editor = sp1.edit();
                        editor.putString("n",name);
                        editor.putString("age",age);
                        editor.putString("phone",phone);
//                        if (rbFemale.isChecked())
//                        {Sex = "Female" ;}
//
//                if (radioGroup.getCheckedRadioButtonId() == -1)
//                {
//                    // no radio buttons are checked
//                }
//                else
//                {
//                    // one of the radio buttons is checked
//                }            rbFemale.is
//
//                        editor.putString("sex",Sex);
                        editor.commit();

                Toast.makeText(MainActivity.this,"Data Saved",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Binfo.class));
                etAge.setText("");
                etPhone.setText("");
                etName.setText("");


            }
        });
    }
}