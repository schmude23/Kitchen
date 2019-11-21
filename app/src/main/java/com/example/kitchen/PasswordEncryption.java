package com.example.kitchen;

import java.math.BigInteger;
import java.security.MessageDigest;

public class PasswordEncryption {
    private static byte[] encryptMD5(byte[] data) throws Exception {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();

    }

    public String main(String password){
        byte[] md5Input = password.getBytes();
        BigInteger md5Data = null;

        try {
            md5Data = new BigInteger(1,encryptMD5(md5Input));
        }
        catch(Exception e){
            return null;
        }
        String md5Str = md5Data.toString(16);
        if(md5Str.length() < 32){
            md5Str = 0 + md5Str;
        }
        return md5Str;
    }
}