package com.example.mediscreenapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private EditText userAge;

    private TextView valfn;
    private TextView valln;
    private TextView valpn;
    private TextView vala;
    private TextView valg;
    private Button savechangesbtn;
    private Spinner spinner;
    private String fname;
    private String lname;
    private String phoneno;
    private String gender;
    private String ageString;
    private int age;
    private boolean validate;
    private boolean confirmValidation;
    private boolean spinnerSelectedValidation;
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
        userAge = (EditText) findViewById(R.id.editTextAge);
        valfn = (TextView) findViewById(R.id.textViewVal9);
        valln = (TextView) findViewById(R.id.textViewVal10);
        valpn = (TextView) findViewById(R.id.textViewVal11);
        valg = (TextView) findViewById(R.id.textViewVal12);
        vala = (TextView) findViewById(R.id.textViewVal13);
        savechangesbtn = (Button) findViewById(R.id.saveEditButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBarEdit);
        spinner = (Spinner) findViewById(R.id.spinnerGender);

        //set up the adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if(item.equals(getString(R.string.genderValidationChoice)))
                {
                    spinnerSelectedValidation = false;
                }
                else
                {
                    spinnerSelectedValidation = true;
                    gender = item;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });

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

                                try {

                                    String a = Integer.toString(document.getLong("Age").intValue());
                                    String g = document.getString("Gender");

                                    userAge.setText(a);

                                    int selection = 0;
                                    if(g.equals("Female"))
                                    {
                                        selection = 1;
                                    }
                                    if(g.equals("Male"))
                                    {
                                        selection = 2;
                                    }
                                    spinner.setSelection(selection);

                                }
                                catch(Exception e) {

                                }

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
                ageString = userAge.getText().toString();

                //make progress bar visible
                progressBar.setVisibility(View.VISIBLE);

                //check inputs
                confirmValidation = validateInputs();

                //if all inputs are correct, update the data in the database
                if(confirmValidation)
                {
                    age = Integer.parseInt(userAge.getText().toString());
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

        userRef
                .update("First_Name", fname,
                        "Last_Name", lname,
                        "Phone_No", phoneno,
                        "Age", age,
                        "Gender", gender)
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
        vala.setText("");
        valg.setText("");

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

        if (TextUtils.isEmpty(ageString))
        {
            validate = false;
            vala.setText(getString(R.string.ageValTextView));

        }


        if (TextUtils.isEmpty(phoneno))
        {
            validate = false;
            valpn.setText(getString(R.string.phoneNoValTextView));

        }

        if (!spinnerSelectedValidation)
        {
            validate = false;
            valg.setText(getString(R.string.genderValTextView));

        }
        return validate;
    }
}
