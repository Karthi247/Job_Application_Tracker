package com.pratice.Job_Application_Tracker.exception;

public class InvalidCurrentPasswordException extends RuntimeException {
    public InvalidCurrentPasswordException() { super("Current password is incorrect."); }
}
