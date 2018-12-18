package com.example.arfin.programmersassistant;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

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
    EditText editTextHr;
    EditText editTextAddress;
    CircularProgressButton buttonUpdateSolve;
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
        editTextHr = (EditText) findViewById(R.id.editTextHr);
//        editTextAddress = (EditText) findViewById(R.id.editTextAddress);

        //Update Buttons
        buttonUpdateSolve = (CircularProgressButton) findViewById(R.id.buttonUpdateSolve);
        Button buttonUpdateProfile = (Button) findViewById(R.id.buttonUpdateProfile);
        buttonProfile = (Button) findViewById(R.id.buttonProfile);

        //Adding Click Listener
        buttonUpdateSolve.setOnClickListener(this);
        buttonUpdateProfile.setOnClickListener(this);
        mDisplayDate.setOnClickListener(this);
        buttonProfile.setOnClickListener(this);

        FirebaseUser user = mAuth.getCurrentUser();


//        loadUserSolveData();
    }

    private void updateSolveCount() {
        buttonUpdateSolve.startAnimation();

//      String address = editTextAddress.getText().toString().trim();
        String user_id = mAuth.getCurrentUser().getUid();
        final DatabaseReference current_user_db = mDatabase.child(user_id).child("solve_data");


        String cf_str = editTextCf.getText().toString().trim();
        String uva_str = editTextUva.getText().toString().trim();
        String loj_str = editTextLoj.getText().toString().trim();
        String hr_str = editTextHr.getText().toString().trim();

        if(!TextUtils.isEmpty(date)){
            String id = date;
//            String id = ""+date.charAt(3)+date.charAt(4)+date.charAt(0)+date.charAt(1)
//                          +date.charAt(6)+date.charAt(7)+date.charAt(8)+date.charAt(9);
//            String id =  current_user_db.push().getKey();
//            current_user_db.child(date).child("id").setValue(id);
            current_user_db.child(id).child("date").setValue(date);


            if (!(cf_str.isEmpty())) {
                int cf = Integer.parseInt(cf_str);
                DatabaseReference db = current_user_db.child(id).child("cf");
                db.setValue(cf);
//                onChangeSum(db, cf);
            }
            if (!(uva_str.isEmpty())) {
                int uva = Integer.parseInt(uva_str);
                DatabaseReference db = current_user_db.child(id).child("uva");
                db.setValue(uva);
//                onChangeSum(db, uva);
            }
            if (!(loj_str.isEmpty())) {
                int loj = Integer.parseInt(loj_str);
                DatabaseReference db = current_user_db.child(id).child("loj");
                db.setValue(loj);
//                onChangeSum(db, loj);
            }
            if (!(hr_str.isEmpty())) {
                int hr = Integer.parseInt(hr_str);
                DatabaseReference db = current_user_db.child(id).child("hr");
                db.setValue(hr);
//                onChangeSum(db, loj);
            }
            buttonUpdateSolve.revertAnimation();
            startActivity(new Intent(this, ProfileShow.class));

        }
        else{
            buttonUpdateSolve.revertAnimation();
            Toast.makeText(HomeActivity.this, "Please Select a Date", Toast.LENGTH_SHORT).show();
        }
    }

    /////////////////////////////////////--Mutation Code addding and substracting directly in firebase--//////////////////
    /*
        https://github.com/firebase/quickstart-android/blob/a209db500904b6de6d5f49f8488acbde348abf38/database/app/src/main/java/com/google/firebase/quickstart/database/java/fragment/PostListFragment.java#L125-L156
     */
    private void onChangeSum(DatabaseReference postRef, final int change) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer value = 0;
                value = mutableData.getValue(Integer.class);
                value = value + change;
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.buttonUpdateProfile){
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.buttonUpdateSolve){
            updateSolveCount();
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

                    date = year  + "-" + month + "-" + day;
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

    private void loadUserSolveData() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

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

                        editTextCf.setHint(cf_str);
                        editTextUva.setHint(uva_str);
                        editTextLoj.setHint(loj_str);



                    } catch (Exception e) {
                        // This will catch any exception, because they are all descended from Exception
                        System.out.println("Error " + e.getMessage());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }
}
