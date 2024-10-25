package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class viewparkinglocation extends AppCompatActivity {

  ListView v;
  String url;
  ArrayList<String> name,address,landmark,latitude,longitude;
  SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewparkinglocation);
        v=(ListView)findViewById(R.id.lv);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        url ="http://"+sp.getString("ip", "") + ":5000/parking_location_add1";
        RequestQueue queue = Volley.newRequestQueue(viewparkinglocation.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {

                    JSONArray ar = new JSONArray(response);
                    address = new ArrayList<>();
                    name = new ArrayList<>();
                    landmark = new ArrayList<>();
                    latitude = new ArrayList<>();
                    longitude = new ArrayList<>();

                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        address.add(jo.getString("address"));
                        name.add(jo.getString("name"));
                        landmark.add(jo.getString("landmark"));
                        latitude.add(jo.getString("latitude"));
                        longitude.add(jo.getString("longitude"));


                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    v.setAdapter(new custom5(viewparkinglocation.this, name, address, landmark, latitude, longitude));
//                    v.setOnItemClickListener(viewparkinglocation.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(viewparkinglocation.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
        queue.add(stringRequest);


    }
}