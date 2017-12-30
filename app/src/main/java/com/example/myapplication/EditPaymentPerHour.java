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

public class EditPaymentPerHour extends AppCompatActivity implements View.OnClickListener {
    Button bEdit , bAdd;
    EditText etRule, etPayment;
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payment_per_hour);

        bEdit = (Button) findViewById (R.id.bEdit);
        bEdit.setOnClickListener(this);

        bAdd = (Button) findViewById(R.id.bAdd);
        bAdd.setOnClickListener(this);

        etRule = (EditText) findViewById(R.id.etRole);
        etPayment = (EditText) findViewById(R.id.etPayment);

        database = FirebaseDatabase.getInstance();
        mFirebaseDatabase = database.getReference("Payment per hour");
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bEdit:
                final String rule = etRule.getText().toString();
                final String payment = etPayment.getText().toString();
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(rule.equals("") || payment.equals("")){
                            Toast.makeText(EditPaymentPerHour.this, "Fill All", Toast.LENGTH_LONG).show();
                        } else if (dataSnapshot.child(rule).exists()) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put(rule, payment);
                            mFirebaseDatabase.updateChildren(map);
                            Toast.makeText(EditPaymentPerHour.this, "Update succeeded", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), afterLoginMangaer.class);
                            intent.putExtra("username", "");
                            startActivity(intent);
                        } else {
                            Toast.makeText(EditPaymentPerHour.this, "This Role not Exists", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(EditPaymentPerHour.this, "An error occured", Toast.LENGTH_LONG).show();

                    }
                });
                break;
            case R.id.bAdd:
                final String rule1 = etRule.getText().toString();
                final String payment1 = etPayment.getText().toString();
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(rule1.equals("") || payment1.equals("")){
                            Toast.makeText(EditPaymentPerHour.this, "Fill All", Toast.LENGTH_LONG).show();
                        } else if(dataSnapshot.child(rule1).exists()) {
                            Toast.makeText(EditPaymentPerHour.this, "this rule is exists", Toast.LENGTH_LONG).show();
                        } else {
                            mFirebaseDatabase.child(rule1).setValue(payment1);
                            Toast.makeText(EditPaymentPerHour.this, "Success ADD", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), afterLoginMangaer.class);
                            intent.putExtra("username", "");
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(EditPaymentPerHour.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
}