package in.mngo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Dashboard extends AppCompatActivity
{
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        text = findViewById(R.id.text);


    //to open csv file and store it in JSON
        ArrayList<String> title = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray();

        try
        {
            InputStream is = this.getResources().openRawResource(R.raw.saran);
            CSVReader reader = new CSVReader(new InputStreamReader(is, "UTF8"));

        //to get the title of the array
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null)
            {
                int len = nextLine.length;
                for(int i= 0; i< len; i++)
                {
                    title.add(nextLine[i]);
                }

                break;
            }

        //to get elements of the csv file as JSON
            while ((nextLine = reader.readNext()) != null)
            {
                int len = nextLine.length;

                JSONObject item = new JSONObject();

                for(int i= 0; i< len; i++)
                {
                    item.put(title.get(i), nextLine[i]);
                }

                jsonArray.put(item);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        text.setText(jsonArray.toString());

    }
}
