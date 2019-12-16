package com.example.mediscreenapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mediscreenapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    //firebase
    private FirebaseAnalytics analytics;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private static final String TAG = "MediScreen Firestore";

    //variables
    private Button callbtn;
    private Button diagnosisbtn;
    private Button profilebtn;
    private TextView welcomeMessage;
    private String userFullName;

    //menu appearing in the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //menu actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item1:
                openSupportActivity();
                break;
            case R.id.item2:
                openFeedbackActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declaring firebase instances
        analytics = FirebaseAnalytics.getInstance(this);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //declaring UI components
        callbtn = (Button) findViewById(R.id.callButton);
        diagnosisbtn = (Button) findViewById(R.id.aiButton);
        profilebtn = (Button) findViewById(R.id.profileButton);
        welcomeMessage = (TextView) findViewById(R.id.textViewWelcome);

        //get logged in user email
        String userEmail = auth.getCurrentUser().getEmail(); //load the current user

        Query getUserData = db.collection("patients").whereEqualTo("Email", userEmail);

        getUserData
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String fname = document.getString("First_Name");
                                String lname = document.getString("Last_Name");
                                userFullName = fname + " " + lname;
                                welcomeMessage.setText(userFullName);

                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        //on click actions
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

        profilebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openProfileActivity();
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
