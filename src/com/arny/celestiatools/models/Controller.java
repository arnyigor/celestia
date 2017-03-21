package com.arny.celestiatools.models;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Locale;

import com.sun.xml.internal.rngom.parse.host.Base;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;

public class Controller {
	private String operationResult, parseMpcNeamCEL, parseMpcNeamSE;
	private static final String MPC_NEAs_DOWNLOAD_PATH = "http://minorplanetcenter.net/Extended_Files/nea_extended.json.gz";
	private static final String MPC_PHAs_DOWNLOAD_PATH = "http://minorplanetcenter.net/Extended_Files/pha_extended.json.gz";
	private static final String MPC_ASTER_DOWNLOADED_FILE = "mpc_downloaded.json.gz";
	private static final String MPC_ASTER_JSON_FILE = "mpc_unpacked.json";
	private static final String MPC_NEAM_LAST_SCC = "asteroids.ssc";
	private static final String MPC_NEAM_LAST_SC_SE = "Asteroids.sc";
	private int cnt;
	private File
			unpackedJsonfile = new File(MPC_ASTER_JSON_FILE),
			asteroidsFileCEL = new File(MPC_NEAM_LAST_SCC),
			asteroidsFileSE = new File(MPC_NEAM_LAST_SCC);
	private ArrayList<String> orbitalTypes;
	private StringBuilder neamParseBuilderCEL, neamParseBuilderSE;
	private ArrayList<CelestiaAsteroid> celestiaObjects;
    private Connection connection = null;

	public Controller() {
        connection = SqliteConnection.dbConnection();
    }

