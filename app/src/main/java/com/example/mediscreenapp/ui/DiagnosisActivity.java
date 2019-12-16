package com.example.mediscreenapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mediscreenapp.R;


public class DiagnosisActivity extends AppCompatActivity {
    //variables
    private Button diabetesbtn;
    private Button lungcancerbtn;
    private Button heartdiseasebtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        //declaring UI components
        diabetesbtn = (Button) findViewById(R.id.buttonDiabetes);
        lungcancerbtn = (Button) findViewById(R.id.buttonLungs);
        heartdiseasebtn = (Button) findViewById(R.id.buttonHeart);

        //on click actions
        diabetesbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openDiabetesActivity();
            }
        });

        lungcancerbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openLungActivity();
            }
        });

        heartdiseasebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openHeartActivity();
            }
        });
    }

    //methods
    private void openLungActivity()
    {
        Intent intent = new Intent(this, LungActivity.class);
        startActivity(intent);
    }

    private void openDiabetesActivity()
    {
        Intent intent = new Intent(this, DiabetesActivity.class);
        startActivity(intent);
    }


    private void openHeartActivity()
    {
        Intent intent = new Intent(this, HeartActivity.class);
        startActivity(intent);
    }

}
