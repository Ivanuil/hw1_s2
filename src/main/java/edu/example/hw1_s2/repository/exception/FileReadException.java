package edu.example.hw1_s2.repository.exception;

public class FileReadException extends Exception {

    public FileReadException() {
    }

    public FileReadException(String message) {
        super(message);
    }

    public FileReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileReadException(Throwable cause) {
        super(cause);
    }
}