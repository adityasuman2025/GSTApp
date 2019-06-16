package in.mngo.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Dashboard extends AppCompatActivity
{
    TextView text;
    TextView nonFilersCount;
    TextView GSTCount;

    ListView listGST;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        viewInitializer();

    //checking cookies for username
        sharedPreferences = getSharedPreferences("AppData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "DNE");

    //getting data from csv file as JSON format (in different thread other than UIThread using Async task)
        try
        {
            String JSONResultString = new CsvToJSON(this).execute(username).get();

            if(JSONResultString.equals("-1"))
            {
                text.setText("CSV file not found");
            }
            else  if(JSONResultString.equals("-2"))
            {
                text.setText("Failed to parse CSV file");
            }
            else
            {
                text.setText("");
                text.setVisibility(View.GONE);

            //extracting JSON array from json string result
                JSONArray jsonArray = new JSONArray(JSONResultString);
                int len = jsonArray.length();

            //getting the Non-Filers and Registered GST data
                JSONObject jsonObject = jsonArray.getJSONObject(len-1);

                String nonFilers = jsonObject.getString("NON FILERS AS ON 29TH MAY");
                String GSTIN = jsonObject.getString("GSTIN");

                nonFilersCount.setText("Non Filers:" + nonFilers);
                GSTCount.setText("Registered GST: " + GSTIN);
            }
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void viewInitializer()
    {
        text = findViewById(R.id.text);
        text.setText("Please wait! Parsing CSV file");

        nonFilersCount = findViewById(R.id.nonFilersCount);
        GSTCount = findViewById(R.id.GSTCount);

        listGST = findViewById(R.id.listGST);
    }

}
