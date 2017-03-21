package com.arny.celestiatools.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static void downloadUsingStream(String urlStr, String file) throws IOException{
        BufferedInputStream bis = null;
        FileOutputStream fis = null;
        try {
            URL url = new URL(urlStr);
            bis = new BufferedInputStream(url.openStream());
            fis = new FileOutputStream(file);
            byte[] buffer = new byte[10485760];
            int count=0;
            while((count = bis.read(buffer,0,10485760)) != -1)
            {
                fis.write(buffer, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public static void unzipGZ(String sourcePath, String destinationPath)  throws IOException, DataFormatException {
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
