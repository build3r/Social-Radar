package com.builder.devconnect.network;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.builder.devconnect.MainActivity;
import com.builder.devconnect.R;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONCustomReceiver extends ParsePushBroadcastReceiver {

    private Intent parseIntent;

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        Log.d("tony", "received");
        if (intent == null)
            return;

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.e("PUSHReceiver", "Push received: " + json);

            parseIntent = intent;

            parsePushJson(context, json);

        } catch (JSONException e) {
            Log.e("PUSHReceiver", "Push message json exception: " + e.getMessage());
        }
    }

    private void parsePushJson(Context context, JSONObject json) {
        try {
            String title = "YantraLive";
            String message = json.getString("alert");

            Intent resultIntent = new Intent(context, MainActivity.class);
            showNotificationMessage(context, title, message, resultIntent);

        } catch (JSONException e) {
            Log.e("PUSHReceiver", "Push message json exception: " + e.getMessage());
        }
    }

    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        Intent cIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                cIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create custom notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(1410, notification);
    }
}
