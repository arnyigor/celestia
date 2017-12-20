package com.arny.celestiatools.utils.celestia;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.*;

import com.arny.celestiatools.controller.Controller;
import com.arny.celestiatools.models.CelestiaAsteroid;
import com.arny.celestiatools.utils.DateTimeUtils;
import com.arny.celestiatools.utils.astronomy.AstroConst;
import com.arny.celestiatools.utils.astronomy.AstroUtils;
import com.arny.celestiatools.utils.BaseUtils;
import jdk.nashorn.internal.scripts.JD;
import org.joda.time.DateTime;

import javax.swing.*;

import static com.arny.celestiatools.utils.astronomy.AstroUtils.*;

/**
 * Main Applet Class
 */
public class OrbitViewer extends Applet implements ActionListener {
    public static final String[] timeStepLabel = {
            "1 Sec", "1 Min", "1 Hour", "1 Day", "3 Days", "10 Days", "1 Month", "3 Months", "6 Months", "1 Year"
    };
    private static final String[] strMonthAbbr = {
            "Jan.", "Feb.", "Mar.", "Apr.", "May ", "June",
            "July", "Aug.", "Sep.", "Oct.", "Nov.", "Dec.",
    };
    /**
     * Components
     */
    private Scrollbar scrollHorz, scrollVert, scrollZoom, scrollZoomFactor;
    private OrbitCanvas orbitCanvas;
    private Button buttonDate, buttonRevPlay, buttonRevStep, buttonStop, buttonForStep, buttonForPlay;
    private Choice choiceTimeStep, choiceCenterObject, choiceOrbitObject;
    private Checkbox checkPlanetName, checkObjectName, checkDistanceLabel, checkDateLabel;

    private DateDialog dateDialog = null;

    /**
     * Player thread
     */
    private OrbitPlayer orbitPlayer;
    private Thread playerThread = null;

    /**
     * Current Time Setting
     */
    private ATime atime;

    /**
     * Time step
     */
    private static final int timeStepCount = 8;
    public static final TimeSpan[] timeStepSpan = {
            new TimeSpan(0, 0, 0, 0, 0, 1.0),
            new TimeSpan(0, 0, 0, 0, 1, 0.0),
            new TimeSpan(0, 0, 0, 1, 0, 0.0),
            new TimeSpan(0, 0, 1, 0, 0, 0.0),
            new TimeSpan(0, 0, 3, 0, 0, 0.0),
            new TimeSpan(0, 0, 10, 0, 0, 0.0),
            new TimeSpan(0, 1, 0, 0, 0, 0.0),
            new TimeSpan(0, 3, 0, 0, 0, 0.0),
            new TimeSpan(0, 6, 0, 0, 0, 0.0),
            new TimeSpan(1, 0, 0, 0, 0, 0.0),
    };
    public TimeSpan timeStep = timeStepSpan[3];
    public int playDirection = ATime.F_INCTIME;

    /**
     * Centered Object
     */
    private static final int CenterObjectCount = 11;
    private static final String CenterObjectLabel[] = {
            "Sun", "Asteroid/Comet", "Mercury", "Venus", "Earth",
            "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"
    };
    private int CenterObjectSelected = 1;

    /**
     * Orbits Displayed
     */
    private static final int OrbitDisplayCount = 14;
    private static final String OrbitDisplayLabel[] = {
            "Default Orbits", "All Orbits", "No Orbits", "------",
            "Asteroid/Comet", "Mercury", "Venus", "Earth",
            "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"
    };
    private int OrbitCount = 11;
    private boolean OrbitDisplay[] = {false, true, true, true, true, true, true,
            false, false, false, false};
    private boolean OrbitDisplayDefault[] = {false, true, true, true, true, true, true,
            false, false, false, false};

    /**
     * Limit of ATime
     */
//	private ATime minATime = new ATime(-30000,1,1,0,0,0.0,0.0);
    private ATime minATime = new ATime(1600, 1, 1, 0, 0, 0.0, 0.0);
    private ATime maxATime = new ATime(2200, 1, 1, 0, 0, 0.0, 0.0);

    /**
     * Initial Settings
     */
    private static final int initialScrollVert = 180;
    private static final int initialScrollHorz = 255;
    private static final int initialScrollZoom = 100;
    private static final int initialScrollZoomFactor = 300;
    private static final int fontSize = 16;
    private CelestiaAsteroid celestiaAsteroid;
    private double minDist;
    private ArrayList<Double> averDist = new ArrayList<>();

    /**
     * Get Abbreviated Month Name
     */
    static public String getMonthAbbr(int nMonth) {
        return strMonthAbbr[nMonth - 1];
    }


