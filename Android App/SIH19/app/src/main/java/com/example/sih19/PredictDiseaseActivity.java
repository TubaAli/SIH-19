package com.example.sih19;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PredictDiseaseActivity extends AppCompatActivity {
    ArrayList<String> select_symptoms;

    private String TAG = "PredictDiseaseActivity";
    private EditText pincode;
    private Spinner spinner;
    private String spincode;
    private Button predictDisease;
    private TextView predictedDisease;
    public static HashMap<Integer,Integer> symptomId;
    public static ArrayList<Integer> checkedIds;
    String tag_json_arry = "json_array_req";
    String tag_json_arry1 = "json_array_req";
    String url2="http://ddb281c5.ngrok.io/db/region/";
    String url = "https://sandbox-healthservice.priaid.ch/symptoms?token=REDACTED_PRIAID_TOKEN&format=json&language=en-gb";
    //String url = "https://sandbox-healthservice.priaid.ch/symptoms?token=REDACTED_PRIAID_TOKEN&format=json&language=en-gb";
    //String url = "https://sandbox-healthservice.priaid.ch/symptoms?token=REDACTED_PRIAID_TOKEN&format=json&language=en-gb";
    //String url1 = "https://sandbox-healthservice.priaid.ch/diagnosis?symptoms="+ Arrays.toString(checkedIds.toArray())+"&gender=male&year_of_birth=1998&token=REDACTED_PRIAID_TOKEN&format=json&language=en-gb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict_disease);

        symptomId = new HashMap<Integer,Integer>();
        select_symptoms = new ArrayList<String>();
        checkedIds = new ArrayList<Integer>();

        spinner = (Spinner) findViewById(R.id.spinner);
        predictedDisease = (TextView)findViewById(R.id.predictedDisease);

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        for (int i=0; i<response.length();i++)
                        {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                symptomId.put(i,obj.getInt("ID"));
                                select_symptoms.add(obj.getString("Name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayList<StateVO> listVOs = new ArrayList<>();

                        for (int i = 0; i < 10; i++) {
                            StateVO stateVO = new StateVO();
                            stateVO.setTitle(select_symptoms.get(i));
                            stateVO.setSelected(false);
                            listVOs.add(stateVO);
                        }
                        MyAdapter myAdapter = new MyAdapter(PredictDiseaseActivity.this, 0,
                                listVOs);
                        spinner.setAdapter(myAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage() + error.networkResponse);
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
        pincode = (EditText)findViewById(R.id.editTextPincode);
        predictDisease = (Button)findViewById(R.id.predict_disease);

        predictDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spincode = pincode.getText().toString();

                Log.d(TAG,Arrays.toString(checkedIds.toArray()));
                JsonArrayRequest req = new JsonArrayRequest("https://sandbox-healthservice.priaid.ch/diagnosis?symptoms="+ Arrays.toString(checkedIds.toArray())+"&gender=male&year_of_birth=1998&token=REDACTED_PRIAID_TOKEN&format=json&language=en-gb",
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());
                                if (response.length()>0)
                                {
                                    try {
                                        String s = "Predicted Disease : "+response.getJSONObject(0).getJSONObject("Issue").getString("Name")+"\nAccuracy : "+response.getJSONObject(0).getJSONObject("Issue").getString("Accuracy")+"%";
                                        Log.d(TAG,s);
                                        predictedDisease.setText(s);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage()+error.networkResponse);
                    }
                });

// Adding request to request queue
                AppController.getInstance().addToRequestQueue(req, tag_json_arry1);
            }
        });
    }
}
