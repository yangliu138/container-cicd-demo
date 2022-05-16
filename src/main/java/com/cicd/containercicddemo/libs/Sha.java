package com.cicd.containercicddemo.libs;

import java.security.NoSuchAlgorithmException;

public interface Sha {
    public String getShaString(String input) throws NoSuchAlgorithmException;
}
