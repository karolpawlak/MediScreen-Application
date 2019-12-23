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

public class MedicalHistoryActivityP2 extends AppCompatActivity {
    //firebase
    private FirebaseAnalytics analytics;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private static final String TAG = "MediScreen Firestore";

    //variables
    private Button nextbtn;
    private Button skipbtn;
    private EditText input3;
    private EditText input4;
    private EditText input5;
    private EditText input6;
    private EditText input7;
    private EditText input8;
    private EditText input9;
    private EditText input10;
    private EditText input11;
    private EditText input12;
    private EditText input13;
    private EditText input14;
    private TextView val;
    private String in3;
    private String in4;
    private String in5;
    private String in6;
    private String in7;
    private String in8;
    private String in9;
    private String in10;
    private String in11;
    private String in12;
    private String in13;
    private String in14;
    private String[] arr = new String[12];
    private List<String> inputArray;
    private boolean confirmValidation;
    private boolean validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history_p2);

        //declaring firebase instances
        analytics = FirebaseAnalytics.getInstance(this);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //declaring UI components
        nextbtn = (Button) findViewById(R.id.buttonNextHeart);
        skipbtn = (Button) findViewById(R.id.buttonSkipHeart);
        input3 = (EditText) findViewById(R.id.editTextHeartInput3);
        input4 = (EditText) findViewById(R.id.editTextHeartInput4);
        input5 = (EditText) findViewById(R.id.editTextHeartInput5);
        input6 = (EditText) findViewById(R.id.editTextHeartInput6);
        input7 = (EditText) findViewById(R.id.editTextHeartInput7);
        input8 = (EditText) findViewById(R.id.editTextHeartInput8);
        input9 = (EditText) findViewById(R.id.editTextHeartInput9);
        input10 = (EditText) findViewById(R.id.editTextHeartInput10);
        input11 = (EditText) findViewById(R.id.editTextHeartInput11);
        input12 = (EditText) findViewById(R.id.editTextHeartInput12);
        input13 = (EditText) findViewById(R.id.editTextHeartInput13);
        input14 = (EditText) findViewById(R.id.editTextHeartInput14);
        val = (TextView) findViewById(R.id.textViewVal15);


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in3 = input3.getText().toString();
                in4 = input4.getText().toString();
                in5 = input5.getText().toString();
                in6 = input6.getText().toString();
                in7 = input7.getText().toString();
                in8 = input8.getText().toString();
                in9 = input9.getText().toString();
                in10 = input10.getText().toString();
                in11 = input11.getText().toString();
                in12 = input12.getText().toString();
                in13 = input13.getText().toString();
                in14 = input14.getText().toString();

                confirmValidation = validateInputs();

                if(confirmValidation)
                {
                    arr[0] = in3;
                    arr[1] = in4;
                    arr[2] = in5;
                    arr[3] = in6;
                    arr[4] = in7;
                    arr[5] = in8;
                    arr[6] = in9;
                    arr[7] = in10;
                    arr[8] = in11;
                    arr[9] = in12;
                    arr[10] = in13;
                    arr[11] = in14;

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
        Intent intent = new Intent(this, MedicalHistoryActivityP3.class);
        startActivity(intent);
    }

    private void updateUserData(String d)
    {
        DocumentReference userRef = db.collection("patients").document(d);

        userRef
                .update("Medical_History.Heart", inputArray)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Toast.makeText(MedicalHistoryActivityP2.this, getString(R.string.diagnosisValuesAddedToast), Toast.LENGTH_SHORT).show();

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

        if (TextUtils.isEmpty(in8))
        {
            validate = false;

        }

        if (TextUtils.isEmpty(in9))
        {
            validate = false;

        }

        if (TextUtils.isEmpty(in10))
        {
            validate = false;

        }

        if (TextUtils.isEmpty(in11))
        {
            validate = false;

        }

        if (TextUtils.isEmpty(in12))
        {
            validate = false;

        }

        if (TextUtils.isEmpty(in13))
        {
            validate = false;

        }

        if (TextUtils.isEmpty(in14))
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
