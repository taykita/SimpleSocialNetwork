package source.exception;

public class AccStorageException extends SSNException {
    public AccStorageException(String msg, Exception e) {
        super(msg, e);
    }

    public AccStorageException(String msg) {
        super(msg);
    }
}
