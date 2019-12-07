package com.example.mediscreenapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity
{
    //firebase
    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private static final String TAG = "MediScreen Firestore";

    //variables
    private EditText userId;
    private EditText userPassword;
    private Button loginbtn;
    private Button resetbtn;
    private String username;
    private String pword;
    private ProgressBar progressBar;
    private TextView vallog;
    private TextView valpwd;
    private TextView forgotPassword;
    private boolean validate;
    private boolean confirmValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //declaring firebase instances
        analytics = FirebaseAnalytics.getInstance(this);
        auth = FirebaseAuth.getInstance();

        //declaring UI components
        userId = (EditText) findViewById(R.id.editTextEmailLogin);
        userPassword = (EditText) findViewById(R.id.editTextPasswordLogin);
        loginbtn = (Button) findViewById(R.id.loginButton);
        resetbtn = (Button) findViewById(R.id.resetButton);
        vallog = (TextView) findViewById(R.id.textViewVal6);
        valpwd = (TextView) findViewById(R.id.textViewVal7);
        forgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);


        //on click actions
        loginbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //get data from UI components
                username = userId.getText().toString();
                pword = userPassword.getText().toString();

                //make progress bar visible
                progressBar.setVisibility(View.VISIBLE);

                //check inputs
                confirmValidation = validateInputs();
                if(confirmValidation)
                {
                    //authenticate user and password in firebase
                    auth.signInWithEmailAndPassword(username, pword)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(),getString(R.string.loginToast) , Toast.LENGTH_SHORT).show();
                                        openMainActivity();
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), getString(R.string.loginFailToast) , Toast.LENGTH_SHORT).show();
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

        forgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openRecoveryActivity();
            }
        });



        resetbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                userId.setText("");
                userPassword.setText("");
            }
        });
    }

    private boolean validateInputs()
    {
        validate = true;
        vallog.setText("");
        valpwd.setText("");

        if (!(username.contains("@")))
        {
            validate = false;
            vallog.setText(getString(R.string.emailAtValTextView));
        }


        if (TextUtils.isEmpty(pword))
        {
            validate = false;
            valpwd.setText(getString(R.string.passwordValTextView));
        }


        if (pword.length() < 6)
        {
            validate = false;
            valpwd.setText(getString(R.string.passwordLengthValTextView));
        }

        return validate;
    }

    //methods
    private void openMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openRecoveryActivity()
    {
        Intent intent = new Intent(this, RecoveryActivity.class);
        startActivity(intent);
    }
}
