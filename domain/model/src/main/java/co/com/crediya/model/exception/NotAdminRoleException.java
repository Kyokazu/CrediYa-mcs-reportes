package co.com.crediya.model.exception;

public class NotAdminRoleException extends RuntimeException {
    public NotAdminRoleException(String message) {
        super(message);
    }
}
