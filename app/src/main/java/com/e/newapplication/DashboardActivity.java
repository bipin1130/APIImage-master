package com.e.newapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {
    private Button btnShow, btnAdd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnAdd= findViewById(R.id.btnAdd);
        btnShow= findViewById(R.id.btnShow);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(DashboardActivity.this, AddHeroActivity.class);
                startActivity(addIntent);
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showIntent = new Intent(DashboardActivity.this, GetAllHeroesActivity.class);
                startActivity(showIntent);

            }
        });

    }
}
