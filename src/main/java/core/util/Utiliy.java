package core.util;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

public class Utiliy {
    private static Utiliy utiliy = null;

    public static Utiliy instance() {
        if (utiliy == null) utiliy = new Utiliy();
        return utiliy;
    }

    // 首字母转大写
    public String ucfirst(String word) {
        if (Character.isUpperCase(word.charAt(0)))
            return word;
        else
            return (new StringBuilder()).append(Character.toUpperCase(word.charAt(0))).append(word.substring(1))
                    .toString();
    }

    // 首字母小写
    public String lcfirst(String word) {
        if (Character.isLowerCase(word.charAt(0)))
            return word;
        else
            return (new StringBuilder()).append(Character.toLowerCase(word.charAt(0))).append(word.substring(1))
                    .toString();
    }

    public String getTodayDate() {
        return this.formatDate("yyyy-MM-dd HH:mm:ss");
    }
    
    public Date strToSqlDate(String date_string) {
    	return java.sql.Date.valueOf(date_string);
    }
    public Timestamp strToSqlTimestamp(String date_string) {
    	return java.sql.Timestamp.valueOf(date_string);
    }
    public Date getDate(Integer add_day) {
        Date date = new Date();//outputting the date in user reading format.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // adding  day
        calendar.add(Calendar.DATE, add_day);// getting the new date from the calendar
        Date datetime = calendar.getTime();     
        return datetime;
    }
    
    public java.sql.Date getDate(Date day) {
        return new java.sql.Date(day.getTime());
    }
    
    public String formatDate(Date date, String formatData) {//formatData = "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat format = new SimpleDateFormat(formatData);//
        String dateStr = format.format(date);
        return dateStr;
    }
    
    public String formatDate(String formatData) {
        Date date = new Date();//formatData = "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat format = new SimpleDateFormat(formatData);//
        String dateStr = format.format(date);
        return dateStr;
    }

    /*
     *
     */
    public Map<String, Object> getRequestMap(HttpServletRequest request) {
        Map<String, Object> caseInsensitiveMap = new CaseInsensitiveMap<String, Object>();
        for (Enumeration<String> requestNames = request.getParameterNames(); requestNames.hasMoreElements(); ) {
            String key = requestNames.nextElement();
            caseInsensitiveMap.put(key, request.getParameter(key));
        }
        return caseInsensitiveMap;
    }

    /*
     * Map<String, Object> caseInsensitiveMap = new CaseInsensitiveMap();
     */
    public <T> T executeResultSet(Class<T> classT, Map<String, Object> setMap) throws Exception {
        T objectT = classT.getDeclaredConstructor().newInstance();
        // Field[] fields = classT.getFields();
        Field[] fields = classT.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if (setMap.containsKey(fieldName)) {
                Object value = setMap.get(fieldName);
                field.setAccessible(true);
                field.set(objectT, value);
            }
        }
        return objectT;
    }
}
