package com.example.sih19;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MyService extends Service {
    Socket mSocket;
    private String URL = "https://fa130431.ngrok.io";
    myNotification nm;
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        nm = new myNotification();
        try {
            mSocket = IO.socket(URL);
            mSocket.on("notif", onNewMessage);

        } catch (URISyntaxException e) {

            throw new RuntimeException(e);
        }

        mSocket.connect();
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("myTag","received something");

            JSONObject data = (JSONObject) args[0];
            String message,contact;
            try {
                message = data.getString("message");
                contact = data.getString("contact");

                nm.showNotification(getApplicationContext(),contact,message,null);

            } catch (JSONException e) {
                return;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("notif", onNewMessage);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        nm = new myNotification();
        try {
            mSocket = IO.socket(URL);
            mSocket.on("notif", onNewMessage);

        } catch (URISyntaxException e) {

            throw new RuntimeException(e);
        }

        mSocket.connect();

        return Service.START_STICKY;
    }
}
