package ro.unibuc.master.groupexpensetracker.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class StringUtils {
    public static final String SIMPLE_EXPENSE = "Simple Expense";
    public static final String GROUP_EXPENSE = "Group Expense";
    public static final String INITIAL_GROUP_EXPENSE = "Initial Group Expense";
    public static final String COLLECT_EXPENSE = "Collect Expense";
    public static final String INITIAL_COLLECT_EXPENSE = "Initial Collect Expense";
    public static final String FINAL_COLLECT_EXPENSE = "Final Collect Expense";

    public static LocalDate convertStringToDate(String stringDate) {
        String datePattern = "yyyy-mm-dd";
//        String datePattern = "dd-M-yyyy";
        DateFormat format = new SimpleDateFormat(datePattern);
        try {
            return format.parse(stringDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (Exception e) {
            throw new IllegalArgumentException("Filter " + stringDate + " is not like this pattern " + datePattern);
        }
    }

    public static LocalDateTime convertStringToDateTime(String stringDate) {
        String datePattern = "yyyy-MM-dd HH:mm";
        DateFormat format = new SimpleDateFormat(datePattern);
        try {
            return format.parse(stringDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e) {
            throw new IllegalArgumentException("Filter " + stringDate + " is not like this pattern " + datePattern);
        }
    }


}
