package com.vbes.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;

/**
 * 异步加载图片并设置到ImageView
 * (可设置是否保存图片到缓存目录)
 *
 * Created by Vbe on 2018/10/11.
 */
public class AsyncImageLoad extends AsyncTask<String, Intent, Bitmap> {
    private ImageView mView;
    private Context context;
    private String path;
    private boolean isCircle = true;
    private boolean isExist = false;
    private boolean isSave = false;
    private String savePath;
    private OnLoadListener listener;

    private AsyncImageLoad() {}

    public AsyncImageLoad(Context c, ImageView v) {
        mView = v;
        context = c;
    }

    public AsyncImageLoad(Context c, ImageView v, String save, OnLoadListener lis) {
        mView = v;
        context = c;
        listener = lis;
        savePath = save;
        isSave = true;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];
        if (!url.startsWith("http"))
            url = "http://" + url;
        path = MD5Util.getMD5(url) + ".png";
        if (isSave)
            isExist = VbeUtil.isFileExistForUrl(path, savePath);
        if (isExist)
            return existFile();
        else
            return VbeUtil.getNetBitmap(url);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            if (listener != null)
                listener.onComplete(bitmap.copy(bitmap.getConfig(), true));
            if (mView != null) {
                if (isCircle)
                    mView.setImageDrawable(VbeUtil.getCircularDrawable(context, bitmap));
                else
                    mView.setImageBitmap(bitmap);
            }
            if (!isExist && isSave) {
                String ext = VbeUtil.getExtensionName(path);
                if (ext.equals("jpg"))
                    VbeUtil.saveBitmapToJPG(bitmap, new File(savePath, path));
                else
                    VbeUtil.saveBitmapToPNG(bitmap, new File(savePath, path));
            }
        }
        //super.onPostExecute(bitmap);
    }

    /*public void execute(String p) {
        String url = p;
        if (!p.startsWith("http"))
            url = App.serverUrl + p;
        path = MD5Util.getMD5(url) + ".png";
        if (Util.isFileExistForUrl(path))
            existFile();
        else
            this.execute(url);
    }*/


    public Bitmap existFile() {
        return VbeUtil.readBitmap(new File(savePath, path));
    }

    public String getFileName() {
        return path;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String path) {
        savePath = path;
        isSave = true;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setCircle(boolean circle) {
        isCircle = circle;
    }

    public interface OnLoadListener {
        void onComplete(Bitmap path);
    }
}
