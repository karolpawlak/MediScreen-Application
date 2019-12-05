package com.example.mediscreenapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.mediscreenapp.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    //firebase
    private FirebaseAnalytics analytics;
    private FirebaseFirestore db;

    //variables
    private Button supportbtn;
    private Button callbtn;
    private Button diagnosisbtn;
    private Button feedbackbtn;
    private Button profilebtn;
    private Button helpbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declaring firebase instances
        analytics = FirebaseAnalytics.getInstance(this);
        db = FirebaseFirestore.getInstance();

        //declaring UI components
        supportbtn = (Button) findViewById(R.id.supportButton);
        callbtn = (Button) findViewById(R.id.callButton);
        diagnosisbtn = (Button) findViewById(R.id.aiButton);
        feedbackbtn = (Button) findViewById(R.id.feedbackButton);
        profilebtn = (Button) findViewById(R.id.profileButton);
        helpbtn = (Button) findViewById(R.id.helpButton);

        //on click actions
        supportbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openSupportActivity();
            }
        });

        callbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        diagnosisbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openDiagnosisActivity();
            }
        });

        feedbackbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openFeedbackActivity();
            }
        });

        profilebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openProfileActivity();
            }
        });

        helpbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openHelpActivity();
            }
        });
    }

    //methods
    private void openSupportActivity()
    {
        Intent intent = new Intent(this, SupportActivity.class);
        startActivity(intent);
    }

    private void openDiagnosisActivity()
    {
        Intent intent = new Intent(this, DiagnosisActivity.class);
        startActivity(intent);
    }


    private void openFeedbackActivity()
    {
        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
    }

    private void openProfileActivity()
    {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void openHelpActivity()
    {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    private void logLoginEvent()
    {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.METHOD, "MediScreen");
        analytics.logEvent(FirebaseAnalytics.Event.LOGIN, params);
    }

}
