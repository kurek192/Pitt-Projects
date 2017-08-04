//WILLIAM KUREK
//CS 1501
//SUMMER 2017
import java.util.*;
import java.security.SecureRandom;

public class Add128 implements SymCipher {

    private byte[] key;

    public Add128() {
        key = new byte[128];
        Random rand = new SecureRandom();
        rand.nextBytes(key);  //random keys
    }

    public Add128(byte[] k) {
        key = k;
    }
  
    public byte[] encode(String S) {
        byte[] msg = S.getBytes(); //array of key from msg
        byte[] encrypt = new byte[msg.length];
        int index = 0;
        
        for (int i = 0; i < msg.length; i++) { //iterate through the msg and add the key value
            if (index >= key.length) {
                index = 0;
            } else {
                encrypt[i] = (byte) (msg[i] + key[i]);
                index++;
            }
        }
        return encrypt;
    }

    public String decode(byte[] bytes) {
        byte[] decrypt = new byte[bytes.length];
        int index = 0;

       
        for (int i = 0; i < bytes.length; i++) {  //iterate through the msg and subtract the key value
            if (index >= key.length) {
                index = 0;
            } else {
                decrypt[i] = (byte) (bytes[i] - key[i]);
                index++;
            }
        }
        return new String(decrypt);
    }
    
    public byte[] getKey() {
        return key;
    }

}
