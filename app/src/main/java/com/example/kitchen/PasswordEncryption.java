package com.example.kitchen;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;


//TODO: how to doccument used code from https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
public class PasswordEncryption
{
    public static String main(String password) throws NoSuchAlgorithmException, NoSuchProviderException
    {
        String passwordToHash = password;
        byte[] salt = getSalt();

        //String securePassword = getSecurePassword(passwordToHash, salt);
        //System.out.println(securePassword); //Prints 83ee5baeea20b6c21635e4ea67847f66

        //String regeneratedPassowrdToVerify = getSecurePassword(passwordToHash, salt);
       // System.out.println(regeneratedPassowrdToVerify); //Prints 83ee5baeea20b6c21635e4ea67847f66

        return getSecurePassword(password, salt);
    }

    private static String getSecurePassword(String passwordToHash, byte[] salt)
    {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(salt);
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //hex formatted hashed password
            generatedPassword = stringBuilder.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Add salt
    private static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        //SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }
}
