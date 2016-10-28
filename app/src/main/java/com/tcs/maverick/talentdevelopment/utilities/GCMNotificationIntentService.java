package com.tcs.maverick.talentdevelopment.utilities;

/**
 * Created by abhi on 3/2/2016.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GcmListenerService;
import com.tcs.maverick.talentdevelopment.R;
import com.tcs.maverick.talentdevelopment.activities.HomeActivity;

public class GCMNotificationIntentService extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 9001;
    NotificationCompat.Builder mNotifyBuilder;
    NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString(AppConstants.MSG_KEY);
        createNotification(message);
    }

    private void createNotification(String body) {
        Intent resultIntent = new Intent(this, HomeActivity.class);
        resultIntent.putExtra("msg", body);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyBuilder = new NotificationCompat.Builder(this);
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        mNotifyBuilder.setContentTitle("MLCP");
        mNotifyBuilder.setContentText(body);
        mNotifyBuilder.setAutoCancel(true);
        mNotifyBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mNotifyBuilder.build());
    }
}