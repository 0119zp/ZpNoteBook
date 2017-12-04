package zp.com.zpbase.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理工具类
 * Created by Administrator on 2017/10/14 0014.
 */

public class ZpFileUtil {

    private static final int DEFAULT_COMPRESS_QUALITY = 100;
    private static final String PATH_DOCUMENT = "document";
    private static final String PATH_TREE = "tree";

    /**
     * 从内容URI中获取到媒体的id
     * <p>
     * 该方法会先判断contentUri是否是文档URI，如果是文档URI，
     * 则尝试用文档URI的路径格式进行解析，取到id，如果cotentUri是通用的内容URI，
     * 则使用内容URI的格式进行解析，取到id
     *
     * @param contentUri 内容URI
     * @return 内容的id，如果取不到id，则为-1
     */
    private static long getMeidaIdFrom(Uri contentUri) {
        if (contentUri == null || !"content".equals(contentUri.getScheme())) {
            throw new IllegalArgumentException("Uri " + contentUri + " is invalid");
        }

        long id = -1;
        String host = contentUri.getHost();
        if ("com.android.providers.media.documents".equals(host)) {  //判断是否是文档URI
            String docId = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 如果大于KITKAT，直接使用原生API获取
                docId = DocumentsContract.getDocumentId(contentUri);
            } else {
                // 否则自己解析
                docId = getDocumentId(contentUri);
            }
            if (!TextUtils.isEmpty(docId)) {

                //文档URI的id格式是：media:{id}
                if (docId.contains(":")) {
                    String[] idArgument = docId.split(":");
                    if (idArgument.length > 1) {
                        try {
                            id = Long.parseLong(idArgument[1]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        id = Long.parseLong(docId);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {  // 否则以内容URI的格式获取id
            id = ContentUris.parseId(contentUri);
        }

        return id;
    }

    /**
     * 从文档URI中获取文档的id
     *
     * @param documentUri
     * @return
     */
    private static String getDocumentId(Uri documentUri) {
        final List<String> paths = documentUri.getPathSegments();
        if (paths.size() >= 2 && "document".equals(paths.get(0))) {
            return paths.get(1);
        }
        if (paths.size() >= 4 && "tree".equals(paths.get(0))
                && "document".equals(paths.get(2))) {
            return paths.get(3);
        }
        throw new IllegalArgumentException("Invalid URI: " + documentUri);
    }

    public static String getFilePath(Context context, Uri uri) {
        // 如果已是文件路径，直接返回path
        if (uri != null && "file".equals(uri.getScheme())) {
            return uri.getPath();
        }

        long docId = getMeidaIdFrom(uri);
        if (docId < 0) {
            throw new IllegalArgumentException("Cannot get document id");
        }
        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(docId)};
        return getFilePathFromUri(context, contentUri, selection, selectionArgs);
    }

    private static String getFilePathFromUri(Context context, Uri uri, String selection, String[] selectionArgs) {
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
     * get SDPath, if no sdcard, return null
     *
     * @return
     */
    public static String getSDPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        } else {
            Log.e("SDCard", "no sdcard found!");
            return null;
        }
    }

    public static String getBasePath(Context context) {
        if (null == getSDPath()) {
            return context.getCacheDir().getAbsolutePath();
        }

        File file = new File(getSDPath() + File.separator + context.getPackageName());
        if (!file.exists()) {
            file.mkdir();
        }

        return file.getAbsolutePath();
    }

    /**
     * Saves the bitmap by given directory, filename, and format; if the directory is given null, then saves it under the cache directory.
     */
    public static File saveBitmap(Context context, Bitmap bitmap, String directory, String filename,
                                  Bitmap.CompressFormat format) {

        if (TextUtils.isEmpty(directory)) {
            directory = getBasePath(context);
        }

        OutputStream os = null;
        filename = (format == Bitmap.CompressFormat.PNG) ? filename + ".png" : filename + ".jpg";
        File file = new File(directory, filename);
        file.mkdirs();
        if (file.exists()) {
            file.delete();
        }

        if (bitmap == null) {
            return file;
        }

        try {
            file.createNewFile();
            os = new FileOutputStream(file);
            bitmap.compress(format, DEFAULT_COMPRESS_QUALITY, os);
            os.flush();
            Uri data = Uri.parse("file:///" + file.getAbsolutePath());
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));

            Log.e("zpan", "saveBitmap:" + file.length() / 1024 + "KB");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(os);
        }
        return file;
    }

    /**
     * 保存图片
     *
     * @param context
     */
    public static File saveBitmap(Context context, Bitmap bitmap, String directory, String filename) {
        if (TextUtils.isEmpty(directory)) {
            directory = getBasePath(context);
        }
        OutputStream os = null;
        filename = filename + ".jpg";
        File file = new File(directory, filename);
        file.mkdirs();
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            Uri data = Uri.parse("file:///" + file.getAbsolutePath());
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(os);
        }
        return file;
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 读取assets文件夹的文件
    public static String getFromAssets(Context context, String fileName) {
        BufferedReader reader = null;
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            reader = new BufferedReader(new InputStreamReader(in));

            char[] buf = new char[1024];
            int count = 0;
            StringBuffer sb = new StringBuffer(in.available());
            while ((count = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, count);
                sb.append(readData);
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(reader);
        }

        return "";
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除多个文件
     */
    public static void deleteFiles(ArrayList<String> filePaths) {
        for (int i = 0; i < filePaths.size(); i++) {
            deleteFile(filePaths.get(i));
        }
    }

    /**
     * 返回temp路径
     *
     * @param context
     * @return
     */
    public static String getTempPath(Context context) {
        String path = "";
        if (null == getSDPath()) {
            path = context.getCacheDir().getAbsolutePath() + File.separator + "temp";
        } else {
            path = getSDPath() + File.separator + "temp";
        }

        return path;
    }

    public static void download(String url, String fileName) {

    }

}
