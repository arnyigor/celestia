package com.arny.celestiatools.utils;

import com.arny.celestiatools.models.onProgressUpdate;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.zip.DataFormatException;

public class FileUtils {

    private static final String DOCUMENT_SEPARATOR = ":";
    private static final String FOLDER_SEPARATOR = "/";

    public static void unzipFunction(String destinationFolder, String zipFile) {
        File directory = new File(destinationFolder);
        System.out.println("directory = " + directory.exists());
        // if the output directory doesn't exist, create it
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // buffer for read and write data to file
        byte[] buffer = new byte[2048];

        try {
            FileInputStream fInput = new FileInputStream(zipFile);
            ZipInputStream zipInput = new ZipInputStream(fInput);

            ZipEntry entry = zipInput.getNextEntry();

            while (entry != null) {
                String entryName = entry.getName();
                File file = new File(destinationFolder + File.separator + entryName);
                System.out.println("Unzip file " + entryName + " to " + file.getAbsolutePath());
                // create the directories of the zip directory
                if (entry.isDirectory()) {
                    File newDir = new File(file.getAbsolutePath());
                    if (!newDir.exists()) {
                        boolean success = newDir.mkdirs();
                        if (success == false) {
                            System.out.println("Problem creating Folder");
                        }
                    }
                } else {
                    FileOutputStream fOutput = new FileOutputStream(file);
                    int count = 0;
                    while ((count = zipInput.read(buffer)) > 0) {
                        // write 'count' bytes to the file output stream
                        fOutput.write(buffer, 0, count);
                    }
                    fOutput.close();
                }
                // close ZipEntry and take the next one
                zipInput.closeEntry();
                entry = zipInput.getNextEntry();
            }

            // close the last ZipEntry
            zipInput.closeEntry();

            zipInput.close();
            fInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void populateDesktopHttpHeaders(URLConnection urlCon) {
        // add custom header in order to be easily detected
        urlCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        urlCon.setRequestProperty("Accept-Language", "el-gr,el;q=0.8,en-us;q=0.5,en;q=0.3");
        urlCon.setRequestProperty("Accept-Charset", "ISO-8859-7,utf-8;q=0.7,*;q=0.7");
    }

    /**
     * Gets file size using an HTTP connection with GET method
     *
     * @return file size in bytes
     * @throws IOException
     */
    public static long getFileSize(String fileUrl) throws IOException {
        URL oracle = new URL(fileUrl);
        HttpURLConnection yc = (HttpURLConnection) oracle.openConnection();
        populateDesktopHttpHeaders(yc);
        long fileSize = 0;
        try {
            // retrieve file size from Content-Length header field
            fileSize = Long.parseLong(yc.getHeaderField("Content-Length"));
        } catch (NumberFormatException nfe) {
        }

        return fileSize;
    }


    public static String formatFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size
                / Math.pow(1024, digitGroups))
                + " " + units[digitGroups];
    }

    public static String formatFileSize(long size, int digits) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        StringBuilder digs = new StringBuilder();
        for (int i = 0; i < digits; i++) {
            digs.append("#");
        }
        return new DecimalFormat("#,##0." + digs.toString()).format(size
                / Math.pow(1024, digitGroups))
                + " " + units[digitGroups];
    }

    public static void downloadUsingStream(String urlStr, String file) throws Exception {
        BufferedInputStream bis;
        FileOutputStream fis;
        URL url = new URL(urlStr);
        bis = new BufferedInputStream(url.openStream());
        fis = new FileOutputStream(file);
        int bytes = 10485760;
        byte[] buffer = new byte[bytes];
        int count;
        int cnt = 0;
        while ((count = bis.read(buffer, 0, bytes)) != -1) {
            cnt += count;
            System.out.println(" cnt:"  +cnt);
            fis.write(buffer, 0, count);
        }
    }

    public static void unzipGZ(String sourcePath, String destinationPath) throws Exception {
        //Allocate resources.
        FileInputStream fis = new FileInputStream(sourcePath);
        FileOutputStream fos = new FileOutputStream(destinationPath);
        GZIPInputStream gzis = new GZIPInputStream(fis);
        byte[] buffer = new byte[1024];
        int len = 0;

        //Extract compressed content.
        while ((len = gzis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }

        //Release resources.
        fos.close();
        fis.close();
        gzis.close();
        buffer = null;
    }

}
