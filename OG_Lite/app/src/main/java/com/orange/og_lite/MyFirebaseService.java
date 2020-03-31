package com.orange.og_lite;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.customerlibrary.SetRemoteMessage;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getFrom().equals("/topics/ogliteupdate")){
            Log.i("MyFirebaseService","收到");
            Log.i("MyFirebaseService","title "+remoteMessage.getData().get("data_title").toString());
            Log.i("MyFirebaseService","body "+remoteMessage.getData().get("data_content").toString());
            if(remoteMessage.getData().get("data_title").equals("update")){
                SharedPreferences data=this.getApplicationContext().getSharedPreferences("Setting", Context.MODE_PRIVATE);
                data.edit().putString("update",remoteMessage.getData().get("data_content").toString()).commit();
            }
        }else{
            SetRemoteMessage.Companion.pushMessage(remoteMessage,getApplicationContext());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MyFirebaseService","close");
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i("MyFirebaseService","token "+s);
    }

}