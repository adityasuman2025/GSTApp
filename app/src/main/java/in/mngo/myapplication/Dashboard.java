package in.mngo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;

public class Dashboard extends AppCompatActivity
{
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        text = findViewById(R.id.text);
        String t = "";

        try
        {
//            String csvfileString = this.getApplicationInfo().dataDir + File.separatorChar + "csvfile.csv";
//            File csvfile = new File(csvfileString);

            CSVReader reader = new CSVReader(new FileReader("yourfile.csv"));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null)
            {
                // nextLine[] is an array of values from the line
                t += (nextLine[0] + nextLine[1] + "etc...");
            }
        }
        catch (IOException e)
        {

        }

        text.setText(t);

    }
}
