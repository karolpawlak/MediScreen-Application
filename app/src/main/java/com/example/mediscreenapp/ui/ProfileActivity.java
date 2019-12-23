package com.example.mediscreenapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mediscreenapp.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    //firebase
    private FirebaseAnalytics analytics;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private static final String TAG = "MediScreen Firestore";

    //variables
    private Button editbtn;
    private Button gpbtn;
    private Button insurerbtn;
    private Button historybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //declaring firebase instances
        analytics = FirebaseAnalytics.getInstance(this);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //declaring UI components
        editbtn = (Button) findViewById(R.id.buttonEditProfile);
        gpbtn = (Button) findViewById(R.id.buttonGPDetails);
        insurerbtn = (Button) findViewById(R.id.buttonInsurerDetails);
        historybtn = (Button) findViewById(R.id.buttonMedicalHistory);


        //on click actions
        editbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openEditActivity();
            }
        });
        historybtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openMedicalHistoryActivity();
            }
        });


        gpbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openGPActivity();
            }
        });

        insurerbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openInsurerActivity();
            }
        });
    }

    //methods
    private void openEditActivity()
    {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    private void openGPActivity()
    {
        Intent intent = new Intent(this, GPActivity.class);
        startActivity(intent);
    }


    private void openInsurerActivity()
    {
        Intent intent = new Intent(this, InsurerActivity.class);
        startActivity(intent);
    }

    private void openMedicalHistoryActivity()
    {
        Intent intent = new Intent(this, MedicalHistoryActivityP1.class);
        startActivity(intent);
    }
}
