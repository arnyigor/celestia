package com.arny.celestiatools.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.arny.celestiatools.models.*;
import com.arny.celestiatools.utils.AstroUtils;
import com.arny.celestiatools.utils.BaseUtils;
import com.arny.celestiatools.utils.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Controller {

    private String operationResult, parseMpcNeamCEL, parseMpcNeamSE;
	private static final String MPC_NEAs_DOWNLOAD_PATH = "http://minorplanetcenter.net/Extended_Files/nea_extended.json.gz";
	private static final String MPC_PHAs_DOWNLOAD_PATH = "http://minorplanetcenter.net/Extended_Files/pha_extended.json.gz";
	private static final String MPC_ASTER_DOWNLOADED_FILE = "mpc_downloaded.json.gz";
	private static final String MPC_ASTER_JSON_FILE = "mpc_unpacked.json";
	private static final String MPC_NEAM_LAST_SCC = "asteroids.ssc";
	private static final String MPC_NEAM_LAST_SC_SE = "Asteroids.sc";
	private static final String MPC_FILES_DIR = System.getProperty("user.dir") + "/files/";
	private File
			unpackedJsonfile = new File(MPC_FILES_DIR + MPC_ASTER_JSON_FILE),
			asteroidsFileCEL = new File(MPC_FILES_DIR + MPC_NEAM_LAST_SCC),
			asteroidsFileSE = new File(MPC_FILES_DIR + MPC_NEAM_LAST_SC_SE);
	private ArrayList<String> orbitalTypes;
	private StringBuilder neamParseBuilderCEL, neamParseBuilderSE;
	private ArrayList<CelestiaAsteroid> celestiaObjects;
    private Connection connection = null;
    private int asteroidCnt,totalProgress,iterateProgress;

    public Controller() {
        connection = SqliteConnection.dbConnection();
    }

    public void getAsterTableData(onResultCelestiaAsteroids celestiaAsteroidsCallbacks) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                celestiaAsteroidsCallbacks.dataCallback(SqliteConnection.getAllCelestiaAsteroids(connection));
            }
        }).start();
    }

    public void workJsonFile(File file, onResultCallback resultParse) {
		operationResult = "";
		if (file.length() > 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						long start = System.currentTimeMillis();
						FileUtils.unzipGZ(file.getAbsolutePath(), MPC_FILES_DIR + MPC_ASTER_JSON_FILE);
						operationResult += "\nОперация заняла:" + (System.currentTimeMillis() - start) + " ms";
						resultParse.result("json", true, operationResult);
					} catch (Exception e) {
						resultParse.result("json", false, e.getMessage());
					}

				}
			}).start();
		}
	}

	public void writeOrbitalParamFile(ArrayList<String> orbitalTypes, onResultCallback resultParse, onResultCelestiaAsteroids celestiaData, onProgressUpdate onProgressUpdate) {
		this.orbitalTypes = orbitalTypes;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
                    long start = System.currentTimeMillis();
                    File folder = new File(MPC_FILES_DIR);
                    boolean folderFilesExist = folder.exists() || folder.mkdir();
				    if (!folderFilesExist) return;

                    if (asteroidsFileCEL != null) {
                        try {
                            asteroidsFileCEL.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                            resultParse.result("writessc", false, e.getMessage());
                            return;
                        }
                    }

                    if (asteroidsFileSE != null) {
                        try {
                            asteroidsFileSE.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                            resultParse.result("writessc", false, e.getMessage());
                            return;
                        }
                    }
                    if (unpackedJsonfile == null) {
						operationResult = "Нет распакованного файла";
						resultParse.result("writessc", false, operationResult);
						return;
					}

					parseJson(unpackedJsonfile,onProgressUpdate);
					celestiaData.dataCallback(SqliteConnection.getAllCelestiaAsteroids(connection));

					if (BaseUtils.empty(parseMpcNeamCEL)) {
						operationResult = "Нечего записывать";
						resultParse.result("writessc", false, operationResult);
						return;
					}


					Files.write(Paths.get(MPC_FILES_DIR + MPC_NEAM_LAST_SCC), parseMpcNeamCEL.getBytes(StandardCharsets.UTF_8),
							StandardOpenOption.CREATE);
					Files.write(Paths.get(MPC_FILES_DIR + MPC_NEAM_LAST_SC_SE), parseMpcNeamSE.getBytes(StandardCharsets.UTF_8),
							StandardOpenOption.CREATE);
					operationResult += " Операция заняла:" + (System.currentTimeMillis() - start) + " ms";
					resultParse.result("writessc", true, operationResult);

				} catch (IOException e) {
					resultParse.result("writessc", false, e.getMessage());
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

	public void downloadFile(int source, onResultCallback resultParse) {
		String downloadPath = getDownloadPath(source);
		new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				try {
                    File folder = new File(MPC_FILES_DIR);
                    boolean folderFilesExist = folder.exists() || folder.mkdir();
                    if (!folderFilesExist) return;
                    FileUtils.downloadUsingStream(downloadPath, MPC_FILES_DIR+ MPC_ASTER_DOWNLOADED_FILE);
                } catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					File file = new File(MPC_ASTER_DOWNLOADED_FILE);
					operationResult = "файл:" + file.getName() + " загружен и имеет размер " + file.length() + " байт";
					operationResult += "\nОперация заняла:" + (System.currentTimeMillis() - start) + " ms";
					resultParse.result("download", true, operationResult);
				} catch (Exception e) {
					e.printStackTrace();
					resultParse.result("download", false, e.getMessage());
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

	private void parseJson(File file,onProgressUpdate onProgressUpdate) {
		JSONParser parser = new JSONParser();
		neamParseBuilderCEL = new StringBuilder();
		neamParseBuilderSE = new StringBuilder();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONArray array = new JSONArray();
			array.add(obj);
			JSONArray asteroids = (JSONArray) array.get(0);
			asteroidCnt = 0;
            celestiaObjects = new ArrayList<>();
            totalProgress = asteroids.size();
            iterateProgress = 0;
            for (Object asteroid : asteroids) {
				JSONObject astroObject = (JSONObject) asteroid;
                CelestiaAsteroid celestiaAsteroid = new CelestiaAsteroid();
                convertJsonAsteroid(astroObject, celestiaAsteroid);

                updateOrInsertDb(celestiaAsteroid);
                iterateProgress++;
                onProgressUpdate.update("dbupdate",totalProgress,iterateProgress);

                convertJPLAsteroidsCEL(celestiaAsteroid);
				convertJPLAsteroidsSE(celestiaAsteroid);
                celestiaObjects.add(celestiaAsteroid);

			}
			parseMpcNeamCEL = neamParseBuilderCEL.toString();
			parseMpcNeamSE = neamParseBuilderSE.toString();
			operationResult = "Найдено: " + asteroidCnt + " астероидов ";

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * update|insert db
     * @param asteroid
     */
    private void updateOrInsertDb(CelestiaAsteroid asteroid) {
        HashMap dbValues = new HashMap<>();
        setAsterDbValues(dbValues,asteroid);
        String cond = SqliteConnection.DB_ASTER_KEY_NAME + "='" + asteroid.getName()+"'";
        int asterDbId = SqliteConnection.getId(connection, cond);
        if (asterDbId != 0) {
            SqliteConnection.updateSqliteData(connection,SqliteConnection.DB_TABLE_ASTEROIDS, dbValues, cond);
        }else{
            SqliteConnection.insertSqliteData(connection, SqliteConnection.DB_TABLE_ASTEROIDS, dbValues);
        }
    }

    /**
     * set db values
     * @param dbValues
     * @param asteroid
     */
    private void setAsterDbValues(HashMap dbValues,CelestiaAsteroid asteroid) {
        dbValues.put(SqliteConnection.DB_ASTER_KEY_NAME, "'"+asteroid.getName()+"'");
        dbValues.put(SqliteConnection.DB_ASTER_KEY_RADIUS, String.valueOf(asteroid.getRadius()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_ORBIT_TYPE, "'"+asteroid.getOrbitType()+"'");
        dbValues.put(SqliteConnection.DB_ASTER_KEY_PERIOD, String.valueOf(asteroid.getPeriod()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_SMA, String.valueOf(asteroid.getSma()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_INC, String.valueOf(asteroid.getInc()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_NODE, String.valueOf(asteroid.getNode()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_ECC, String.valueOf(asteroid.getEcc()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_PERIC, String.valueOf(asteroid.getPeric()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_MA, String.valueOf(asteroid.getMa()));
        dbValues.put(SqliteConnection.DB_ASTER_KEY_EPOCH, String.valueOf(asteroid.getEpoch()));
    }

    public String formatAsteroidData(CelestiaAsteroid asteroid){
        String res = "Period:" + asteroid.getPeriod();
        res += "\nSemiMajorAxis:" + asteroid.getSma();
        res += "\nInclination:" + asteroid.getInc();
        res += "\nAscendingNode:" + asteroid.getNode();
        res += "\nEccentricity:" + asteroid.getEcc();
        res += "\nArgOfPericenter:" + asteroid.getPeric();
        res += "\nMeanAnomaly:" + asteroid.getMa();
        res += "\nEpoch:" + asteroid.getEpoch() + " datetime = " + BaseUtils.getDateTime(AstroUtils.getDateFromJD(asteroid.getEpoch()),"dd MM yyyy HH:mm");
        return res;
    }

    private void convertJsonAsteroid(JSONObject astroObject, CelestiaAsteroid asteroid) {
        String name = "";
        if (astroObject.get("Name") == null) {
            name = astroObject.get("Principal_desig").toString();
        } else {
            name = astroObject.get("Name").toString();
            if (astroObject.get("Principal_desig") != null) {
                name += ":" + astroObject.get("Principal_desig").toString();
            }
        }

        try {
            asteroid.setName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            asteroid.setOrbitType(astroObject.get("Orbit_type").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        double radius = AstroUtils.getRadiusFromAbsoluteMagn(Double.parseDouble(astroObject.get("H").toString()), 0.15);
        try {
            asteroid.setRadius(radius);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            asteroid.setPeriod(Double.parseDouble(astroObject.get("Orbital_period").toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            asteroid.setSma(Double.parseDouble(astroObject.get("a").toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            asteroid.setInc(Double.parseDouble(astroObject.get("i").toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            asteroid.setNode(Double.parseDouble(astroObject.get("Node").toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            asteroid.setEcc(Double.parseDouble(astroObject.get("e").toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            asteroid.setPeric(Double.parseDouble(astroObject.get("Peri").toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            asteroid.setMa(Double.parseDouble(astroObject.get("M").toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            asteroid.setEpoch(Double.parseDouble(astroObject.get("Epoch").toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean hasItemInList(String item, ArrayList<String> list) {
		return list.indexOf(item) != -1;
	}

	private void convertJPLAsteroidsCEL(CelestiaAsteroid asteroid) {
		if (!hasItemInList(asteroid.getOrbitType(), orbitalTypes))  return;
        try {
            neamParseBuilderCEL.append("\n\"").append(asteroid.getName()).append("\"");
            neamParseBuilderCEL
                    .append("  \"").append("Sol").append("\"")
                    .append("\n{")
                    .append("\n     Class   \"asteroid\"")
                    .append("\n     Texture \"asteroid.jpg\"")
                    .append("\n     Mesh    \"eros.cmod\"")
                    .append("\n     Radius  ").append(String.format(Locale.US, "%.3f",asteroid.getRadius() ))
                    .append("\n     EllipticalOrbit");
            neamParseBuilderCEL.append(getObjectOrbit(asteroid));
            neamParseBuilderCEL.append("\n}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        asteroidCnt++;
	}

	private String getObjectOrbit(CelestiaAsteroid asteroid) {
		StringBuilder orbit = new StringBuilder();
		orbit.append("\n	{")
				.append("\n	        Period             ").append(asteroid.getPeriod())
				.append("\n         SemiMajorAxis      ").append(asteroid.getSma())
				.append("\n         Inclination        ").append(asteroid.getInc())
				.append("\n         AscendingNode      ").append(asteroid.getNode())
				.append("\n         Eccentricity      ").append(asteroid.getEcc())
				.append("\n         ArgOfPericenter    ").append(asteroid.getPeric())
				.append("\n         MeanAnomaly        ").append(asteroid.getMa())
				.append("\n         Epoch              ").append(asteroid.getEpoch())
				.append("\n	}");
		return orbit.toString();
	}

	private void convertJPLAsteroidsSE(CelestiaAsteroid asteroid) {
        if (!hasItemInList(asteroid.getOrbitType(), orbitalTypes))  return;
        try {
            neamParseBuilderSE.append("\nAsteroid	    \"").append(asteroid.getName()).append("\"");
            neamParseBuilderSE
                    .append("\n{\n	ParentBody\"").append("Sol").append("\"")
                    .append("\n     Radius  ").append(String.format(Locale.US, "%.3f", asteroid.getRadius()) )
                    .append("\n     Orbit");
            neamParseBuilderSE.append(getObjectOrbit(asteroid));
            neamParseBuilderSE.append("\n}");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public void calculate(onResultCallback resultCallback) {
		operationResult = "";
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					operationResult = "2017-Mar-02 14:05 JD = " + AstroUtils.JD(BaseUtils.convertTimeStringToLong("2017-03-02 17:05","yyyy-MM-dd HH:mm"));
					AstroUtils.setA1(2.201273459788872);
					AstroUtils.setE1(0.6018904752619388);
					AstroUtils.setI1(1.375014776910502);
					AstroUtils.setPeri1(80.37016102969110);
					AstroUtils.setNode1(161.8394032832525);
					AstroUtils.setM1(2.801826762569547E+0);

                    AstroUtils.setA1(1.000498640811768);
                    AstroUtils.setE1(1.666719180066958E-02);
                    AstroUtils.setI1(3.086491258554233E-03);
                    AstroUtils.setPeri1(2.981260335253010E+02);
                    AstroUtils.setNode1(1.668223242649487E+02);
                    AstroUtils.setM1(5.741534538676495E+01);

					AstroUtils.setA2(1.000498640811769);
					AstroUtils.setE2(1.666719180066958E-02);
					AstroUtils.setI2(3.086491258554233E-03);
					AstroUtils.setPeri2(2.981260335253010E+02);
					AstroUtils.setNode2(1.668223242649487E+02);
					AstroUtils.setM2(5.741534538676495E+01);

                    operationResult  = "\ngetMOID = " + AstroUtils.getMOID() ;
                    resultCallback.result("moid", true, operationResult);
				} catch (Exception e) {
                    resultCallback.result("moid", false, e.getMessage());
				}

			}
		}).start();
	}

}