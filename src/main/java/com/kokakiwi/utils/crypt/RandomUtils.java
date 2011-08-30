package com.kokakiwi.utils.crypt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class RandomUtils
{
    // private static SecureRandom random;
    private static Random        random;
    private static MessageDigest sha;
    
    static
    {
        try
        {
            // If I use SecureRandom instead, the process time will be MORE
            // longer (like 50s)
            // random = SecureRandom.getInstance("SHA1PRNG");
            random = new Random();
            sha = MessageDigest.getInstance("MD5");
        }
        catch (final NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }
    
    public static Random getRandom()
    {
        return random;
    }

    public static MessageDigest getSha()
    {
        return sha;
    }

    public static void init()
    {
    }
    
    public static String generateString(int length)
    {
        final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTVWXYZ";
        return generateString(chars, length);
    }
    
    public static String generateString(String chars, int length)
    {
        final int size = chars.length() - 1;
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            final int idx = (int) Math.round(Math.random() * size);
            sb.append(chars.charAt(idx));
        }
        
        return sb.toString();
    }
    
    public static String generateID()
    {
        final byte[] bytes = new byte[128];
        random.nextBytes(bytes);
        final byte[] encoded = sha.digest(bytes);
        final String out = new BigInteger(encoded).toString(16);
        return out;
    }
}
