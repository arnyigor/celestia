package com.arny.celestiatools.utils;

import io.reactivex.*;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static String trimInside(String text) {
        return text.trim().replace(" ", "");
    }

    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Remove the file extension from a filename, that may include a path.
     * <p>
     * e.g. /path/to/myfile.jpg -> /path/to/myfile
     */
    public static String removeExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        if (index == -1) {
            return filename;
        } else {
            return filename.substring(0, index);
        }
    }

    /**
     * Return the file extension from a filename, including the "."
     * <p>
     * e.g. /path/to/myfile.jpg -> .jpg
     */
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }

        int index = indexOfExtension(filename);

        if (index == -1) {
            return filename;
        } else {
            return filename.substring(index);
        }
    }

    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        }
        int extensionPos = filename.lastIndexOf(".");
        int lastDirSeparator = filename.lastIndexOf("/");
        if (lastDirSeparator > extensionPos) {
            return -1;
        }
        return extensionPos;
    }

    public static ArrayList<String> sortDates(ArrayList<String> dates, final String format) {
        Collections.sort(dates, new Comparator<String>() {
            private SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

            @Override
            public int compare(String o1, String o2) {
                int result = -1;
                try {
                    result = sdf.parse(o1).compareTo(sdf.parse(o2));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                return result;
            }
        });
        return dates;
    }

    public static boolean matcher(String regex, String string) {
        return Pattern.matches(regex, string);
    }

    public static String match(String where, String pattern, int groupnum) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(where);
        while (m.find()) {
            if (!m.group(groupnum).equals("")) {
                return m.group(groupnum);
            }
        }
        return null;
    }

    public static int[] bubbleSort(int[] arr) {
        for (int i = arr.length - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int t = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = t;
                }
            }
        }
        return arr;
    }

    public static boolean empty(Object obj) {
        if (obj == null) {
            return true;
        } else {
            if (obj instanceof String) {
                String s = (String) obj;
                return s.trim().equals("null") || s.trim().isEmpty();
            } else if (obj instanceof List) {
                return ((List) obj).isEmpty();
            } else {
                return false;
            }
        }
    }

    public static Float[] interpolate(float oldCnt, float newcnt, int cnt) {
        float diff = newcnt - oldCnt;
        float onePoint = diff / cnt;
        float current = oldCnt;
        ArrayList<Float> arr = new ArrayList<>();
        while (true) {
            arr.add(current);
            current = current + onePoint;
            if (current >= newcnt) {
                break;
            }
        }
        arr.add(newcnt);

        Float[] tointer = new Float[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            tointer[i] = arr.get(i);
        }
        return tointer;
    }

    public static <T> boolean contains(ArrayList<T> array, T v) {
        for (T e : array) {
            if (v.equals(e)) {
                return true;
            }
        }
        return false;
    }

    public static void iterHashMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
        }
    }

    public static String stringContains(String where, String[] cases, String[] answers) {
        for (int i = 0; i < cases.length; i++) {
            if (where.contains(cases[i])) return answers[i];
        }
        return where;
    }

    public static <T> boolean contains(final T[] array, T v) {
        for (T e : array) {
            if (v.equals(e)) {
                return true;
            }
        }
        return false;
    }

    public static <V, T> ArrayList<T> getValuesFromMap(HashMap<V, T> hashMap) {
        ArrayList<T> list = new ArrayList<>();
        for (Map.Entry<V, T> entry : hashMap.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public static <V, T> ArrayList<V> getKeysFromMap(HashMap<V, T> map) {
        ArrayList<V> list = new ArrayList<>();
        for (Map.Entry<V, T> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    public static String getFields(Object o) {
        Collection<Field> fields = getFields(o.getClass());
        StringBuilder builder = new StringBuilder();
        builder.append(o.getClass().getSimpleName()).append("(");
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String msg = field.getName() + ":'" + field.get(o) + "'(" + field.getType().getSimpleName() + ");";
                builder.append(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }
        builder.append(") \n");
        return builder.toString();
    }

    /**
     * Get all fields of a class.
     *
     * @param clazz The class.
     * @return All fields of a class.
     */
    public static Collection<Field> getFields(Class<?> clazz) {
        Map<String, Field> fields = new HashMap<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.getName().equalsIgnoreCase("shadow$_klass_") && !field.getName().equalsIgnoreCase("serialVersionUID") && !field.getName().equalsIgnoreCase("$change") && !field.getName().equalsIgnoreCase("shadow$_monitor_")) {
                    if (!fields.containsKey(field.getName())) {
                        fields.put(field.getName(), field);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields.values();
    }

    public static String getFields(Object cls, String[] include) {
        Collection<Field> fields = getFields(cls.getClass());
        StringBuilder builder = new StringBuilder();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String msg = "\n" + field.getName() + ":" + field.get(cls) + "; ";
                for (String s : include) {
                    if (s.equalsIgnoreCase(field.getName())) {
                        builder.append(msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }
        return builder.toString();
    }


    @NonNull
    public static <T> ArrayList<T> getListCopy(List<T> list) {
        ArrayList<T> arrayList = new ArrayList<>();
        arrayList.addAll(list);
        ArrayList<T> listCopy = new ArrayList<>(arrayList.size());
        listCopy.addAll((ArrayList<T>) arrayList.clone());
        Collections.copy(listCopy, list);
        return listCopy;
    }

    public static String listToString(List<String> strings) {
        StringBuilder res = new StringBuilder();
        boolean first = true;
        for (String s : strings) {
            if (!first) {
                res.append(",");
                first = false;
            }
            res.append(s);
        }
        return res.toString();
    }

    public static <T> String objectsListToString(List<T> tList) {
        StringBuilder res = new StringBuilder();
        for (T s : tList) {
            res.append(s.toString());
            res.append(",");
        }
        res.delete(res.length() - 1, res.length());
        return res.toString();
    }

    public static <T> ArrayList<T> getExcludeList(ArrayList<T> list, List<T> items, Comparator<T> comparator) {
        ArrayList<T> res = new ArrayList<>();
        for (T t : list) {
            int pos = Collections.binarySearch(items, t, comparator);
            if (pos < 0) {
                res.add(t);
            }
        }
        return res;
    }

    public static String getThread() {
        return Thread.currentThread().getName();
    }

}
