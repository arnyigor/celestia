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
import java.util.List;
import java.util.Locale;

import com.arny.celestiatools.models.*;
import com.arny.celestiatools.utils.AstroUtils;
import com.arny.celestiatools.utils.BaseUtils;
import com.arny.celestiatools.utils.FileUtils;
import com.arny.celestiatools.utils.astro.OrbitViewer;
import javafx.concurrent.Worker;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;

import static com.arny.celestiatools.utils.AstroUtils.*;

public class Controller {

    private String operationResult, parseMpcNeamCEL, parseMpcNeamSE;
	private static final String MPC_NEAs_DOWNLOAD_PATH = "http://minorplanetcenter.net/Extended_Files/nea_extended.json.gz";
	private static final String MPC_PHAs_DOWNLOAD_PATH = "http://minorplanetcenter.net/Extended_Files/pha_extended.json.gz";
	private static final String MPC_DAYLY_DOWNLOAD_PATH = "http://minorplanetcenter.net/Extended_Files/daily_extended.json.gz";
	private static final String MPC_MCORB_DOWNLOAD_PATH = "http://minorplanetcenter.net/Extended_Files/mpcorb_extended.json.gz";
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
    private Connection connection = null;
    private int asteroidCnt,totalProgress,iterateProgress;
    private int changed,added;
	private Worker worker;
	private Thread write;

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
			new Thread(() -> {
				try {
					long start = System.currentTimeMillis();
					FileUtils.unzipGZ(file.getAbsolutePath(), MPC_FILES_DIR + MPC_ASTER_JSON_FILE);
					operationResult += "\nРаспаковка заняла:" + BaseUtils.convertExtendTime((System.currentTimeMillis() - start));
					resultParse.result("unzip", true, operationResult);
				} catch (Exception e) {
					resultParse.result("unzip", false, e.getMessage());
				}

			}).start();
		}
	}

