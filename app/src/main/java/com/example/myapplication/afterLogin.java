package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Keep;
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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.*;

/* afterLogin
This class is the second activity in the Application
Once a user has successfully logged in, they can get the details, view their profile photo, and get their live location on Google Maps. Of course, now the user can also logout.
Another option in this activity – the user can edit his profile and update his details.
*/

public class afterLogin extends AppCompatActivity implements View.OnClickListener {

    Button bInfo, bMap, bPayment, bConstraints, bViewShifts, bTypeOfShifts;
    ImageButton bBack;
    ImageView imageView;
    TextView hello, Logout, Edit, View;
    @Keep
    private String username;
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseAuth mAuth;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        Intent login = getIntent();
        username = login.getExtras().getString("username");

        bInfo = (Button) findViewById(R.id.bInfo);
        bInfo.setOnClickListener(this);

        bMap = (Button) findViewById(R.id.bMap);
        bMap.setOnClickListener(this);

        bPayment = (Button) findViewById(R.id.bPayment);
        bPayment.setOnClickListener(this);

        bConstraints = (Button) findViewById(R.id.bConstraints);
        bConstraints.setOnClickListener(this);

        bViewShifts = (Button) findViewById(R.id.bViewShifts);
        bViewShifts.setOnClickListener(this);

        bTypeOfShifts = (Button) findViewById(R.id.bTypeOfShifts);
        bTypeOfShifts.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.imageView);

        bBack = (ImageButton) findViewById(R.id.bBack);
        bBack.setOnClickListener(this);

        hello = (TextView)findViewById(R.id.HelloMessage);
        hello.setText("Hello " + username);

        Logout = (TextView)findViewById(R.id.Logout);
        Logout.setOnClickListener(this);

        Edit = (TextView) findViewById(R.id.Edit);
        Edit.setOnClickListener(this);

        View = (TextView) findViewById(R.id.View);
        View.setText("");
        View.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bInfo:
                //the user want to show his info.
                mFirebaseDatabase = database.getReference("users");
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Analytics - how many show the "my Info"
                        Bundle params = new Bundle();
                        params.putString("username", username);
                        mFirebaseAnalytics.logEvent("my_info", params);

                        //get the information from user in the database
                        String nValue = dataSnapshot.child(username).child("_name").getValue().toString();
                        String lValue = dataSnapshot.child(username).child("_lastName").getValue().toString();
                        String cValue = dataSnapshot.child(username).child("_city").getValue().toString();
                        String aValue = dataSnapshot.child(username).child("_address").getValue().toString();
                        String mValue = dataSnapshot.child(username).child("_email").getValue().toString();
                        String imgValue;
                        //if the user not define img
                        if(!dataSnapshot.child(username).child("_img").exists()){
                            imgValue = "";
                        }
                        else{
                            imgValue = dataSnapshot.child(username).child("_img").getValue().toString();
                        }
                        //show the img (with bitmap)
                        byte[] encodeByte = Base64.decode(imgValue, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        imageView.setImageBitmap(bitmap);
                        String s = "Name: " + nValue + "\n" +
                                "Last Name: " + lValue + "\n" +
                                "City: " + cValue + "\n" +
                                "Address: " + aValue + "\n"+
                                "Email: " + mValue + "\n" ;
                        View.setText(s);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(afterLogin.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                });
                break;

            //if the user want to logOut
            // disconnect the user
            case R.id.Logout:
                mAuth.signOut();
                startActivity(new Intent(afterLogin.this, MainActivity.class));
                Toast.makeText(this, "Success Logout", Toast.LENGTH_LONG).show();
                break;

            //moves to GoogleMap activity
            case R.id.bMap:
                startActivity(new Intent(this, MapsActivity.class));
                break;

            case R.id.bBack:
                finish();
                break;

            //move to the edit profile class
            case R.id.Edit:
                Intent intent=new Intent(getApplicationContext(), EditMyProfile.class);
                intent.putExtra("username", username);
                startActivity(intent);
                break;

            //we show the payment for any worker type
            case R.id.bPayment:
                byte[] encodeByte = Base64.decode("", Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                imageView.setImageBitmap(bitmap);
                mFirebaseDatabase = database.getReference("Payment per hour");
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s = "";
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            s = s + d.getKey().toString() + " - " + d.getValue().toString() + "\n";
                        }
                        View.setText("\n"+s);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(afterLogin.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                });
                break;

            //moved to other activity, there the user can enter his constraints
            case R.id.bConstraints:
                Intent intent1=new Intent(getApplicationContext(), EmployeeConstraints.class);
                intent1.putExtra("username", username);
                startActivity(intent1);
                break;

            //show the shifts in the rasturant for this week
            case R.id.bViewShifts:
                byte[] encodeByte1 = Base64.decode("", Base64.DEFAULT);
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(encodeByte1, 0, encodeByte1.length);
                imageView.setImageBitmap(bitmap1);
                mFirebaseDatabase = database.getReference("shifts per week");
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s = "";
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            s = s + d.getKey().toString() + "\n" + "\n";
                            for (DataSnapshot p: d.getChildren()){
                                s = s + p.getKey().toString() + "\n";
                                for (DataSnapshot a: p.getChildren()){
                                    s = s + a.getKey().toString() + " , ";
                                    s = s + a.getValue().toString() + "\n";
                                }
                                s = s + "\n";
                            }
                            s = s + "\n";
                        }
                        View.setText("\n"+s);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(afterLogin.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            //the type of shifts in the resturant
            case R.id.bTypeOfShifts:
                byte[] encodeByte2 = Base64.decode("", Base64.DEFAULT);
                Bitmap bitmap2 = BitmapFactory.decodeByteArray(encodeByte2, 0, encodeByte2.length);
                imageView.setImageBitmap(bitmap2);
                mFirebaseDatabase = database.getReference("shifts");
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s ="";
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            s += d.getKey() + "\n";
                            for (DataSnapshot f: d.getChildren()){
                                s += f.getKey() + " " + f.getValue() + "\n";
                            }
                            s += "\n";
                        }
                        View.setText("\n"+s);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(afterLogin.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                });

        }
    }
}

