package com.example.arfin.programmersassistant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arfin.programmersassistant.solveactivities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener{
    EditText editTextResetPasswordEmail;
    Button buttonResetPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        mAuth = FirebaseAuth.getInstance();

        editTextResetPasswordEmail = (EditText) findViewById(R.id.editTextResetPasswordEmail);
        buttonResetPassword = (Button) findViewById(R.id.buttonResetPassword);
        findViewById(R.id.buttonGoHome).setOnClickListener(this);



        buttonResetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.buttonResetPassword){
            String email = editTextResetPasswordEmail.getText().toString().trim();

            if(!TextUtils.isEmpty(email)){
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PasswordResetActivity.this, "Reset Email Sent", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(PasswordResetActivity.this, MainActivity.class));
                        }
                        else{
                            Toast.makeText(PasswordResetActivity.this, "User Email Doesn't exist", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
            else{
                Toast.makeText(PasswordResetActivity.this, "Please enter password", Toast.LENGTH_LONG).show();
            }
        }

        if(view.getId() == R.id.buttonGoHome){
            startActivity(new Intent(PasswordResetActivity.this, MainActivity.class));
        }
    }
}
