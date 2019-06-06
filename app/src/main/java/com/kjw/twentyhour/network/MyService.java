package com.kjw.twentyhour.network;


import android.app.*;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.google.gson.JsonObject;
import com.kjw.twentyhour.R;
import com.kjw.twentyhour.StateActivity;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class MyService extends Service {

    private static final String CHANNEL_ID = "mynoti";
    NotificationManager notificationManager;
    ServiceThread thread;
    Socket socket;




    public MyService() {



    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        thread.stopForever();
        thread = null;
        super.onDestroy();
    }

    class myServiceHandler extends Handler {

        @Override
        public void handleMessage(android.os.Message msg) {

            createNotificationChannel();

            Intent intent = new Intent(MyService.this , StateActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_favorite_24px)
                    .setContentTitle("주문끝")
                    .setContentText("행복시작")
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setDefaults(NotificationCompat.DEFAULT_SOUND);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            notificationManager.notify(666, mBuilder.build());







//            Toast.makeText(MyService.this, "?" , Toast.LENGTH_LONG).show();




        }


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "내꼬야";
            String description = "라비앙로즈";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID , name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}
