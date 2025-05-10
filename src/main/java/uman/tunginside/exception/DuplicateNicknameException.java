package uman.tunginside.exception;

public class DuplicateNicknameException extends RuntimeException {
    public DuplicateNicknameException() {}
    public DuplicateNicknameException(String message) { super(message); }
}