	public void workJsonFile(File file, onResultParse resultParse) {
		operationResult = "";
		if (file.length() > 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						long start = System.currentTimeMillis();
						FileUtils.unzipGZ(file.getAbsolutePath(), MPC_ASTER_JSON_FILE);
						operationResult += "\nОперация заняла:" + (System.currentTimeMillis() - start) + " ms";
						resultParse.parseResult("json", true, operationResult);
					} catch (Exception e) {
						resultParse.parseResult("json", false, e.getMessage());
					}

				}
			}).start();
		}
	}

	public void writeOrbitalParamFile(ArrayList<String> orbitalTypes, onResultParse resultParse,onResultCelestiaAsteroids celestiaData) {
		this.orbitalTypes = orbitalTypes;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

                    if (asteroidsFileCEL != null) {
                        try {
                            asteroidsFileCEL.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                            resultParse.parseResult("writessc", false, e.getMessage());
                            return;
                        }
                    }

                    if (asteroidsFileSE != null) {
                        try {
                            asteroidsFileSE.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                            resultParse.parseResult("writessc", false, e.getMessage());
                            return;
                        }
                    }
                    if (unpackedJsonfile == null) {
						operationResult = "Нет распакованного файла";
						resultParse.parseResult("writessc", false, operationResult);
						return;
					}

					parseJson(unpackedJsonfile);
					celestiaData.dataCallback(celestiaObjects);
					if (BaseUtils.empty(parseMpcNeamCEL)) {
						operationResult = "Нечего записывать";
						resultParse.parseResult("writessc", false, operationResult);
						return;
					}

					long start = System.currentTimeMillis();
					Files.write(Paths.get(MPC_NEAM_LAST_SCC), parseMpcNeamCEL.getBytes(StandardCharsets.UTF_8),
							StandardOpenOption.CREATE);
					Files.write(Paths.get(MPC_NEAM_LAST_SC_SE), parseMpcNeamSE.getBytes(StandardCharsets.UTF_8),
							StandardOpenOption.CREATE);
					operationResult += "Операция заняла:" + (System.currentTimeMillis() - start) + " ms";
					resultParse.parseResult("writessc", true, operationResult);

				} catch (IOException e) {
					resultParse.parseResult("writessc", false, e.getMessage());
				}

			}
		}).start();
	}

	public static File getSettingsDirectory() {
		String userHome = System.getProperty("user.home");
		if (userHome == null) {
			throw new IllegalStateException("user.home==null");
		}
		File home = new File(userHome);
		File settingsDirectory = new File(home, ".myappdir");
		if (!settingsDirectory.exists()) {
			if (!settingsDirectory.mkdir()) {
				throw new IllegalStateException(settingsDirectory.toString());
			}
		}
		return settingsDirectory;
	}

	public static String getMessage(boolean success, String method) {
		String message = "Операция завершена";
		if (success) {
			switch (method) {
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
		} else {
			switch (method) {
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

	public void downloadFile(int source, onResultParse resultParse) {
		String downloadPath = getDownloadPath(source);
		new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				try {
					FileUtils.downloadUsingStream(downloadPath, MPC_ASTER_DOWNLOADED_FILE);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				try {
					File file = new File(MPC_ASTER_DOWNLOADED_FILE);
					operationResult = "файл:" + file.getName() + " загружен и имеет размер " + file.length() + " байт";
					operationResult += "\nОперация заняла:" + (System.currentTimeMillis() - start) + " ms";
					resultParse.parseResult("download", true, operationResult);
				} catch (Exception e) {
					e.printStackTrace();
					resultParse.parseResult("download", false, e.getMessage());
				}
			}
		}).start();
	}

	private String getDownloadPath(int source) {
		String downloadPath = MPC_PHAs_DOWNLOAD_PATH;
		switch (source) {
			case 0:
				downloadPath = MPC_PHAs_DOWNLOAD_PATH;
				break;
			case 1:
				downloadPath = MPC_NEAs_DOWNLOAD_PATH;
				break;
		}
		return downloadPath;
	}

	private void parseJson(File file) {
		JSONParser parser = new JSONParser();
		neamParseBuilderCEL = new StringBuilder();
		neamParseBuilderSE = new StringBuilder();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONArray array = new JSONArray();
			array.add(obj);
			JSONArray asteroids = (JSONArray) array.get(0);
			cnt = 0;
			celestiaObjects = new ArrayList<>();
			for (Object asteroid : asteroids) {
				JSONObject astroObject = (JSONObject) asteroid;
				convertJPLAsteroidsCEL(astroObject);
				convertJPLAsteroidsSE(astroObject);
			}
			parseMpcNeamCEL = neamParseBuilderCEL.toString();
			parseMpcNeamSE = neamParseBuilderSE.toString();
			operationResult = "Найдено: " + cnt + " астероидов";

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean hasItemInList(String item, ArrayList<String> list) {
		return list.indexOf(item) != -1;
	}

	private void convertJPLAsteroidsCEL(JSONObject astroObject) {
        CelestiaAsteroid asteroid = new CelestiaAsteroid();
		if (hasItemInList(astroObject.get("Orbit_type").toString(), orbitalTypes)) {
			try {
				if (astroObject.get("Name") == null) {
					neamParseBuilderCEL.append("\n\"").append(astroObject.get("Principal_desig").toString()).append("\"");
					asteroid.setName(astroObject.get("Principal_desig").toString());
				} else {
					asteroid.setName(astroObject.get("Name").toString());
					neamParseBuilderCEL.append("\n\"").append(astroObject.get("Name").toString());
					if (astroObject.get("Principal_desig") != null) {
						neamParseBuilderCEL.append(":").append(astroObject.get("Principal_desig").toString());
					}
					neamParseBuilderCEL.append("\"");
				}
				celestiaObjects.add(asteroid);

				double radius = AstroUtils.getRadiusFromAbsoluteMagn(Double.parseDouble(astroObject.get("H").toString()), 0.15);
				asteroid.setRadius(radius);
				neamParseBuilderCEL
						.append("  \"").append("Sol").append("\"")
						.append("\n{")
						.append("\n     Class   \"asteroid\"")
						.append("\n     Texture \"asteroid.jpg\"")
						.append("\n     Mesh    \"eros.cmod\"")
						.append("\n     Radius  ").append(String.format(Locale.US, "%.3f",radius ))
						.append("\n     EllipticalOrbit");
				neamParseBuilderCEL.append(getObjectOrbit(astroObject));
				neamParseBuilderCEL.append("\n}");
			} catch (Exception e) {
				e.printStackTrace();
			}
			cnt++;
		}
	}

	private String getObjectOrbit(JSONObject astroObject) {
		StringBuilder orbit = new StringBuilder();
		orbit.append("\n	{")
				.append("\n	    Period             ").append(astroObject.get("Orbital_period"))
				.append("\n         SemiMajorAxis      ").append(astroObject.get("a"))
				.append("\n         Inclination        ").append(astroObject.get("i"))
				.append("\n         AscendingNode      ").append(astroObject.get("Node"))
				.append("\n         Eccentricity      ").append(astroObject.get("e"))
				.append("\n         ArgOfPericenter    ").append(astroObject.get("Peri"))
				.append("\n         MeanAnomaly        ").append(astroObject.get("M"))
				.append("\n         Epoch              ").append(astroObject.get("Epoch"))
				.append("\n	}");
		return orbit.toString();
	}

	private void convertJPLAsteroidsSE(JSONObject astroObject) {

		if (hasItemInList(astroObject.get("Orbit_type").toString(), orbitalTypes)) {
			try {
				String fullname = "";
				if (astroObject.get("Name") == null) {
					neamParseBuilderSE.append("\nAsteroid	    \"").append(astroObject.get("Principal_desig").toString()).append("\"");
				} else {
					neamParseBuilderSE.append("\nAsteroid	    \"").append(astroObject.get("Name").toString()).append("\"");
				}
				neamParseBuilderSE
						.append("\n{\n	ParentBody\"").append("Sol").append("\"")
						.append("\n     Radius  ").append(String.format(Locale.US, "%.3f", AstroUtils.getRadiusFromAbsoluteMagn(Double.parseDouble(astroObject.get("H").toString()), 0.15)))
						.append("\n     Orbit");
				neamParseBuilderSE.append(getObjectOrbit(astroObject));
				neamParseBuilderSE.append("\n}");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void calculateMOID(onResultParse resultParse) {
		operationResult = "";
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					operationResult = "2017-Mar-02 14:05 JD = " + AstroUtils.getJD(BaseUtils.convertTimeStringToLong("2017-03-02 17:05","yyyy-MM-dd HH:mm"));
					AstroUtils.setA1(2.201273459788872);
					AstroUtils.setE1(0.6018904752619388);
					AstroUtils.setI1(1.375014776910502);
					AstroUtils.setPeri1(80.37016102969110);
					AstroUtils.setNode1(161.8394032832525);
					AstroUtils.setM1(2.801826762569547E+0);

					AstroUtils.setA2(1.000498640811769);
					AstroUtils.setE2(1.666719180066958E-02);
					AstroUtils.setI2(3.086491258554233E-03);
					AstroUtils.setPeri2(2.981260335253010E+02);
					AstroUtils.setNode2(1.668223242649487E+02);
					AstroUtils.setM2(5.741534538676495E+01);


//					double res = AstroUtils.getMOID() ;
//                    operationResult = "res = " + res;
                    operationResult  = "\nres = " + AstroUtils.getAfelDist(2.201273459788872,6.638253324461043E-01);

					resultParse.parseResult("moid", true, operationResult);
				} catch (Exception e) {
					resultParse.parseResult("moid", false, e.getMessage());
				}

			}
		}).start();
	}

}
