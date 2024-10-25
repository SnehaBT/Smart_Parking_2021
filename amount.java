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

public class amount extends AppCompatActivity {
    Button b1;
    SharedPreferences sp;
    EditText e1,e2,e3,e4;
    String accno,accho,ifsc,bank,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount);
        b1= (Button) findViewById(R.id.button15);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText)findViewById(R.id.editTextTextPersonName3);
        e2=(EditText)findViewById(R.id.editTextTextPersonName7);
        e3=(EditText)findViewById(R.id.editTextTextPersonName8);
        e4=(EditText)findViewById(R.id.editTextTextPersonName11);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //userid=sp.getString("lid","0");
                accno=e1.getText().toString();
                accho=e2.getText().toString();
                ifsc=e3.getText().toString();
                bank=e4.getText().toString();
                id=getIntent().getStringExtra("id");
                if(accno.equalsIgnoreCase(""))
                {
                    e1.setError("Enter account no");
                }
                else if(accno.length()!=9)
                {
                    e1.setError("Invalid account no");
                }
                else if(accho.equalsIgnoreCase(""))
                {
                    e2.setError("Enter account holder name");
                }
                else if(!accho.matches("^[a-zA-Z]*$"))
                {
                    e2.setError("only alphabets are permissible");
                }
                else if(ifsc.equalsIgnoreCase(""))
                {
                    e3.setError("Enter IFSC");
                }
                else if(bank.equalsIgnoreCase(""))
                {
                    e4.setError("Enter place");
                }

                // Instantiate the RequestQueue.

                RequestQueue queue = Volley.newRequestQueue(amount.this);
                String url ="http://"+sp.getString("ip", "")+":5000/pay";
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
                              Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();

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
                        params.put("accno", accno);
                        params.put("accho", accho);
                        params.put("ifsc",ifsc );
                        params.put("bank",bank);
                        params.put("amount",sp.getString("amount",""));
                        params.put("id",id);
                        params.put("q1",sp.getString("q1",""));
                        params.put("q2",sp.getString("q2",""));

                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);


            }
        });

    }
}