package com.example.trynewgun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    public ProgressBar progressBar;
    ImageView image;
    FloatingActionButton addBtn;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        addBtn = findViewById(R.id.btnAdd);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == addBtn)
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_gun, null, false);
                    builder.setView(dialogView);
                    AlertDialog ad4 = builder.create();
                    Button buttonAdd = dialogView.findViewById(R.id.buttonAdd);
                    EditText etPrice = dialogView.findViewById(R.id.etPrice);
                    EditText etToyName = dialogView.findViewById(R.id.etGunModel);
                    EditText etManufacturer = dialogView.findViewById(R.id.etManufacturer);

                    Button etImageURL = dialogView.findViewById(R.id.etImageURL);

                    EditText etUnitsInStock = dialogView.findViewById(R.id.etInStock);
                    EditText etMagOptions = dialogView.findViewById(R.id.etMagOptions);
                    EditText etCaliber = dialogView.findViewById(R.id.etCaliber);
                    EditText etWeight = dialogView.findViewById(R.id.etWeight);

                    image = dialogView.findViewById(R.id.firebaseImage);

                    ad4.show();

                    etImageURL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setType("image/");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, 100);
//                            image.setImageURI(imageUri);
                        }
                    });
                    buttonAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String modelName = etToyName.getText().toString();
                            String stPrice = etPrice.getText().toString(); //remember conv to int
                            String manufacturer = etManufacturer.getText().toString();


//                            String imgUrl = etImageURL.getText().toString(); ------------------------------------------

                            String stInStock = etUnitsInStock.getText().toString(); //remember conv to int
//                            String stStandardMagCapacity = etStandardMagCapacity.getText().toString(); //remember conv to int
                            String magOptions = etMagOptions.getText().toString();
                            String caliber = etCaliber.getText().toString();
                            String stWeight = etWeight.getText().toString(); //remember conv to int
//                            String stBarrelLength = etBarrelSize.getText().toString(); //remember conv to int
//                            String stTriggerPull = etTriggerPull.getText().toString(); //remember conv to int

                            if (modelName.isEmpty() || stPrice.isEmpty() || manufacturer.isEmpty() || /*imgUrl.isEmpty() ||*/ stInStock.isEmpty() || magOptions.isEmpty() || caliber.isEmpty() || stWeight.isEmpty() || imageUri == null) {
                                Toast.makeText(MainActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.VISIBLE);

                                int price = Integer.parseInt(stPrice);
                                int inStock = Integer.parseInt(stInStock);
//                                int standardMagCapacity = Integer.parseInt(stStandardMagCapacity);
                                int weight = Integer.parseInt(stWeight);
//                                int barrelLength = Integer.parseInt(stBarrelLength);
//                                int triggerPull = Integer.parseInt(stTriggerPull);
                                Gun gun = new Gun(modelName, manufacturer, /*imgUrl,*/ price, inStock, magOptions, caliber, weight);
                                firestore
                                        .collection("guns")
                                        .document("" + manufacturer + " " + modelName)
                                        .set(gun)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(MainActivity.this, "Gun added!", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.INVISIBLE);

                                                    //                                                storageReference = FirebaseStorage.getInstance().getReference("image/" + manufacturer + " " + modelName);
//                                                storageReference.putFile(imageUri)
//                                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                                            @Override
//                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                                                image.setImageURI(null);
//                                                                Toast.makeText(MainActivity.this, "Image added", Toast.LENGTH_SHORT).show();
//
//                                                                recreate();
//
//                                                                if (progressBar.getVisibility() == View.VISIBLE) {
//                                                                    progressBar.setVisibility(View.GONE);
//                                                                }
//                                                            }
//                                                        }).addOnFailureListener(new OnFailureListener() {
//                                                            @Override
//                                                            public void onFailure(@NonNull Exception e) {
//                                                                if (progressBar.getVisibility() == View.VISIBLE) {
//                                                                    progressBar.setVisibility(View.GONE);
//                                                                    Toast.makeText(MainActivity.this, "Failed image upload", Toast.LENGTH_SHORT).show();
//                                                                }
//
//                                                            }
//                                                        });
                                                } else {
                                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                            }
                        }

                    });


                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();


            try {
                image.setImageURI(imageUri);
            } catch (Exception e) {
                Toast.makeText(this, "faillll", Toast.LENGTH_SHORT).show();
            }


        }
    }

}