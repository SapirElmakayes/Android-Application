package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.graphics.Bitmap;
import java.io.File;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity implements View.OnClickListener{
    Button bRegister, bupimg, bTakePic;
    EditText etUserName, etPassword, etName, etLastName, etCity, etAddress, etEMail;
    ImageView imagetoupload;
    Uri selectedImg;
    String encodedImg;
    UserLocalStore userLocalStore;
    DatabaseReference mDatabase;
    private FirebaseAuth auth;
    //String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etName = (EditText) findViewById(R.id.etName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etCity = (EditText) findViewById(R.id.etCity);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etEMail = (EditText) findViewById(R.id.etEMail);
        userLocalStore = new UserLocalStore(this);
        imagetoupload = (ImageView) findViewById(R.id.imagetoupload);

        bupimg = (Button) findViewById(R.id.bupimg);
        bupimg.setOnClickListener(this);

        bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);

        bTakePic = (Button) findViewById(R.id.bTakePic);
        bTakePic.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bupimg:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
                break;
            case R.id.bTakePic:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
                break;
            case R.id.bRegister:
                final String userName = etUserName.getText().toString();
                final String password = etPassword.getText().toString();
                String name = etName.getText().toString();
                String lastName = etLastName.getText().toString();
                String city = etCity.getText().toString();
                String address = etAddress.getText().toString();
                final String email = etEMail.getText().toString();
                String img = encodedImg;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                final User registeredData = new User(userName, password, name, lastName, city, address, email, img);
                mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(userName).exists())
                            Toast.makeText(Register.this, "The UserName is already exists!", Toast.LENGTH_LONG).show();
                        if (dataSnapshot.child(userName).child(password).getValue().toString().length()<6){
                            Toast.makeText(Register.this, "Enter password with 6 characters", Toast.LENGTH_LONG).show();
                        }
                        else {
                            mDatabase.child("users").child(userName).setValue(registeredData);
                            //create users and logout
                            auth.createUserWithEmailAndPassword(email, password);
                            auth.signOut();
                            //
                            Toast.makeText(Register.this, "Success Register!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), afterLoginMangaer.class);
                            intent.putExtra("username", "");
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
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

                Toast.makeText(Register.this, "You take a picture", Toast.LENGTH_LONG).show();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bimg.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                encodedImg = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                break;
        }
    }
}
