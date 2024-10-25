package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Map;

public class payment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button b1;
    SharedPreferences sp;
    EditText e1;
    TextView tv1;
    Spinner s;
    String amount,slot;
    String url1,slotid,booking_id;
    ArrayList<String> slott,sid,bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        b1= (Button)findViewById(R.id.button14);
        slotid=getIntent().getStringExtra("sid");
        booking_id=getIntent().getStringExtra("bid");
//        s=(Spinner)findViewById(R.id.spinner);
        tv1=findViewById(R.id.textView38);
        tv1.setText(slotid);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText)findViewById(R.id.editTextTextPersonName10);
//        s.setOnItemSelectedListener(this);


        RequestQueue queue = Volley.newRequestQueue(payment.this);

        if(booking_id!="") {
            String url = "http://" + sp.getString("ip", "") + ":5000/viewslotamt";
            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the response string.
                    Log.d("+++++++++++++++++", response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String res = json.getString("task");

                        if (!res.equalsIgnoreCase("")) {
                            Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();

                            e1.setText("" + res);
                        } else {

                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

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

                    params.put("bid", booking_id);

                    return params;


                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }









        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amount=e1.getText().toString();
                String url ="http://"+sp.getString("ip", "")+":5000/payment";

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
                                String id=json.getString("id");

                                Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor ed=sp.edit();
                                ed.putString("amount",amount);

                                ed.commit();

                                Intent i=new Intent(getApplicationContext(),com.example.smartparking.amount.class);
                                i.putExtra("id",id);



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
//                        params.put("slotid",slotid );
                        params.put("amount",amount);
                        params.put("slot",slotid);
                        params.put("booking_id",booking_id+"");

                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);


            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }






    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}