package com.murey.poster.postermaster.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils extends android.text.format.DateUtils{

    public static final String FORMAT_DATE_POSTER_MASTER = "yyyy-MM-dd'T'kk:mm:ssZ";
    public static final String FORMAT_DATE_FROM_ESEO = "yyyy-MM-dd";
    public static final String FORMAT_DATE_DEFAULT_SESSION = "yyyy_MM_dd_kk:mm:ss";
    public static final String FORMAT_DATE_GMT_MIDNIGHT = "yyyy-MM-dd'T'00:00:00Z";

    private final static String FRENCH_DATE_FORMAT = "ccc dd MMM yyyy";

    public static final String FORMAT_DATE_COMPARE_DAY = "yyyyMMdd";

    public static Date getDateFromString(String strDate, String strFormat){
        DateFormat format = new SimpleDateFormat(strFormat, Locale.ENGLISH);
        try {
            return format.parse(strDate);
        } catch (ParseException e) {
            LogUtils.e(LogUtils.DEBUG_TAG,"Error !",e);
            return null;
        }
    }

    public static String convertTimeToHourMinSecond(Calendar c1, Calendar c2){
        long elapsedTime = c2.getTimeInMillis() - c1.getTimeInMillis();
        return convertSecondsToFormatApp((int)elapsedTime/1000);
    }

    public static String getFriendlyFromDateString(String alertDate, String strFormat){
        Date date = getDateFromString(alertDate, strFormat);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.FRENCH_DATE_FORMAT, Locale.FRANCE);
        return dateFormat.format(date);
    }

    public static String convertSecondsToFormatApp(int timeInSeconds){
        int hours = timeInSeconds / 3600;
        int minutes = (timeInSeconds % 3600) / 60;
        int seconds = timeInSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String getStringFromDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATE_POSTER_MASTER, Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public static String getStringFromTimeInMillis(long timeInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return getStringFromDate(calendar.getTime());
    }

    public static String getEveStringFromDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK,-1);
        date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATE_POSTER_MASTER, Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public static String getStringFromDateGMTmidnight(Date date){
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat(DateUtils.FORMAT_DATE_GMT_MIDNIGHT, Locale.ENGLISH);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatGmt.format(date);
    }

    public static Calendar getCalendarFromDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static boolean isSameDay(Date date1, Date date2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return  cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isSameMonth(Date date1, Date date2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return  cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    public static Integer getHourOfTheDateGMT(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTime(date);
        // gets hour in 24h format
        calendar.get(Calendar.HOUR_OF_DAY);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static long daysBetween(Date startDate, Date endDate) {
        Calendar sDate = Calendar.getInstance();
        sDate.setTime(startDate==null ? new Date() : startDate);
        Calendar eDate = Calendar.getInstance();
        eDate.setTime(endDate);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_YEAR, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    /**
     * Converts a Date object into a french style date like "Lun. 12 Jun 2016"
     * @param date the Date object to format
     * @return the formatted String into french style date
     */
    public static String parseDateToFrenchString(Date date) {
        String strDate = DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE).format(date);
        String[] strDateWords = strDate.split(" ");
        strDate = "";
        for(int i=0;i<strDateWords.length;i++){
            strDateWords[i] = strDateWords[i].substring(0, 1).toUpperCase() + strDateWords[i].substring(1);
            strDate+=strDateWords[i]+" ";
        }
        return strDate;
    }

    public static String getStringMonthAndYearFromNumbers(int monthNumber, int yearNumber){
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.MONTH,monthNumber-1);
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.FRANCE);
        String monthName = month_date.format(cal.getTime());
        return monthName.substring(0, 1).toUpperCase() + monthName.substring(1) + " " + yearNumber;
    }

    public static String convertTimeDefaultSessionTime(Calendar endInstant){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATE_DEFAULT_SESSION, Locale.FRANCE);
        return dateFormat.format(endInstant.getTime());
    }
}