package Exceptions;

public class FMLParsingException extends Exception {

    public FMLParsingException(String msg) {
        super(msg);
    }

    public FMLParsingException(String msg, Throwable t) {
        super(msg, t);
    }
}
