package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ipset extends AppCompatActivity {
    EditText e1;
    Button b;
    String ip="";
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipset);

        e1=(EditText)findViewById(R.id.editTextTextPersonName2);
        b=(Button)findViewById(R.id.button4);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip=e1.getText().toString();
                if(ip.equalsIgnoreCase(""))
                {
                    e1.setError("enter your IP");
                }
                else
                {
                    SharedPreferences.Editor ed=sh.edit();
                    ed.putString("ip",ip);
                    ed.commit();
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}