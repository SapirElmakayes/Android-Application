package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
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

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button bLogin;
    EditText etUserName, etPassword;
    FirebaseDatabase database;
    DatabaseReference mFirebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);

        bLogin.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        mFirebaseDatabase = database.getReference("users");
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bLogin:
                SharedPreferences user = getSharedPreferences("userDetails", 0);
                String uValue = user.getString("userName", "");
                String pValue = user.getString("password", "");
                final String username=etUserName.getText().toString();
                final String password1=etPassword.getText().toString();
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(username).exists()){
                            if(!username.isEmpty()){
                                final User user1 = dataSnapshot.child(username).getValue(User.class);
                                if(user1._password.equals(password1)){
                                    Toast.makeText(Login.this, "Success Login", Toast.LENGTH_LONG).show();
                                    if(username.equals("manager")){
                                        Intent intent = new Intent(Login.this, afterLoginMangaer.class);
                                        intent.putExtra("username", username);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(Login.this, afterLogin.class);
                                        intent.putExtra("username", username);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(Login.this, "Password is Worng", Toast.LENGTH_LONG).show();
                               }
                            } else {
                                Toast.makeText(Login.this, "Username is not register", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Login.this, "Username is not register", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
              //  if(etUserName.getText().toString().equals(uValue) && etPassword.getText().toString().equals(pValue)) {
            //        startActivity(new Intent(this, afterLogin.class));
          //      } else {
        //            Toast.makeText(Login.this, "Incorrect password or userName", Toast.LENGTH_LONG).show();
         //       }
             break;
        }
    }
}
