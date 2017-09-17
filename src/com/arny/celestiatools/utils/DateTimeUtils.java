package com.arny.celestiatools.utils;


import com.arny.celestiatools.utils.BaseUtils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

public class DateTimeUtils {

    public static final String[] RU_MONTHES_FULL = new String[]{"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    public static final String[] RU_MONTHES_LO = new String[]{"дек","янв","фев","мар","апр","май","июн","июл","авг","сен","окт","ноя"};
    public static final String[] RU_MONTHES_UP = new String[]{"Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"};
    private static final String TIME_SEPARATOR_TWICE_DOT = ":";
    private static final String TIME_SEPARATOR_DOT = ".";


    private static Locale getLocale(String myTimestamp) {
        boolean isUS = Pattern.matches("(?i).*[A-z]+.*", myTimestamp);
        return isUS? Locale.US: Locale.getDefault();
    }

    public static String dateFormatChooser(String myTimestamp) {
        HashMap<String, String> pregs = new HashMap<>();
        pregs.put("^\\d{1,2}\\.\\d{2}\\.\\d{4}$", "dd.MM.yyyy");
        pregs.put("^\\d{1,2}\\.\\d{2}\\.\\d{2}$", "dd.MM.yy");
        pregs.put("^\\d{1,2}\\-\\D+\\-\\d{2}$", "dd-MMM-yy");
        pregs.put("^\\d{1,2}\\-\\D+\\-\\d{4}$", "dd-MMM-yyyy");
        pregs.put("^\\d{1,2}\\s+\\D+\\s\\d{2}$", "dd MMM yy");
        pregs.put("^\\d{1,2}\\s+\\D+\\s+\\d{4}$", "dd MMM yyyy");
        pregs.put("^\\d{1,2}\\s+\\d{2}+\\s\\d{2}$", "dd MM yy");
        String format = "dd MMM yyyy";
        for (HashMap.Entry<String, String> entry : pregs.entrySet()) {
            boolean matches = Pattern.matches(entry.getKey(), myTimestamp);
            if (matches) {
                return entry.getValue();
            }
        }
        return format;
    }

    public static String getDateTime(long milliseconds, String format) {
        milliseconds = (milliseconds == 0) ? Calendar.getInstance().getTimeInMillis() : milliseconds;
        format = (format == null) ? "dd MMM yyyy HH:mm:ss.sss" : format;
        return (new SimpleDateFormat(format, Locale.getDefault())).format(new Date(milliseconds));
    }

    public static String getDateTime(long milliseconds) {
        milliseconds = (milliseconds == 0) ? Calendar.getInstance().getTimeInMillis() : milliseconds;
        return (new SimpleDateFormat("dd MMM yyyy HH:mm:ss.sss", Locale.getDefault())).format(new Date(milliseconds));
    }

    public static String getDateTime() {
        return (new SimpleDateFormat("dd MMM yyyy HH:mm:ss.sss", Locale.getDefault())).format(new Date(System.currentTimeMillis()));
    }

    public static String getDateTime(String format) {
        return (new SimpleDateFormat(format, Locale.getDefault())).format(new Date(System.currentTimeMillis()));
    }

    private static DateFormatSymbols myDateFormatSymbolsFull = new DateFormatSymbols(){
        @Override
        public String[] getMonths() {
            return RU_MONTHES_FULL;
        }
    };

    private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols(){
        @Override
        public String[] getMonths() {
            return RU_MONTHES_LO;
        }
    };

    private static DateFormatSymbols myDateFormatSymbolsUp = new DateFormatSymbols(){
        @Override
        public String[] getMonths() {
            return RU_MONTHES_UP;
        }
    };


    public static long convertTimeStringToLong(String myTimestamp, String format) {
        DateFormatSymbols formatSimbols = getFormatString(myTimestamp);
        Locale locale = getLocale(myTimestamp);
        SimpleDateFormat formatter = new SimpleDateFormat(format,locale);
        if (locale.getISO3Language().equalsIgnoreCase("rus")) {
            formatter.setDateFormatSymbols(formatSimbols);
        }
        Date date;
        try {
            date = formatter.parse(myTimestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        return date.getTime();
    }

    public static long convertTimeStringToLong(String myTimestamp) {
        DateFormatSymbols formatSimbols = getFormatString(myTimestamp);
        Locale locale = getLocale(myTimestamp);
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormatChooser(myTimestamp),locale);
        if (locale.getCountry().equals("RU")) {
            formatter.setDateFormatSymbols(formatSimbols);
        }
        Date date;
        try {
            date = formatter.parse(myTimestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        return date.getTime();
    }

    private static DateFormatSymbols getFormatString(String myTimestamp) {
        DateFormatSymbols formatSymbols = myDateFormatSymbolsFull;
        for (String s : RU_MONTHES_LO) {
            if (BaseUtils.matcher(".*" + s + ".*", myTimestamp)) {
                return myDateFormatSymbols;
            }
        }
        for (String s : RU_MONTHES_UP) {
            if (BaseUtils.matcher(".*" + s + ".*", myTimestamp)) {
                return myDateFormatSymbolsUp;
            }
        }
        return formatSymbols;
    }

    /**
     * @param date
     * @param format
     * @return String datetime
     */
    public static String getDateTime(Date date, String format) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            long milliseconds = calendar.getTimeInMillis();
            format = (format == null || format.trim().equals("")) ? "dd MMM yyyy HH:mm:ss.sss" : format;
            return (new SimpleDateFormat(format, Locale.getDefault())).format(new Date(milliseconds));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String strLogTime(int logtime) {
        int h = logtime / 60;
        int m = logtime % 60;
        return pad(h) + TIME_SEPARATOR_TWICE_DOT + pad(m);
    }

    public static int convertStringToTime(String time) {
        int hours = 0;
        int mins = 0;
        String delimeter = (time.contains(TIME_SEPARATOR_TWICE_DOT)) ? TIME_SEPARATOR_TWICE_DOT : TIME_SEPARATOR_DOT;
        int posDelim = time.indexOf(delimeter);
        try {
            hours = Integer.parseInt(time.substring(0, posDelim));
            mins = Integer.parseInt(time.substring(posDelim + 1, time.length()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mins + (hours * 60);
    }

    /**
     * add '0' to number before 10
     *
     * @param number
     * @return
     */
    public static String pad(int number) {
        if (number >= 10) {
            return String.valueOf(number);
        } else {
            return "0" + String.valueOf(number);
        }
    }

    /**
     * получение времени формата HH:mm:ss из HH.hh
     * @param time
     * @return
     */
    public static String getTimeHHmmss(double time){
        int hh = (int) time;
        double Mm = (time - hh)*60;
        int mm = (int) Mm;
        int ss = (int)(Mm - mm) * 60;
        return hh + ":" + hh + ":" + hh;
    }


    public static String getStringDateTime(int year, int monthOfYear, int dayOfMonth) {
        String strDateFormat = "MMM";
        String strMonth = new DateFormatSymbols().getMonths()[monthOfYear];
        Date date = null;
        try {
            date = new SimpleDateFormat(strDateFormat, Locale.getDefault()).parse(strMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formDate = new SimpleDateFormat("MMM", Locale.getDefault()).format(date);
        return dayOfMonth + " " + formDate + " " + year;
    }

    public static int getYear(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.YEAR);
    }

    public static int getHour(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMin(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.MINUTE);
    }

    public static int getSec(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.SECOND);
    }

    public static int getMs(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.MILLISECOND);
    }

    public static int getDayofMonth(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayofYear(long time) {
        return Integer.parseInt(getDateTime(time,"dd"));
    }

    public static int getMonth(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static String getStrMonth(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);
        String strDateFormat = "MMM";
        String strM = new DateFormatSymbols().getMonths()[m];
        Date dat = null;
        try {
            dat = new SimpleDateFormat(strDateFormat, Locale.getDefault()).parse(strM);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String mMonth = new SimpleDateFormat("MMM", Locale.getDefault()).format(dat);
        return mMonth + " " + y;
    }

    public static String getStrDate(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);
        String strDateFormat = "MMM";
        String strM = new DateFormatSymbols().getMonths()[m];
        Date dat = null;
        try {
            dat = new SimpleDateFormat(strDateFormat, Locale.getDefault()).parse(strM);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String fDate = new SimpleDateFormat("MMM", Locale.getDefault()).format(dat);
        return d + " " + fDate + " " + y;
    }

    public static long addDays(long date,int dayCnt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.add(Calendar.DATE, dayCnt);
        return calendar.getTimeInMillis();
    }

    public static String getStrTime(long timestamp) {
        Date d = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm", Locale.getDefault());
        return format.format(d);
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);
        String strDateFormat = "MMM";
        String strM = new DateFormatSymbols().getMonths()[m];
        Date dat = null;
        try {
            dat = new SimpleDateFormat(strDateFormat, Locale.getDefault()).parse(strM);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String fDate = new SimpleDateFormat("MMM", Locale.getDefault()).format(dat);
        return d + " " + fDate + " " + y;
    }
}
