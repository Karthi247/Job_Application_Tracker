package com.pratice.Job_Application_Tracker.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource) { super(resource + " was not found."); }
}
