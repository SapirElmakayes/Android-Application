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

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button bLogin;
    EditText etUserName, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);

        bLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bLogin:
                SharedPreferences user = getSharedPreferences("userDetails", 0);
                String uValue = user.getString("userName", "");
                String pValue = user.getString("password", "");

                if(etUserName.getText().toString().equals(uValue) && etPassword.getText().toString().equals(pValue)) {
                    startActivity(new Intent(this, afterLogin.class));
                } else {
                    Toast.makeText(Login.this, "Incorrect password or userName", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
