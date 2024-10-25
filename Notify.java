package com.example.smartparking;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Notify extends Service  {

    Handler hd;
    int NOTIFICATION_ID = 234;
    NotificationManager notificationManager;
    SharedPreferences sp;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        try {
            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        hd = new Handler();
        hd.post(r);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        hd.removeCallbacks(r);
    }

    public Runnable r = new Runnable() {

        @Override
        public void run() {
            try {
                getNotification();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }

            hd.postDelayed(r, 30000);
        }
    };

    public void notification_popup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//			mChannel.setVibrationPattern(new long[]{0, 800, 200, 1200, 300, 2000, 400, 4000});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "my_channel_01")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Smart parking")
                .setContentText("alert from smartparking.....!!!");

//		Intent resultIntent = new Intent(getApplicationContext(), Details.class);
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//		stackBuilder.addParentStack(MainActivity.class);
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//		builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }




    void getNotification() {
        RequestQueue queue = Volley.newRequestQueue(Notify.this);
        String url = "http://" + sp.getString("ip", "")+ ":5000/alertnoti";
        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                MediaPlayer mPlayer2;
                try {
                    JSONArray ar = new JSONArray(response);
                   if(ar.length()>0)
                   {

                       mPlayer2 = MediaPlayer.create(Notify.this, R.raw.noti_sound);
                       mPlayer2.start();

                       notification_popup();
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
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid",sp.getString("lid","") );

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


}