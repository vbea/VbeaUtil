package com.vbes.util;


import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import androidx.annotation.NonNull;

/**
 * 使用单位显示文件大小(KM、MB、GB等)
 */
public class FileUtil {

    /**
     * 获取文件大小单位为以下类型的double值
     */
    public enum SIZE_TYPE {
        /**
         * 获取文件大小单位为字节的double值
         */
        TYPE_B,
        /**
         * 获取文件大小单位为千字节的double值
         */
        TYPE_KB,
        /**
         * 获取文件大小单位为兆字节的double值
         */
        TYPE_MB,
        /**
         * 获取文件大小单位为吉字节的double值
         */
        TYPE_GB;
    }

    /**
     * 获取指定文件大小
     *
     * @param file 指定文件
     * @return 指定文件的大小(字节)
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        if (file.exists()) {
            return file.length();
        } else {
            return 0;
        }
    }

    /**
     * 递归获取指定文件夹下的大小
     *
     * @param f 文件目录
     * @return 总大小(字节)
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 获取指定文件的指定单位的大小
     *
     * @param blockSize 文件大小
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileSizeNumber(@NonNull long blockSize, @NonNull SIZE_TYPE sizeType) {
        return formatFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getFileOrDirectoryFormatSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FileSizeUtil", "获取失败!");
        }
        return formatFileSize(blockSize);
    }

    /**
     * 转换文件大小
     *
     * @param bytes 文件字节数
     * @return 格式化后的文件大小
     */
    public static String getFormatSize(@NonNull long bytes) {
        if (bytes < 1024)
            return bytes + "B";
        double kiloByte = bytes / 1024;
        if (kiloByte < 1024) {
            BigDecimal result1 = new BigDecimal(kiloByte);
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1024) {
            BigDecimal result2 = new BigDecimal(megaByte);
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1024) {
            BigDecimal result3 = new BigDecimal(gigaByte);
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        double teraByte = gigaByte / 1024;
        BigDecimal result4 = new BigDecimal(teraByte);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 转换文件大小
     *
     * @param fileS 文件字节数
     * @return 格式化后的文件大小
     */
    private static String formatFileSize(@NonNull long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return 格式化后大小
     */
    private static double formatFileSize(long fileS, SIZE_TYPE sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case TYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case TYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case TYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case TYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
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
     * 获取指定文件路径的文件名
     * @param path 文件路径
     * @return 文件名
     */
    public static String getFileNameForPath(String path) {
        return path.substring(path.lastIndexOf(File.separator)+1);
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

    public static boolean isImageFile(String path) {
        String p = path.toLowerCase();
        return p.endsWith(".jpg") || p.endsWith(".png") || p.endsWith(".gif") || p.endsWith(".bmp");
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
}
