package com.vbes.util;


import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 使用单位显示文件大小(KM、MB、GB等)
 */
public class FileSizeUtil {

    /**
     * 获取文件大小单位为以下类型的double值
     */
    public static enum TYPE {
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
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, TYPE sizeType) {
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
        return formatFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
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
     * 获取指定文件大小
     *
     * @param file 指定文件
     * @return 指定文件的大小(字节)
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("FileSizeUtil", "文件不存在!");
        }
        return size;
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
     * 转换文件大小
     *
     * @param fileS 文件字节数
     * @return 格式化后的文件大小
     */
    private static String formatFileSize(long fileS) {
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
     * 转换文件大小
     *
     * @param bytes 文件字节数
     * @return 格式化后的文件大小
     */
    public static String getFormatSize(long bytes) {
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
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return 格式化后大小
     */
    public static double formatFileSize(long fileS, TYPE sizeType) {
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
}
