package in.mngo.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText usernameInput;
    EditText passwordInput;
    Button loginBtn;
    TextView loginFeed;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInitializer();

    //checking cookies
        sharedPreferences = getSharedPreferences("AppData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        final Intent dashboardIntent = new Intent(MainActivity.this, Dashboard.class);

    //on clcking on login button
        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if(username.equals("saran") && password.equals("saran"))
                {
                    editor.putString("username", username);

                //redirecting to the dashboard activity
                    startActivity(dashboardIntent);
                    finish();
                }
                else if(username.equals("patnasc") && password.equals("patnasc"))
                {
                    editor.putString("username", username);

                //redirecting to the dashboard activity
                    startActivity(dashboardIntent);
                    finish();
                }
                else
                {
                    loginFeed.setText("Login credentials is not correct");
                }
            }
        });
    }


    public void viewInitializer()
    {
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

        loginBtn = findViewById(R.id.loginBtn);
        loginFeed = findViewById(R.id.loginFeed);
    }
}
