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

public class EditShiftPerWeek extends AppCompatActivity implements View.OnClickListener {

    Button bEdit , bAdd;
    EditText etDay, etType, etName, etRole;
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shift_per_week);

        bEdit = (Button) findViewById (R.id.bEdit);
        bEdit.setOnClickListener(this);

        bAdd = (Button) findViewById(R.id.bAdd);
        bAdd.setOnClickListener(this);

        etDay=(EditText) findViewById( R.id.etDay);
        etType=(EditText) findViewById(R.id.etType);
        etRole = (EditText) findViewById(R.id.etRole);
        etName = (EditText) findViewById(R.id.etName);

        database = FirebaseDatabase.getInstance();
        mFirebaseDatabase = database.getReference("shifts per week");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bEdit:
                final String day = etDay.getText().toString();
                final String type = etType.getText().toString();
                final String name = etName.getText().toString();
                final String Role = etRole.getText().toString();
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(day.equals("") || type.equals("") || name.equals("") || Role.equals("")){
                            Toast.makeText(EditShiftPerWeek.this, "Fill All", Toast.LENGTH_LONG).show();
                        } else if (dataSnapshot.child(day).child(type).child(name).exists()) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put(name, Role);
                            mFirebaseDatabase.child(day).child(type).updateChildren(map);
                            Toast.makeText(EditShiftPerWeek.this, "Update succeeded", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), afterLoginMangaer.class);
                            intent.putExtra("username", "");
                            startActivity(intent);;
                        } else {
                            Toast.makeText(EditShiftPerWeek.this, "This Role not Exists", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(EditShiftPerWeek.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.bAdd:
                final String day1 = etDay.getText().toString();
                final String type1 = etType.getText().toString();
                final String name1 = etName.getText().toString();
                final String Role1 = etRole.getText().toString();
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(day1.equals("") || type1.equals("") || name1.equals("") || Role1.equals("")){
                            Toast.makeText(EditShiftPerWeek.this, "Fill All", Toast.LENGTH_LONG).show();
                        } else if(dataSnapshot.child(day1).child(type1).child(name1).exists()) {
                            Toast.makeText(EditShiftPerWeek.this, "this name is exists", Toast.LENGTH_LONG).show();
                        } else {
                            mFirebaseDatabase.child(day1).child(type1).child(name1).setValue(Role1);
                            Toast.makeText(EditShiftPerWeek.this, "Success ADD", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), afterLoginMangaer.class);
                            intent.putExtra("username", "");
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(EditShiftPerWeek.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
}
