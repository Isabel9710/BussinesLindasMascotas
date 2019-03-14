
package co.com.bussineslm.utilities;

//Esta clase es para encriptar las contraseñas y token.

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

public class Encrypter {
    private final static String ALG = "AES";
    private final static String CI = "AES/CBC/PKCS5Padding";
    
    private final static String KEY = "33C0Ksl;HP?{@FW";
    private final static String IV = "9FpL%Qm.1iPO~}C";
    
    public static String encrypt(String string) throws Exception{
        Cipher cipher = Cipher.getInstance(CI);
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALG);
        IvParameterSpec spec = new IvParameterSpec(IV.getBytes());
        
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);
        byte encrypted[] = cipher.doFinal(string.getBytes());
        
        return new String(encodeBase64(encrypted));
    }
    
    public static String decrypt(String encrypted) throws Exception{
        Cipher cipher = Cipher.getInstance(CI);
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALG);
        IvParameterSpec spec = new IvParameterSpec(IV.getBytes());
        
        byte enc[] = decodeBase64(encrypted);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);
        byte decrypted[] = cipher.doFinal(enc);
        
        return new String(decrypted);
    }   
}
