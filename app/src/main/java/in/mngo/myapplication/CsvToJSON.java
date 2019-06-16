package in.mngo.myapplication;

import android.content.Context;
import android.os.AsyncTask;

import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CsvToJSON extends AsyncTask<String,Void,String>
{
    Context context;

    public CsvToJSON(Context context)
    {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params)
    {
        String username = params[0];

        ArrayList<String> title = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray();

        try
        {
        //to open csv file and store it in JSON
            //InputStream is = this.getResources().openRawResource(R.raw.saran);

            InputStream is = null;

            if(username.equals("saran"))
            {
                is = context.getApplicationContext().getResources().openRawResource(R.raw.saran);
            }
            else if(username.equals("patnasc"))
            {
                is = context.getApplicationContext().getResources().openRawResource(R.raw.patnasc);
            }
            else
            {
                return "-1";
            }

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

        return jsonArray.toString();
    }
}
