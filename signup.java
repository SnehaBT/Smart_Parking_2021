package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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

public class signup extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5,e6,e7,e8;
    RadioButton r1,r2;

    Button b1;
    SharedPreferences sp;
    String fn,mn,ln,dob,email,pno,un,ps,gender,ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        e1=(EditText)findViewById(R.id.editTextTextPersonName4);
        e2=(EditText)findViewById(R.id.editTextTextPersonName5);
        e3=(EditText)findViewById(R.id.editTextTextPersonName6);
        e4=(EditText)findViewById(R.id.editTextDate);
        e5=(EditText)findViewById(R.id.editTextTextEmailAddress2);
        e6=(EditText)findViewById(R.id.editTextPhone);
        e7=(EditText)findViewById(R.id.editTextTextPersonName9);
        e8=(EditText)findViewById(R.id.editTextTextPassword2);
        b1=(Button)findViewById(R.id.button11);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sp.getString("ip","");
        r1=(RadioButton)findViewById(R.id.radioButton);
        r2=(RadioButton)findViewById(R.id.radioButton2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fn=e1.getText().toString();
                mn=e2.getText().toString();

                ln=e3.getText().toString();
                dob=e4.getText().toString();
                email=e5.getText().toString();
                pno=e6.getText().toString();
                un=e7.getText().toString();
                ps=e8.getText().toString();

                if(r1.isChecked())
                {
                    gender=r1.getText().toString();
                }
                else
                {
                    gender=r2.getText().toString();
                }

                if(fn.equalsIgnoreCase(""))
                {
                    e1.setError("Enter first name");
                }

                else if(!fn.matches("^[a-zA-Z]*$"))
                {
                    e1.setError("only alphabets are permissible");
                }
                else if(mn.equalsIgnoreCase(""))
                {
                    e2.setError("Enter middle name");
                }
                else if(!mn.matches("^[a-zA-Z]*$"))
                {
                    e2.setError("only alphabets are permissible");
                }
                else if(ln.equalsIgnoreCase(""))
                {
                    e3.setError("Enter your middile name");
                }
                else if(!ln.matches("^[a-zA-Z]*$"))
                {
                    e3.setError("only alphabets are permissible");
                }
                else if(dob.equalsIgnoreCase(""))
                {
                    e4.setError("Enter dob");
                }

                else if(email.equalsIgnoreCase(""))
                {
                    e5.setError("Enter email");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    e5.setError("Enter Valid Email");
                    e5.requestFocus();
                }
                else if(pno.equalsIgnoreCase(""))
                {
                    e6.setError("Enter phoneno");
                }
                else if(pno.length()!=10)
                {
                    e6.setError("Invalid phoneno");
                }
                else if(un.equalsIgnoreCase(""))
                {
                    e7.setError("enter username");
                }
                else if(ps.equalsIgnoreCase(""))
                {
                    e8.setError("enter password");
                }

                else{











                //Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(signup.this);
                String url = "http://" + ip + ":5000/signup";
                Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();
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
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            } else {
                                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(getApplicationContext(), "Error ffffff"+error, Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("un", un);
                        params.put("pw", ps);
                        params.put("firstname", fn);
                        params.put("midn", mn);
                        params.put("lsn", ln);
                        params.put("dob", dob);
                        params.put("gender", gender);
                        params.put("phone", pno);
                        params.put("email", email);

                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }}

        });

    }
}
