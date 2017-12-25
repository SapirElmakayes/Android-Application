package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class afterLoginMangaer extends AppCompatActivity implements View.OnClickListener  {
    Button bInfo, bAdd, bShift, bshiftperweek, bPayment , bPaymentToM;
    TextView textView,hello, Logout, View;
    ImageButton bBack;
    private String username;
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_mangaer);

        Intent login=getIntent();
        username=login.getExtras().getString("username");

        bInfo = (Button) findViewById(R.id.bInfo);
        bInfo.setOnClickListener(this);

        bAdd = (Button) findViewById(R.id.bAdd);
        bAdd.setOnClickListener(this);

        bShift = (Button) findViewById(R.id.bShift);
        bShift.setOnClickListener(this);

        bshiftperweek = (Button) findViewById(R.id.bshiftperweek);
        bshiftperweek.setOnClickListener(this);

        bPayment= (Button) findViewById(R.id.bPayment);
        bPayment.setOnClickListener(this);

        bPaymentToM= (Button) findViewById(R.id.bPaymentToM);
        bPaymentToM.setOnClickListener(this);

        textView=(TextView) findViewById(R.id.textView);
        textView.setOnClickListener(this);

        View=(TextView) findViewById(R.id.View);
        View.setText("");
        View.setOnClickListener(this);

        bBack = (ImageButton) findViewById(R.id.bBack);
        bBack.setOnClickListener(this);
        database= FirebaseDatabase.getInstance();

        hello=(TextView)findViewById(R.id.textView1);
        if (!username.equals("")) {
            hello.setText("Hello "+username);

        }
        Logout= (TextView)findViewById(R.id.Logout);
        Logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bInfo:
                mFirebaseDatabase = database.getReference("users");
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s = "";
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            User user1 = d.getValue(User.class);
                            s = s + user1._name + " " + user1._lastName + "\n"
                                    +user1._userName+"\n"
                                    + user1._address + ", " + user1._city + "\n"
                                    + user1._email + "\n\n";
                        }
                        View.setText(s);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(afterLoginMangaer.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                });
                textView.setText("");
                break;

            case R.id.bShift:
                mFirebaseDatabase = database.getReference("shifts");
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s=dataSnapshot.getValue().toString()+"\n";
                        String ans="";
                        for (int i=0;  i<s.length(); i++ ){
                            if(s.charAt(i)!='{' && s.charAt(i)!='}' && s.charAt(i)!='=' && s.charAt(i)!=',' && s.charAt(i)!=' '){
                                ans=ans+s.charAt(i);
                            }
                            else if(s.charAt(i)=='=' && (i+1)<s.length()&& s.charAt(i+1)=='{'){
                                ans=ans+"\n";
                            }
                            else if(s.charAt(i)==','){
                                ans=ans+"\n";
                            }
                            else if(s.charAt(i)=='='){
                                ans=ans+" ";
                            }
                            else if(s.charAt(i)=='}'){
                                ans=ans+"\n";
                            }
                        }
                        View.setText("\n"+ans);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(afterLoginMangaer.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                });
                textView.setText("Edit Shifts");
                break;

            case R.id.bAdd:
                startActivity(new Intent(this, Register.class));
                break;

            case R.id.bPayment:
                mFirebaseDatabase = database.getReference("Payment per hour");
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s="";
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            s=s+d.getKey().toString()+" - "+d.getValue().toString()+"\n";
                        }
                        View.setText("\n"+s);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(afterLoginMangaer.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                });
                textView.setText("Edit Payment Per Hour");
                break;

            case R.id.bPaymentToM:
                mFirebaseDatabase=database.getReference("Payment For Management");
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s="";
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            s=s+d.getKey().toString()+" - "+d.getValue().toString()+"\n";
                        }
                        View.setText("\n"+s);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                textView.setText("Edit Payment for management");
                break;

            case R.id.bshiftperweek:
                mFirebaseDatabase = database.getReference("shifts per week");
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s="";
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            s=s+d.getKey().toString()+"\n"+"\n";
                            for (DataSnapshot p: d.getChildren()){
                                s=s+p.getKey().toString()+"\n";
                                for (DataSnapshot a: p.getChildren()){
                                    s=s+a.getKey().toString()+" , ";
                                    s=s+a.getValue().toString()+"\n";
                                }
                                s=s+"\n";
                            }
                            s=s+"\n";
                        }
                        View.setText("\n"+s);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(afterLoginMangaer.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                });
                textView.setText("Edit Shift per Week");
                break;

            case R.id.textView:
                if(textView.getText().toString()=="Edit Shifts"){
                    startActivity(new Intent(this, EditShifts.class));
                }
                else if(textView.getText().toString()=="Edit Payment Per Hour"){
                    startActivity(new Intent(this, EditPaymentPerHour.class));
                }
                else if(textView.getText().toString()=="Edit Shift per Week"){
                    startActivity(new Intent(this,EditShiftPerWeek.class));
                }
                else if(textView.getText().toString()=="Edit Payment for management"){
                    startActivity(new Intent(this, EditPaymentForMangment.class));
                }
                break;

            case R.id.View:
                View.setText("");
                textView.setText("");
                break;
            case R.id.Logout:
                startActivity(new Intent(afterLoginMangaer.this, MainActivity.class));
                Toast.makeText(this, "Success Logout", Toast.LENGTH_LONG).show();
                break;
            case R.id.bBack:
                finish();
                break;
        }
    }
}
