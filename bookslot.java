package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class bookslot extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView v;
    String url,sltid="";
    ArrayList<String> slot,slot_id;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookslot);
        v=(ListView)findViewById(R.id.lvv);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        url ="http://"+sp.getString("ip", "") + ":5000/viewslot1";
        RequestQueue queue = Volley.newRequestQueue(bookslot.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {

                    JSONArray ar = new JSONArray(response);

                    slot = new ArrayList<>();
                    slot_id=new ArrayList<>();


                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        slot.add(jo.getString("slot_number"));
                        slot_id.add(jo.getString("slot_id"));

                    }

                     ArrayAdapter<String> ad=new ArrayAdapter<>(bookslot.this,android.R.layout.simple_list_item_1,slot);
                    v.setAdapter(ad);
                    v.setOnItemClickListener(bookslot.this);



                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(bookslot.this, "err"+error, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sltid=slot_id.get(position);
        AlertDialog.Builder ald=new AlertDialog.Builder(bookslot.this);
        ald.setTitle("File")
                .setPositiveButton(" Send Request ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        RequestQueue queue = Volley.newRequestQueue(bookslot.this);
                        String url ="http://"+sp.getString("ip", "")+":5000/viewslot2";
                        Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();
                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.
                                Log.d("+++++++++++++++++",response);
                                try {
                                    JSONObject json=new JSONObject(response);
                                    String res=json.getString("task");

                                    if(res.equalsIgnoreCase("success"))
                                    {
                                        Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();

                                        Intent i=new Intent(getApplicationContext(),com.example.smartparking.home.class);
                                        startActivity(i);
                                    }
                                    else
                                    {

                                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),"errrrr"+e,Toast.LENGTH_LONG).show();

                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {


                                Toast.makeText(getApplicationContext(),"Error"+error,Toast.LENGTH_LONG).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("uid", sp.getString("lid" ,""));
                                params.put("sltid", sltid);

                                return params;
                            }
                        };
                        // Add the request to the RequestQueue.
                        queue.add(stringRequest);

                    }
                });
//                .setNegativeButton(" Share File ", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//
//                        Intent i=new Intent(getApplicationContext(),ShareFile.class);
//                        i.putExtra("imgid", fid.get(pos));
//                        startActivity(i);
//                    }
//                });

        AlertDialog al=ald.create();
        al.show();


    }
}