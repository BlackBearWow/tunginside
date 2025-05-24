package uman.tunginside.exception;

public class NewPasswordException extends RuntimeException {
    public NewPasswordException() { }
    public NewPasswordException(String message) { super(message); }
}
