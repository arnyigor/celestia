package com.arny.celestiatools.models;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Locale;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Controller {
    private String operationResult,parseMpcNeam;
    private static final String MPC_NEAM00_DOWNLOAD_PATH = "http://minorplanetcenter.net/Extended_Files/neam00_extended.json.gz";
    private static final String MPC_PHAs_DOWNLOAD_PATH = "http://minorplanetcenter.net/Extended_Files/pha_extended.json.gz";
    private static final String MPC_ASTER_DOWNLOADED_FILE = "mpc_aster_neam_downloaded.json.gz";
    private static final String MPC_ASTER_JSON_FILE = "mpc_aster_neam_unpacked.json";
    private static final String MPC_NEAM_LAST_SCC = "asteroids.ssc";
    private int cnt;
    private File 
            neamFile = new File(MPC_ASTER_JSON_FILE),
            asteroidsFile = new File(MPC_NEAM_LAST_SCC);
    private ArrayList<String> orbitalTypes;
    private StringBuilder neamParseBuilder;

    
    public Controller(){
        System.out.println("sett = " + System.getProperty("user.dir"));
    }
    
    public void workJsonFile(File file,onResultParse resultParse) {
        operationResult  = "";
	if (file.length() > 0) {
	    new Thread(new Runnable() {
		@Override
		public void run() {
                    try {
                        long start = System.currentTimeMillis();
                        FileUtils.unzipGZ(file.getAbsolutePath(), MPC_ASTER_JSON_FILE);
                        operationResult +="\nОперация заняла:" + (System.currentTimeMillis() - start) + " ms";
                        resultParse.parseResult("json", true, operationResult);
                    } catch (Exception e) {
                        resultParse.parseResult("json", false, e.getMessage());
                    }


		}
	    }).start();
	}
    }
    
    public void writeOrbitalParamFile(ArrayList<String> orbitalTypes,onResultParse resultParse){
        this.orbitalTypes = orbitalTypes;
        new Thread(new Runnable() {
		@Override
		public void run() {  
                    try {
                        
                        if (asteroidsFile!=null) {
                            try {
                                asteroidsFile.delete();
                            } catch (Exception e) {
                                e.printStackTrace();
                                resultParse.parseResult("writessc", false, e.getMessage());
                                return;
                            }
                        }
                        
                        if (neamFile ==null) {
                            operationResult ="Нет распакованного файла";
                            resultParse.parseResult("writessc", false, operationResult);
                            return;
                        }
                        
                        parseJson(neamFile);
			

                        if (BaseUtils.empty(parseMpcNeam)) {
                            operationResult ="Нечего записывать";
                            resultParse.parseResult("writessc", false, operationResult);
                            return;
                        }

                        long start = System.currentTimeMillis();
                        Files.write( Paths.get(MPC_NEAM_LAST_SCC), parseMpcNeam.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
                        operationResult +="Операция заняла:" + (System.currentTimeMillis() - start) + " ms";
                        resultParse.parseResult("writessc", true, operationResult);
                        
                        
                        
//                        try {
//			    neamFile.delete();
//			} catch (Exception e) {
//                            e.printStackTrace();
//			    resultParse.parseResult("writessc", false, e.getMessage());
//			}
                        
                        
                    } catch (IOException e) {
                        resultParse.parseResult("writessc", false, e.getMessage());
                    }


		}
	    }).start();
    }
    
    public static File getSettingsDirectory() {
        String userHome = System.getProperty("user.home");
        if(userHome == null) {
            throw new IllegalStateException("user.home==null");
        }
        File home = new File(userHome);
        File settingsDirectory = new File(home, ".myappdir");
        if(!settingsDirectory.exists()) {
            if(!settingsDirectory.mkdir()) {
                throw new IllegalStateException(settingsDirectory.toString());
            }
        }
    return settingsDirectory;
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
                        case "writessc":
                            message = "Орбиты записаны";
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
                        case "writessc":
                            message = "Орбиты не записаны";
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
                        FileUtils.downloadUsingStream(MPC_PHAs_DOWNLOAD_PATH,MPC_ASTER_DOWNLOADED_FILE);
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
        neamParseBuilder = new StringBuilder();
	try {
	    Object obj = parser.parse(new FileReader(file));
	    JSONArray array = new JSONArray();
	    array.add(obj);
	    JSONArray asteroids = (JSONArray) array.get(0);
	    cnt = 0;
	    for (int i = 0; i < asteroids.size(); i++) {
		JSONObject astroObject = (JSONObject) asteroids.get(i);
		convertJPLAsteroids(astroObject);
	    }
            parseMpcNeam =neamParseBuilder.toString();
	    operationResult = "Найдено: " + cnt + " астероидов";

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    private static boolean hasItemInList(String item,ArrayList<String> list){
            return list.indexOf(item) !=-1;
    }

    private void convertJPLAsteroids(JSONObject astroObject) {
        
	if (hasItemInList(astroObject.get("Orbit_type").toString(), orbitalTypes)) {
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
		    .append("\n         AscendingNode      ").append(astroObject.get("Node"))
                    .append("\n         ArgOfPericenter    ").append(astroObject.get("Peri"))
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

    public void calculateMOID(onResultParse resultParse) {
        operationResult  = "";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AstroUtils.setA1(1.000599536428770);
                    AstroUtils.setE1(1.708171796576660E-02);
                    AstroUtils.setI1(4.308590940041780E-03);
                    AstroUtils.setPeri1(2.862660689324113E+02);
                    AstroUtils.setNode1(1.783733089509388E+02);
                    AstroUtils.setM1(7.866459824015972E+01);
                    
                    
                    AstroUtils.setA2(9.226121225828366E-01);
                    AstroUtils.setE2(1.915177655650811E-01);
                    AstroUtils.setI2(3.336780867715445);
                    AstroUtils.setPeri2(1.266909737179885E+02);
                    AstroUtils.setNode2(2.040607387803847E+02);
                    AstroUtils.setM2(4.835939208607312E+01);
                    double dist = AstroUtils.getMOID();
                    operationResult = "Расстояние в au = " + dist;
                    resultParse.parseResult("moid", true, operationResult);
                } catch (Exception e) {
                    resultParse.parseResult("moid", false, e.getMessage());
                }

            }
        }).start();
    }
    
}
