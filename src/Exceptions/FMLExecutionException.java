package Exceptions;

public class FMLExecutionException extends Exception {

    public FMLExecutionException(String msg) {
        super(msg);
    }

    public FMLExecutionException(String msg, Throwable e) {
        super(msg, e);
    }
}
