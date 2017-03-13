package com.arny.celestiatools.models;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Controller {
    private String operationResult,parseMpcNeam;
    private static final String MPC_NEAM00_DOWNLOAD_PATH = "http://minorplanetcenter.net/Extended_Files/neam00_extended.json.gz";
    private static final String MPC_ASTER_DOWNLOADED_FILE = "mpc_aster_neam_downloaded.json.gz";
    private static final String MPC_ASTER_JSON_FILE = "mpc_aster_neam_unpacked.json";
    private static final String MPC_NEAM_LAST_SCC = "asteroids.ssc";
    private int cnt;

    public void workJsonFile(File file,onResultParse resultParse) {
        operationResult  = "";
	if (file.length() > 0) {
	    new Thread(new Runnable() {
		@Override
		public void run() {
                    try {
                        long start = System.currentTimeMillis();
                        File outFile  = new File(MPC_ASTER_JSON_FILE);
                        FileUtils.unzipGZ(file.getAbsolutePath(), MPC_ASTER_JSON_FILE);
			parseJson(outFile);
			try {
			    outFile.delete();
			} catch (Exception e) {
			    e.printStackTrace();
			}
                        Files.write( Paths.get(MPC_NEAM_LAST_SCC), parseMpcNeam.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
                        operationResult +="\nОперация заняла:" + (System.currentTimeMillis() - start) + " ms";
                        resultParse.parseResult("json", true, operationResult);
                    } catch (Exception e) {
                        resultParse.parseResult("json", false, e.getMessage());
                    }


		}
	    }).start();
	}
    }



    public static String getMessage(boolean success,String method) {
        String message = "Операция завершена";
            if (success) {
                switch(method){
                        case "json":
                            message = "Парсинг закончен успешно";
                            break;
                        case "download":
                            message = "Файл загружен";
                            break;
                    }
            }else{
                switch(method){
                        case "json":
                            message = "Ошибка парсинга";
                            break;
                        case "download":
                            message = "Ошибка загрузки файла";
                            break;
                    }
            }

                return message;
    }


    public void downloadFile(onResultParse resultParse){
        new Thread(new Runnable() {
		@Override
		public void run() {
		    long start = System.currentTimeMillis();
                    try {
                        FileUtils.downloadUsingStream(MPC_NEAM00_DOWNLOAD_PATH,MPC_ASTER_DOWNLOADED_FILE);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        File file = new File(MPC_ASTER_DOWNLOADED_FILE);
                        operationResult = "файл:"+file.getName()+" загружен и имеет размер " + file.length() + " байт";
                        operationResult +="\nОперация заняла:" + (System.currentTimeMillis() - start) + " ms";
                        resultParse.parseResult("download", true, operationResult);
                    } catch (Exception e) {
                        e.printStackTrace();
                        resultParse.parseResult("download", false, e.getMessage());
                    }
		}
	    }).start();
    }

    private static double getRadiusFromAbsoluteMagn(double magn,double albedo){
        double tmpPow = -0.2*magn;
        double delimoe = 1329*Math.pow(10, tmpPow);
        double delitel = Math.sqrt(albedo);
        return (delimoe/delitel)/2;
    }

    private void parseJson(File file) {
	JSONParser parser = new JSONParser();
        StringBuilder neamParseBuilder = new StringBuilder();
	try {
	    Object obj = parser.parse(new FileReader(file));
	    JSONArray array = new JSONArray();
	    array.add(obj);
	    JSONArray asteroids = (JSONArray) array.get(0);
	    cnt = 0;
	    for (int i = 0; i < asteroids.size(); i++) {
		JSONObject astroObject = (JSONObject) asteroids.get(i);
		convertJPLAsteroids(astroObject, neamParseBuilder);
	    }
            parseMpcNeam =neamParseBuilder.toString();
	    operationResult += "Найдено: " + cnt + " астероидов";

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void convertJPLAsteroids(JSONObject astroObject, StringBuilder neamParseBuilder) {
	if (astroObject.get("Orbit_type").toString().equals("Apollo")) {
	try {
	    String fullname = "";
	    if (astroObject.get("Name") == null) {
		neamParseBuilder.append("\n\"").append(astroObject.get("Principal_desig").toString()).append("\"");
	    } else {
		neamParseBuilder.append("\n\"").append(astroObject.get("Name").toString());
		if (astroObject.get("Principal_desig") != null) {
		    neamParseBuilder.append(":").append(astroObject.get("Principal_desig").toString());
		}
		neamParseBuilder.append("\"");
	    }
	    neamParseBuilder
		    .append("  \"").append("Sol").append("\"")
		    .append("\n{")
		    .append("\n     Class   \"asteroid\"")
		    .append("\n     Texture \"asteroid.jpg\"")
		    .append("\n     Mesh    \"eros.cmod\"")
		    .append("\n     Radius  ").append(String.format(Locale.US, "%.3f", getRadiusFromAbsoluteMagn(Double.parseDouble(astroObject.get("H").toString()), 0.15)))
		    .append("\n     EllipticalOrbit")
		    .append("\n     {")
		    .append("\n	    Period             ").append(astroObject.get("Orbital_period"))
		    .append("\n         SemiMajorAxis      ").append(astroObject.get("a"))
		    .append("\n         Inclination        ").append(astroObject.get("i"))
		    .append("\n         AscendingNode      ").append(astroObject.get("Node")).append("\n         ArgOfPericenter    ").append(astroObject.get("Peri"))
		    .append("\n         MeanAnomaly        ").append(astroObject.get("M"))
		    .append("\n         Epoch              ").append(astroObject.get("Epoch"))
		    .append("\n     }")
		    .append("\n}");
	} catch (Exception e) {
	    e.printStackTrace();
	    }
	    cnt++;
	}
    }

}
