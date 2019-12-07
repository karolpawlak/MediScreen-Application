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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity
{
    //firebase
    private FirebaseAnalytics analytics;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private static final String TAG = "MediScreen Firestore";

    //variables
    private Map<String, Object> patient;
    private EditText userFirstName;
    private EditText userLastName;
    private EditText userEmail;
    private EditText userPhoneNo;
    private EditText userPatientNo;
    private EditText userPassword;
    private EditText userRePassword;
    private TextView valfn;
    private TextView valln;
    private TextView valem;
    private TextView valpn;
    private TextView valpw;
    private Button registerbtn;
    private String fname;
    private String lname;
    private String email;
    private String phoneno;
    private String pword;
    private String pword_repeat;
    private boolean validate;
    private boolean confirmValidation;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //declaring firebase instances
        analytics = FirebaseAnalytics.getInstance(this);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        //declaring UI components
        userFirstName = (EditText) findViewById(R.id.editTextFirstName);
        userLastName = (EditText) findViewById(R.id.editTextLastName);
        userEmail = (EditText) findViewById(R.id.editTextEmailRegister);
        userPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        userPassword = (EditText) findViewById(R.id.editTextPasswordRegister);
        userRePassword = (EditText) findViewById(R.id.editTextRePassword);
        registerbtn = (Button) findViewById(R.id.registerButton);
        valfn = (TextView) findViewById(R.id.textViewVal1);
        valln = (TextView) findViewById(R.id.textViewVal2);
        valem = (TextView) findViewById(R.id.textViewVal3);
        valpn = (TextView) findViewById(R.id.textViewVal4);
        valpw = (TextView) findViewById(R.id.textViewVal5);
        progressBar = (ProgressBar) findViewById(R.id.progressBarRegister);




        //on click actions
        registerbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //get data from UI components
                fname = userFirstName.getText().toString();
                lname = userLastName.getText().toString();
                email = userEmail.getText().toString();
                phoneno = userPhoneNo.getText().toString();
                pword = userPassword.getText().toString();
                pword_repeat = userRePassword.getText().toString();

                //make progress bar visible
                progressBar.setVisibility(View.VISIBLE);

                //check inputs
                confirmValidation = validateInputs();

                //if all inputs are correct, create a user and login immediately to the main activity
                if (confirmValidation)
                {
                    //create user with email and password in firebase authentication and database
                    auth.createUserWithEmailAndPassword(email, pword)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful())
                                    {
                                        //FirebaseUser user = auth.getCurrentUser(); //load the current user
                                        Toast.makeText(getApplicationContext(), getString(R.string.accountCreatedToast), Toast.LENGTH_SHORT).show();
                                        createPatient();
                                        openMainActivity();
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), getString(R.string.accountCreateFailToast), Toast.LENGTH_SHORT).show();
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
        valfn.setText("");
        valln.setText("");
        valem.setText("");
        valpn.setText("");
        valpw.setText("");

        if (TextUtils.isEmpty(fname))
        {
            validate = false;
            valfn.setText(getString(R.string.firstNameValTextView));

        }

        if (TextUtils.isEmpty(lname))
        {
            validate = false;
            valln.setText(getString(R.string.lastNameValTextView));

        }

        if (!(email.contains("@")))
        {
            validate = false;
            valem.setText(getString(R.string.emailAtValTextView));
        }

        if (TextUtils.isEmpty(phoneno))
        {
            validate = false;
            valpn.setText(getString(R.string.phoneNoValTextView));

        }

        if (TextUtils.isEmpty(pword) || TextUtils.isEmpty(pword_repeat))
        {
            validate = false;
            valpw.setText(getString(R.string.passwordValTextView));
        }

        if (!(pword.equals(pword_repeat)))
        {
            validate = false;
            valpw.setText(getString(R.string.passwordMatchValTextView));
        }

        if (pword.length() < 6)
        {
            validate = false;
            valpw.setText(getString(R.string.passwordLengthValTextView));
        }

        return validate;
    }

    private void openMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void createPatient()
    {
        //create patient
        patient = new HashMap<>();
        patient.put("Email", email);
        patient.put("First_Name", fname);
        patient.put("Last_Name", lname);
        patient.put("Phone_No", phoneno);

        addPatientToDatabase(patient);
    }

    private void addPatientToDatabase(Map<String, Object> p)
    {

        db.collection("patients")
                .add(p)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference DocumentReference) {
                        Log.d(TAG, "Patient successfully added to db!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing patient details!", e);
                    }
                });
    }
}
