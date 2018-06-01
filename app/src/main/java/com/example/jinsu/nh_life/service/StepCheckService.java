package com.example.jinsu.nh_life.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.jinsu.nh_life.R;
import com.example.jinsu.nh_life.activity.MainActivity;
import com.example.jinsu.nh_life.common.Constants;


public class StepCheckService extends Service implements SensorEventListener {
    public static int step = 0;
    int count = Constants.getInstance().getStep();
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private NotificationManager Notifi_M;
   // private ServiceThread thread;
    private Notification Notifi ;
    private RemoteViews remoteViews;

    private float x, y, z;
    private static final int SHAKE_THRESHOLD = 800;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("onCreate", "IN");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    } // end of onCreate

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        Log.i("onStartCommand", "IN");
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        } // end of if

        Intent intent2 = new Intent(StepCheckService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(StepCheckService.this, 0, intent2,PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews = new RemoteViews(getPackageName(), R.layout.remote_barcode);
        remoteViews.setOnClickPendingIntent(R.id.layout_remote,pendingIntent);

        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notifi = new Notification.Builder(StepCheckService.this)
                .setContent(remoteViews)
                .setPriority(Notification.PRIORITY_MIN)
                .setSmallIcon(R.drawable.nh)
                .setTicker("알림!!!")
                .setContentIntent(pendingIntent)
                .setWhen(0)
                .setOngoing(true)
                .build();

        //Notifi_M.notify( 777 , Notifi);
        startForeground(1,Notifi);

        return START_NOT_STICKY;
    } // end of onStartCommand

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", "IN");
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            Constants.getInstance().setStep(0);
        } // end of if
    } // end of onDestroy

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("onSensorChanged", "IN");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);

            if (gabOfTime > 100) { //  gap of time of step count
                Log.i("onSensorChanged_IF", "FIRST_IF_IN");
                lastTime = currentTime;

                x = event.values[0];
                y = event.values[1];
                z = event.values[2];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.i("onSensorChanged_IF", "SECOND_IF_IN");
                    Intent myFilteredResponse = new Intent("make.a.yong.manbo");

                    Constants.getInstance().setStep(count++);

                    String msg = Constants.getInstance().getStep() / 2 + "";
                    step = Constants.getInstance().getStep() / 2;
                    myFilteredResponse.putExtra("stepService", msg);
                    sendBroadcast(myFilteredResponse);
                } // end of if

                lastX = event.values[0];
                lastY = event.values[1];
                lastZ = event.values[2];
            } // end of if
        } // end of if

    } // end of onSensorChanged

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static int getStep()
    {
        return step;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
