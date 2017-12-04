package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bLogin, bRegister, bLogout, bReadMe;
    UserLocalStore userLocalStore;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bLogin = (Button) findViewById(R.id.bLogin);
        bRegister = (Button) findViewById(R.id.bRegister);
        bLogout = (Button) findViewById(R.id.bRegister);
        bReadMe = (Button) findViewById(R.id.bReadMe);
        textView = (TextView) findViewById(R.id.textView);

        bLogin.setOnClickListener(this);
        bRegister.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        bReadMe.setOnClickListener(this);
        textView.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bLogin:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.bRegister:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                break;
            case R.id.bReadMe:
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
                textView.setText("");
                break;
        }

    }
}
