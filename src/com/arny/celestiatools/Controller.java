package com.arny.celestiatools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Controller {

    public void workJsonFile(File file) {
	if (file.length() > 0) {
	    new Thread(new Runnable() {
		@Override
		public void run() {
		    long start = System.currentTimeMillis();
		    parseJson(file);
		    System.out.println("time diff = " + (System.currentTimeMillis() - start));
		}
	    }).start();
	}
    }

    private void parseJson(File file) {
	JSONParser parser = new JSONParser();
	try {
	    Object obj = parser.parse(new FileReader(file));
	    JSONArray array = new JSONArray();
	    array.add(obj);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (ParseException e) {
	    e.printStackTrace();
	}
    }
}
