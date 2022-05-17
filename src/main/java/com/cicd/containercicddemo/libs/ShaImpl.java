package com.cicd.containercicddemo.libs;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaImpl implements Sha {
    private static final int STRING_RADIX = 16;
    private static final int NUM_HEX_LEADING_ZEROS = 64;

    public byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(ShaImpl.STRING_RADIX));

        // Pad with leading zeros
        while (hexString.length() < ShaImpl.NUM_HEX_LEADING_ZEROS) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    @Override
    public String getShaString(String input) throws NoSuchAlgorithmException {
        return toHexString(getSHA(input));
    }
}
