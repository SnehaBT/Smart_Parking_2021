package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class sendcomplaints extends AppCompatActivity {
    Button b1;
    SharedPreferences sp;
    EditText e1;
    String userid,complaint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendcomplaints);
        b1= (Button)findViewById(R.id.button12);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText)findViewById(R.id.editTextTextMultiLine);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid = sp.getString("lid", "0");
                complaint = e1.getText().toString();
                if (complaint.equals("")) {
                    e1.setError("this field can not be null");
                    e1.requestFocus();
                }
                // Instantiate the RequestQueue.
                else {
                    RequestQueue queue = Volley.newRequestQueue(sendcomplaints.this);
                    String url = "http://" + sp.getString("ip", "") + ":5000/complaint";
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

                                if (res.equalsIgnoreCase("success")) {
                                    Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();

                                    Intent i = new Intent(getApplicationContext(), com.example.smartparking.home.class);
                                    startActivity(i);
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
                            params.put("uid", userid);
                            params.put("complaint", complaint);

                            return params;
                        }
                    };
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);


                }
            }
        });

    }
}