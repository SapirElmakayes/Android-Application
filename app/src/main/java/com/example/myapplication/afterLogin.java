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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import  android.graphics.Bitmap;

import static android.widget.Toast.*;

public class afterLogin extends AppCompatActivity implements View.OnClickListener {

    Button bInfo, bMap;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        bInfo = (Button) findViewById(R.id.bInfo);
        bInfo.setOnClickListener(this);

        bMap = (Button) findViewById(R.id.bMap);
        bMap.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.imageView);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bInfo:

                SharedPreferences user = getSharedPreferences("userDetails", 0);
                String uValue = user.getString("userName", "");
                String nValue = user.getString("name", "");
                String lValue = user.getString("lastName", "");
                String cValue = user.getString("city", "");
                String aValue = user.getString("address", "");
                String mValue = user.getString("email", "");
                String imgValue = user.getString("img", "");

                byte [] encodeByte = Base64.decode(imgValue, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                imageView.setImageBitmap(bitmap);

                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
                ListView list = (ListView) findViewById(R.id.listView);
                adapter.add("User Name: ");
                adapter.add(uValue);
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
                break;

            case R.id.bMap:
                startActivity(new Intent(this, MapsActivity.class));
                break;
        }

    }


}

