package com.puti.education.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;



public class FileUtils {
    public static final int CPY_BUFFER_SIZE = 4 * 1024;



    private static Bitmap ratingImage(String filePath, Bitmap bitmap) {
        int degree = readPictureDegree(filePath);
        return rotaingImageView(degree, bitmap);
    }


    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
//        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {

        }
        return degree;
    }

    /**
     * 把字节数组保存为一个文件
     *
     * @param b
     * @param outputFile
     * @return
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        File ret = null;
        BufferedOutputStream stream = null;
        try {
            ret = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(ret);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {

        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {

                }
            }
        }
        return ret;
    }



    /**
     * 获取目录名称
     *
     * @param url
     * @return FileName
     */
    public static String getFileName(String url) {
        int lastIndexStart = url.lastIndexOf("/");
        if (lastIndexStart != -1) {
            return url.substring(lastIndexStart + 1, url.length());
        } else {
            return null;
        }
    }

    /**
     * 删除该目录下的文件
     *
     * @param path
     */
    public static void delFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 本地是否存在此文件
     */
    public static boolean hasFile(String fileName) {

        return new File(fileName).exists();

    }

    /**
     * 字符串的MD5
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            sb.append(hash[i]);
        }
        return sb.toString();
    }

    private static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 复制文件
     *
     * @param srcFile
     *            源文件路径
     * @param dstFile
     *            目标文件路径
     * @return
     */
    public static boolean copyFile(File srcFile, File dstFile) {
        boolean resu = false;

        FileInputStream fis = null;
        BufferedOutputStream fos = null;

        try {
            fis = new FileInputStream(srcFile);
            fos = new BufferedOutputStream(new FileOutputStream(dstFile));

            byte[] buffer = new byte[CPY_BUFFER_SIZE];

            int readLen = 0;

            while (-1 != (readLen = fis.read(buffer))) {
                fos.write(buffer, 0, readLen);
            }

            fos.flush();

            resu = true;
        } catch (IOException e) {
            resu = false;
        } finally {
            Closer.close(fos);
            Closer.close(fis);
        }

        return resu;
    }

    /**
     * 返回应用存储数据的根目录，sd卡优先.
     * sd卡上的: sd卡根目录/formax/app名称;
     * 内卡上的: /data/data/<包名>/files
     */
    public static String getAppFilePath() {
        boolean sdAvailable = hasSDCard();
        if (!sdAvailable) {
            return "";
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "aEducation" + File.separator;
            path = path + "gen";
            ensureDirectoryExist(path);
            return path;
        }
    }

    /**
     * app在sd卡上的目录.
     * 如果sd卡目录不可用, 则返回""
     */
    public static String getAppSDFilePath() {
        boolean sdAvailable = hasSDCard();
        if (!sdAvailable) {
            return "";
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "aEducation" + File.separator;
            path = path + "gen";
            ensureDirectoryExist(path);
            return path;
        }
    }



    /**
     * 往指定路径写一个字符串
     *
     * @param path
     * @param text
     * @return
     */
    public static boolean write(String path, String text, boolean append) {
        File file = new File(path);
        File parent = file.getParentFile();
        if (!parent.exists())
            parent.mkdirs();
        else if (!parent.isDirectory()) {
            parent.delete();
            parent.mkdirs();
        }
        FileWriter fw = null;
        try {
            file.createNewFile();
            fw = new FileWriter(file, append);
            fw.write(text);
        } catch (IOException e) {
        } finally {
            Closer.close(fw);
        }
        return false;
    }

    /**
     * 读取指定文件里的字符串
     *
     * @param path
     * @return
     */
    public static String read(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            return br.readLine();
        } catch (IOException e) {
        } finally {
            Closer.close(br);
            Closer.close(fr);
        }
        return "";
    }

    /**获取temp路径*/
    public static String getTempDirPath() {
        String tempPath = FileUtils.getAppFilePath() + File.separator + "temp";
        ensureDirectoryExist(tempPath);
        return tempPath;
    }

    public static String getReportRootPath() {
        String path = FileUtils.getAppFilePath() + File.separator + "report";
        ensureDirectoryExist(path);
        return path;
    }

    public static String getAttachmentRootPath() {
        String path = FileUtils.getAppFilePath() + File.separator + "attachment";
        ensureDirectoryExist(path);
        return path;
    }

    public static String getAppPhotoRootPath() {
        String path = FileUtils.getAppFilePath() + File.separator + "photo";
        ensureDirectoryExist(path);
        return path;
    }

    public static String getAppDownloadPath() {
        String path = FileUtils.getAppFilePath() + File.separator + "download";
        ensureDirectoryExist(path);
        return path;
    }

    public static String getAppExceptionPath() {
        String path = FileUtils.getAppFilePath() + File.separator + "exception";
        ensureDirectoryExist(path);
        return path;
    }

    private static void ensureDirectoryExist(String dir) {
        if (TextUtils.isEmpty(dir)) {
            //do nothing
        } else {
            File file = new File(dir);
            if (!file.exists()){
                file.mkdirs();
            } else {
                if (!file.isDirectory()){
                    file.delete();
                    file.mkdirs();
                }
            }
        }
        Log.e("FileUtils", "路径: " + dir);
    }

    /**
     * 打开系统文件选择器
     */
    public static boolean openFileChooser(Activity activity, int resultCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            activity.startActivityForResult(Intent.createChooser(intent, ""),
                    resultCode);
            return true;
        } catch (android.content.ActivityNotFoundException ex) {
        }
        return false;
    }

    public static byte[] getByteArrayFromFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found:" + path);
        }
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        try {
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] buffer = new byte[2048];
            int size = 0;
            while ((size = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, size);
            }
            return baos.toByteArray();
        } finally {
            Closer.close(fis);
            Closer.close(baos);
        }
    }

    /**
     * 将Bitmap保存到本地
     *
     * @param bmp      需要保存的Bitmap对象
     * @param filePath 保存路径
     * @param size 压缩率
     */
    public static void saveBitmap(Bitmap bmp, String filePath,int size) {
        File file = new File(filePath);
        if (file != null && file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(CompressFormat.PNG, size, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    public static  Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        options.inPreferredConfig = Config.RGB_565;
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /*
	 * 保存文本文件
	 */
    public static boolean appendTextFile(String strPath, String strText) {
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        boolean bRet = true;
        File file = null;

        try {
            file = new File(strPath);

            outputStream = new FileOutputStream(file, true);
            outputStreamWriter = new OutputStreamWriter(outputStream);

            outputStreamWriter.write("\n");
            outputStreamWriter.write(strText);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (Exception e) {
            bRet = false;
        }

        return bRet;
    }


    public static final int ZIP_BUFFER_SIZE = 4 * 1024;

    /**
     * 尝试删除文件/文件夹。如果删除失败，尝试在虚拟机退出时删除。
     *
     * @param file
     *            文件/文件夹
     * @return 删除成功/失败
     */
    public static boolean deleteFile(File file) {
        if (file != null) {
            // 是文件，直接删除
            if (file.isFile()) {
                if (!file.delete()) {
                    file.deleteOnExit();

                    return false;
                } else {
                    return true;
                }
            }
            // 是目录，递归删除
            else if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        deleteFile(subFile);
                    }
                }
                return file.delete();
            }
            // 那你是啥嘛……
            else {
                return false;
            }
        } else {
            return false;
        }
    }

    public interface OnDeleteFileListener {
        void onDelete(File file, long size);
    }

    public static boolean deleteFile(File file, OnDeleteFileListener listener) {
        if (file != null) {
            // 是文件，直接删除
            if (file.isFile()) {
                if (listener != null) {
                    listener.onDelete(file, file.length());
                }
                if (!file.delete()) {
                    file.deleteOnExit();
                    return false;
                } else {
                    return true;
                }
            }
            // 是目录，递归删除
            else if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        deleteFile(subFile, listener);
                    }
                }
                return file.delete();
            }
            // 那你是啥嘛……
            else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * ZIP压缩多个文件/文件夹
     *
     * @param srcFiles
     *            要压缩的文件/文件夹列表
     * @param dest
     *            目标文件
     * @return 压缩成功/失败
     */
    public static boolean zip(File[] srcFiles, File dest) {
        // 参数检查
        if (srcFiles == null || srcFiles.length < 1 || dest == null) {
            return false;
        }
        boolean result = false;
        ZipOutputStream zos = null;
        try {
            byte[] buffer = new byte[ZIP_BUFFER_SIZE];
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(dest, false)));
            // 添加文件到ZIP压缩流
            for (File src : srcFiles) {
                doZip(zos, src, null, buffer);
            }
            zos.flush();
            zos.closeEntry();
            result = true;
        } catch (IOException e) {
            result = false;
        } finally {
            Closer.close(zos);
        }

        return result;
    }

    /**
     * 方法：ZIP压缩单个文件/文件夹
     *
     * @param src
     *            源文件/文件夹
     * @param dest
     *            目标文件
     * @return 压缩成功/失败
     */
    public static boolean zip(File src, File dest) {
        return zip(new File[] { src }, dest);
    }

    /**
     * 方法：解压缩单个ZIP文件
     *
     * @param src
     *            源文件/文件夹
     * @param destFolder
     *            目标文件夹
     * @return 解压缩成功/失败
     */
    public static boolean unzip(File src, File destFolder) {
        if (src == null || src.length() < 1 || !src.canRead()) {
            return false;
        }
        boolean resu = false;
        if (!destFolder.exists()) {
            destFolder.mkdirs();
        }
        ZipInputStream zis = null;
        BufferedOutputStream bos = null;
        ZipEntry entry = null;
        byte[] buffer = new byte[8 * 1024];
        int readLen = 0;
        try {
            zis = new ZipInputStream(new FileInputStream(src));
            while (null != (entry = zis.getNextEntry())) {
//                System.out.println(entry.getName());
                if (entry.isDirectory()) {
                    new File(destFolder, entry.getName()).mkdirs();
                } else {
                    File entryFile = new File(destFolder, entry.getName());
                    entryFile.getParentFile().mkdirs();
                    bos = new BufferedOutputStream(new FileOutputStream(entryFile));
                    while (-1 != (readLen = zis.read(buffer, 0, buffer.length))) {
                        bos.write(buffer, 0, readLen);
                    }
                    bos.flush();
                    bos.close();
                }
            }
            zis.closeEntry();
            zis.close();
            resu = true;
        } catch (IOException e) {
            resu = false;
        } finally {
            Closer.close(bos);
            Closer.close(zis);
        }

        return resu;
    }

    /**
     * 压缩文件/文件夹到ZIP流中 <br>
     * <br>
     * <i>本方法是为了向自定义的压缩流添加文件/文件夹，若只是要压缩文件/文件夹到指定位置，请使用 {@code FileUtils.zip()} 方法</i>
     *
     * @param zos
     *            ZIP输出流
     * @param file
     *            被压缩的文件
     * @param root
     *            被压缩的文件在ZIP文件中的入口根节点
     * @param buffer
     *            读写缓冲区
     * @throws IOException
     *             读写流时可能抛出的I/O异常
     */
    public static void doZip(ZipOutputStream zos, File file, String root, byte[] buffer) throws IOException {
        // 参数检查
        if (zos == null || file == null) {
            throw new IOException("I/O Object got NullPointerException");
        }

        if (!file.exists()) {
//            throw new FileNotFoundException("Target File is missing");
            return;
        }

        BufferedInputStream bis = null;
        int readLen = 0;
        String rootName = TextUtils.isEmpty(root) ? (file.getName()) : (root + File.separator + file.getName());
        // 文件直接放入压缩流中
        if (file.isFile()) {
            try {
                bis = new BufferedInputStream(new FileInputStream(file));
                zos.putNextEntry(new ZipEntry(rootName));
                while (-1 != (readLen = bis.read(buffer, 0, buffer.length))) {
                    zos.write(buffer, 0, readLen);
                }
                Closer.close(bis);
            } catch (IOException e) {
                Closer.close(bis);
                // 关闭BIS流，并抛出异常
                throw e;
            }
        }
        // 文件夹则子文件递归
        else if (file.isDirectory()) {
            File[] subFiles = file.listFiles();

            for (File subFile : subFiles) {
                doZip(zos, subFile, rootName, buffer);
            }
        }
    }

    public static boolean isPicFile(String filePath) {
        boolean result = false;
        String fileType = estimateFileType(filePath);
        if (fileType.equals("jpg") || fileType.equals("gif") || fileType.equals("bmp") || fileType.equals("png")) {
            result = true;
        }
        return result;
    }

    public static String unKnownFileTypeMark ="unknown_";
    /**
     * 计算图片文件的类型
     * @param buffer
     * @return
     */
    public static String estimateFileType(byte[] buffer) {
        String fileType = "";
        String filecode = "";
        if(buffer!=null && buffer.length>=2){
            for (int i = 0; i < buffer.length; i++) {
                // 获取每个字节与0xFF进行与运算来获取高位，这个读取出来的数据不是出现负数
                // 并转换成字符串
                filecode += Integer.toString((buffer[i] & 0xFF));
            }
            // 把字符串再转换成Integer进行类型判断
            switch (Integer.parseInt(filecode)) {
                case 7790:
                    fileType = "exe";
                    break;
                case 7784:
                    fileType = "midi";
                    break;
                case 8297:
                    fileType = "rar";
                    break;
                case 8075:
                    fileType = "zip";
                    break;
                case 255216:
                    fileType = "jpg";
                    break;
                case 7173:
                    fileType = "gif";
                    break;
                case 6677:
                    fileType = "bmp";
                    break;
                case 13780:
                    fileType = "png";
                    break;
                default:
                    fileType = unKnownFileTypeMark + filecode;
            }
        }else{
            fileType = unKnownFileTypeMark + filecode;
        }

        return fileType;
    }

    public static String estimateFileType(String filePath) {
        String fileType = "";
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[2];
            // 通过读取出来的前两个字节来判断文件类型
            if (inputStream.read(buffer) != -1) {
                fileType = estimateFileType(buffer);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        }
        finally
        {
            if (inputStream != null)
            {
                try {inputStream.close();} catch (IOException e) {}
            }
        }
        return fileType;
    }


    //遍历目录
    public static List<File> getFileList(File directory){
        List<File> fileList = new ArrayList<>();
        if(directory == null || !directory.exists()){
            return fileList;
        }
        File[] files = directory.listFiles();
        if(files == null){
            return fileList;
        }
        for(File file : files){
            if(file.isDirectory()){
                getFileList(file);
            } else {
                fileList.add(file);
            }
        }
        return fileList;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                : Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }



    /**
     * 获取一个文件 或 目录下所有文件 的 大小
     */
    public static long getFolderSize(File file) {
        if (file == null) {
            return 0;
        } else {
            try {
                File[] fileList = file.listFiles();
                if (fileList == null) {
                    return 0;

                } else {
                    long size = 0;
                    for (int i = 0; i < fileList.length; i++) {
                        try {
                            File item = fileList[i];
                            if (item == null) {
                                //do nothing
                            } else if (item.isDirectory()) {
                                size = size + getFolderSize(item);
                            } else {
                                size = size + item.length();
                            }
                        } catch (Exception e) {
                            // do nothing
                        }
                    }
                    return size;
                }
            } catch (Exception e) {
                return 0;
            }
        }
    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }

        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    public static String getFolderSizeStr(File file) {
        return getFormatSize(getFolderSize(file));
    }

}
