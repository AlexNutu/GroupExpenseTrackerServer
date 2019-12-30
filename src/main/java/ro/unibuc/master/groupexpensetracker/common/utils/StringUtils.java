package ro.unibuc.master.groupexpensetracker.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class StringUtils {
    public static final String SIMPLE_EXPENSE = "Simple Expense";
    public static final String GROUP_EXPENSE = "Group Expense";
    public static final String COLLECT_EXPENSE = "Collect Expense";

    public static LocalDate convertStringToDate(String stringDate) {
        String datePattern = "dd-M-yyyy";
        DateFormat format = new SimpleDateFormat(datePattern);
        try {
            return format.parse(stringDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (Exception e) {
            throw new IllegalArgumentException("Filter " + stringDate + " is not like this pattern " + datePattern);
        }
    }
}