    public void DynamicTimeStep(double edistance) {
        double stepLessSecDistKm = AstroUtils.DistanceConvert(0.05E6, DistanceTypes.km, DistanceTypes.AU);
        double stepSecDistKm = AstroUtils.DistanceConvert(0.1E6, DistanceTypes.km, DistanceTypes.AU);
        double stepMinDistKm = AstroUtils.DistanceConvert(0.5E6, DistanceTypes.km, DistanceTypes.AU);
        double stepHourDistKm = AstroUtils.DistanceConvert(5E6, DistanceTypes.km, DistanceTypes.AU);
        double stepDayDistKm = AstroUtils.DistanceConvert(50E6, DistanceTypes.km, DistanceTypes.AU);
        double step3DayDistKm = AstroUtils.DistanceConvert(500E6, DistanceTypes.km, DistanceTypes.AU);
        int step = 3;
        if (edistance >= step3DayDistKm) {
            step = 4;
        } else if (edistance <= stepDayDistKm && edistance > step3DayDistKm) {
            step = 3;
        } else if (edistance <= stepHourDistKm && edistance > stepMinDistKm) {
            step = 2;
        } else if (edistance <= stepMinDistKm && edistance > stepSecDistKm) {
            step = 1;
        } else if (edistance <= stepSecDistKm && edistance > stepLessSecDistKm) {
            step = 0;
        } else if (edistance <= stepLessSecDistKm) {
            step = 9;
        }
        timeStep = timeStepSpan[step];
        choiceTimeStep.select(timeStepLabel[step]);
    }

    public void minEdistance(double ed, ATime atime) {
        if (minDist == 0) {
            minDist = ed;
            String mindisttime = BaseUtils.getDateTime(AstroUtils.DateFromJD(atime.getJd()), "dd MMM yyyy HH:mm");
            orbitCanvas.setStrATime(mindisttime);
        } else if (ed < minDist) {
            minDist = ed;
            String mindisttime = BaseUtils.getDateTime(AstroUtils.DateFromJD(atime.getJd()), "dd MMM yyyy HH:mm");
            orbitCanvas.setStrATime(mindisttime);
        }
        orbitCanvas.setMinEdist(minDist);
    }

    public void averageEdistance(double ed) {
        double average = 0;
        if (averDist.size() < 10) {
            averDist.add(ed);
        }
        for (int x = 0; x < averDist.size(); x++) {
            average = (average / (x + 1)) * (x) + averDist.get(x) / (x + 1);
        }
        if (averDist.size() >= 10) {
            averDist.remove(0);
            averDist.add(ed);
        }
//        System.out.println("Tim:d"+timeStep.nDay+" h:"+timeStep.nHour+" m:"+timeStep.nMin+" aver:" + MathUtils.round(AstroUtils.DistanceConvert(average,DistanceTypes.AU,DistanceTypes.km),3));
    }

    public double getEsDistance() {
        return orbitCanvas.getEdistance();
    }

    /**
     * Applet information
     */
    public String getAppletInfo() {
        return "OrbitViewer v1.3 Copyright(C) 1996-2001 by O.Ajiki/R.Baalke";
    }

    /**
     * Convert time in format "YYYYMMDD.D" to ATime
     */
    private static ATime ymdStringToAtime(String strYmd) {
        String Ymd = strYmd.substring(0, strYmd.indexOf('.'));
        double fDays = new BigDecimal(strYmd).remainder(BigDecimal.ONE).doubleValue();
        DateTime time = DateTimeUtils.getDateTime(Ymd, "yyyyMMdd");
        int nYear = time.getYear();
        int nMonth = time.getMonthOfYear();
        double fDay = fDays + (double) time.getDayOfMonth();
        return new ATime(nYear, nMonth, fDay, 0.0);
    }

    /**
     * Get required double parameter
     */
    private double getRequiredParameter(String strName) {
        String strValue = getParameter(strName);
        if (strValue == null) {
            throw new Error("Required parameter '"
                    + strName + "' not found.");
        }
        return Double.valueOf(strValue);
    }

