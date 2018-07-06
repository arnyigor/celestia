package com.arny.celestiatools.controller;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.*;

import com.arny.celestiatools.models.*;
import com.arny.celestiatools.utils.*;
import com.arny.celestiatools.utils.astronomy.*;
import com.arny.celestiatools.utils.celestia.ATime;
import com.arny.celestiatools.utils.celestia.OrbitViewer;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;

import static com.arny.celestiatools.controller.SqliteConnection.getConnection;

public class Controller {

    public static final String METHOD_DOWNLOAD = "download";
    private String operationResult, parseMpcNeamCEL, parseMpcNeamSE;
    public static final String MPC_NEAs_DOWNLOAD_PATH = "https://minorplanetcenter.net/Extended_Files/nea_extended.json.gz";
    public static final String MPC_PHAs_DOWNLOAD_PATH = "https://minorplanetcenter.net/Extended_Files/pha_extended.json.gz";
    public static final String MPC_DAYLY_DOWNLOAD_PATH = "https://minorplanetcenter.net/Extended_Files/daily_extended.json.gz";
    public static final String MPC_MCORB_DOWNLOAD_PATH = "https://minorplanetcenter.net/Extended_Files/mpcorb_extended.json.gz";
    public static final String MPC_ASTER_DOWNLOADED_FILE = "mpc_downloaded.json.gz";
    public static final String MPC_ASTER_JSON_FILE = "mpc_unpacked.json";
    public static final String MPC_NEAM_LAST_SCC = "asteroids.ssc";
    public static final String MPC_NEAM_LAST_SC_SE = "Asteroids.sc";
    public static final String MPC_FILES_DIR = System.getProperty("user.dir") + "/files/";
    public File
            unpackedJsonfile = new File(MPC_FILES_DIR + MPC_ASTER_JSON_FILE),
            asteroidsFileCEL = new File(MPC_FILES_DIR + MPC_NEAM_LAST_SCC),
            asteroidsFileSE = new File(MPC_FILES_DIR + MPC_NEAM_LAST_SC_SE);
    private StringBuilder neamParseBuilderCEL, neamParseBuilderSE;
    private int asteroidCnt, totalProgress, iterateProgress;
    private int changed, added;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public Controller() {
        SqliteConnection.dbInit();
    }

    public static void setFrameForm(JFrame frame, int w, int h) {
        frame.setPreferredSize(new Dimension(w, h));
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int dx = (int) (dimension.getWidth() / 2);
        int dy = (int) (dimension.getHeight() / 2);
        frame.setLocation(dx - (w / 2), dy - (h / 2));
    }

    public Observable<ArrayList<CelestiaAsteroid>> getAsterTableData() {
        Observable<ArrayList<CelestiaAsteroid>> observable = Observable.create(e -> {
            e.onNext(SqliteConnection.getAllCelestiaAsteroids());
            e.onComplete();
        });
        compositeDisposable.add(observable.subscribe());
        return observable;
    }

    public void updateDb(ArrayList<String> orbitalTypes, onResultCallback resultParse, onResultCelestiaAsteroids celestiaData, onProgressUpdate onProgressUpdate) {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        compositeDisposable.add(RxUtils.IOThreadObservable(Observable.fromCallable(() ->
                updateDb(celestiaData, onProgressUpdate, orbitalTypes)))
                .subscribe(s -> {
                    System.out.println("res with thread:" + Thread.currentThread() + " :" + stopwatch.getElapsedTimeSecs(3) + " sec");
                    resultParse.result("dbwrite", true, s + " обновление базы заняло:" + stopwatch.getElapsedTimeSecs(3) + " секунд");
                }, throwable -> {
                    throwable.printStackTrace();
                    resultParse.result("dbwrite", false, throwable.getMessage());
                }));
    }

