package com.example.arfin.programmersassistant;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    //For Date Picker
    private static final String TAG = "HomeActivity";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String date;

    //ALL layout variables
    TextView textViewHome;
    TextView textViewHomeName;
    TextView textViewHomeAddress;
    EditText editTextCf;
    EditText editTextUva;
    EditText editTextLoj;
    EditText editTextAddress;
    Button buttonUpdateSolve;
    Button buttonUpdateProfile;
    Button buttonProfile;

    //Database
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener authListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Date
        mDisplayDate = (TextView) findViewById(R.id.tvDate);


        //Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //Textviews
        TextView textViewHome = (TextView) findViewById(R.id.textViewHome);


        //Update Edit text
        editTextCf = (EditText) findViewById(R.id.editTextCf);
        editTextUva = (EditText) findViewById(R.id.editTextUva);
        editTextLoj = (EditText) findViewById(R.id.editTextLoj);
//        editTextAddress = (EditText) findViewById(R.id.editTextAddress);

        //Update Buttons
        buttonUpdateSolve = (Button) findViewById(R.id.buttonUpdateSolve);
        Button buttonUpdateProfile = (Button) findViewById(R.id.buttonUpdateProfile);
        buttonProfile = (Button) findViewById(R.id.buttonProfile);

        //Adding Click Listener
        buttonUpdateSolve.setOnClickListener(this);
        buttonUpdateProfile.setOnClickListener(this);
        mDisplayDate.setOnClickListener(this);
        buttonProfile.setOnClickListener(this);

        FirebaseUser user = mAuth.getCurrentUser();



    }

    private void updateAddress() {
//        String address = editTextAddress.getText().toString().trim();
        String cf_str = editTextCf.getText().toString().trim();
        String uva_str = editTextUva.getText().toString().trim();
        String loj_str = editTextLoj.getText().toString().trim();
        if (cf_str.isEmpty()) {
            editTextCf.setError("Value required");
            editTextCf.requestFocus();
            return;
        }
        if (uva_str.isEmpty()) {
            editTextUva.setError("Value required");
            editTextUva.requestFocus();
            return;
        }
        if (loj_str.isEmpty()) {
            editTextLoj.setError("Value required");
            editTextLoj.requestFocus();
            return;
        }

        int cf = Integer.parseInt(cf_str);
        int uva = Integer.parseInt(uva_str);
        int loj = Integer.parseInt(loj_str);

        if(!TextUtils.isEmpty(cf_str) && !TextUtils.isEmpty(uva_str) && !TextUtils.isEmpty(loj_str)){

            String user_id = mAuth.getCurrentUser().getUid();
            final DatabaseReference current_user_db = mDatabase.child(user_id);
    //        current_user_db.child("address").setValue(address);
            current_user_db.child("solve_count").setValue(new SolveCount(cf,uva,loj));

            startActivity(new Intent(this, ProfileShow.class));

        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.buttonUpdateProfile){
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        
        if(view.getId() == R.id.buttonUpdateSolve){
            updateAddress();
        }

        if(view.getId() == R.id.buttonProfile){
            startActivity(new Intent(this, ProfileShow.class));
        }

        if(view.getId() == R.id.tvDate){
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                    date = month + "-" + day + "-" + year;
                    mDisplayDate.setText(date);
                }
            };

            DatePickerDialog dialog = new DatePickerDialog(
                    HomeActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }
}
