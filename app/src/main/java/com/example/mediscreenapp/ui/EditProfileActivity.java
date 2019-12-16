package com.example.mediscreenapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EditProfileActivity extends AppCompatActivity {
    //firebase
    private FirebaseAnalytics analytics;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private static final String TAG = "MediScreen Firestore";

    //variables
    private EditText userFirstName;
    private EditText userLastName;
    private EditText userPhoneNo;
    private TextView valfn;
    private TextView valln;
    private TextView valpn;
    private Button savechangesbtn;
    private String fname;
    private String lname;
    private String phoneno;
    private boolean validate;
    private boolean confirmValidation;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //declaring firebase instances
        analytics = FirebaseAnalytics.getInstance(this);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //declaring UI components
        userFirstName = (EditText) findViewById(R.id.editTextFirstNameEdit);
        userLastName = (EditText) findViewById(R.id.editTextLastNameEdit);
        userPhoneNo = (EditText) findViewById(R.id.editTextPhoneNoEdit);
        valfn = (TextView) findViewById(R.id.textViewVal9);
        valln = (TextView) findViewById(R.id.textViewVal10);
        valpn = (TextView) findViewById(R.id.textViewVal11);
        savechangesbtn = (Button) findViewById(R.id.saveEditButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBarEdit);

        String userEmail = auth.getCurrentUser().getEmail(); //load the current user

        Query getUserData = db.collection("patients").whereEqualTo("Email", userEmail);

        getUserData
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String fn = document.getString("First_Name");
                                String ln = document.getString("Last_Name");
                                String pn = document.getString("Phone_No");

                                userFirstName.setText(fn);
                                userLastName.setText(ln);
                                userPhoneNo.setText(pn);

                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        savechangesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data from UI components
                fname = userFirstName.getText().toString();
                lname = userLastName.getText().toString();
                phoneno = userPhoneNo.getText().toString();

                //make progress bar visible
                progressBar.setVisibility(View.VISIBLE);

                //check inputs
                confirmValidation = validateInputs();

                //if all inputs are correct, update the data in the database
                if(confirmValidation)
                {
                    String userEmail = auth.getCurrentUser().getEmail(); //load the current user
                    Query getUserData = db.collection("patients").whereEqualTo("Email", userEmail);

                    getUserData
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String id = document.getId();
                                            updateUserData(id);
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
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

    private void updateUserData(String d)
    {
        DocumentReference userRef = db.collection("patients").document(d);

        // Set the "isCapital" field of the city 'DC'
        userRef
                .update("First_Name", fname,
                        "Last_Name", lname,
                        "Phone_No", phoneno)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Toast.makeText(EditProfileActivity.this, getString(R.string.profileUpdatedToast), Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    private boolean validateInputs()
    {
        validate = true;
        valfn.setText("");
        valln.setText("");
        valpn.setText("");

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


        if (TextUtils.isEmpty(phoneno))
        {
            validate = false;
            valpn.setText(getString(R.string.phoneNoValTextView));

        }
        return validate;
    }
}
