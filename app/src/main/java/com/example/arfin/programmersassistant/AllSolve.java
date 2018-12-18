package com.example.arfin.programmersassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AllSolve extends AppCompatActivity {
    ListView listViewAllSolve;
    List<SolveCount> allSolve;

    //Database
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_solve);

        listViewAllSolve = (ListView) findViewById(R.id.listViewAllSolve);

        //Database
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("solve_data");

        allSolve = new ArrayList<>();


    }

    public void invertUsingCollectionsReverse(Object[] array) {
        List<Object> list = Arrays.asList(array);
        Collections.reverse(list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                allSolve.clear();
                Log.d("Creation", "addEventKaj korse");
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    SolveCount solvecount = postSnapshot.getValue(SolveCount.class);


                    //adding artist to the list
                    allSolve.add(solvecount);
                    Log.d("Creation", solvecount.getDate());




                }

                Collections.reverse(allSolve);

                SolveCountList artistAdapter = new SolveCountList(AllSolve.this, allSolve);
                Log.d("Creation", "Kam korbooo?");
                //attaching adapter to the listview
                listViewAllSolve.setAdapter(artistAdapter);
                Log.d("Creation", "Kam kore");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
