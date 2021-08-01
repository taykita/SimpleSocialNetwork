package source.exception;

public class SSNException extends Exception{
    public SSNException(String msg, Exception e) {
        super(msg, e);
    }
    public SSNException(String msg) {
        super(msg);
    }
    public SSNException(Exception e) {
        super(e);
    }

}
