package com.arny.celestiatools.models;

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
    private String operationResult;

    public void workJsonFile(File file,onResultParse resultParse) {
        operationResult  = "";
	if (file.length() > 0) {
	    new Thread(new Runnable() {
		@Override
		public void run() {
		    long start = System.currentTimeMillis();
		    parseJson(file);
		    operationResult +="\nОперация заняла:" + (System.currentTimeMillis() - start) + "ms";
                 resultParse.parseResult("json", true, operationResult);
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
            JSONArray asteroids = (JSONArray) array.get(0);
            System.out.println("obj = " + (JSONObject)asteroids.get(0));
            for (int i = 0; i < asteroids.size(); i++) {
                JSONObject astroObject = (JSONObject)asteroids.get(i);
                
//                System.out.println("name "+i+" = " + astroObject.get("Name"));
//                System.out.println("Principal_desig "+i+" = " + astroObject.get("Principal_desig"));
//                System.out.println("Number "+i+" = " + astroObject.get("Number"));
//                System.out.println("node "+i+" = " + astroObject.get("Node"));
//                System.out.println("slope "+i+" = " + astroObject.get("G"));
//                System.out.println("magn "+i+" = " + astroObject.get("H"));
//                System.out.println("mean anom "+i+" = " + astroObject.get("M"));
//                System.out.println("Orbital_period "+i+" = " + astroObject.get("Orbital_period"));
//                System.out.println("SMA"+i+" = " + astroObject.get("a"));
//                System.out.println("ecentr "+i+" = " + astroObject.get("e"));
//                System.out.println("Epoch "+i+" = " + astroObject.get("Epoch"));
//                System.out.println("incin "+i+" = " + astroObject.get("i"));
//                System.out.println("Peri "+i+" = " + astroObject.get("Peri"));
            }
            operationResult +="Найдено: " + asteroids.size() + " астероидов"; 
          
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
