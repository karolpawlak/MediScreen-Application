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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MedicalHistoryActivityP1 extends AppCompatActivity {
    //firebase
    private FirebaseAnalytics analytics;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private static final String TAG = "MediScreen Firestore";

    //variables
    private Button nextbtn;
    private Button skipbtn;
    private EditText input1;
    private EditText input2;
    private EditText input3;
    private EditText input4;
    private EditText input5;
    private EditText input6;
    private EditText input7;
    private TextView val;
    private boolean confirmValidation;
    private boolean validate;
    private String in1;
    private String in2;
    private String in3;
    private String in4;
    private String in5;
    private String in6;
    private String in7;
    private String[] arr = new String[7];
    private List<String> inputArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history_p1);

        //declaring firebase instances
        analytics = FirebaseAnalytics.getInstance(this);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //declaring UI components
        nextbtn = (Button) findViewById(R.id.buttonNextDiabetes);
        skipbtn = (Button) findViewById(R.id.buttonSkipDiabetes);
        input1 = (EditText) findViewById(R.id.editTextDiabetesInput1);
        input2 = (EditText) findViewById(R.id.editTextDiabetesInput2);
        input3 = (EditText) findViewById(R.id.editTextDiabetesInput3);
        input4 = (EditText) findViewById(R.id.editTextDiabetesInput4);
        input5 = (EditText) findViewById(R.id.editTextDiabetesInput5);
        input6 = (EditText) findViewById(R.id.editTextDiabetesInput6);
        input7 = (EditText) findViewById(R.id.editTextDiabetesInput7);
        val = (TextView) findViewById(R.id.textViewVal14);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in1 = input1.getText().toString();
                in2 = input2.getText().toString();
                in3 = input3.getText().toString();
                in4 = input4.getText().toString();
                in5 = input5.getText().toString();
                in6 = input6.getText().toString();
                in7 = input7.getText().toString();

                confirmValidation = validateInputs();

                if(confirmValidation)
                {
                    arr[0] = in1;
                    arr[1] = in2;
                    arr[2] = in3;
                    arr[3] = in4;
                    arr[4] = in5;
                    arr[5] = in6;
                    arr[6] = in7;

                    inputArray = new ArrayList<String>(Arrays.asList(arr));

                    String userEmail = auth.getCurrentUser().getEmail(); //load the current user
                    Query getUserData = db.collection("patients").whereEqualTo("Email", userEmail);

                    getUserData
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
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

                    openNextPage();
                    finish();
                }
            }
        });

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextPage();
            }
        });


    }

    //methods
    private void openNextPage()
    {
        Intent intent = new Intent(this, MedicalHistoryActivityP2.class);
        startActivity(intent);
    }

    private void updateUserData(String d)
    {
        DocumentReference userRef = db.collection("patients").document(d);

        userRef
                .update("Medical_History.Diabetes", inputArray)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Toast.makeText(MedicalHistoryActivityP1.this, getString(R.string.diagnosisValuesAddedToast), Toast.LENGTH_SHORT).show();

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
        val.setText("");

        if (TextUtils.isEmpty(in1))
        {
            validate = false;

        }

        if (TextUtils.isEmpty(in2))
        {
            validate = false;

        }

        if (TextUtils.isEmpty(in3))
        {
            validate = false;

        }

        if (TextUtils.isEmpty(in4))
        {
            validate = false;

        }

        if (TextUtils.isEmpty(in5))
        {
            validate = false;

        }

        if (TextUtils.isEmpty(in6))
        {
            validate = false;

        }

        if (TextUtils.isEmpty(in7))
        {
            validate = false;

        }

        if(!validate)
        {
            val.setText(getString(R.string.diagnosisInputValTextView));
        }


        return validate;
    }
}

