package com.pratice.Job_Application_Tracker.exception;

public class MobileAlreadyRegisteredException extends RuntimeException {
    public MobileAlreadyRegisteredException() { super("An account with this mobile number already exists."); }
}
