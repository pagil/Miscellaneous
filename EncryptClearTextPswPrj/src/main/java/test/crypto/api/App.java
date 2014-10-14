package test.crypto.api;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;

/**
 * See JCE specification for more details.<br>
 * https://www.owasp.org/index.php/How_to_encrypt_a_properties_file
 * 
 */
public class App {

    private static sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
    private static sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    private Cipher encrypter, decrypter;
    // Make up your own array of bytes
    private static byte[] salt = { -11, -117, 93, -127, -101, -70, 64, 24 };

    public App(String password) throws Exception {
        PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(salt, 20);
        SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(password.toCharArray()));
        encrypter = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
        decrypter = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
        encrypter.init(Cipher.ENCRYPT_MODE, k, ps);
        decrypter.init(Cipher.DECRYPT_MODE, k, ps);
    }

    private synchronized String decrypt(String str) throws Exception {
        byte[] dec = decoder.decodeBuffer(str);
        byte[] utf8 = decrypter.doFinal(dec);
        return new String(utf8, "UTF-8");
    }

    private synchronized String encrypt(String str) throws Exception {
        byte[] utf8 = str.getBytes("UTF-8");
        byte[] enc = encrypter.doFinal(utf8);
        return encoder.encode(enc);
    }

    // usage: java EncryptedProperties test.properties password
    public static void main(String[] args) throws Exception {

        // System.console().readPassword();
        String value = "value";
        App instance = new App("password");
        String cipher = instance.encrypt(value);
        System.out.println(cipher);
        value = instance.decrypt(cipher);
        System.out.println(value);
    }
}
