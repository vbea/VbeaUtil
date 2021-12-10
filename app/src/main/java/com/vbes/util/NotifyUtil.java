package com.vbes.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

/**
 * 发送通知工具
 * @author Created by Vbe on 2020/1/15.
 */
public class NotifyUtil {
    private Builder mBuilder;
    private NotificationManager notificationManager;

    private NotifyUtil(Builder builder) {
        mBuilder = builder;
        notificationManager = (NotificationManager) builder.context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (VbeUtil.isAndroidO()) {
            if (builder.notificationChannel == null) {
                NotificationChannel notificationChannel = new NotificationChannel(builder.channelId, builder.channelName, builder.importance);
                notificationChannel.enableVibration(builder.vibrate);
                notificationManager.createNotificationChannel(notificationChannel);
            } else {
                notificationManager.createNotificationChannel(builder.notificationChannel);
            }
        }
    }

    public void createNotification(String title, String content, @Nullable PendingIntent pending) {
        createNotification(0, title, content, false, pending);
    }

    public void createNotificationOngoing(String title, String content, @Nullable PendingIntent pending) {
        createNotification(0, title, content, true, pending);
    }

    public void createNotification(int id, String title, String content, boolean onGoing, @Nullable PendingIntent pending) {
        Notification.Builder builder = new Notification.Builder(mBuilder.context);
        if (VbeUtil.isAndroidO()) {
            builder.setChannelId(mBuilder.channelId);
        }
        builder.setSmallIcon(mBuilder.smallIcon);
        //BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)
        if (mBuilder.largeIcon != null)
            builder.setLargeIcon(mBuilder.largeIcon);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setOngoing(onGoing);
        if (pending != null)
            builder.setContentIntent(pending);
        createNotification(id, builder.build());
    }

    public void createNotification(int id, Notification notification) {
        notificationManager.notify(id, notification);
    }

    public void cancel(int id) {
        notificationManager.cancel(id);
    }

    public void cancelAll() {
        notificationManager.cancelAll();
    }

    public static class Builder {
        Context context;
        String channelId = "0";
        String channelName = "DefaultVbe";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        boolean vibrate = false;
        int smallIcon = 0;
        Bitmap largeIcon;
        NotificationChannel notificationChannel;

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

        public Builder setSmallIcon(@DrawableRes int id) {
            this.smallIcon = id;
            return this;
        }

        public Builder setLargeIcon(Bitmap bitmap) {
            this.largeIcon = bitmap;
            return this;
        }

        public Builder setChannel(NotificationChannel channel) {
            this.notificationChannel = channel;
            return this;
        }

        public NotifyUtil build() {
            return new NotifyUtil(this);
        }
    }
}
