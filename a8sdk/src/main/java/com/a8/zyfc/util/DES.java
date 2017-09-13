package com.a8.zyfc.util;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DES {  
	
	public static final String PASSWORD_CRYPT_KEY = "96818968";
    private static byte[] iv = {1,2,3,4,5,6,7,8};  
    
    public static String encrypt(String encryptString, String encryptKey) {  
//      IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);   
        IvParameterSpec zeroIv = new IvParameterSpec(iv);  
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");  

		try {
			 Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			 cipher.init(Cipher.ENCRYPT_MODE, key);  
		     byte[] encryptedData = cipher.doFinal(encryptString.getBytes());  
		     return Base64.encode(encryptedData);  
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
        
       
       
    }  
    public static String decrypt(String decryptString, String decryptKey) {  
        byte[] byteMi = new Base64().decode(decryptString);  
        IvParameterSpec zeroIv = new IvParameterSpec(iv);  
//      IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);   
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");     
        try {
        	 Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");  
             cipher.init(Cipher.DECRYPT_MODE, key);  
             byte decryptedData[] = cipher.doFinal(byteMi);  
            
             return new String(decryptedData);  
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
    }  
}  