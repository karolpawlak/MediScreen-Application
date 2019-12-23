package com.example.mediscreenapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class MedicalHistoryActivityP3 extends AppCompatActivity {
    //firebase
    private FirebaseAnalytics analytics;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private static final String TAG = "MediScreen Firestore";

    //variables
    private Button finishbtn;
    private Button skipbtn;
    private EditText input3;
    private EditText input5;
    private TextView val;
    private RadioGroup input22;
    private RadioGroup input44;
    private RadioButton checkedButton1;
    private RadioButton checkedButton2;
    private String radio1;
    private String radio2;
    private String in2;
    private String in3;
    private String in4;
    private String in5;
    private String[] arr = new String[4];
    private List<String> inputArray;
    private boolean confirmValidation;
    private boolean validate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history_p3);

        //declaring firebase instances
        analytics = FirebaseAnalytics.getInstance(this);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //declaring UI components
        finishbtn = (Button) findViewById(R.id.buttonFinishLung);
        skipbtn = (Button) findViewById(R.id.buttonSkipLung);
        input3 = (EditText) findViewById(R.id.editTextLungInput3);
        input5 = (EditText) findViewById(R.id.editTextLungInput5);
        val = (TextView) findViewById(R.id.textViewVal16);
        input22 = (RadioGroup) findViewById(R.id.radioGroupLung1);
        input44 = (RadioGroup) findViewById(R.id.radioGroupLung2);

        input22.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                checkedButton1 = (RadioButton) findViewById(checkedId);
                radio1 = checkedButton1.getText().toString();
                if(radio1.equals(getResources().getString(R.string.yesRadioButton)))
                {
                    in2 = "1";
                }
                else
                {
                    in2 = "0";
                }


            }
        });

        input44.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                checkedButton2 = (RadioButton) findViewById(checkedId);
                radio2 = checkedButton2.getText().toString();
                if(radio2.equals(getResources().getString(R.string.yesRadioButton)))
                {
                    in4 = "1";
                }
                else
                {
                    in4 = "0";
                }
            }
        });

        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in3 = input3.getText().toString();
                in5 = input5.getText().toString();



                confirmValidation = validateInputs();

                if(confirmValidation) {
                    arr[0] = in2;
                    arr[1] = in3;
                    arr[2] = in4;
                    arr[3] = in5;

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

                    Toast.makeText(getApplicationContext(), getString(R.string.redirectingToast), Toast.LENGTH_SHORT).show();

                    openDiagnosisActivity();
                    finish();
                }
            }
        });

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainPage();
                finish();
            }
        });


    }

    //methods
    private void openMainPage()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openDiagnosisActivity()
    {
        Intent intent = new Intent(this, DiagnosisActivity.class);
        startActivity(intent);
    }

    private void updateUserData(String d)
    {
        DocumentReference userRef = db.collection("patients").document(d);

        userRef
                .update("Medical_History.Lung", inputArray)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");

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


        if(!validate)
        {
            val.setText(getString(R.string.diagnosisInputValTextView));
        }


        return validate;
    }
}
