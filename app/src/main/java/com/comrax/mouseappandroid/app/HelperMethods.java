package com.mouse.world.app;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by bez on 20/05/2015.
 */
public class HelperMethods {


    public static JSONObject loadJsonDataFromFile(String FILENAME) {
        File yourFile = new File(FILENAME);
        JSONObject jsonObject = null;
        try {
            FileInputStream stream = new FileInputStream(yourFile);
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            String jsonStr = Charset.defaultCharset().decode(bb).toString();
            stream.close();
            try {
                jsonObject = new JSONObject(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }



    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        if (!targetDirectory.exists())  //non-existant:
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
                }
            } finally {
                zis.close();
            }
    }


    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);
        fileOrDirectory.delete();
    }



//    private void sendErrorMail(Exception e) {
//        //String ex = String.format("%s: %s", throwable.getCause(), throwable.getMessage());
//        String ex = String.format("%s: %s, Line: %d",
//                e.getClass().getName(),
//                e.getMessage(),
//                e.getStackTrace()[0].getLineNumber());
//
//        final Intent intent = new Intent(Intent.ACTION_SEND);
//        //Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//        intent.setType("plain/text");
//        //intent.setType("message/rfc822");
//        //sendIntent.setData(Uri.parse("comraxepad@gmail.com"));
//        //sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
//        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bez@comrax.com"});
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Mouse App Exception");
//        intent.putExtra(Intent.EXTRA_TEXT, ex);
//
//        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//        Intent mailer = Intent.createChooser(intent, null);
//        mailer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//
//        startActivity(mailer);
//
//    }


}