    private String updateDb(onResultCelestiaAsteroids celestiaData, onProgressUpdate onProgressUpdate, ArrayList<String> orbitalTypes) throws Exception {
        long start = System.currentTimeMillis();
        File folder = new File(MPC_FILES_DIR);
        boolean folderFilesExist = folder.exists() || folder.mkdir();
        if (!folderFilesExist) {
            return "Папка для сохранения не найдена";
        }
        if (unpackedJsonfile == null) {
            operationResult = "Нет распакованного файла";
            return operationResult;
        }
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(unpackedJsonfile));
        JSONArray array = new JSONArray();
        array.add(obj);
        JSONArray asteroids = (JSONArray) array.get(0);
        totalProgress = asteroids.size();
        long st = System.currentTimeMillis();
        changed = added = asteroidCnt = iterateProgress = 0;
        for (Object asteroid : asteroids) {
            if (compositeDisposable.isDisposed()) {
                long esTime = BaseUtils.getEsTime(st, System.currentTimeMillis(), iterateProgress, totalProgress);
                operationResult = "Найдено: " + asteroidCnt + " астероидов,добавлено:" + added + " обновлено:" + changed;
                operationResult += " Операция заняла:" + BaseUtils.convertExtendTime(esTime);
                return operationResult;
            }
            asteroidCnt++;
            JSONObject astroObject = (JSONObject) asteroid;
            CelestiaAsteroid celestiaAsteroid = new CelestiaAsteroid();
            celestiaAsteroid = ControllerUtilsKt.convertJsonAsteroid(astroObject, celestiaAsteroid);
            updateOrInsertDb(celestiaAsteroid, orbitalTypes);
            long esTime = BaseUtils.getEsTime(st, System.currentTimeMillis(), iterateProgress, totalProgress);
            onProgressUpdate.update("dbwrite", totalProgress, iterateProgress, BaseUtils.convertExtendTime(esTime));
            iterateProgress++;
        }
        celestiaData.dataCallback(SqliteConnection.getAllCelestiaAsteroids());
        long esTime = System.currentTimeMillis() - start;
        operationResult = "Найдено: " + asteroidCnt + " астероидов,добавлено:" + added + " обновлено:" + changed;
        operationResult += " Операция заняла:" + BaseUtils.convertExtendTime(esTime);
        return operationResult;
    }

    public void cancel(onResultCallback callback) {
        compositeDisposable.clear();
        callback.result("dbwrite", true, "Операция прервана");
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

    public void downloadFile(int source, onResultCallback resultParse, onProgressUpdate onProgressUpdate) {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        String downloadPath = getDownloadPath(source);
        compositeDisposable.add(Observable.create((ObservableOnSubscribe<String>) e -> {
            e.onNext(downloadFile(downloadPath, onProgressUpdate));
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    System.out.println("do on subscribe with thread:" + Thread.currentThread() + ":" + stopwatch.getElapsedTimeSecs(3) + " sec");
                    onProgressUpdate.update("", 0, 0, "Загрузка файла...");
                }).subscribe(s -> {
                    System.out.println("res with thread:" + Thread.currentThread() + " :" + stopwatch.getElapsedTimeSecs(3) + " sec");
                    resultParse.result(METHOD_DOWNLOAD, true, s + " загрузка файла заняла:" + stopwatch.getElapsedTimeSecs(3) + " секунд");
                }, throwable -> {
                    System.out.println("throwable :" + stopwatch.getElapsedTimeSecs(3) + " sec");
                    stopwatch.stop();
                    throwable.printStackTrace();
                    resultParse.result(METHOD_DOWNLOAD, false, throwable.toString());
                }));
    }

    private String downloadFile(String downloadPath, onProgressUpdate onProgressUpdate) throws Exception {
        File folder = new File(MPC_FILES_DIR);
        boolean folderFilesExist = folder.exists() || folder.mkdir();
        if (!folderFilesExist) {
            throw new Exception("папки для загрузки не существует");
        }
        long fileSize = FileUtils.getFileSize(downloadPath);
        System.out.println("fileSize:" + fileSize);
        onProgressUpdate.update(METHOD_DOWNLOAD, 0, 0, "Загрузка файла..." + FileUtils.formatFileSize(fileSize, 3));
        FileUtils.downloadUsingStream(downloadPath, MPC_FILES_DIR + MPC_ASTER_DOWNLOADED_FILE);
        onProgressUpdate.update(METHOD_DOWNLOAD, 0, 0, "Загрузка файла..." + FileUtils.formatFileSize(fileSize, 3) + " OK");
        File file = new File(MPC_FILES_DIR + MPC_ASTER_DOWNLOADED_FILE);
        operationResult = "файл загружен,размер " + BaseUtils.convertExtendFileLength(file.length());
        long start = System.currentTimeMillis();
        FileUtils.unzipGZ(file.getAbsolutePath(), MPC_FILES_DIR + MPC_ASTER_JSON_FILE);
        FileUtils.deleteFile(file.getAbsolutePath());
        operationResult += "\nРаспаковка заняла:" + BaseUtils.convertExtendTime((System.currentTimeMillis() - start));
        return operationResult;
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

    public void writeOrbitsFiles(ArrayList<CelestiaAsteroid> celestiaAsteroids, onResultCallback resultCallback) {
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
     * update|insert db
     *
     * @param asteroid     inset
     * @param orbitalTypes
     */
    private void updateOrInsertDb(CelestiaAsteroid asteroid, ArrayList<String> orbitalTypes) {
        if (orbitalTypes.size() > 0) {
            if (!hasItemInList(asteroid.getOrbitType(), orbitalTypes)) return;
        }
        HashMap<String, String> dbValues = new HashMap<>();
        SqliteConnection.setAsterDbValues(dbValues, asteroid);
        String cond = SqliteConnection.DB_ASTER_KEY_NAME + "='" + asteroid.getName() + "'";
        CelestiaAsteroid asterDb = SqliteConnection.getAsteroid(getConnection(), cond);
        if (asterDb == null) {
            SqliteConnection.insertSqliteData(SqliteConnection.DB_TABLE_ASTEROIDS, dbValues);
            added++;
        } else {
            if (isAsteroidChanged(asteroid, asterDb)) {
                SqliteConnection.updateSqliteData(getConnection(), SqliteConnection.DB_TABLE_ASTEROIDS, dbValues, cond);
                changed++;
            }
        }
    }

    private boolean isAsteroidChanged(CelestiaAsteroid asteroid, CelestiaAsteroid asterDb) {
        boolean changed = false;
        if (asterDb != null) {
            if (asteroid.getEpoch() != asterDb.getEpoch()) {
                changed = true;
            }
            if (asteroid.getEcc() != asterDb.getEcc()) {
                changed = true;
            }
            if (asteroid.getInc() != asterDb.getInc()) {
                changed = true;
            }
            if (asteroid.getPeriod() != asterDb.getPeriod()) {
                changed = true;
            }
            if (asteroid.getNode() != asterDb.getNode()) {
                changed = true;
            }
            if (asteroid.getMa() != asterDb.getMa()) {
                changed = true;
            }
            if (asteroid.getSma() != asterDb.getSma()) {
                changed = true;
            }
            if (asteroid.getPeric() != asterDb.getPeric()) {
                changed = true;
            }
            if (asteroid.getRadius() != asterDb.getRadius()) {
                changed = true;
            }
        } else {
            changed = true;
        }
        return changed;
    }

    public String formatAsteroidData(CelestiaAsteroid asteroid) {
        String res = "Name:" + asteroid.getName();
        res += "\nPeriod:" + asteroid.getPeriod();
        res += "\nSemiMajorAxis:" + asteroid.getSma();
        res += "\nInclination:" + asteroid.getInc();
        res += "\nAscendingNode:" + asteroid.getNode();
        res += "\nEccentricity:" + asteroid.getEcc();
        res += "\nArgOfPericenter:" + asteroid.getPeric();
        res += "\nMeanAnomaly:" + asteroid.getMa();
        res += "\nEpoch:" + asteroid.getEpoch() + " datetime = " + BaseUtils.getDateTime(AstroUtils.DateFromJD(asteroid.getEpoch()), "dd MM yyyy HH:mm");
        return res;
    }

    private void convertJsonAsteroid(JSONObject astroObject, CelestiaAsteroid asteroid) {
        System.out.println(astroObject);
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
            Object h = astroObject.get("H");
            magn = Double.parseDouble(h.toString());
        } catch (Exception e) {
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
                    .append("\n     Radius  ").append(String.format(Locale.US, "%.3f", asteroid.getRadius()))
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
                    .append("\n     Radius  ").append(String.format(Locale.US, "%.3f", asteroid.getRadius()))
                    .append("\n     Orbit");
            neamParseBuilderSE.append(getObjectOrbit(asteroid));
            neamParseBuilderSE.append("\n}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void orbitViewerStart(CelestiaAsteroid asteroid) {
        OrbitViewer orbitViewer = new OrbitViewer();
        orbitViewer.setCelestiaAsteroid(asteroid);
        orbitViewer.init();
    }

    public double calcGradToFloat(String foramttedgrads) {
        foramttedgrads = foramttedgrads.trim();
        boolean gr1 = BaseUtils.matcher("^\\d{1,3}\\s{0,}\\.{0,1}$", foramttedgrads);
        boolean gr2 = BaseUtils.matcher("^\\d{1,3}\\s{0,}\\d{1,2}\\s{0,}\\.{0,1}$", foramttedgrads);
        boolean gr3 = BaseUtils.matcher("^\\d{1,3}\\s{0,}\\d{1,2}\\s{0,}\\d{1,2}\\s{0,1}\\.{0,1}\\d{0,3}$", foramttedgrads);
        if (gr1) {
            String substring = foramttedgrads.substring(0, 3);
            String trim = substring.trim();
            int grad = Integer.parseInt(trim);
            return AstroUtils.getGrad(grad, 0, 0, true);
        } else if (gr2) {
            String substring = foramttedgrads.substring(0, 3);
            String trim = substring.trim();
            int grad = Integer.parseInt(trim);
            String substring1 = foramttedgrads.substring(4, 7);
            String trim1 = substring1.trim();
            int min = Integer.parseInt(trim1);
            return AstroUtils.getGrad(grad, min, 0, true);
        } else if (gr3) {
            String substring = foramttedgrads.substring(0, 3);
            String trim = substring.trim();
            int grad = Integer.parseInt(trim);
            String substring1 = foramttedgrads.substring(4, 7);
            String trim1 = substring1.trim();
            int min = Integer.parseInt(trim1);
            String substring2 = foramttedgrads.substring(7, foramttedgrads.length());
            String trim2 = substring2.trim();
            if (BaseUtils.matcher("^\\d{1}\\s{1}\\.$", trim2)) {
                trim2 = trim2.substring(0, trim2.length() - 1).trim();
            }
            double sec = Double.parseDouble(trim2);
            return AstroUtils.getGrad(grad, min, sec, true);
        }
        return 0;
    }

    public String calcGradToMasked(String floatedGrads) {
        if (floatedGrads.length() == 0) {
            return "000 00 00.000";
        }
        double grad1 = Double.parseDouble(floatedGrads);
        grad1 = AstroUtils.correctAngle(grad1, 360);
        GradMinSec gms = AstroUtils.getGradMinSec(grad1);
        int grad = (int) gms.getGrad();
        int min = (int) gms.getMin();
        double sec = gms.getSec();
        String secformat = sec > 9 ? "%02.3f" : "0%02.3f";
        return String.format("%03d %02d " + secformat, grad, min, sec).replace(",", ".");
    }

    public String calcDatTimeToJD(String datetime) {
        boolean matcher = BaseUtils.matcher("^\\d{2}\\s\\d{2}\\s\\d{4}\\s\\d{2}:\\d{2}:\\d{2}$", datetime);
        if (matcher) {
            long dt = DateTimeUtils.convertTimeStringToLong(datetime, "dd MM yyyy HH:mm:ss");
            double jd = AstroUtils.JD(dt);
            return String.valueOf(jd);
        }
        return "Неверная дата";
    }

    public String calcJDToDatTime(String jds) {
        jds = jds.length() > 0 ? jds.trim() : "";
        double jd = Double.parseDouble(jds);
        long dt = AstroUtils.DateFromJD(jd);
        return DateTimeUtils.getDateTime(dt, "dd MM yyyy HH:mm:ss");
    }

    public void testTime() {
        DateTime dateTime = DateTimeUtils.getDateTime();
        long timeInMillis = dateTime.getMillis();
        ATime aTime = new ATime(timeInMillis);
        double jd1 = JulianDate.JD(timeInMillis);
        double jd2 = aTime.getJd();
        double t = aTime.getT();
        double t2 = aTime.getT2();
        long datetime = aTime.getDatetime();
        double rawOffset = aTime.getTimezone();
    }

}