    @Override
    public String getParameter(String name) {
        String info[][] = {
                {"Name", celestiaAsteroid.getName()},
                {"T",},
                {"e", String.valueOf(celestiaAsteroid.getEcc())},
                {"q",},
                {"Peri", String.valueOf(celestiaAsteroid.getPeric())},
                {"Node", String.valueOf(celestiaAsteroid.getNode())},
                {"Incl", String.valueOf(celestiaAsteroid.getInc())},
                {"Eqnx", "2000.0"},
                {"Epoch", String.valueOf(celestiaAsteroid.getEpoch())},
                {"M", String.valueOf(celestiaAsteroid.getMa())},
                {"a", String.valueOf(celestiaAsteroid.getSma())},
                {"Date", "20170216.0"},
        };

        String res = null;
        for (String[] anInfo : info) {
            for (int i = 0; i < anInfo.length; i++) {
//                System.out.println(name + " " + i + "->" + anInfo[0] + " equals:" + name.equals(anInfo[0]));
                if (name.equals(anInfo[0])) {
                    if (anInfo.length < 2) {
                        return null;
                    } else {
                        res = anInfo[1];
                        break;
                    }
                }
            }
        }
        return res;
    }

    private Comet getObject() {
        String strName = getParameter("Name");
        if (strName == null) {
            strName = "Object";
        }
        double e, q;
        ATime T;
        String strParam;
        if ((strParam = getParameter("e")) == null) {
            throw new Error("required parameter 'e' not found.");
        }
        e = Double.valueOf(strParam);
        if ((strParam = getParameter("T")) != null) {
            T = ymdStringToAtime(strParam);
            if ((strParam = getParameter("q")) != null) {
                q = Double.valueOf(strParam);
            } else if ((strParam = getParameter("a")) != null) {
                double a = Double.valueOf(strParam);
                if (Math.abs(e - 1.0) < 1.0e-15) {
                    throw new Error("Orbit is parabolic, but 'q' not found.");
                }
                q = a * (1.0 - e);
            } else {
                throw new Error("Required parameter 'q' or 'a' not found.");
            }
        } else if ((strParam = getParameter("Epoch")) != null) {
            double jdt = Double.parseDouble(strParam);
            String ymDd = YMDd(jdt);
            ATime Epoch = ymdStringToAtime(ymDd);
            if (e > 0.95) {
                throw new Error("Orbit is nearly parabolic, but 'T' not found.");
            }
            double a;
            if ((strParam = getParameter("a")) != null) {
                a = Double.valueOf(strParam);
                q = a * (1.0 - e);
            } else if ((strParam = getParameter("q")) != null) {
                q = Double.valueOf(strParam);
                a = q / (1.0 - e);
            } else {
                throw new Error("Required parameter 'q' or 'a' not found.");
            }
            if (q < 1.0e-15) {
                throw new Error("Too small perihelion distance.");
            }
            double n = AstroConst.GAUSS / (a * Math.sqrt(a));
            if ((strParam = getParameter("M")) == null) {
                throw new Error("Required parameter 'M' not found.");
            }
            double M = Double.valueOf(strParam)
                    * Math.PI / 180.0;

            double epo = Double.parseDouble(getParameter("Epoch"));
            if (M < Math.PI) {
                T = new ATime(epo - M / n, 0.0);
            } else {
                T = new ATime(epo + (Math.PI * 2.0 - M) / n, 0.0);
            }
        } else {
            throw new Error("Required parameter 'T' or 'Epoch' not found.");
        }
        return new Comet(strName, T.getJd(), e, q,
                getRequiredParameter("Peri") * Math.PI / 180.0,
                getRequiredParameter("Node") * Math.PI / 180.0,
                getRequiredParameter("Incl") * Math.PI / 180.0,
                getRequiredParameter("Eqnx"));
    }

