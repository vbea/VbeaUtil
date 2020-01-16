package com.vbes.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;

/**
 * Created by Vbe on 2020/1/15.
 */
public class NotifyUtil {
    private Context context;
    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;
    private NotifyUtil(Context c, String channelId, String channelName, boolean vibrate, int importance) {
        context = c;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (VbeUtil.isAndroidO()) {
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableVibration(vibrate);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    /*public void createNotification(String title, String content) {
        createNotification(0, title, content, false);
    }

    public void createNotificationOngoing(String title, String content) {
        createNotification(0, title, content, true);
    }

    public void createNotification(int id, String title, String content, boolean onGoing) {
        Notification.Builder builder = new Notification.Builder(context);
        if (VbeUtil.isAndroidO()) {
            builder.setChannelId(notificationChannel.getId());
        }
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setOngoing(onGoing);
        //builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT));
        createNotification(id, builder.build());
    }*/

    public void createNotification(int id, Notification notification) {
        notificationManager.notify(id, notification);
    }

    public void cancel(int id) {
        notificationManager.cancel(id);
    }

    public void cancelAll() {
        notificationManager.cancelAll();
    }

    public class Builder {
        private Context context;
        private String channelId = "0";
        private String channelName = "Default";
        private int importance = NotificationManager.IMPORTANCE_DEFAULT;
        private boolean vibrate = false;
        public Builder(Context c) {
            context = c;
        }

        public Builder setChannelId(String id) {
            this.channelId = id;
            return this;
        }

        public Builder setChannelName(String name) {
            this.channelName = name;
            return this;
        }

        public Builder vibrate(boolean v) {
            this.vibrate = v;
            return this;
        }

        public Builder setImportance(int imports) {
            this.importance = imports;
            return this;
        }

        public NotifyUtil build() {
            return new NotifyUtil(context, channelId, channelName, vibrate, importance);
        }
    }
}
