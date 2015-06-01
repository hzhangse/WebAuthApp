package com.train.HttpClient.digestAuth;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
 
public  class Coder {  
    public static final String KEY_SHA = "SHA";  
    public static final String KEY_MD5 = "MD5";  
 
     
    public static byte[] decryptBASE64(String key) throws Exception {  
        return (new BASE64Decoder()).decodeBuffer(key);  
    }  
 
     
    public static String encryptBASE64(byte[] key) throws Exception {  
        return (new BASE64Encoder()).encodeBuffer(key);  
    }  
 
     
    public static byte[] encryptMD5(byte[] data) throws Exception {  
 
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);  
        md5.update(data);  
 
        return md5.digest();  
 
    }  
 
     
    public static byte[] encryptSHA(byte[] data) throws Exception {  
 
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);  
        sha.update(data);  
 
        return sha.digest();  
 
    }  
    
    public static String md5base64(String s) {
        if (s == null)
            return null;
        String encodeStr = "";
        byte[] utfBytes = s.getBytes();
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(utfBytes);
            byte[] md5Bytes = mdTemp.digest();
            BASE64Encoder b64Encoder = new BASE64Encoder();
            encodeStr = b64Encoder.encode(md5Bytes);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return encodeStr;
    }
    
    @Test 
    public void test() throws Exception {  
        String inputStr = "tomcat";  
        System.err.println("原文:\n\n" + inputStr);  
 
        byte[] inputData = inputStr.getBytes();  
        String code = Coder.encryptBASE64(inputData);  
 
        System.err.println("BASE64加密后:\n\n" + code);  
 
        byte[] output = Coder.decryptBASE64(code);  
 
        String outputStr = new String(output);  
 
        System.err.println("BASE64解密后:\n\n" + outputStr);  
 
        System.out.println(md5base64("tomcat"));
        System.out.println(MD5Object.encrypt("tomcat"));
        byte[] md5= Coder.encryptMD5(inputData);
        String md5_base64 = Coder.encryptBASE64(md5);  
        System.out.println("md5_base64:"+md5_base64);
//        // 验证BASE64加密解密一致性  
        assertEquals(inputStr, outputStr);  
// 
//        // 验证MD5对于同一内容加密是否一致  
       assertArrayEquals(Coder.encryptMD5(inputData), Coder  
                .encryptMD5(inputData));  
// 
//        // 验证SHA对于同一内容加密是否一致  
        assertArrayEquals(Coder.encryptSHA(inputData), Coder  
               .encryptSHA(inputData));  
    }  
} 