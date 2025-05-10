package uman.tunginside.exception;

public class DuplicateUseridException extends RuntimeException{
    public DuplicateUseridException() {}
    public DuplicateUseridException(String message) {super(message);}
}
