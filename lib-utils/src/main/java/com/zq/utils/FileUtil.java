package com.zq.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Desc : 文件工具类
 * Author : Lauzy
 * Date : 2017/12/4
 * Blog : http://www.jianshu.com/u/e76853f863a9
 * Email : freedompaladin@gmail.com
 */
public class FileUtil {

    /**
     * 读取 assets 文件
     *
     * @param context 上下文
     * @param name    文件名称
     * @return 解析后的字符串
     */
    @Nullable
    public static String readAssetsFile(Context context, String name) {

        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        try {
            StringBuilder result = new StringBuilder();
            inputStream = context.getAssets().open(name);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String temp;
            while (null != (temp = bufferedReader.readLine())) {
                result.append(temp);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            IoUtil.closeSilently(inputStream);
            IoUtil.closeSilently(bufferedReader);
        }
        return null;
    }

    /**
     * 判断SD卡是否挂载
     *
     * @return 是否存在SD卡
     */
    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()) && Environment.getExternalStorageDirectory().canWrite();
    }

    /**
     * 获取照片目录
     *
     * @return 照片目录
     */
    public static String getCameraDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/camera/";
    }

    /**
     * 获取data目录
     *
     * @return data
     */
    public static String getDataDir() {
        return Environment.getDataDirectory().getAbsolutePath();
    }

    /**
     * 创建文件
     *
     * @param folder 文件夹
     * @return 文件
     */
    @Nullable
    public static File createFile(File folder) {

        if (!folder.exists() || !folder.isDirectory()) {

            boolean success = folder.mkdirs();
            if (!success) {
                return null;
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = "IMG_" + dateFormat.format(new Date(System.currentTimeMillis())) + ".jpg";
        return new File(folder, filename);
    }

    /**
     * 适配7.0获取fileProvider
     *
     * @param context 上下文
     * @return fileProvider
     */
    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".provider";
    }


    /**
     * 压缩文件
     *
     * @param fileOrDirectory source
     * @param outFile         target
     * @return success
     * @throws IOException e
     */
    public static void zip(File fileOrDirectory, File outFile) throws IOException {
        //定义压缩输出流
        ZipOutputStream out = null;
        try {
            //传入压缩输出流
            out = new ZipOutputStream(new FileOutputStream(outFile));
            //判断是否是一个文件或目录
            //如果是文件则压缩
            if (fileOrDirectory.isFile()) {
                zipFileOrDirectory(out, fileOrDirectory, "");
            } else {
                //否则列出目录中的所有文件递归进行压缩
                File[] entries = fileOrDirectory.listFiles();
                for (File entry : entries) {
                    zipFileOrDirectory(out, entry, "");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            IoUtil.closeSilently(out);
        }
    }

    private static void zipFileOrDirectory(ZipOutputStream out, File fileOrDirectory, String curPath) throws IOException {
        FileInputStream in = null;
        try {
            //判断目录是否为null
            if (!fileOrDirectory.isDirectory()) {
                byte[] buffer = new byte[4096];
                int bytes_read;
                in = new FileInputStream(fileOrDirectory);
                //归档压缩目录
                ZipEntry entry = new ZipEntry(curPath + fileOrDirectory.getName());
                //将压缩目录写到输出流中
                out.putNextEntry(entry);
                while ((bytes_read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                }
                out.closeEntry();
            } else {
                //列出目录中的所有文件
                File[] entries = fileOrDirectory.listFiles();
                for (File entry : entries) {
                    //递归压缩
                    zipFileOrDirectory(out, entry, curPath + fileOrDirectory.getName() + "/");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            IoUtil.closeSilently(in);
        }
    }

    /**
     * 解压缩一个文件
     *
     * @param zipFile 压缩文件
     * @param dstDir  解压缩的目标目录
     * @throws IOException 当解压缩过程出错时抛出
     */
    public static void unZipFile(File zipFile, File dstDir) throws IOException {

        if (!dstDir.exists()) {
            boolean success = dstDir.mkdirs();
            if (!success) {
                throw new IOException("can not create directory : " + dstDir.getAbsolutePath());
            }
        }

        ZipFile zf = null;
        try {

            zf = new ZipFile(zipFile);

            for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                if (entry.isDirectory()) {
                    String dirStr = entry.getName();
                    dirStr = new String(dirStr.getBytes("8859_1"), "GB2312");
                    File f = new File(dstDir, dirStr);
                    if (!f.exists()) {
                        boolean mkDir = f.mkdir();
                        if (!mkDir) {
                            throw new IOException("can not create directory : " + f.getAbsolutePath());
                        }
                    }
                    continue;
                }

                InputStream in = null;
                OutputStream out = null;

                try {

                    in = zf.getInputStream(entry);
                    File desFile = new File(dstDir, entry.getName());
                    if (!desFile.exists()) {
                        boolean success = desFile.createNewFile();
                        if (!success) {
                            throw new IOException("can not create file : " + desFile.getAbsolutePath());
                        }
                    }

                    out = new FileOutputStream(desFile);
                    byte buffer[] = new byte[1024];
                    int realLength;
                    while ((realLength = in.read(buffer)) > 0) {
                        out.write(buffer, 0, realLength);
                    }
                    out.flush();
                } finally {

                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                }
            }
        } finally {

            if (zf != null) {
                zf.close();
            }
        }
    }

    /**
     * 复制目录或文件到指定目录
     *
     * @param src    src
     * @param dstDir dstDir
     * @throws IOException
     */
    public static void copy(File src, File dstDir) throws IOException {

        if (!src.exists()) {
            throw new IOException("file not found exception : " + src.getAbsolutePath());
        }

        if (!dstDir.exists()) {
            boolean mkDirs = dstDir.mkdirs();
            if (!mkDirs) {
                throw new IOException("can not create directory : " + dstDir.getAbsolutePath());
            }
        }

        if (src.isDirectory()) {

            File file = new File(dstDir, src.getName());
            if (!file.exists()) {
                boolean mkDir = file.mkdir();
                if (!mkDir) {
                    throw new IOException("can not create directory : " + file.getAbsolutePath());
                }
            }
            File[] files = src.listFiles();
            if (files == null) {
                return;
            }
            for (File file1 : files) {
                copy(file1, file);
            }
        } else if (src.isFile()) {

            copyFile(src, new File(dstDir, src.getName()));
        }
    }

    /**
     * 复制文件
     *
     * @param src     src
     * @param dstFile dst
     * @throws IOException
     */
    public static void copyFile(File src, File dstFile) throws IOException {

        OutputStream os = null;
        InputStream is = null;
        try {
            int bufferLength = 8 * 1024;
            is = new FileInputStream(src);
            os = new BufferedOutputStream(new FileOutputStream(dstFile, false));
            byte data[] = new byte[bufferLength];
            int len;
            while ((len = is.read(data, 0, bufferLength)) != -1) {
                os.write(data, 0, len);
            }
            os.flush();
        } finally {
            IoUtil.closeSilently(os);
            IoUtil.closeSilently(is);
        }
    }


    /**
     * 输入流读到输出流
     * @param inputStream inputStream
     * @param outputStream outputStream
     * @throws IOException
     */
    public static void readStream(@NonNull InputStream inputStream, @NonNull OutputStream outputStream) throws IOException {

        try {
            final int bufferLength = 1024;
            byte data[] = new byte[1024];
            int len;
            while ((len = inputStream.read(data, 0, bufferLength)) != -1) {
                outputStream.write(data, 0, len);
            }
            outputStream.flush();
        } finally {
            IoUtil.closeSilently(inputStream);
            IoUtil.closeSilently(outputStream);
        }
    }

    /**
     * 保存图片到本地
     *
     * @param bitmap bitmap
     */
    public static void saveImage(@NonNull Bitmap bitmap, @NonNull File saveFile) {

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(saveFile));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtil.closeSilently(bos);
        }
    }
}
