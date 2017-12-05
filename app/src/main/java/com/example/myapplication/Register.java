package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
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
import android.graphics.Bitmap;
import java.io.File;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class Register extends AppCompatActivity implements View.OnClickListener{

    Button bRegister, bupimg, bTakePic;
    EditText etUserName, etPassword, etName, etLastName, etCity, etAddress, etEMail;
    ImageView imagetoupload;
    Uri selectedImg;
    String encodedImg;

    UserLocalStore userLocalStore;

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
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                String name = etName.getText().toString();
                String lastName = etLastName.getText().toString();
                String city = etCity.getText().toString();
                String address = etAddress.getText().toString();
                String email = etEMail.getText().toString();
                String img = encodedImg;

                User registeredData = new User(userName, password, name, lastName, city, address, email, img);
                userLocalStore.storeUserData(registeredData);

                startActivity(new Intent(this, Login.class));
                break;
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
