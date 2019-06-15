package in.mngo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Dashboard extends AppCompatActivity
{
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        text = findViewById(R.id.text);

        String result = "";
        try
        {
            InputStream is = this.getResources().openRawResource(R.raw.saran);

            CSVReader reader = new CSVReader(new InputStreamReader(is, "UTF8"));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null)
            {
            // nextLine[] is an array of values from the line
                result += (nextLine[0] + nextLine[1] + "etc...");
            }
        }
        catch (IOException e)
        {

        }

        text.setText(result);

    }
}
