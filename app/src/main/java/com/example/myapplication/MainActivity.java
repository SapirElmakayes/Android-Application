package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/* MainActivity:
This class is the first activity in the Application
There the user enter the username and password and can use the application.
There have the first connection to the firebase.
We can find in this activity the README text by button press.
When you click on LogIn button you got a Toast message that say if you successes to logged In.
*/
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bLogin, bReadMe;
    EditText etUserName, etPassword;
    TextView textView;
    private FirebaseDatabase database;
    private DatabaseReference mFirebaseDatabase;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bLogin = (Button) findViewById(R.id.bLogin);
        bReadMe = (Button) findViewById(R.id.bReadMe);
        textView = (TextView) findViewById(R.id.textView);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);

        bLogin.setOnClickListener(this);
        bReadMe.setOnClickListener(this);
        textView.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseDatabase = database.getReference("users");
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            //the Login button
            case R.id.bLogin:
                final String username = etUserName.getText().toString();
                final String password1 = etPassword.getText().toString();
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if the user is exists
                        if (dataSnapshot.child(username).exists()){
                            if (!username.isEmpty()) {
                                final User user1 = dataSnapshot.child(username).getValue(User.class);
                                //check if the password correct
                                if(user1._password.equals(password1)) {
                                    //make a toast
                                    Toast.makeText(MainActivity.this, "Success Login", Toast.LENGTH_LONG).show();
                                    //if you manager you have more permissions
                                    if(username.equals("manager")) {
                                        Intent intent = new Intent(MainActivity.this, afterLoginMangaer.class);
                                        intent.putExtra("username", username);
                                        startActivity(intent);
                                    } else {
                                        //Analytics
                                        Bundle params = new Bundle();
                                        params.putString("username", username);
                                        mFirebaseAnalytics.logEvent("Logged_in_successfully", params);

                                        Intent intent = new Intent(MainActivity.this, afterLogin.class);
                                        intent.putExtra("username", username);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Password is Worng", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Username is not register", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Username is not register", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                break;
            case R.id.bReadMe:
                //show README of the application
                textView.setText("סידורון\n" +
                        "אפליקציה לניהול סידורי עבודה.\n" +
                        "למה להשתמש בסידורון?\n" +
                        "* ללא עלויות - סידורון משתמשת באינטרנט של הטלפון שלך על מנת להתחבר לאפליקציה.\n" +
                        "\n" +
                        "* צפיה בזמן אמת במשמרות שנקבעו באותו השבוע\n" +
                        "\n" +
                        "* אופציה לקביעת משתמשים שונים בעלי הרשאות שונות (עובד ומנהל)\n" +
                        "\n" +
                        "* צפיה במשכורות החל מחודש אחורה.\n" +
                        "\n" +
                        "* הזנת בקשות למשמרות שבועיות ע\"פ אילוצי העובד.\n" +
                        "\n" +
                        "* צפיה במספר השעות החודשיות שבוצעו ע\"ב עובד\n" +
                        "\n" +
                        "* התחברות אמינה ע\"ב שם משתמש וסיסמא\n" +
                        "\n" +
                        "* ועוד הרבה: הוספה/מחיקה של עובד בצורה מהירה, צפיה בתלושי המשכורת ע\"ב כל עובד, הוספת תמונת עובד ועוד!");
                break;
            case R.id.textView:
                //hide the read me by click on the text
                textView.setText("");
                break;
        }
    }
}
