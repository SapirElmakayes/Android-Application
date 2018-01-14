package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EmployeeConstraints extends AppCompatActivity implements View.OnClickListener {
    EditText etDay, etType, etDay1, etType1, etDay2, etType2;
    Button bEnter;
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseDatabase;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_constraints);

        Intent login = getIntent();
        username = login.getExtras().getString("username");

        etDay = (EditText) findViewById(R.id.etDay);
        etType = (EditText) findViewById(R.id.etType);
        etDay1 = (EditText) findViewById(R.id.etDay1);
        etType1 = (EditText) findViewById(R.id.etType1);
        etDay2 = (EditText) findViewById(R.id.etDay2);
        etType2 = (EditText) findViewById(R.id.etType2);

        bEnter = (Button) findViewById(R.id.bEnter);
        bEnter.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        mFirebaseDatabase = database.getReference("Employees's Constraints");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bEnter:
                final String day = etDay.getText().toString();
                final String type = etType.getText().toString();
                final String day1 = etDay1.getText().toString();
                final String type1 = etType1.getText().toString();
                final String day2 = etDay2.getText().toString();
                final String type2 = etType2.getText().toString();
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(username).exists()){
                            mFirebaseDatabase.child(username).removeValue();
                        }
                        HashMap<String, Object> map = new HashMap<>();
                        if(!day.equals("") && !type.equals("")) {
                            map.put(day, type);
                        }
                        if(!day1.equals("") && !type1.equals("")) {
                            map.put(day1, type1);
                        }
                        if(!day2.equals("") && !type2.equals("")) {
                            map.put(day2, type2);
                        }
                        mFirebaseDatabase.child(username).updateChildren(map);
                        Toast.makeText(EmployeeConstraints.this, "Success!", Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), afterLogin.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(EmployeeConstraints.this,"An error occured", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
}
