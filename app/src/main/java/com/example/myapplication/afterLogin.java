package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import  android.graphics.Bitmap;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.*;

public class afterLogin extends AppCompatActivity implements View.OnClickListener {

    Button bInfo, bMap;
    ImageButton bBack;
    ImageView imageView;
    TextView hello, Logout;
    private String username;
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        bInfo = (Button) findViewById(R.id.bInfo);
        bInfo.setOnClickListener(this);

        bMap = (Button) findViewById(R.id.bMap);
        bMap.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.imageView);

        bBack = (ImageButton) findViewById(R.id.bBack);
        bBack.setOnClickListener(this);
        Intent login = getIntent();
        username = login.getExtras().getString("username");
        database = FirebaseDatabase.getInstance();
        mFirebaseDatabase = database.getReference("users");
        hello = (TextView)findViewById(R.id.textView2);
        hello.setText("Hello " + username);

        Logout = (TextView)findViewById(R.id.Logout);
        Logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bInfo:
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       User user1=dataSnapshot.child(username).getValue(User.class);

                       String nValue =user1._name;
                       String lValue = user1._lastName;
                       String cValue =user1._city;
                       String aValue =user1._address;
                       String mValue =user1._email;
                       String imgValue =user1._img;
                       if(user1._img == null){
                           imgValue="";
                       }
                       byte[] encodeByte = Base64.decode(imgValue, Base64.DEFAULT);
                       Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                       imageView.setImageBitmap(bitmap);
                       ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);
                        ListView list = (ListView) findViewById(R.id.listView);
                       adapter.add("User Name: ");
                       adapter.add(username);
                       adapter.add("Name: ");
                       adapter.add(nValue);
                       adapter.add("Last Name: ");
                       adapter.add(lValue);
                       adapter.add("City: ");
                       adapter.add(cValue);
                       adapter.add("Address: ");
                       adapter.add(aValue);
                       adapter.add("E-Mail: ");
                       adapter.add(mValue);

                        list.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

             //   ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

                break;
            case R.id.Logout:
                startActivity(new Intent(afterLogin.this, MainActivity.class));
                Toast.makeText(this, "Success Logout", Toast.LENGTH_LONG).show();
                break;
            case R.id.bMap:
                startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.bBack:
                finish();
        }
    }
}

