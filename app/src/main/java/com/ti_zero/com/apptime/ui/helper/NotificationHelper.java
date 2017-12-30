package com.ti_zero.com.apptime.ui.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.ti_zero.com.apptime.MainTimeActivity;
import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

/**
 * Created by anstue on 12/30/17.
 */

public class NotificationHelper {

    private static int NOTIFICATION_ID_RUNNING_ITEM = 10001;
    public static int NOTIFICATION_ID_RUNNING_JSON_GENERATION = 10002;

    public static void removeNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID_RUNNING_ITEM);
    }

    public static void createNotification(AbstractItem item, Context context) {
        Logging.logDebug(LogTag.UI, "creating notification: " + item.getName());
        //TODO only works for older android versions, consider newer ones too, API 26
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("AppTime is running")
                        .setContentText("Item: " + item.getName());

        Intent notificationIntent = new Intent(context, MainTimeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID_RUNNING_ITEM, builder.build());


    }

    public static void createLoadingNotification(Context context) {
        Logging.logDebug(LogTag.UI, "creating notification json ");
        //TODO only works for older android versions, consider newer ones too, API 26
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Creating JSON file");

        Intent notificationIntent = new Intent(context, MainTimeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID_RUNNING_JSON_GENERATION, builder.build());
    }

    public static void removeLoadingNotification(Context context) {
        Logging.logDebug(LogTag.UI, "removing notification json ");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID_RUNNING_JSON_GENERATION);
    }
}
