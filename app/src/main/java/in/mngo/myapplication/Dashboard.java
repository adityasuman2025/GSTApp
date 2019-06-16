package in.mngo.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.concurrent.ExecutionException;

public class Dashboard extends AppCompatActivity
{
    TextView text;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        text = findViewById(R.id.text);
        text.setText("Please wait! Parsing CSV file");

        sharedPreferences = getSharedPreferences("AppData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "DNE");

    //getting data from csv file as JSON format (in different thread other than UIThread using Async task)
        try
        {
            String JSONvalue = new CsvToJSON(this).execute(username).get();

            if(JSONvalue.equals("-1"))
            {
                text.setText("CSV file not found");
            }
            else
            {
                text.setText(JSONvalue);
            }
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
