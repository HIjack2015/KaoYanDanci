package cn.jk.kaoyandanci.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/4/15.
 */

public class FileUtil {
    private static final int REQUEST_WRITE_STORAGE = 112;

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }



    public static String streamToString(InputStream is) throws IOException {
        String content;


        int size = is.available();

        byte[] buffer = new byte[size];

        is.read(buffer);

        is.close();

        content = new String(buffer, "UTF-8");
        return content;
    }

    public static void dbFileToSdCard(Context context) {
        try {

            boolean hasPermission = (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            }

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (!sd.canWrite()) {
                ToastUtil.showShort(context, "不能写卡");
                return;
            }
            String currentDBPath = "/data/data/" + context.getPackageName() + "/databases/word.db";
            String backupDBPath = "backupname.db";
            File currentDB = new File(currentDBPath);
            File backupDB = new File(sd, backupDBPath);

            if (!currentDB.exists()) {
                ToastUtil.showShort(context, "db不存在");
                return;
            }
            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();


        } catch (Exception e) {
            Log.e(TAG, "dbFileToSdCard: " + e.toString());
        }
    }

    public static void saveImg(Context context, String fileName, Bitmap bitmap) {
        OutputStream fOut = null;
        Uri outputFileUri = null;
        try {
            File root = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "kaoyandanci" + File.separator + "image" + File.separator);
            root.mkdirs();
            File sdImageMainDirectory = new File(root, fileName);
            outputFileUri = Uri.fromFile(sdImageMainDirectory);
            fOut = new FileOutputStream(sdImageMainDirectory);
        } catch (Exception e) {
            Toast.makeText(context, "Error occured. Please try again later.",
                    Toast.LENGTH_SHORT).show();
        }
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
        }
        File imgFile = new File(outputFileUri.getPath());
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imgFile)));
    }

    public static void saveString(Context context, String content, String path) {
        File saveFile = new File(path);
        saveFile.getParentFile().mkdirs();
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(saveFile);
            stream.write(content.getBytes());
        } catch (Exception e) {
            Log.e("FileUtil", e.getMessage());
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e) {
                Log.e("FileUtil", e.getMessage());
            }

        }

    }

    public static void unzip(File zipFile, File targetDirectory) {
        try {
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(new FileInputStream(zipFile)));
            try {
                ZipEntry ze;
                int count;
                byte[] buffer = new byte[8192];
                while ((ze = zis.getNextEntry()) != null) {
                    File file = new File(targetDirectory, ze.getName());
                    File dir = ze.isDirectory() ? file : file.getParentFile();
                    if (!dir.isDirectory() && !dir.mkdirs())
                        throw new FileNotFoundException("Failed to ensure directory: " +
                                dir.getAbsolutePath());
                    if (ze.isDirectory())
                        continue;
                    FileOutputStream fout = new FileOutputStream(file);
                    try {
                        while ((count = zis.read(buffer)) != -1)
                            fout.write(buffer, 0, count);
                    } finally {
                        fout.close();
                    }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
                }
            } finally {
                zis.close();
            }
        } catch (Exception e) {
            Log.e("error___", e.toString());
        }

    }
}
