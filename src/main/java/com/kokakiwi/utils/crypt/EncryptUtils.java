package com.kokakiwi.utils.crypt;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtils
{
    private static KeyGenerator     keyAESGenerator;
    private static KeyPairGenerator keyRSAGenerator;
    private static KeyFactory       keyRSAFactory;
    private static Cipher           aesCipher;
    private static Cipher           rsaCipher;
    private static Cipher           blowfishCipher;
    private static SecureRandom     random;
    private static MessageDigest    md5;
    private static MessageDigest    sha;
    
    static
    {
        try
        {
            keyAESGenerator = KeyGenerator.getInstance("AES");
            keyAESGenerator.init(random);
            
            keyRSAGenerator = KeyPairGenerator.getInstance("RSA");
            keyRSAGenerator.initialize(2048);
            
            keyRSAFactory = KeyFactory.getInstance("RSA");
            
            aesCipher = Cipher.getInstance("AES"); // "AES/CBC/PKCS7Padding"
            rsaCipher = Cipher.getInstance("RSA");
            blowfishCipher = Cipher.getInstance("Blowfish");
            
            random = SecureRandom.getInstance("SHA1PRNG");
            md5 = MessageDigest.getInstance("MD5");
            sha = MessageDigest.getInstance("SHA-1");
        }
        catch (final NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (final NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void init()
    {
    }
    
    public static byte[] encodeMD5(byte[] input)
    {
        final byte[] encoded = md5.digest(input);
        return encoded;
    }
    
    public static byte[] encodeSHA1(byte[] input)
    {
        final byte[] encoded = sha.digest(input);
        return encoded;
    }
    
    public static byte[] encryptWithPasswordAES(byte[] input, byte[] password)
    {
        try
        {
            byte[] output = null;
            SecretKeySpec keySpec = new SecretKeySpec(password, "AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, keySpec);
            output = aesCipher.doFinal(input);
            
            return output;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return input;
    }
    
    public static byte[] decryptWithPasswordAES(byte[] input, byte[] password)
    {
        try
        {
            byte[] output = null;
            SecretKeySpec keySpec = new SecretKeySpec(password, "AES");
            aesCipher.init(Cipher.DECRYPT_MODE, keySpec);
            output = aesCipher.doFinal(input);
            
            return output;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return input;
    }
    
    public static byte[] encryptWithPasswordBlowfish(byte[] input,
            byte[] password)
    {
        try
        {
            byte[] output = null;
            SecretKeySpec keySpec = null;
            keySpec = new SecretKeySpec(password, "Blowfish");
            blowfishCipher.init(Cipher.ENCRYPT_MODE, keySpec);
            output = blowfishCipher.doFinal(input);
            
            return output;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return input;
    }
    
    public static byte[] decryptWithPasswordBlowfish(byte[] input,
            byte[] password)
    {
        try
        {
            byte[] output = null;
            SecretKeySpec keySpec = null;
            keySpec = new SecretKeySpec(password, "Blowfish");
            blowfishCipher.init(Cipher.DECRYPT_MODE, keySpec);
            output = blowfishCipher.doFinal(input);
            
            return output;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return input;
    }
    
    public static KeyPair generateKeyPair()
    {
        return keyRSAGenerator.generateKeyPair();
    }
    
    public static SecretKey generateSecretKey()
    {
        return keyAESGenerator.generateKey();
    }
    
    public static byte[] encryptWithKey(byte[] input, PublicKey key)
    {
        try
        {
            rsaCipher.init(Cipher.ENCRYPT_MODE, key);
            final byte[] output = rsaCipher.doFinal(input);
            return output;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return input;
    }
    
    public static byte[] decryptWithKey(byte[] input, PrivateKey key)
    {
        try
        {
            rsaCipher.init(Cipher.DECRYPT_MODE, key);
            final byte[] output = rsaCipher.doFinal(input);
            return output;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return input;
    }
    
    public static KeyGenerator getKeyAESGenerator()
    {
        return keyAESGenerator;
    }
    
    public static KeyPairGenerator getKeyRSAGenerator()
    {
        return keyRSAGenerator;
    }
    
    public static Cipher getAesCipher()
    {
        return aesCipher;
    }
    
    public static Cipher getRsaCipher()
    {
        return rsaCipher;
    }
    
    public static Cipher getBlowfishCipher()
    {
        return blowfishCipher;
    }
    
    public static SecureRandom getRandom()
    {
        return random;
    }
    
    public static MessageDigest getSha()
    {
        return md5;
    }
    
    public static KeyFactory getRSAKeyFactory()
    {
        return keyRSAFactory;
    }
    
}
