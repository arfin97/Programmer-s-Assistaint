package com.example.arfin.programmersassistant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileShow extends AppCompatActivity implements View.OnClickListener {

    private static final int CHOOSE_IMAGE = 101;

    TextView textView;
    ImageView imageView;
    TextView textViewDisplayName;
    Button buttonUpdateProfile;
    Button buttonUpdateData;
    Button logOut;

    Uri uriProfileImage;
    ProgressBar progressBar;

    String profileImageUrl;

    //Solve Data text view
    TextView textViewCf;
    TextView textViewUva;
    TextView textViewLoj;

    //Database
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener authListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_show);

        //Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //Textviews
        textViewCf  = (TextView) findViewById(R.id.textViewCf);
        textViewUva = (TextView) findViewById(R.id.textViewUva);
        textViewLoj = (TextView) findViewById(R.id.textViewLoj);


        textViewDisplayName = (TextView) findViewById(R.id.textViewDisplayName);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        textView = (TextView) findViewById(R.id.textViewVerified);


        loadUserInformation();



        logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(this);
        buttonUpdateProfile = (Button) findViewById(R.id.buttonUpdateProfile);
        buttonUpdateProfile.setOnClickListener(this);
        buttonUpdateData = (Button) findViewById(R.id.buttonUpdateData);
        buttonUpdateData.setOnClickListener(this);





    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        fetchSolveData();

    }

    private void fetchSolveData() {


        String user_id = mAuth.getCurrentUser().getUid();

        DatabaseReference current_user_db = mDatabase.child(user_id).child("solve_count");



        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    SolveCount sc = dataSnapshot.getValue(SolveCount.class);

                    String cf_str = Integer.toString(sc.getCf());
                    String uva_str = Integer.toString(sc.getUva());
                    String loj_str = Integer.toString(sc.getLoj());

                    textViewCf.setText("Codeforces: "+cf_str);
                    textViewUva.setText("Uva: "+uva_str);
                    textViewLoj.setText("LightOj: "+loj_str);



                } catch (Exception e) {
                    // This will catch any exception, because they are all descended from Exception
                    System.out.println("Error " + e.getMessage());

                } finally {

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("safd", "basd");
            }
        });

    }

    private void goToUpdate(){
        finish();
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imageView);
            }

            if (user.getDisplayName() != null) {
                textViewDisplayName.setText(user.getDisplayName());
            }

            if (user.isEmailVerified()) {
                textView.setText("Email Verified");
            } else {
                textView.setText("Email Not Verified (Click to Verify)");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ProfileShow.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        final StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);


                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profileImageUrl = uri.toString();
                                    Toast.makeText(getApplicationContext(), "Image Upload Successful", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }





















    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.logOut){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        if(v.getId() == R.id.buttonUpdateProfile){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

        if(v.getId() == R.id.buttonUpdateData){
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

    }



}

