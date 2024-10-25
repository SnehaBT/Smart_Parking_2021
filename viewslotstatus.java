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

public class viewslotstatus extends AppCompatActivity {

    ListView v;
    String url;
    ArrayList<String> slotnumber,status;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewslotstatus);
        v=(ListView)findViewById(R.id.lv);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        url ="http://"+sp.getString("ip", "") + ":5000/viewslot";
        RequestQueue queue = Volley.newRequestQueue(viewslotstatus.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {

                    JSONArray ar = new JSONArray(response);

                    slotnumber = new ArrayList<>();
                    status = new ArrayList<>();

                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        slotnumber.add(jo.getString("slot_number"));
                        status.add(jo.getString("status"));



                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    v.setAdapter(new custom2(viewslotstatus.this, slotnumber,status));
//                    v.setOnItemClickListener(viewparkinglocation.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(viewslotstatus.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uid", sp.getString("lid",""));

                return params;
            }
        };
        queue.add(stringRequest);


    }
}