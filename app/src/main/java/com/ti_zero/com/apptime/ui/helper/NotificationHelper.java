package com.ti_zero.com.apptime.ui.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.transition.Visibility;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.ti_zero.com.apptime.MainTimeActivity;
import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.android.receiver.ToggleItemBroadcastReceiver;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

/**
 * Created by anstue on 12/30/17.
 */

public class NotificationHelper {

    public static final String NOTIFICATION_ACTION_BTN_TOGGLE = "actionBtnToggle";
    private static final String ITEM_CHANNEL_ID = "ITEM_CHANNEL";
    private static int NOTIFICATION_ID_RUNNING_ITEM = 10001;
    public static int NOTIFICATION_ID_RUNNING_JSON_GENERATION = 10002;

    public static void removeNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID_RUNNING_ITEM);
    }

    public static void createNotification(AbstractItem item, Context context) {
        Logging.logDebug(LogTag.UI, "creating notification: " + item.getName());
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence tickerText = "AppTime";
        long when = System.currentTimeMillis();


        Intent toggleReceive = new Intent(context, ToggleItemBroadcastReceiver.class);
        toggleReceive.putExtra(NOTIFICATION_ACTION_BTN_TOGGLE, item.getUniqueID());
        PendingIntent pendingIntentToggle = PendingIntent.getBroadcast(context, 0, toggleReceive,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ITEM_CHANNEL_ID);
        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setContentTitle("AppTime");
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.notification_item);
        if (item.isRunning()) {
            view.setTextViewText(R.id.btnNotificationToggle, "Stop");
            view.setViewVisibility(R.id.txtCounting, 0);
        } else {
            view.setTextViewText(R.id.btnNotificationToggle, "Start");
            view.setViewVisibility(R.id.txtCounting, 8);
        }
        view.setTextViewText(R.id.txtNotificationName, item.getName());
        view.setTextViewText(R.id.txtSince, item.getShortStartTime());
        view.setOnClickPendingIntent(R.id.btnNotificationToggle, pendingIntentToggle);
        builder.setCustomBigContentView(view);
        builder.setCustomContentView(view);
        builder.setCustomHeadsUpContentView(view);
        Notification notification = builder.build();
        notification.when = when;
        notification.tickerText = tickerText;

//        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_item);
//        if (item.isRunning()) {
//            contentView.setTextViewText(R.id.btnNotificationToggle, "Stop");
//            contentView.setViewVisibility(R.id.txtCounting, 0);
//        } else {
//            contentView.setTextViewText(R.id.btnNotificationToggle, "Start");
//            contentView.setViewVisibility(R.id.txtCounting, 8);
//        }
//        contentView.setTextViewText(R.id.txtNotificationName, item.getName());
        //setListeners(contentView, context, item);

        // notification.contentView = contentView;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        manager.notify(NOTIFICATION_ID_RUNNING_ITEM, notification);
    }

//    private static void setListeners(RemoteViews contentView, Context context, AbstractItem item) {
//        Intent toggle = new Intent(context, NotificationHelperActivity.class);
//        Logging.logInfo(LogTag.NOTIFICATION, "NotificationHelper setListeners for " + item.getUniqueID());
//        toggle.putExtra(NOTIFICATION_ACTION_BTN_TOGGLE, item.getUniqueID());
//        PendingIntent pToggle = PendingIntent.getActivity(context, 0, toggle, 0);
//        contentView.setOnClickPendingIntent(R.id.btnNotificationToggle, pToggle);
//    }

    public static void createLoadingNotification(Context context) {
        Logging.logDebug(LogTag.UI, "creating notification json ");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ITEM_CHANNEL_ID);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info);
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setContentTitle("AppTime");
        builder.setContentText("Generating JSON...");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        long when = System.currentTimeMillis();
        Notification notification = builder.build();
        notification.when = when;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        manager.notify(NOTIFICATION_ID_RUNNING_JSON_GENERATION, notification);
    }

    public static void removeLoadingNotification(Context context) {
        Logging.logDebug(LogTag.UI, "removing notification json ");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID_RUNNING_JSON_GENERATION);
    }
}
