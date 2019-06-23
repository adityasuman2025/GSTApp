package in.mngo.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Dashboard extends AppCompatActivity
{
    TextView text;
    TextView nonFilersCount;
    TextView GSTCount;

    ListView listGST;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ArrayList<String> DIVISION_array = new ArrayList();
    ArrayList<String> GSTIN_array = new ArrayList();
    ArrayList<String> TR_NAME_array = new ArrayList();
    ArrayList<String> MOBILE_NU_array = new ArrayList();
    ArrayList<String> NON_FILERS_AS_ON_29TH_MAY_array = new ArrayList();
    ArrayList<String> MAJOR_COMMODITY_array = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        viewInitializer();

        //Log.i("myApp", "Hello World");

    //checking cookies for username
        sharedPreferences = getSharedPreferences("AppData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "DNE");

    //getting data from csv file as JSON format (in different thread other than UIThread using Async task)
        try
        {
//            String type = "list_reports_in_db";
//            String report_listResult = new DatabaseActions().execute(type).get();

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

                nonFilersCount.setText("Non Filers: " + nonFilers);
                GSTCount.setText("Registered GST: " + GSTINCount);

            //storing JSON in array format
                JSONObject jo = null;

                for (int i = 0; i < len - 1; i++)
                {
                //getting values from JSON
                    jo = jsonArray.getJSONObject(i);

                    String DIVISION = jo.getString("DIVISION");
                    String GSTIN = jo.getString("GSTIN");
                    String TR_NAME = jo.getString("TR_NAME");
                    String MOBILE_NU = jo.getString("MOBILE_NU");
                    String NON_FILERS_AS_ON_29TH_MAY = jo.getString("NON FILERS AS ON 29TH MAY");
                    String MAJOR_COMMODITY = jo.getString("MAJOR COMMODITY");

                //storing values in array
                    if(!NON_FILERS_AS_ON_29TH_MAY.equals("0"))
                    {
                        DIVISION_array.add(DIVISION);
                        GSTIN_array.add(GSTIN);
                        TR_NAME_array.add(TR_NAME);
                        MOBILE_NU_array.add(MOBILE_NU);
                        NON_FILERS_AS_ON_29TH_MAY_array.add(NON_FILERS_AS_ON_29TH_MAY);
                        MAJOR_COMMODITY_array.add(MAJOR_COMMODITY);
                    }
                }

            //listing in array adapter
                ListGSTAdapter listGSTAdapter = new ListGSTAdapter();
                listGST.setAdapter(listGSTAdapter);

            //on clicking on list
//                listGST.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
//                    {
//                        //Toast.makeText(Dashboard.this, MOBILE_NU_array.get(position), Toast.LENGTH_SHORT).show();
//                    }
//                });
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
    public class ListGSTAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return DIVISION_array.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            final int position = i;

        //rendering the layout
            view = getLayoutInflater().inflate(R.layout.list_gst_adapter, null);

        //defining variables
            TextView trName = view.findViewById(R.id.trName);
            TextView mobileNo = view.findViewById(R.id.mobileNo);
            TextView gstin = view.findViewById(R.id.gstin);
            TextView nonFilerAs29May = view.findViewById(R.id.nonFilerAs29May);
            TextView division = view.findViewById(R.id.division);

            ImageView callIcon = view.findViewById(R.id.callIcon);

            ImageView reportDoneIcon = view.findViewById(R.id.reportDoneIcon);
            ImageView reportPendingIcon = view.findViewById(R.id.reportPendingIcon);

            reportDoneIcon.setVisibility(View.GONE);

        //setting the variables to a value
            trName.setText(TR_NAME_array.get(i));
            mobileNo.setText(MAJOR_COMMODITY_array.get(i));
            gstin.setText(GSTIN_array.get(i));
            nonFilerAs29May.setText("DP #: " + NON_FILERS_AS_ON_29TH_MAY_array.get(i));
            division.setText(DIVISION_array.get(i));

        //calling to that customer on clicking on phone icon
            callIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + MOBILE_NU_array.get(position)));
                    startActivity(intent);
                }
            });

        //on clicking on report done icon
            reportPendingIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //Intent submitReportIntent = new Intent(Dashboard.this, );
                }
            });

            return view;
        }
    }
}
