package ro.unibuc.master.groupexpensetracker.exception;

public class IllegalExpensiveException extends RuntimeException {

    public IllegalExpensiveException(String message) {
        super(message);
    }
}