    /**
     * Get orbital elements of the object from applet parameter
     */
    private Comet getObjectNew() {

//		 * <PARAM NAME="Name"  VALUE="1P/Halley">
// * <PARAM NAME="T"     VALUE="19860209.7695">
// * <PARAM NAME="e"     VALUE="0.967267">
// * <PARAM NAME="q"     VALUE="0.587096">
// * <PARAM NAME="Peri"  VALUE="111.8466">
// * <PARAM NAME="Node"  VALUE=" 58.1440">
// * <PARAM NAME="Incl"  VALUE="162.2393">
// * <PARAM NAME="Eqnx"  VALUE="1950.0">

//		String strName = "1P/Halley";
//		ATime T;
//		String strParam;
//		if ((strParam = "19860209.7695") != null) {
//			T = ymdStringToAtime(strParam);
//			if ((strParam = "0.587096") != null) {
//				q = Double.valueOf(strParam);
//			} else if ((strParam = getParameter("a")) != null) {
//				double a = Double.valueOf(strParam);
//				if (Math.abs(e - 1.0) < 1.0e-15) {
//					throw new Error("Orbit is parabolic, but 'q' not found.");
//				}
//				q = a * (1.0 - e);
//			} else {
//				throw new Error("Required parameter 'q' or 'a' not found.");
//			}
//		}
//
//		else if ((strParam = getParameter("Epoch")) != null) {
//			ATime Epoch = ymdStringToAtime(strParam);
//			if (e > 0.95) {
//				throw new
//						Error("Orbit is nearly parabolic, but 'T' not found.");
//			}
//			double a;
//			if ((strParam = getParameter("a")) != null) {
//				a = Double.valueOf(strParam);
//				q = a * (1.0 - e);
//			} else if ((strParam = getParameter("q")) != null) {
//				q = Double.valueOf(strParam);
//				a = q / (1.0 - e);
//			} else {
//				throw new Error("Required parameter 'q' or 'a' not found.");
//			}
//			if (q < 1.0e-15) {
//				throw new Error("Too small perihelion distance.");
//			}
//			double n = AstroConst.GAUSS / (a * Math.sqrt(a));
//			if ((strParam = getParameter("M")) == null) {
//				throw new Error("Required parameter 'M' not found.");
//			}
//			double M = Double.valueOf(strParam)
//					* Math.PI / 180.0;
//			if (M < Math.PI) {
//				T = new ATime(Epoch.getJd() - M / n, 0.0);
//			} else {
//				T = new ATime(Epoch.getJd() + (Math.PI * 2.0 - M) / n, 0.0);
//			}
//		} else {
//			throw new Error("Required parameter 'T' or 'Epoch' not found.");
//		}
        String strName = getCelestiaAsteroid().getName();

        ATime Epoch = ymdStringToAtime(String.valueOf(getCelestiaAsteroid().getEpoch()));
        ATime T;
        double M = getCelestiaAsteroid().getMa() * Math.PI / 180.0;
        double a = getCelestiaAsteroid().getSma();
        double n = AstroConst.GAUSS / (a * Math.sqrt(a));
        if (M < Math.PI) {
            T = new ATime(Epoch.getJd() - M / n, 0.0);
        } else {
            T = new ATime(Epoch.getJd() + (Math.PI * 2.0 - M) / n, 0.0);
        }
        double fT = T.getJd();
        double e = getCelestiaAsteroid().getEcc();
        double q = a * (1.0 - e);
        return new Comet(strName, fT, e, q,
                getCelestiaAsteroid().getPeric() * Math.PI / 180.0,
                getCelestiaAsteroid().getNode() * Math.PI / 180.0,
                getCelestiaAsteroid().getInc() * Math.PI / 180.0, 2000.0);
    }

    /**
     * Limit ATime between minATime and maxATime
     */
    private ATime limitATime(ATime atime) {
        if (atime.getJd() <= minATime.getJd()) {
            return new ATime(minATime);
        } else if (maxATime.getJd() <= atime.getJd()) {
            return new ATime(maxATime);
        }
        return atime;
    }

    /**
     * Set date and redraw canvas
     */
    private void setNewDate() {
        this.atime = limitATime(this.atime);
        orbitCanvas.setDate(this.atime);
        orbitCanvas.repaint();
    }

    /**
     * OrbitPlayer interface
     */
    public ATime getAtime() {
        return atime;
    }

    public void setNewDate(ATime atime) {
        this.atime = limitATime(atime);
        orbitCanvas.setDate(this.atime);
        orbitCanvas.repaint();
    }

