package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class viewalert extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView v;
    String url, ip;
    ArrayList<String> alertid, time, image, status;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewalert);
        v = (ListView) findViewById(R.id.lv);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        v.setOnItemClickListener(this);

        url = "http://" + sp.getString("ip", "") + ":5000/viewalert";
        RequestQueue queue = Volley.newRequestQueue(viewalert.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
//                Toast.makeText(viewalert.this, "res" + response, Toast.LENGTH_SHORT).show();

                try {

                    JSONArray ar = new JSONArray(response);
                    alertid = new ArrayList<>();
                    time = new ArrayList<>();
                    image = new ArrayList<>();
                    status = new ArrayList<>();

                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        alertid.add(jo.getString("alert_id"));
                        time.add(jo.getString("time"));
                        status.add(jo.getString("status"));
                        image.add(jo.getString("image"));


                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    v.setAdapter(new custom3(viewalert.this, time, image, status));
//                    v.setOnItemClickListener(viewparkinglocation.this);

                } catch (Exception e) {
                    Toast.makeText(viewalert.this, "eeeeee" + e, Toast.LENGTH_SHORT).show();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(viewalert.this, "err" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uid", sp.getString("lid", ""));


                return params;
            }
        };
        queue.add(stringRequest);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        RequestQueue queue = Volley.newRequestQueue(viewalert.this);
        String url = "http://" + sp.getString("ip", "") + ":5000/alertstatus";
//        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
                    JSONObject json = new JSONObject(response);
                    String res = json.getString("task");

                    if (res.equalsIgnoreCase("success")) {
                        Toast.makeText(getApplicationContext(), "successfully updated", Toast.LENGTH_LONG).show();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "errrrr" + e, Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("aid", alertid.get(position));


                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}