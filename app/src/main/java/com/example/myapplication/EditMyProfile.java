package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;

public class EditMyProfile extends AppCompatActivity implements View.OnClickListener{
    Button bUpdate, bupimg, bTakePic;
    EditText etUserName, etPassword, etName, etLastName, etCity, etAddress, etEMail;
    ImageView imagetoupload;
    private String username;
    Uri selectedImg;
    String encodedImg;
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        Intent login = getIntent();
        username = login.getExtras().getString("username");

        database = FirebaseDatabase.getInstance();
        mFirebaseDatabase = database.getReference("users");

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etName = (EditText) findViewById(R.id.etName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etCity = (EditText) findViewById(R.id.etCity);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etEMail = (EditText) findViewById(R.id.etEMail);

        imagetoupload = (ImageView) findViewById(R.id.imagetoupload);

        bupimg = (Button) findViewById(R.id.bupimg);
        bupimg.setOnClickListener(this);

        bUpdate = (Button) findViewById(R.id.bUpdate);
        bUpdate.setOnClickListener(this);

        bTakePic = (Button) findViewById(R.id.bTakePic);
        bTakePic.setOnClickListener(this);

        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user1=dataSnapshot.child(username).getValue(User.class);
                etUserName.setText(username);
                etName.setText(user1._name);
                etLastName.setText(user1._lastName);
                etCity.setText(user1._city);
                etAddress.setText(user1._address);
                etEMail.setText(user1._email);
                etPassword.setText(user1._password);
                String imgValue =user1._img;
                if(user1._img==null){
                    imgValue="";
                }
                byte [] encodeByte = Base64.decode(imgValue, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                imagetoupload.setImageBitmap(bitmap);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bupimg:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
                break;

            case R.id.bTakePic:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
                break;

            case R.id.bUpdate:
                final String userName = etUserName.getText().toString();
                final String password = etPassword.getText().toString();
                final String name = etName.getText().toString();
                final String lastName = etLastName.getText().toString();
                final String city = etCity.getText().toString();
                final String address = etAddress.getText().toString();
                final String email = etEMail.getText().toString();
                final String img=encodedImg;
                 User registeredData = new User(userName, password, name, lastName, city, address, email, img);
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(!userName.equals(username) && dataSnapshot.child(userName).exists()) {
                            Toast.makeText(EditMyProfile.this, "The UserName is already exists!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            User registeredData;
                            if(img == null && dataSnapshot.child(username).child("_img").exists()){
                                String img1=dataSnapshot.child(username).child("_img").getValue().toString();
                                mFirebaseDatabase.child(username).removeValue();
                                registeredData=new User(userName,password,name,lastName,city,address,email,img1);
                            }
                            else{
                                mFirebaseDatabase.child(username).removeValue();
                                registeredData=new User(userName,password,name,lastName,city,address,email,img);
                            }
                            mFirebaseDatabase.child(userName).setValue(registeredData);
                            Toast.makeText(EditMyProfile.this, "Success Update!", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(), afterLogin.class);
                            intent.putExtra("username", userName);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(EditMyProfile.this,"An error occured", Toast.LENGTH_LONG).show();
                    }
                });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
                    selectedImg = data.getData();
                    imagetoupload.setImageURI(selectedImg);

                    Bitmap Bimg = ((BitmapDrawable) imagetoupload.getDrawable()).getBitmap();

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    Bimg.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    encodedImg = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                }
                break;

            case 0:
                Bitmap Bimg = (Bitmap)data.getExtras().get("data");
                imagetoupload.setImageBitmap(Bimg);

                Toast.makeText(EditMyProfile.this, "You take a picture", Toast.LENGTH_LONG).show();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bimg.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                encodedImg = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                break;

        }

    }
}
