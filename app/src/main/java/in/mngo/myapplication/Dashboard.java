package in.mngo.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

                nonFilersCount.setVisibility(View.GONE);
                GSTCount.setVisibility(View.GONE);
            }
            else  if(JSONResultString.equals("-2"))
            {
                text.setText("Failed to parse CSV file");

                nonFilersCount.setVisibility(View.GONE);
                GSTCount.setVisibility(View.GONE);
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
                String GSTINCount = jsonObject.getString("GSTIN");

                nonFilersCount.setText("Non Filers:" + nonFilers);
                GSTCount.setText("Registered GST: " + GSTINCount);

                JSONObject jo = new JSONObject();

                for (int i=0; i < len - 1; i++)
                {
                    jo = jsonArray.getJSONObject(i);

                    String DIVISION = jo.getString("DIVISION");
                    String GSTIN = jo.getString("GSTIN");
                    String TR_NAME = jo.getString("TR_NAME");
                    String MOBILE_NU = jo.getString("MOBILE_NU");
                    String NON_FILERS_AS_ON_29TH_MAY = jo.getString("NON FILERS AS ON 29TH MAY");

                }
            }
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            text.setText("Failed to parse CSV file");
        }
    }

//function to initialize all the views
    public void viewInitializer()
    {
        text = findViewById(R.id.text);
        text.setText("Please wait! Parsing CSV file");

        nonFilersCount = findViewById(R.id.nonFilersCount);
        GSTCount = findViewById(R.id.GSTCount);

        listGST = findViewById(R.id.listGST);
    }

//creating custom adapter to list issued keys
//    class IssuedKeysHistoryAdapter extends BaseAdapter
//    {
//        @Override
//        public int getCount()
//        {
//            return issue_ids.length;
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup)
//        {
//            //rendering the layout
//            view = getLayoutInflater().inflate(R.layout.list_gst_adapter, null);
//
//            //defining variables
//            TextView key_name = view.findViewById(R.id.key_name);
//            TextView issue_date = view.findViewById(R.id.issue_date);
//
//            TextView issue_status = view.findViewById(R.id.issue_status);
//
//            //setting the variables to a value
//            key_name.setText(key_names[i]);
//            issue_date.setText(issued_ons[i]);
//
//            String status = "NA";
//            if(statuses[i].equals("1"))//not returned
//            {
//                status = "N-R"; //Not-Returned
//            }
//            else if(statuses[i].equals("2"))//returned
//            {
//                status = "R"; //Returned
//            }
//
//            issue_status.setText(status);
//
//            return view;
//        }
//    }

}
