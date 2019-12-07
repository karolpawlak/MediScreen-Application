package com.example.mediscreenapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediscreenapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RecoveryActivity extends AppCompatActivity {
    //firebase
    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private static final String TAG = "MediScreen Firestore";

    //variables
    private EditText userEmail;
    private TextView valem;
    private Button submitbtn;
    private boolean validate;
    private boolean confirmValidation;
    private String email;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        //declaring firebase instances
        analytics = FirebaseAnalytics.getInstance(this);
        auth = FirebaseAuth.getInstance();

        //declaring UI components
        userEmail = (EditText) findViewById(R.id.editTextEmailRecovery);
        valem = (TextView) findViewById(R.id.textViewVal8);
        submitbtn = (Button) findViewById(R.id.submitButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBarRecovery);


        //on click actions
        submitbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //get data from UI components
                email = userEmail.getText().toString();

                //make progress bar visible
                progressBar.setVisibility(View.VISIBLE);

                //check inputs
                confirmValidation = validateInputs();

                //if all inputs are correct, create send a recovery email with firebase
                if (confirmValidation)
                {
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RecoveryActivity.this, getString(R.string.passwordRecoveredToast), Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(RecoveryActivity.this, getString(R.string.passwordRecoveredFailToast), Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private boolean validateInputs()
    {
        validate = true;
        valem.setText("");

        if (!(email.contains("@")))
        {
            validate = false;
            valem.setText(getString(R.string.emailAtValTextView));
        }

        return validate;
    }
}
