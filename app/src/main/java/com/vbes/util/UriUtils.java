package com.vbes.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Uri与文件路径互转工具
 * @author Created by Vbe on 2020/8/24.
 */
public class UriUtils {
    private UriUtils(){}

    /**
     * 根据Uri获取文件真实路径
     * @param context Activity content
     * @param uri 文件Uri
     * @return 文件路径
     */
    public static String getFileRealPath(Context context, Uri uri) {
        String path = "";
        try {
            path = queryFileRealPath(context, uri);
        } catch (Exception e) {
            try {
                path = getFileForUri(context, uri);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return path;
    }

    private static String queryFileRealPath(Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }

                }
                cursor.close();
            }
            if (data == null) {
                data = getImageAbsolutePath(context, uri);
            }

        }
        return data;
    }

    private static String getFileForUri(Context context, Uri uri) throws Exception {
        String realPath = "";
        String pathUri = uri.getEncodedPath();
        String authority = uri.getAuthority();
        int splitIndex = pathUri.indexOf(47, 1);
        String tags = Uri.decode(pathUri.substring(1, splitIndex));
        pathUri = Uri.decode(pathUri.substring(splitIndex + 1));
        ProviderInfo info = context.getPackageManager().resolveContentProvider(authority, PackageManager.GET_META_DATA);
        XmlResourceParser in = info.loadXmlMetaData(context.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
        if (in == null) {
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
        } else {
            int type;
            while((type = in.next()) != 1) {
                String tag = in.getName();
                String name = in.getAttributeValue((String)null, "name");
                String path = in.getAttributeValue((String)null, "path");
                if (tags.equals(name)) {
                    File target = null;
                    if ("root-path".equals(tag)) {
                        target = new File("/");;
                    } else if ("files-path".equals(tag)) {
                        target = context.getFilesDir();
                    } else if ("cache-path".equals(tag)) {
                        target = context.getCacheDir();
                    } else if ("external-path".equals(tag)) {
                        target = Environment.getExternalStorageDirectory();
                    } else {
                        File[] externalMediaDirs;
                        if ("external-files-path".equals(tag)) {
                            externalMediaDirs = ContextCompat.getExternalFilesDirs(context, (String)null);
                            if (externalMediaDirs.length > 0) {
                                target = externalMediaDirs[0];
                            }
                        } else if ("external-cache-path".equals(tag)) {
                            externalMediaDirs = ContextCompat.getExternalCacheDirs(context);
                            if (externalMediaDirs.length > 0) {
                                target = externalMediaDirs[0];
                            }
                        } else if (Build.VERSION.SDK_INT >= 21 && "external-media-path".equals(tag)) {
                            externalMediaDirs = context.getExternalMediaDirs();
                            if (externalMediaDirs.length > 0) {
                                target = externalMediaDirs[0];
                            }
                        }
                    }
                    if (target != null) {
                        realPath = target.getAbsolutePath() + path.replaceFirst("(/|.)", "") + File.separator + pathUri;
                    }
                    return realPath;
                }
            }
        }
        return realPath;
        //File root = (File)this.mRoots.get(tag);
    }

    @Deprecated
    public static Uri getUri(final String filePath) {
        return Uri.fromFile(new File(filePath));
    }

    public static Uri getUri(Context context, String authority, String filePath) {
        return getUri(context, authority, new File(filePath));
    }

    public static Uri getUri(Context context, String authority, File file) {
        return FileProvider.getUriForFile(context, authority, file);
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null) {
            return null;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }

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
}