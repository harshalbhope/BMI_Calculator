package com.example.acer.bmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewHistory extends AppCompatActivity {
    TextView tvView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);


        tvView = (TextView)findViewById(R.id.tvView);

        String data = MainActivity.db.viewRecord();
        if (data.length() == 0){
            tvView.setText("No Records Found");
        }
        else tvView.setText(data);
    }

}

