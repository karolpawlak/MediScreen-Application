package com.example.mediscreenapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.mediscreenapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;

public class DiabetesActivity extends AppCompatActivity {
    //firebase
    private FirebaseAnalytics analytics;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private static final String TAG = "MediScreen Firestore";
    private FirebaseCustomLocalModel localModel;
    private FirebaseCustomRemoteModel remoteModel;
    private float[][] input;
    private TextView outputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabetes);

        //declaring firebase instances
        analytics = FirebaseAnalytics.getInstance(this);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //declaring variables
        input = new float[1][8];


        outputView = (TextView) findViewById(R.id.textViewOutput);

        input[0][0] = 3;
        input[0][1] = 100;
        input[0][2] = 50;
        input[0][3] = 50;
        input[0][4] = 300;
        input[0][5] = 30;
        input[0][6] = 1;
        input[0][7] = 40;



        try {
            runInference(input);
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }

    }

    private void configureHostedModelSource() {
        // [START mlkit_cloud_model_source]
        FirebaseCustomRemoteModel remoteModel =
                new FirebaseCustomRemoteModel.Builder("diabetes-model").build();
        // [END mlkit_cloud_model_source]
    }

    private void configureLocalModelSource() {
        // [START mlkit_local_model_source]
        localModel = new FirebaseCustomLocalModel.Builder()
                .setAssetFilePath("diabetes-model.tflite")
                .build();
        // [END mlkit_local_model_source]
    }

    private void startModelDownloadTask(FirebaseCustomRemoteModel remoteModel) {
        // [START mlkit_model_download_task]
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Success.
                    }
                });
        // [END mlkit_model_download_task]
    }

    private FirebaseModelInterpreter createInterpreter(FirebaseCustomLocalModel localModel) throws FirebaseMLException {
        // [START mlkit_create_interpreter]
        FirebaseModelInterpreter interpreter = null;
        try {
            FirebaseModelInterpreterOptions options =
                    new FirebaseModelInterpreterOptions.Builder(localModel).build();
            interpreter = FirebaseModelInterpreter.getInstance(options);
        } catch (FirebaseMLException e) {
            // ...
        }
        // [END mlkit_create_interpreter]

        return interpreter;
    }
    private FirebaseModelInputOutputOptions createInputOutputOptions() throws FirebaseMLException {
        // [START mlkit_create_io_options]
        FirebaseModelInputOutputOptions inputOutputOptions =
                new FirebaseModelInputOutputOptions.Builder()
                        .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 8}) //model inputs
                        .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 1}) //model output
                        .build();
        // [END mlkit_create_io_options]

        return inputOutputOptions;
    }


    private void runInference(float[][] i) throws FirebaseMLException {
        configureLocalModelSource();
        FirebaseModelInterpreter firebaseInterpreter = createInterpreter(localModel);
        FirebaseModelInputOutputOptions inputOutputOptions = createInputOutputOptions();

        //float array to be passed in the method
        //initiate the array as input


        // [START mlkit_run_inference]
        FirebaseModelInputs inputs = new FirebaseModelInputs.Builder()
                .add(i)  // add() as many input arrays as your model requires
                .build();
        firebaseInterpreter.run(inputs, inputOutputOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseModelOutputs>() {
                            @Override
                            public void onSuccess(FirebaseModelOutputs result) {
                                // [START_EXCLUDE]
                                // [START mlkit_read_result]
                                float[][] output = result.getOutput(0);
                                String res = String.valueOf(output[0][0]);
                                outputView.setText(res);
                                float[] probabilities = output[0];
                                Log.d("Successful","This worked");
                                // [END mlkit_read_result]
                                // [END_EXCLUDE]
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                System.out.println("Exception : " + e);
                                Log.d("Unsuccessful","Not worked");                            }
                        });
        // [END mlkit_run_inference]
    }
}
