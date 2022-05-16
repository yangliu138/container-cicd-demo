package com.cicd.containercicddemo.libs;

import java.security.NoSuchAlgorithmException;

public interface Sha {
    String getShaString(String input) throws NoSuchAlgorithmException;
}