//	class Worker extends SwingWorker<Void, Integer> {
//
//		int counter = 0;
//
//		@Override
//		protected Void doInBackground() throws Exception {
//			while(true) {
//				counter++;
//				publish(counter);
//				Thread.sleep(60);
//			}
//		}
//
//		@Override
//		protected void process(List<Integer> chunk) {
//			// get last result
//			Integer counterChunk = chunk.get(chunk.size()-1);
//			counterLabel.setText(counterChunk.toString());
//		}
//
//	}

	public void writeOrbitalParamFile(ArrayList<String> orbitalTypes, onResultCallback resultParse, onResultCelestiaAsteroids celestiaData, onProgressUpdate onProgressUpdate) {
		this.orbitalTypes = orbitalTypes;

		write = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					long start = System.currentTimeMillis();
					File folder = new File(MPC_FILES_DIR);
					boolean folderFilesExist = folder.exists() || folder.mkdir();
					if (!folderFilesExist) return;
					if (unpackedJsonfile == null) {
						operationResult = "Нет распакованного файла";
						resultParse.result("writessc", false, operationResult);
						return;
					}
//				parseJson(unpackedJsonfile, onProgressUpdate);

					JSONParser parser = new JSONParser();
					try {
						Object obj = parser.parse(new FileReader(unpackedJsonfile));
						JSONArray array = new JSONArray();
						array.add(obj);
						JSONArray asteroids = (JSONArray) array.get(0);
						totalProgress = asteroids.size();
						long st = System.currentTimeMillis();
						changed = added = asteroidCnt = iterateProgress = 0;
						for (Object asteroid : asteroids) {
							asteroidCnt++;
							JSONObject astroObject = (JSONObject) asteroid;
							CelestiaAsteroid celestiaAsteroid = new CelestiaAsteroid();
							convertJsonAsteroid(astroObject, celestiaAsteroid);
							updateOrInsertDb(celestiaAsteroid);
							long esTime = BaseUtils.getEsTime(st, System.currentTimeMillis(), iterateProgress, totalProgress);
							onProgressUpdate.update("dbupdate", totalProgress, iterateProgress, BaseUtils.convertExtendTime(esTime));
							iterateProgress++;
						}
						operationResult = "Найдено: " + asteroidCnt + " астероидов,добавлено:" + added + " обновлено:" + changed;

					} catch (Exception e) {
						e.printStackTrace();
					}
					celestiaData.dataCallback(SqliteConnection.getAllCelestiaAsteroids(connection));
					long esTime = System.currentTimeMillis() - start;
					operationResult += " Операция заняла:" + BaseUtils.convertExtendTime(esTime);
					resultParse.result("dbwrite", true, operationResult);

				} catch (Exception e) {
					resultParse.result("dbwrite", false, e.getMessage());
				}
			}
		});
		write.start();
	}

	public void InterruptThread(onResultCallback callback){
		try {
			write.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		callback.result("dbwrite",true,"Операция прервана");
	};

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
                case "unzip":
					message = "Файл распакован";
					break;
				case "writessc":
					message = "Орбиты записаны";
					break;
                case "dbwrite":
					message = "База обновлена";
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
                case "unzip":
                    message = "Файл не распакован";
                    break;
				case "dbwrite":
					message = "База не обновлена";
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
				try {
					File folder = new File(MPC_FILES_DIR);
					boolean folderFilesExist = folder.exists() || folder.mkdir();
					if (!folderFilesExist) return;
					FileUtils.downloadUsingStream(downloadPath, MPC_FILES_DIR + MPC_ASTER_DOWNLOADED_FILE);
					File file = new File(MPC_FILES_DIR + MPC_ASTER_DOWNLOADED_FILE);
					operationResult = "файл загружен,размер " + BaseUtils.convertExtendFileLength(file.length());
					long start = System.currentTimeMillis();
					FileUtils.unzipGZ(file.getAbsolutePath(), MPC_FILES_DIR + MPC_ASTER_JSON_FILE);
					FileUtils.deleteFile(file.getAbsolutePath());
					operationResult += "\nРаспаковка заняла:" + BaseUtils.convertExtendTime((System.currentTimeMillis() - start));
					resultParse.result("download", true, operationResult);
				} catch (Exception e) {
					e.printStackTrace();
					resultParse.result("download", false, e.getMessage());
				}
			}
		}).start();
	}

	private String getDownloadPath(int source) {
		String downloadPath = MPC_DAYLY_DOWNLOAD_PATH;
		switch (source) {
			case 0:
				downloadPath = MPC_DAYLY_DOWNLOAD_PATH;
				break;
			case 1:
				downloadPath = MPC_PHAs_DOWNLOAD_PATH;
				break;
			case 2:
				downloadPath = MPC_NEAs_DOWNLOAD_PATH;
				break;
			case 3:
				downloadPath = MPC_MCORB_DOWNLOAD_PATH;
				break;
		}
		return downloadPath;
	}

    public void writeOrbitsFiles(ArrayList<CelestiaAsteroid> celestiaAsteroids,onResultCallback resultCallback){
        new Thread(() -> {
            try {
                neamParseBuilderCEL = new StringBuilder();
                neamParseBuilderSE = new StringBuilder();
                if (celestiaAsteroids != null) {
	                try {
		                asteroidsFileCEL.delete();
	                } catch (Exception e) {
		                e.printStackTrace();
	                }
	                try {
		                asteroidsFileSE.delete();
	                } catch (Exception e) {
		                e.printStackTrace();
	                }
                    iterateProgress = 0;
                    for (CelestiaAsteroid asteroid : celestiaAsteroids) {
                        convertJPLAsteroidsCEL(asteroid);
                        convertJPLAsteroidsSE(asteroid);
                        iterateProgress++;
                    }
                    parseMpcNeamCEL = neamParseBuilderCEL.toString();
                    parseMpcNeamSE = neamParseBuilderSE.toString();

                    if (BaseUtils.empty(parseMpcNeamCEL)) {
                        operationResult = "Нечего записывать";
                        resultCallback.result("writessc", false, operationResult);
                        return;
                    }

                    Files.write(Paths.get(MPC_FILES_DIR + MPC_NEAM_LAST_SCC), parseMpcNeamCEL.getBytes(StandardCharsets.UTF_8),
                            StandardOpenOption.CREATE);
                    Files.write(Paths.get(MPC_FILES_DIR + MPC_NEAM_LAST_SC_SE), parseMpcNeamSE.getBytes(StandardCharsets.UTF_8),
                            StandardOpenOption.CREATE);

                    operationResult = "Орбит записано:" + iterateProgress;
                    resultCallback.result("writessc", true, operationResult);
                }
            } catch (IOException e) {
                e.printStackTrace();
                resultCallback.result("writessc", false, e.getMessage());
            }
        }).start();
    }

	/**
	 *  Основная ф-я парсинга
	 * @param file
	 * @param onProgressUpdate
	 */
	private void parseJson(File file,onProgressUpdate onProgressUpdate) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONArray array = new JSONArray();
			array.add(obj);
			JSONArray asteroids = (JSONArray) array.get(0);
            totalProgress = asteroids.size();
            long st = System.currentTimeMillis();
            changed = added = asteroidCnt = iterateProgress = 0;
            for (Object asteroid : asteroids) {
                asteroidCnt++;
	            JSONObject astroObject = (JSONObject) asteroid;
                CelestiaAsteroid celestiaAsteroid = new CelestiaAsteroid();
                convertJsonAsteroid(astroObject, celestiaAsteroid);
	            updateOrInsertDb(celestiaAsteroid);
	            long esTime = BaseUtils.getEsTime(st, System.currentTimeMillis(), iterateProgress, totalProgress);
	            onProgressUpdate.update("dbupdate", totalProgress, iterateProgress, BaseUtils.convertExtendTime(esTime));
                iterateProgress++;
			}
			operationResult = "Найдено: " + asteroidCnt + " астероидов,добавлено:" + added + " обновлено:" + changed;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * update|insert db
     * @param asteroid inset
     */
    private void updateOrInsertDb(CelestiaAsteroid asteroid) {
        if (orbitalTypes.size() > 0) {
            if (!hasItemInList(asteroid.getOrbitType(), orbitalTypes)) return;
        }
        HashMap<String,String> dbValues = new HashMap<>();
        SqliteConnection.setAsterDbValues(dbValues,asteroid);
        String cond = SqliteConnection.DB_ASTER_KEY_NAME + "='" + asteroid.getName()+"'";
	    CelestiaAsteroid asterDb = SqliteConnection.getAsteroid(connection, cond);
	    if (asterDb == null) {
            SqliteConnection.insertSqliteData(connection, SqliteConnection.DB_TABLE_ASTEROIDS, dbValues);
            added++;
	    } else {
		    if (isAsteroidChanged(asteroid, asterDb)) {
			    SqliteConnection.updateSqliteData(connection, SqliteConnection.DB_TABLE_ASTEROIDS, dbValues, cond);
                changed++;
            }
	    }
    }

	private boolean isAsteroidChanged(CelestiaAsteroid asteroid, CelestiaAsteroid asterDb) {
		boolean changed = false;
		if (asterDb != null) {
			if (asteroid.getEpoch()!=asterDb.getEpoch()){
				changed = true;
			}
			if (asteroid.getEcc()!=asterDb.getEcc()){
				changed = true;
			}
			if (asteroid.getInc()!=asterDb.getInc()){
				changed = true;
			}
			if (asteroid.getPeriod()!=asterDb.getPeriod()){
				changed = true;
			}
			if (asteroid.getNode()!=asterDb.getNode()){
				changed = true;
			}
			if (asteroid.getMa()!=asterDb.getMa()){
				changed = true;
			}
			if (asteroid.getSma()!=asterDb.getSma()){
				changed = true;
			}
			if (asteroid.getPeric()!=asterDb.getPeric()){
				changed = true;
			}
			if (asteroid.getRadius()!=asterDb.getRadius()){
				changed = true;
			}
		}else{
			changed = true;
		}
		return changed;
	}

    public String formatAsteroidData(CelestiaAsteroid asteroid){
	    String res = "Name:" + asteroid.getName();
        res += "\nPeriod:" + asteroid.getPeriod();
        res += "\nSemiMajorAxis:" + asteroid.getSma();
        res += "\nInclination:" + asteroid.getInc();
        res += "\nAscendingNode:" + asteroid.getNode();
        res += "\nEccentricity:" + asteroid.getEcc();
        res += "\nArgOfPericenter:" + asteroid.getPeric();
        res += "\nMeanAnomaly:" + asteroid.getMa();
        res += "\nEpoch:" + asteroid.getEpoch() + " datetime = " + BaseUtils.getDateTime(AstroUtils.DateFromJD(asteroid.getEpoch()),"dd MM yyyy HH:mm");
        return res;
    }

    private void convertJsonAsteroid(JSONObject astroObject, CelestiaAsteroid asteroid) {
        String name;
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
        double magn;
        try {
            magn = Double.parseDouble(astroObject.get("H").toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            magn = 25.0;
        }
        double radius = AstroUtils.getRadiusFromAbsoluteMagn(magn, 0.15);
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
		return "\n	{" +
				"\n	        Period             " + asteroid.getPeriod() +
				"\n         SemiMajorAxis      " + asteroid.getSma() +
				"\n         Inclination        " + asteroid.getInc() +
				"\n         AscendingNode      " + asteroid.getNode() +
				"\n         Eccentricity      " + asteroid.getEcc() +
				"\n         ArgOfPericenter    " + asteroid.getPeric() +
				"\n         MeanAnomaly        " + asteroid.getMa() +
				"\n         Epoch              " + asteroid.getEpoch() +
				"\n	}";
	}

	private void convertJPLAsteroidsSE(CelestiaAsteroid asteroid) {
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

	public void orbitViewerStart(CelestiaAsteroid asteroid){
		OrbitViewer orbitViewer = new OrbitViewer();
        orbitViewer.setCelestiaAsteroid(asteroid);
        long curtime = System.currentTimeMillis();
		orbitViewer.init(YMDd(JD(curtime)));
	}

	public void calculate(onResultCallback resultCallback) {
		operationResult = "";
        long curtime = BaseUtils.convertTimeStringToLong("2017 02 16","yyyy ");
        try {
            double jd = JD(curtime);
            operationResult  = "\nres = " + jd;
            System.out.println(operationResult);
            resultCallback.result("moid", true, operationResult);
        } catch (Exception e) {
            resultCallback.result("moid", false, e.getMessage());
        }
	}

}
