package com.arny.celestiatools.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    public static File unzipGZ(File file, File outputDir) {
	GZIPInputStream in = null;
	OutputStream out = null;
	File target = null;
	try {
	    // Open the compressed file
	    in = new GZIPInputStream(new FileInputStream(file));

	    // Open the output file
	    target = new File(outputDir, file.getName());
	    out = new FileOutputStream(target);

	    // Transfer bytes from the compressed file to the output file
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		out.write(buf, 0, len);
	    }

	    // Close the file and stream
	    in.close();
	    out.close();
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	    if (out != null) {
		try {
		    out.close();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
	return target;
    }

}
