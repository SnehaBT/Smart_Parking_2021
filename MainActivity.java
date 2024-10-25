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

public class MainActivity extends AppCompatActivity {
    EditText e1,e2;
    Button b1,b2;
    String username,password,ip;
    SharedPreferences sp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         e1=(EditText)findViewById(R.id.editTextTextPersonName);
        e2=(EditText)findViewById(R.id.editTextTextPassword);
        b1=(Button)findViewById(R.id.button3);
        b2=(Button)findViewById(R.id.button13);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sp.getString("ip","");

       b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               username =e1.getText().toString();


               password = e2.getText().toString();

               if(username.equalsIgnoreCase(""))
               {
                   e1.setError("enter username");
               }
               else if(password.equalsIgnoreCase(""))
               {
                   e2.setError("enter password");
               }
               else {


                   // Instantiate the RequestQueue.
                   RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                   String url = "http://" + ip + ":5000/login";
                   Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                   // Request a string response from the provided URL.
                   StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           // Display the response string.
                           Log.d("+++++++++++++++++", response);
                           try {
                               JSONObject json = new JSONObject(response);
                               String res = json.getString("result");

                               if (res.equalsIgnoreCase("error")) {


                                   Toast.makeText(getApplicationContext(), "Invalid password or username", Toast.LENGTH_LONG).show();
                               } else {
                                   SharedPreferences.Editor ed = sp.edit();
                                   ed.putString("lid", res);
                                   ed.commit();
                                   startActivity(new Intent(getApplicationContext(), home.class));
                                   startService(new Intent(getApplicationContext(),Notify.class));
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
                           params.put("un", username);
                           params.put("ps", password);

                           return params;
                       }
                   };
                   // Add the request to the RequestQueue.
                   queue.add(stringRequest);
               }

               //Toast.makeText(getApplicationContext(), "you are login"+username, Toast.LENGTH_LONG).show();
               //Intent i=new Intent(getApplicationContext(),signup.class);
               //startActivity(i);


           }
       });
b2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
     startActivity(new Intent(getApplicationContext(),signup.class));

    }
});

            }
        }