    /**
     * Initialization of applet
     */
    public void init(String YMDd) {
        this.setBackground(Color.white);
        //
        // Main Panel
        //
        Panel mainPanel = new Panel();
        GridBagLayout gblMainPanel = new GridBagLayout();
        GridBagConstraints gbcMainPanel = new GridBagConstraints();
        gbcMainPanel.fill = GridBagConstraints.BOTH;
        mainPanel.setLayout(gblMainPanel);

        // Orbit Canvas
        Comet object = getObject();
        this.atime = ymdStringToAtime(YMDd);
        orbitCanvas = new OrbitCanvas(object, this.atime);
        gbcMainPanel.weightx = 1.0;
        gbcMainPanel.weighty = 1.0;
        gbcMainPanel.gridwidth = GridBagConstraints.RELATIVE;
        gblMainPanel.setConstraints(orbitCanvas, gbcMainPanel);
        mainPanel.add(orbitCanvas);

        // Vertical Scrollbar
        scrollVert = new Scrollbar(Scrollbar.VERTICAL, initialScrollVert, 12, 0, 192);
        scrollVert.setBackground(Color.WHITE);
        scrollVert.setValue(100);
        gbcMainPanel.weightx = 0.0;
        gbcMainPanel.weighty = 0.0;
        gbcMainPanel.gridwidth = GridBagConstraints.REMAINDER;
        gblMainPanel.setConstraints(scrollVert, gbcMainPanel);
        mainPanel.add(scrollVert);
        orbitCanvas.setRotateVert(180 - scrollVert.getValue());
        scrollVert.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                orbitCanvas.setRotateVert(180 - e.getValue());
                orbitCanvas.repaint();
            }
        });

        // Horizontal Scrollbar
        scrollHorz = new Scrollbar(Scrollbar.HORIZONTAL, initialScrollHorz, 15, 0, 375);
        scrollHorz.setBackground(Color.WHITE);
        gbcMainPanel.weightx = 1.0;
        gbcMainPanel.weighty = 0.0;
        gbcMainPanel.gridwidth = 1;
        gblMainPanel.setConstraints(scrollHorz, gbcMainPanel);
        scrollHorz.addAdjustmentListener(e -> {
            orbitCanvas.setRotateHorz(270 - e.getValue());
            orbitCanvas.repaint();
        });
        mainPanel.add(scrollHorz);
        orbitCanvas.setRotateHorz(270 - scrollHorz.getValue());

        // Right-Bottom Corner Rectangle
        Panel cornerPanel = new Panel();
        gbcMainPanel.weightx = 0.0;
        gbcMainPanel.weighty = 0.0;
        gbcMainPanel.gridwidth = GridBagConstraints.REMAINDER;
        gblMainPanel.setConstraints(cornerPanel, gbcMainPanel);
        mainPanel.add(cornerPanel);

        //
        // Control Panel
        //
        Panel ctrlPanel = new Panel();
        GridBagLayout gblCtrlPanel = new GridBagLayout();
        GridBagConstraints gbcCtrlPanel = new GridBagConstraints();
        gbcCtrlPanel.fill = GridBagConstraints.BOTH;
        ctrlPanel.setLayout(gblCtrlPanel);
        ctrlPanel.setBackground(Color.white);

        // Set Date Button
        buttonDate = new Button(" Date ");
        buttonDate.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        gbcCtrlPanel.gridx = 0;
        gbcCtrlPanel.gridy = 0;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 1.0;
        gbcCtrlPanel.gridwidth = 1;
        gbcCtrlPanel.gridheight = 2;
        gbcCtrlPanel.insets = new Insets(0, 0, 0, 12);
        gblCtrlPanel.setConstraints(buttonDate, gbcCtrlPanel);
        buttonDate.addActionListener(e -> {
            dateDialog = new DateDialog(OrbitViewer.this, atime);
            buttonDate.setEnabled(true);
        });
        ctrlPanel.add(buttonDate);
        // Reverse-Play Button
        buttonRevPlay = new Button("<<");
        buttonRevPlay.setFont(new Font("Dialog", Font.BOLD, fontSize - 2));
        gbcCtrlPanel.gridx = 1;
        gbcCtrlPanel.gridy = 0;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 0.0;
        gbcCtrlPanel.gridwidth = 1;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 0, 3, 0);
        gblCtrlPanel.setConstraints(buttonRevPlay, gbcCtrlPanel);
        buttonRevPlay.addActionListener(e -> {
            if (playerThread != null
                    && playDirection != ATime.F_DECTIME) {
                playerThread.interrupt();
                playerThread = null;
            }
            if (playerThread == null) {
                buttonDate.setEnabled(false);
                playDirection = ATime.F_DECTIME;
                playerThread = new Thread(orbitPlayer);
                playerThread.setPriority(Thread.MIN_PRIORITY);
                playerThread.start();
            }
        });
        ctrlPanel.add(buttonRevPlay);

        // Reverse-Step Button
        buttonRevStep = new Button("|<");
        buttonRevStep.setFont(new Font("Dialog", Font.BOLD, fontSize - 2));
        gbcCtrlPanel.gridx = 2;
        gbcCtrlPanel.gridy = 0;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 0.0;
        gbcCtrlPanel.gridwidth = 1;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 0, 3, 0);
        gblCtrlPanel.setConstraints(buttonRevStep, gbcCtrlPanel);
        ctrlPanel.add(buttonRevStep);
        buttonRevStep.addActionListener(e -> {
            atime.changeDate(timeStep, ATime.F_DECTIME);
            setNewDate();
        });

        // Stop Button
        buttonStop = new Button("||");
        buttonStop.setFont(new Font("Dialog", Font.BOLD, fontSize - 2));
        gbcCtrlPanel.gridx = 3;
        gbcCtrlPanel.gridy = 0;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 0.0;
        gbcCtrlPanel.gridwidth = 1;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 0, 3, 0);
        gblCtrlPanel.setConstraints(buttonStop, gbcCtrlPanel);
        buttonStop.addActionListener(e -> {
            if (playerThread != null) {
                playerThread.interrupt();
                playerThread = null;
                buttonDate.setEnabled(true);
            }
        });
        ctrlPanel.add(buttonStop);

        // Step Button
        buttonForStep = new Button(">|");
        buttonForStep.setFont(new Font("Dialog", Font.BOLD, fontSize - 2));
        gbcCtrlPanel.gridx = 4;
        gbcCtrlPanel.gridy = 0;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 0.0;
        gbcCtrlPanel.gridwidth = 1;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 0, 3, 0);
        gblCtrlPanel.setConstraints(buttonForStep, gbcCtrlPanel);
        buttonForStep.addActionListener(e -> {
            atime.changeDate(timeStep, ATime.F_INCTIME);
            setNewDate();
        });
        ctrlPanel.add(buttonForStep);

        // Play Button
        buttonForPlay = new Button(">>");
        buttonForPlay.setFont(new Font("Dialog", Font.BOLD, fontSize - 2));
        gbcCtrlPanel.gridx = 5;
        gbcCtrlPanel.gridy = 0;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 0.0;
        gbcCtrlPanel.gridwidth = 1;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 0, 3, 0);
        gblCtrlPanel.setConstraints(buttonForPlay, gbcCtrlPanel);
        buttonForPlay.addActionListener(e -> {
            if (playerThread != null && playDirection != ATime.F_INCTIME) {
                playerThread.interrupt();
                playerThread = null;
            }
            if (playerThread == null) {
                buttonDate.setEnabled(false);
                playDirection = ATime.F_INCTIME;
                playerThread = new Thread(orbitPlayer);
                playerThread.setPriority(Thread.MIN_PRIORITY);
                playerThread.start();
            }
        });
        ctrlPanel.add(buttonForPlay);

        // Step choice box
        choiceTimeStep = new Choice();
        choiceTimeStep.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        gbcCtrlPanel.gridx = 1;
        gbcCtrlPanel.gridy = 1;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 0.0;
        gbcCtrlPanel.gridwidth = 5;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 0, 0, 0);
        gblCtrlPanel.setConstraints(choiceTimeStep, gbcCtrlPanel);
        choiceTimeStep.addItemListener(e -> {
            for (int i = 0; i < timeStepCount; i++) {
                if (e.getItem() == timeStepLabel[i]) {
                    timeStep = timeStepSpan[i];
                    break;
                }
            }
        });
        ctrlPanel.add(choiceTimeStep);
        for (int i = 0; i < timeStepCount; i++) {
            choiceTimeStep.addItem(timeStepLabel[i]);
            choiceTimeStep.select(timeStepLabel[2]);
        }

        // Center Object Label
        Label centerLabel = new Label("Center:");
        centerLabel.setAlignment(Label.LEFT);
        centerLabel.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        gbcCtrlPanel.gridx = 0;
        gbcCtrlPanel.gridy = 2;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 1.0;
        gbcCtrlPanel.gridwidth = 1;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 0, 0, 0);
        gblCtrlPanel.setConstraints(centerLabel, gbcCtrlPanel);
        ctrlPanel.add(centerLabel);

        // Center Object choice box
        choiceCenterObject = new Choice();
        choiceCenterObject.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        gbcCtrlPanel.gridx = 1;
        gbcCtrlPanel.gridy = 2;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 0.0;
        gbcCtrlPanel.gridwidth = 5;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 0, 0, 0);
        gblCtrlPanel.setConstraints(choiceCenterObject, gbcCtrlPanel);
        ctrlPanel.add(choiceCenterObject);
        for (int i = 0; i < CenterObjectCount; i++) {
            choiceCenterObject.addItem(CenterObjectLabel[i]);
        }
        orbitCanvas.SelectCenterObject(CenterObjectSelected);
        choiceCenterObject.select(CenterObjectSelected);

        choiceCenterObject.addItemListener(e -> {
            for (int i = 0; i < CenterObjectCount; i++) {
                if (e.getItem() == CenterObjectLabel[i]) {
                    CenterObjectSelected = i;
                    orbitCanvas.SelectCenterObject(i);
                    orbitCanvas.repaint();
                    break;
                }
            }
        });

        // Display Orbits Label
        Label orbitLabel = new Label("Orbits:");
        orbitLabel.setAlignment(Label.LEFT);
        orbitLabel.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        gbcCtrlPanel.gridx = 0;
        gbcCtrlPanel.gridy = 3;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 1.0;
        gbcCtrlPanel.gridwidth = 1;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 0, 0, 0);
        gblCtrlPanel.setConstraints(orbitLabel, gbcCtrlPanel);
        ctrlPanel.add(orbitLabel);

        // Display Orbit choice box
        choiceOrbitObject = new Choice();
        choiceOrbitObject.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        gbcCtrlPanel.gridx = 1;
        gbcCtrlPanel.gridy = 3;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 0.0;
        gbcCtrlPanel.gridwidth = 5;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 0, 0, 0);
        gblCtrlPanel.setConstraints(choiceOrbitObject, gbcCtrlPanel);
        ctrlPanel.add(choiceOrbitObject);
        for (int i = 0; i < OrbitDisplayCount; i++) {
            choiceOrbitObject.addItem(OrbitDisplayLabel[i]);
        }
        System.arraycopy(OrbitDisplayDefault, 0, OrbitDisplay, 0, OrbitCount);
        orbitCanvas.SelectOrbits(OrbitDisplay, OrbitCount);

        choiceOrbitObject.addItemListener(e -> {
            for (int i = 0; i < OrbitDisplayCount; i++) {
                if (e.getItem() == OrbitDisplayLabel[i]) {
                    if (i == 1) {
                        for (int j = 0; j < OrbitCount; j++) {
                            OrbitDisplay[j] = true;
                        }
                    } else if (i == 2) {
                        for (int j = 0; j < OrbitCount; j++) {
                            OrbitDisplay[j] = false;
                        }
                    } else if (i == 0) {
                        for (int j = 0; j < OrbitCount; j++) {
                            OrbitDisplay[j] = OrbitDisplayDefault[j];
                        }
                    } else if (i > 3) {
                        if (OrbitDisplay[i - 3]) {
                            OrbitDisplay[i - 3] = false;
                        } else {
                            OrbitDisplay[i - 3] = true;
                        }
                    }
//                        e.setSource(OrbitDisplayLabel[0]);
//                        evt.arg = OrbitDisplayLabel[0];
                    orbitCanvas.SelectOrbits(OrbitDisplay, OrbitCount);
                    orbitCanvas.repaint();
                    break;
                }
            }
        });

        // Date Label Checkbox
        checkDateLabel = new Checkbox("Date Label");
        checkDateLabel.setState(true);
        checkDateLabel.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        gbcCtrlPanel.gridx = 6;
        gbcCtrlPanel.gridy = 0;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 0.0;
        gbcCtrlPanel.gridwidth = 1;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 12, 0, 0);
        gblCtrlPanel.setConstraints(checkDateLabel, gbcCtrlPanel);
        ctrlPanel.add(checkDateLabel);
        orbitCanvas.switchPlanetName(checkDateLabel.getState());

        // Planet Name Checkbox
        checkPlanetName = new Checkbox("Planet Labels");
        checkPlanetName.setState(true);
        checkPlanetName.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        checkPlanetName.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                orbitCanvas.switchPlanetName(checkPlanetName.getState());
                orbitCanvas.repaint();
            }
        });
        gbcCtrlPanel.gridx = 7;
        gbcCtrlPanel.gridy = 0;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 0.0;
        gbcCtrlPanel.gridwidth = 1;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 12, 0, 0);
        gblCtrlPanel.setConstraints(checkPlanetName, gbcCtrlPanel);
        ctrlPanel.add(checkPlanetName);
        orbitCanvas.switchPlanetName(checkPlanetName.getState());

        // Distance Label Checkbox
        checkDistanceLabel = new Checkbox("Distance");
        checkDistanceLabel.setState(true);
        checkDistanceLabel.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        gbcCtrlPanel.gridx = 6;
        gbcCtrlPanel.gridy = 1;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 0.0;
        gbcCtrlPanel.gridwidth = 1;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 12, 0, 0);
        gblCtrlPanel.setConstraints(checkDistanceLabel, gbcCtrlPanel);
        ctrlPanel.add(checkDistanceLabel);
        orbitCanvas.switchPlanetName(checkDistanceLabel.getState());

        // Object Name Checkbox
        checkObjectName = new Checkbox("Object Label");
        checkObjectName.setState(true);
        checkObjectName.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        gbcCtrlPanel.gridx = 7;
        gbcCtrlPanel.gridy = 1;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 0.0;
        gbcCtrlPanel.gridwidth = 1;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(0, 12, 0, 0);
        gblCtrlPanel.setConstraints(checkObjectName, gbcCtrlPanel);
        ctrlPanel.add(checkObjectName);
        orbitCanvas.switchObjectName(checkObjectName.getState());

        // Zoom Label
        Label zoomLabel = new Label("Zoom:");
        zoomLabel.setAlignment(Label.LEFT);
        zoomLabel.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        gbcCtrlPanel.gridx = 6;
        gbcCtrlPanel.gridy = 2;
        gbcCtrlPanel.weightx = 0.0;
        gbcCtrlPanel.weighty = 1.0;
        gbcCtrlPanel.gridwidth = 2;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(15, 12, 0, 0);
        gblCtrlPanel.setConstraints(zoomLabel, gbcCtrlPanel);
        ctrlPanel.add(zoomLabel);

        // Zoom Scrollbar
        scrollZoom = new Scrollbar(Scrollbar.HORIZONTAL, initialScrollZoom, 15, 1, 350);
        scrollZoom.setBackground(Color.WHITE);
        gbcCtrlPanel.gridx = 6;
        gbcCtrlPanel.gridy = 3;
        gbcCtrlPanel.weightx = 1.0;
        gbcCtrlPanel.weighty = 1.0;
        gbcCtrlPanel.gridwidth = 2;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(5, 12, 30, 2);
        gblCtrlPanel.setConstraints(scrollZoom, gbcCtrlPanel);
        scrollZoom.addAdjustmentListener(e -> {
            orbitCanvas.setZoom(e.getValue());
            orbitCanvas.repaint();
        });
        ctrlPanel.add(scrollZoom);
        orbitCanvas.setZoom(scrollZoom.getValue());

        // Zoom factor Scrollbar
        scrollZoomFactor = new Scrollbar(Scrollbar.HORIZONTAL, initialScrollZoomFactor, 2, 1, 600);
        scrollZoomFactor.setBackground(Color.WHITE);
        gbcCtrlPanel.gridx = 6;
        gbcCtrlPanel.gridy = 3;
        gbcCtrlPanel.weightx = 1.0;
        gbcCtrlPanel.weighty = 1.0;
        gbcCtrlPanel.gridwidth = 2;
        gbcCtrlPanel.gridheight = 1;
        gbcCtrlPanel.insets = new Insets(10, 12, 0, 2);
        gblCtrlPanel.setConstraints(scrollZoomFactor, gbcCtrlPanel);
        scrollZoomFactor.addAdjustmentListener(e -> {
            orbitCanvas.setZoomFactor(e.getValue());
            orbitCanvas.repaint();
        });
        ctrlPanel.add(scrollZoomFactor);
        orbitCanvas.setZoomFactor(scrollZoomFactor.getValue());

        //
        // Applet Layout
        //
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);
        gbc.fill = GridBagConstraints.BOTH;

        // Main Panel
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(mainPanel, gbc);
        add(mainPanel);

        // Control Panel
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(6, 0, 0, 0);
        gbl.setConstraints(ctrlPanel, gbc);
        mainPanel.add(ctrlPanel);

        // Player Thread
        orbitPlayer = new OrbitPlayer(this);
        playerThread = null;

        JFrame mainFrame = new JFrame("Celestia orbit");
        mainFrame.setResizable(true);
        mainFrame.setContentPane(mainPanel);
        mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (playerThread != null) {
                    playerThread.interrupt();
                    playerThread = null;
                }
                e.getWindow().setVisible(false);
                e.getWindow().dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        Controller.setFrameForm(mainFrame, 850, 650);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /**
     * Override Function start()
     */
    public void start() {
        // if you want, you can initialize date here
    }

    /**
     * Override Function stop()
     */
    public void stop() {
        if (dateDialog != null) {
            dateDialog.dispose();
            endDateDialog(null);
        }
        if (playerThread != null) {
            playerThread.stop();
            playerThread = null;
            buttonDate.enable();
        }
    }

    /**
     * Destroy the applet
     */
    public void destroy() {
        removeAll();
    }

    /**
     * message sent by DateDialog (when disposed)
     */
    public void endDateDialog(ATime atime) {
        dateDialog = null;
        buttonDate.enable();
        if (atime != null) {
            this.atime = limitATime(atime);
            orbitCanvas.setDate(atime);
            orbitCanvas.repaint();
        }
    }

    public CelestiaAsteroid getCelestiaAsteroid() {
        return celestiaAsteroid;
    }

    public void setCelestiaAsteroid(CelestiaAsteroid celestiaAsteroid) {
        this.celestiaAsteroid = celestiaAsteroid;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getSource());
    }
}