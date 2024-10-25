package com.example.smartparking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class home extends AppCompatActivity {
    Button b1,b2,b3,b5,b6,b7,b4,b9;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    SharedPreferences sp;
    String sloatid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        b3=(Button)findViewById(R.id.button5);
        b4=(Button)findViewById(R.id.button6);

        b5=(Button)findViewById(R.id.button7);
        b6=(Button)findViewById(R.id.button8);
        b7=(Button)findViewById(R.id.button9);
       // b8=(Button)findViewById(R.id.button10);
        b9=(Button)findViewById(R.id.button16);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),com.example.smartparking.viewparkinglocation.class);
                startActivity(i);

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),com.example.smartparking.viewslotstatus.class);
                startActivity(i);

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),com.example.smartparking.bookslot.class);
                startActivity(i);



            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),com.example.smartparking.viewalert.class);
                startActivity(i);


            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),com.example.smartparking.sendcomplaints.class);
                startActivity(i);

            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),com.example.smartparking.viewreply.class);
                startActivity(i);


            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(ACTION_SCAN);
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException anfe) {
                    showDialog(home.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
                }
            }
        });

//        b8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(getApplicationContext(),com.example.smartparking.payment.class);
//                startActivity(i);
//
//
//            }
//        });


        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),com.example.smartparking.MainActivity.class);
                startActivity(i);


            }
        });

    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

//                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                SharedPreferences.Editor ed = sh.edit();
//                ed.putString("qrid", contents);
//                ed.commit();
                Toast.makeText(getApplicationContext(), "res" + contents, Toast.LENGTH_LONG).show();

//                Intent int_qrpfl = new Intent(getApplicationContext(), payment.class);
//                int_qrpfl.putExtra("qrid", contents);
//                startActivity(int_qrpfl);

                sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                RequestQueue queue = Volley.newRequestQueue(home.this);
                String url ="http://"+sp.getString("ip", "")+":5000/qrscan";
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
                            }
                            else
                            {
                                SharedPreferences.Editor ed=sp.edit();
                                ed.putString("q1",json.getString("q1"));
                                ed.putString("q2",json.getString("q2"));
                                ed.commit();
                                sloatid=json.getString("sid");
                                Intent i=new Intent(getApplicationContext(),payment.class);
                                i.putExtra("sid",sloatid);
                                i.putExtra("bid",json.getString("bid"));

                                startActivity(i);

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
                        params.put("sid", contents);

                        params.put("uid", sp.getString("lid",""));
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        }
    }



}