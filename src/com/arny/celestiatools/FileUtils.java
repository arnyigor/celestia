package com.arny.celestiatools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    /**
     * With the object model read the whole JSON file is loaded on memory and
     * the application gets the desired element.
     */
    public static void readDom() {
	BufferedReader reader = null;
	try {
	    reader = new BufferedReader(new FileReader(file));
	    Gson gson = new GsonBuilder().create();
	    Person[] people = gson.fromJson(reader, Person[].class);

	    System.out.println("Object mode: " + people[0]);

	} catch (FileNotFoundException ex) {
	    Logger.getLogger(GsonRead.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    try {
		reader.close();
	    } catch (IOException ex) {
		Logger.getLogger(GsonRead.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    /**
     * This is a mixed implementation based on stream and object model. The JSON
     * file is read in stream mode and each object is parsed in object model.
     * With this approach we avoid to load all the object in memory and we are
     * only loading one at a time.
     */
    public static void readStream() {
	try {
	    JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
	    Gson gson = new GsonBuilder().create();

	    // Read file in stream mode
	    reader.beginArray();
	    while (reader.hasNext()) {
		// Read data into object model
		Person person = gson.fromJson(reader, Person.class);
		if (person.getId() == 0) {
		    System.out.println("Stream mode: " + person);
		}
		break;
	    }
	    reader.close();
	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(GsonRead.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(GsonRead.class.getName()).log(Level.SEVERE, null, ex);
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
