package com.vbes.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
//import junit.framework.Assert;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.util.Pair;
import androidx.appcompat.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.webkit.MimeTypeMap;
import android.telephony.TelephonyManager;

import com.vbes.util.interfaces.DialogResult;
import com.vbes.util.interfaces.DownloadListener;
import com.vbes.util.view.DialogEditView;
import com.vbes.util.view.MyAlertDialog;

/**
 * 各种实用方法合集
 * @VbeUtil Tool class collection
 * Created by Vbe on 2017/5/20.
 * ©邠心工作室 2013-2021
 */
public class VbeUtil {

    public static String SD_PATH = Environment.getExternalStorageDirectory() + "";
    private DownloadListener agent;

    public static boolean isServerDataId(int id) {
        return id == -3;
    }

    /**
     * byte转为字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0)
            return null;
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
                stringBuilder.append(0);
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals(""))
            return null;
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /*
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     */
    public static String toHexString(String str) {
        //根据默认编码获取字节数组
        byte[] bytes = null;
        try {
            bytes = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.i("VbeUtil.toHexString", e.toString());
            //e.printStackTrace();
        }
        if (bytes == null)
            return null;
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        //将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++) {
            /*
             * 16进制数字字符集
             */
            String hexString = "0123456789ABCDEF";
            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
        }
        return sb.toString();
    }

    /**
     * 转换十六进制编码为字符串
     */
    public static String hexToString(String s) {
        if ("0x".equals(s.substring(0, 2))) {
            s = s.substring(2);
        }
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                Log.i("VbeUtil.hexToString", e.toString());
                //e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        } catch (Exception e1) {
            Log.i("VbeUtil.hexToString_2", e1.toString());
            //e1.printStackTrace();
        }
        return s;
    }

    /**
     * png图片换为字节数组
     * @param bmp Bitmap图片
     * @param needRecycle 是否需要回收
     * @return 字节数组
     */
    public static byte[] pngToByteArray(final Bitmap bmp, final boolean needRecycle) {
        return bmpToByteArray(bmp, CompressFormat.PNG, needRecycle);
    }

    /**
     * jpg图片换为字节数组
     * @param bmp Bitmap图片
     * @param needRecycle 是否需要回收
     * @return 字节数组
     */
    public static byte[] jpgToByteArray(final Bitmap bmp, final boolean needRecycle) {
        return bmpToByteArray(bmp, CompressFormat.JPEG, needRecycle);
    }

    /**
     * 图片转换为字节数组
     * @param bmp Bitmap图片
     * @param format 图片格式
     * @param needRecycle 是否需要回收
     * @return 字节数组
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, Bitmap.CompressFormat format, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(format, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            Log.i("VbeUtil.bmpToByteArray", e.toString());
            //e.printStackTrace();
        }
        return result;
    }

    public static byte[] getHtmlByteArray(final String url) {
        URL htmlUrl = null;
        InputStream inStream = null;
        try {
            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
                inStream = httpConnection.getInputStream();
        } catch (MalformedURLException e) {
            Log.i("VbeUtil.getHtmlByteArr", e.toString());
        } catch (IOException e) {
            Log.i("VbeUtil.getHtmlByteArr", e.toString());
        }
        byte[] data = inputStreamToByte(inStream);
        return data;
    }

    public static byte[] inputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            Log.i("VbeUtil.inputStreamToBy", e.toString());
            //e.printStackTrace();
        }
        return null;
    }

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null)
            return null;
        File file = new File(fileName);
        if (!file.exists())
            return null;
        if (len == -1)
            len = (int) file.length();
        if (offset < 0)
            return null;
        if (len <= 0)
            return null;
        if (offset + len > (int) file.length())
            return null;
        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len];
            in.seek(offset);
            in.readFully(b);
            in.close();
        } catch (Exception e) {
            Log.i("VbeUtil.readFromFile", e.toString());
        }
        return b;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else
            roundedSize = (initialSize + 7) / 8 * 8;
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1))
            return 1;
        else if (minSideLength == -1)
            return lowerBound;
        else
            return upperBound;
    }

    /**
     * 以最省内存的方式读取图片
     */
    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public static Bitmap readBitmap(String path) {
        try {
            FileInputStream stream = new FileInputStream(new File(path));
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 8;
            opts.inPurgeable = true;
            opts.inInputShareable = true;
            Bitmap bitmap = BitmapFactory.decodeStream(stream, null, opts);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 以最省内存的方式读取图片
     */
    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public static Bitmap readBitmap(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 8;
            opts.inPurgeable = true;
            opts.inInputShareable = true;
            Bitmap bitmap = BitmapFactory.decodeStream(stream, null, opts);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将图片保存为png文件
     * @param bm 原始图片
     * @param file 保存的目标文件
     */
    public static void saveBitmapToPNG(Bitmap bm, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bm.compress(CompressFormat.PNG, 80, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片保存为jpg文件
     * @param bm 原始图片
     * @param file 保存的目标文件
     */
    public static void saveBitmapToJPG(Bitmap bm, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bm.compress(CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断文件是都存在
     * @param path 文件路径
     * @param saved 文件保存目录
     * @return
     */
    public static boolean isFileExistForUrl(String path, String saved) {
        File folder = new File(saved);
        if (!folder.exists())
            folder.mkdirs();
        File file = new File(folder, path);
        return file.exists();
    }

    /**
     * 将图片保存到媒体库
     * @param context Activity content
     * @param f 目标文件
     */
    public static void updateGallery(Context context, File f) {
        try {
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), f.getAbsolutePath(), f.getName(), null);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, f.getAbsolutePath());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f.getParentFile())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取设备型号
     * @return 设备型号
     */
    public static String getDeviceModel() {
        try {
            return Build.MODEL;
        } catch (Exception e) {
            return "";
        }
    }

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
        //Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1)
                options.inSampleSize = 1;
            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }
            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX)
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                else
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
            } else {
                if (beY < beX)
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                else
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
            }
            options.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null)
                return null;
            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }
            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null)
                    return bm;
                bm.recycle();
                bm = cropped;
            }
            return bm;
        } catch (OutOfMemoryError e) {
            Log.i("Vbe.extractThumbNail", e.toString());
            options = null;
        }
        return null;
    }

    public static final void showResultDialog(Context context, String msg, String title) {
        showResultDialog(context, msg, title, null);
    }

    public static final void showResultDialog(Context context, String msg, String title, DialogInterface.OnClickListener lis) {
        showResultDialog(context, msg, title, context.getString(R.string.vbe_tips_know), lis);
    }

    public static final void showResultDialog(Context context, String msg, String title, String ok, DialogInterface.OnClickListener lis) {
        if (msg == null) return;
        //String rmsg = msg.replace(",", "\n");
        new MyAlertDialog(context).setTitle(title).setMessage(msg).setNegativeButton(ok, lis).create().show();
    }

    public static String Join(String splitter, String[] strs) {
        if (strs.length <= 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for (String s : strs) {
            sb.append(s + splitter);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    public static String Join(String splitter, List<String> strs) {
        if (strs.size() <= 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for (String s : strs) {
            sb.append(s + splitter);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    //打印消息并且用Toast显示消息
    public static void toastShortMessage(Context activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastLongMessage(Context activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    public static void toastShortMessage(Context context, int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastLongMessage(Context context, int message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String trim(String src, char element) {
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;
        do {
            int beginIndex = src.indexOf(element) == 0 ? 1 : 0;
            int endIndex = src.lastIndexOf(element) + 1 == src.length() ? src.lastIndexOf(element) : src.length();
            src = src.substring(beginIndex, endIndex);
            beginIndexFlag = (src.indexOf(element) == 0);
            endIndexFlag = (src.lastIndexOf(element) + 1 == src.length());
        }
        while (beginIndexFlag || endIndexFlag);
        return src;
    }

    /**
     * 根据一个网络连接(String)获取bitmap图像
     *
     * @param imageUri
     * @return
     * @throws MalformedURLException
     */
    public static Bitmap getNetBitmap(String imageUri) {
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        } catch (IOException e) {
            Log.i("Vbe.getNetBitmap", e.toString());
            bitmap = null;
        }
        return bitmap;
    }

    public static void downloadFile(String uri, File file, DownloadListener agent) {
        // 下载网络上的文件
        try {
            URL myFileUrl = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int length = 0;
            long total = 0L;
            while ((length = is.read(b)) > 0) {
                fos.write(b, 0, length);
                total += length;
                agent.process(total);
            }
            is.close();
            fos.close();
            agent.success();
        } catch (OutOfMemoryError | IOException e) {
            e.printStackTrace();
            agent.failed();
        }
    }

    // =========
    // =通过URI获取本地图片的path
    // =兼容android 5.0
    // ==========
    public static String ACTION_OPEN_DOCUMENT = "android.intent.action.OPEN_DOCUMENT";

    public static String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (isSupportMD() && isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                final String id = getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) { // MediaProvider
                final String docId = getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type))
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                else if ("video".equals(type))
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                else if ("audio".equals(type))
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) { // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) { // File
            return uri.getPath();
        }
        return null;
    }

    private static final String PATH_DOCUMENT = "document";

    private static boolean isDocumentUri(Context context, Uri uri) {
        final List<String> paths = uri.getPathSegments();
        if (paths.size() < 2)
            return false;
        if (!PATH_DOCUMENT.equals(paths.get(0)))
            return false;
        return true;
    }

    private static String getDocumentId(Uri documentUri) {
        final List<String> paths = documentUri.getPathSegments();
        if (paths.size() < 2)
            throw new IllegalArgumentException("Not g document: " + documentUri);
        if (!PATH_DOCUMENT.equals(paths.get(0)))
            throw new IllegalArgumentException("Not g document: " + documentUri);
        return paths.get(1);
    }

    /**
     * 检查APP签名是否合法
     *
     * @param context
     * @return
     */
    public static boolean AuthKey(Context context) {
        try {
            return MD5Util.CheckKey(context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     *                      [url=home.php?mod=space&uid=7300]@return[/url] The value of
     *                      the _data column, which is typically g file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isNullOrEmpty(@Nullable String obj) {
        if (obj != null)
            return obj.equals("");
        return true;
    }

    public static String getFileType(String filename) {
        if (filename.indexOf(".") >= 0)
            return filename.substring(filename.lastIndexOf("."));
        return filename;
    }

    public static String getFileTypeName(String filename) {
        if (filename.indexOf(".") >= 0)
            return filename.substring(filename.lastIndexOf(".") + 1);
        return filename;
    }

    public static boolean isImageFile(String path) {
        String p = path.toLowerCase();
        return p.endsWith(".jpg") || p.endsWith(".png") || p.endsWith(".gif") || p.endsWith(".bmp");
    }

    public static String removeEmptyItem(String[] list) {
        if (list.length == 1)
            return list[0];
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            if (!isNullOrEmpty(s)) {
                sb.append(",");
                sb.append(s);
            }
        }
        return sb.toString().substring(1);
    }

    public static boolean notSupportMD() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isSupportMD() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT;
    }

    public static boolean isAndroidL() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    public static boolean isAndroidM() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public static boolean isAndroidN() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N);
    }

    public static boolean isAndroidO() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O);
    }

    public static boolean isAndroidP() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P);
    }

    public static boolean isAndroidQ() {
        return (Build.VERSION.SDK_INT >= 29);
    }

    public static boolean isAndroidR() {
        return (Build.VERSION.SDK_INT >= 30);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasPermission(Context c, String p) {
        if (isAndroidM())
            return c.checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED;
        else return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasAllPermissions(Context c, String... p) {
        if (isAndroidM()) {
            for (String s : p) {
                if (!hasPermission(c, s))
                    return false;
            }
        }
        return true;
    }

    public static boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkSelfPermission(Context c, String... p) {
        if (isAndroidM()) {
            for (String i : p) {
                if (c.checkSelfPermission(i) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }

    @TargetApi(23)
    public static void requestPermission(Activity a, int code, String... permissions) {
        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (!hasPermission(a, permission))
                list.add(permission);
        }
        if (list.size() > 0) {
            a.requestPermissions(list.toArray(new String[0]), code);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static void addClipboard(Context c, String label, String msg) {
        ClipboardManager cbm = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setPrimaryClip(ClipData.newPlainText(label, msg));
    }

    /**
     * 添加数据到剪贴板
     *
     * @param c   上下文
     * @param msg 文本内容
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static void addClipboard(Context c, String msg) {
        ClipboardManager cbm = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setPrimaryClip(ClipData.newPlainText("VbeUtil", msg));
    }

    /**
     * 读取文本文件并返回
     *
     * @param is InputStream
     * @return 文件内容
     */
    public static String ReadFileToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = -1;
            while ((i = is.read()) != -1) {
                baos.write(i);
            }
            baos.close();
            return baos.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取设备序列号
     *
     * @return
     */
    @Deprecated
    public static String getSerialNo() {
        return Build.getSerial();
    }

    /**
     * 获取设备标识和imei
     *
     * @param context
     * @return
     */
    @Deprecated
    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        try {
            deviceId.append("model:");
            deviceId.append(getDeviceModel());
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!isNullOrEmpty(imei)) {
                deviceId.append(",imei:");
                deviceId.append(imei);
                return deviceId.toString();
            }
        } catch (Exception e) {
            return deviceId.toString();
        }
        return deviceId.toString();
    }

    /**
     * 获取文件扩展名
     *
     * @param name 文件名
     * @return 文件扩展名
     */
    public static String getExtension(String name) {
        String suffix = "";
        int idx = name.lastIndexOf(".");
        if (idx > 0)
            suffix = name.substring(idx);
        return suffix.toLowerCase();
    }

    /**
     * 获取文件扩展名(去掉.)
     *
     * @param name 文件名
     * @return 文件扩展名
     */
    public static String getExtensionName(String name) {
        String suffix = "";
        int idx = name.lastIndexOf(".");
        if (idx > 0)
            suffix = name.substring(idx + 1);
        return suffix.toLowerCase();
    }

    /**
     * 得到扩展名MIME类型
     *
     * @param fileName 文件名
     * @return 文件MIME类型
     */
    public static String getMimeType(String fileName) {
        String mime = "*/*";
        String tmp = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtensionName(fileName));
        if (tmp != null)
            mime = tmp;
        return mime;
    }

    /**
     * 显示敏感信息
     *
     * @param str   原始字符串
     * @param start 隐藏开始位置
     * @param end   隐藏结束位置
     * @return 带*的敏感字符串
     */
    public static String getSecstr(String str, int start, int end) {
        char[] chars = str.toCharArray();
        int _char = (str.length() - start - end);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (i < start) {
                sb.append(chars[i]);
            } else if (i < start + _char) {
                sb.append("*");
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
		/*String regex = "(\\S{"+start+"})(.*)(\\S{"+end+"})";
		Matcher m = Pattern.compile(regex).matcher(str);
		if (m.find())
		{
			String rep = m.group(2);
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<rep.length(); i++)
			{
				sb.append("*");
			}
			return str.replaceAll(rep, sb.toString());
		}
		return str;*/
    }

    //获取清单文件数据
    public static String getMetaValue(Context context, String metaKey) {
        String apiKey = "";
        if (context == null || isNullOrEmpty(metaKey))
            return apiKey;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (ai != null) {
                if (ai.metaData != null)
                    apiKey = ai.metaData.getString(metaKey);
            }
        } catch (Exception e) {
            Log.i("VbeUtil.getMetaValue()", e.toString());
        }
        return apiKey;
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public static int getStatusBarHeight(Activity p) {
        Rect rect = new Rect();
        p.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    //////////////////////////////
    public static boolean isValidFloat(@Nullable Float d) {
        return d != null;
    }

    public static boolean isPlusFloat(@Nullable Float d) {
        return d != null && d > 0;
    }

    public static boolean isValidDouble(@Nullable Double d) {
        return d != null;
    }

    public static boolean isPlusDouble(@Nullable Double d) {
        return d != null && d > 0;
    }

    public static boolean isValidInt(@Nullable Integer i) {
        return i != null;
    }

    public static boolean isPlusInt(@Nullable Integer i) {
        return i != null && i > 0;
    }

    public static boolean isValidObj(@Nullable Object obj) {
        return obj != null;
    }

    /**
     * 将米转换为千米
     *
     * @param meter 米数
     * @return
     */
    public static String getFormatLengthValue(Double meter) {
        if (meter < 1000) {
            BigDecimal result = BigDecimal.valueOf(meter);
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + "m";
        } else {
            BigDecimal result1 = new BigDecimal(Double.toString(meter / 1000));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + "km";
        }
    }

    /**
     * 显示加载框
     *
     * @param context Activity content
     * @param title   标题
     * @param msg     内容
     * @return Dialog
     */
    public static Dialog showProgressDialog(Context context, String title, String msg) {
        return ProgressDialog.show(context, title, msg, true, false);
    }


    /**
     * 显示确认框
     *
     * @param context     Activity content
     * @param title       标题
     * @param message     内容
     * @param posListener 点击事件监听器
     */
    public static void showConfirmCancelDialog(Context context, int title, int message, final DialogResult posListener) {
        new MyAlertDialog(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (posListener != null) posListener.onConfirm();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (posListener != null) posListener.onCancel();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (posListener != null) posListener.onCancel();
                    }
                }).create().show();
    }

    /**
     * 显示确认框
     *
     * @param context     Activity content
     * @param title       标题
     * @param message     内容
     * @param posListener 点击事件监听器
     */
    public static void showConfirmCancelDialog(Context context, String title, String message, final DialogResult posListener) {
        MyAlertDialog dialog = new MyAlertDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (posListener != null) posListener.onConfirm();
            }
        });
        dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (posListener != null) posListener.onCancel();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (posListener != null) posListener.onCancel();
            }
        }).create();
        dialog.show();
    }

    /**
     * 显示确认框
     *
     * @param context     Activity content
     * @param title       标题
     * @param message     弹窗内容
     * @param ok          确认按钮文字
     * @param cancel      取消按钮文字
     * @param posListener 点击事件监听器
     */
    public static void showConfirmCancelDialog(Context context, String title, String message, String ok, String cancel, final DialogResult posListener) {
        MyAlertDialog dialog = new MyAlertDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (posListener != null) posListener.onConfirm();
            }
        });
        dialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (posListener != null) posListener.onCancel();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (posListener != null) posListener.onCancel();
            }
        }).create();
        dialog.show();
    }

    public static void showEditDialog(Context context, String title, DialogEditView view, String ok, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view.getView())
                .setPositiveButton(ok, listener)
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(false)
                .show();
    }

    public static void startActivity(@Nullable Context context, Class<?> cls) {
        if (context != null)
            context.startActivity(new Intent(context, cls));
    }

    /**
     * 获取圆形图片
     *
     * @param context Activity content
     * @param bitmap  原始图片
     * @return 圆形图片
     */
    public static RoundedBitmapDrawable getCircularDrawable(Context context, Bitmap bitmap) {
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        drawable.setCircular(true);
        drawable.setAntiAlias(true);
        return drawable;
    }

    public static Bitmap getResourcesBitmap(Resources res, int resId) {
        return BitmapFactory.decodeResource(res, resId);
    }

    /**
     * 将图标替换颜色-调色板tint
     *
     * @param bitmap
     * @param tintColor
     * @return
     */
    public static Bitmap tintBitmap(Bitmap bitmap, int tintColor) {
        Bitmap bm = bitmap.extractAlpha();
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(tintColor);
        canvas.drawBitmap(bm, 0, 0, paint);
        return bmp;
    }

    /**
     * 高斯模糊图片
     *
     * @param context Activity上下文
     * @param bitmap 要模糊的图片
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Context context, Bitmap bitmap) {
        try {
            Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
            Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
            blurScript.setRadius(25.f);
            blurScript.setInput(allIn);
            blurScript.forEach(allOut);
            allOut.copyTo(outBitmap);
            bitmap.recycle();
            rs.destroy();
            return outBitmap;
        } catch (Exception e) {
            Log.i("VbeUtil.blurBitmap", e.toString());
        }
        return null;
    }

    /**
     * 将数组按照指定字符串连接
     * @param splitter 连接符
     * @param arr 数组
     * @return 连接后的字符串
     */
    public static String join(String splitter, String[] arr) {
        if (arr.length <= 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            sb.append(s).append(splitter);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 将数组按照指定字符串连接
     * @param splitter 连接符
     * @param arr 数组
     * @return 连接后的字符串
     */
    public static String join(String splitter, List<String> arr) {
        if (arr.size() <= 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            sb.append(s).append(splitter);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 深拷贝List数组
     *
     * @param src 源数组
     * @return 拷贝后的数组
     */
    public static <T extends Serializable> List<T> deepCopy(List<T> src) {
        List<T> res = new ArrayList<>();
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            res = (List<T>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 深拷贝二维数组
     *
     * @param arrays 源数组
     * @return 拷贝后的数组
     */
    public static <T extends Serializable> T[][] deepCopy(T[][] arrays) {
        T[][] arr = arrays.clone();
        for (int i = 0; i < arrays.length; i++) {
            arr[i] = deepCopy(arrays[i]);
        }
        return arr;
    }

    /**
     * 深拷贝泛型数组
     *
     * @param array 源数组
     * @return 拷贝后的数组
     */
    public static <T extends Serializable> T[] deepCopy(T[] array) {
        T[] arr = array.clone();
        for (int i = 0; i < array.length; i++) {
            arr[i] = deepCopy(array[i]);
        }
        return arr;
    }

    /**
     * 深拷贝对象(基于Serializable)
     *
     * @param object 源对象
     * @return 拷贝后的对象
     */
    public static <T extends Serializable> T deepCopy(T object) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(object);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            return (T)in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Base64转换为图片
     *
     * @param base64 待转换的base64
     * @return 转换后的图片
     */
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static Bitmap base64ToBitmap(String base64) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(base64, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 图片转换为Base64
     *
     * @param src 待转换的图片
     * @param format 图片格式
     * @param needRecycle 是否需要回收
     * @return 图片的Base64代码
     */
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static String bitmapToBase64(Bitmap src, Bitmap.CompressFormat format, boolean needRecycle) {
        String base64 = "";
        try {
            byte[] bitmapArray = bmpToByteArray(src, format, needRecycle);
            byte[] encode = Base64.encode(bitmapArray, Base64.DEFAULT);
            base64 = new String(encode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }

    public static int spanCount(Context context, int gridExpectedSize) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        float expected = (float) screenWidth / (float) gridExpectedSize;
        int spanCount = Math.round(expected);
        if (spanCount == 0) {
            spanCount = 1;
        }
        return spanCount;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Deprecated
    public static void startActivityOption(Activity context, Intent intent, View view, String shareName) {
        startActivityOptions(context, intent, view, shareName, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void startActivityOptions(Activity context, Intent intent, View view, String shareName) {
        startActivityOptions(context, intent, view, shareName, true);
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public static void startActivityOptions(Activity context, Class<?> cls) {
        startActivityOptions(context, new Intent(context, cls));
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public static void startActivityForResult(Activity context, Class<?> cls, int requestCode) {
        startActivityForResult(requestCode, context, new Intent(context, cls));
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public static void startActivityOptions(Activity context, Intent intent, Pair... pairs) {
        try {
            if (isSupportMD()) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, pairs);
                context.startActivity(intent, options.toBundle());
            } else {
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        } catch (Exception e) {
            context.startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public static void startActivityForResult(int requestCode, Activity context, Intent intent, Pair<View, String>... pairs) {
        try {
            if (isSupportMD()) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, pairs);
                context.startActivityForResult(intent, requestCode, options.toBundle());
            } else {
                context.startActivityForResult(intent, requestCode);
                context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        } catch (Exception e) {
            context.startActivityForResult(intent, requestCode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void startActivityOptions(Activity context, Intent intent, View view, String shareName, boolean setName) {
        try {
            if (isSupportMD()) {
                if (setName)
                    view.setTransitionName(shareName);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, shareName);
                context.startActivity(intent, options.toBundle());
            } else
                startActivityOptions(context, intent);
        } catch (Exception e) {
            context.startActivity(intent);
        }
    }

    /**
     * 安装apk文件
     * @param context Activity content
     * @param file apk文件
     * @param provider file provider
     */
    public static void installAPK(Context context, @NonNull File file, String provider) {
        if (file.exists()) {
            Intent intent;
            if (VbeUtil.isAndroidM()) {
                intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + context.getPackageName()));
            } else {
                intent = new Intent();
            }
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String mime = getMimeType(file.getName());
            if (VbeUtil.isAndroidN()) {
                Uri contentUri = FileProvider.getUriForFile(context, provider, file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(contentUri, mime);
            } else {
                intent.setDataAndType(Uri.fromFile(file), mime);
            }
            context.startActivity(intent);
        }
    }

    /**
     * 延迟执行
     * @param runnable Runnable
     * @param delayed 延迟时间（毫秒）
     */
    public static void runDelayed(Runnable runnable, long delayed) {
        new Handler().postDelayed(runnable, delayed);
    }
}
