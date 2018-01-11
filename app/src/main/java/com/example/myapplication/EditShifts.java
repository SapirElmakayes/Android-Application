package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditShifts extends AppCompatActivity implements View.OnClickListener{

    Button bEdit , bAdd;
    EditText etType, etStart, etEnd;
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shifts);

        bEdit = (Button) findViewById (R.id.bEdit);
        bEdit.setOnClickListener(this);

        bAdd = (Button) findViewById(R.id.bAdd);
        bAdd.setOnClickListener(this);

        etType = (EditText) findViewById(R.id.etType);
        etStart = (EditText) findViewById(R.id.etStart);
        etEnd = (EditText) findViewById(R.id.etEnd);

        database = FirebaseDatabase.getInstance();
        mFirebaseDatabase = database.getReference("shifts");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bEdit:
                final String type = etType.getText().toString();
                final String start=etStart.getText().toString();
                final String end=etEnd.getText().toString();
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(type.equals("") || start.equals("") || end.equals("")){
                            Toast.makeText(EditShifts.this, "Fill All", Toast.LENGTH_LONG).show();
                        } else if (dataSnapshot.child(type).exists()) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("start", start);
                            map.put("end",end);
                            mFirebaseDatabase.child(type).updateChildren(map);
                            Toast.makeText(EditShifts.this, "Update succeeded", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), afterLoginMangaer.class);
                            intent.putExtra("username", "");
                            startActivity(intent);
                        } else {
                            Toast.makeText(EditShifts.this, "This Shift not Exists", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(EditShifts.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.bAdd:
                final String type1 = etType.getText().toString();
                final String start1 = etStart.getText().toString();
                final String end1 = etEnd.getText().toString();
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(type1.equals("") || start1.equals("") || end1.equals("")){
                            Toast.makeText(EditShifts.this, "Fill All", Toast.LENGTH_LONG).show();
                        } else if(dataSnapshot.child(type1).exists()) {
                            Toast.makeText(EditShifts.this, "this shift is exists", Toast.LENGTH_LONG).show();
                        } else {
                            mFirebaseDatabase.child(type1).child("start").setValue(start1);
                            mFirebaseDatabase.child(type1).child("end").setValue(end1);
                            Toast.makeText(EditShifts.this, "Success ADD", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), afterLoginMangaer.class);
                            intent.putExtra("username", "");
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(EditShifts.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
}
