//WILLIAM KUREK
//CS 1501
//SUMMER 2017
import java.util.*;

public class Substitute implements SymCipher {

    private byte[] key;

    public Substitute() {
        key = new byte[256];
        
        for (int i = 0; i < 256; i++) { //initialize byte array with index numbers
            key[i] = (byte) i;
        }
        randomizeArray(key);
    }

    public Substitute(byte[] k) {
        key = k;
    }

    
    public byte[] encode(String S) {
        byte[] msg = S.getBytes();
        byte[] encrypt = new byte[msg.length];

        for (int i = 0; i < msg.length; i++) {
            //ensure current index is valid value, then substitute
            encrypt[i] = key[msg[i] & 0xff];
        }
        return encrypt;
    }

    public String decode(byte[] bytes) {     
        byte[] decrypt = new byte[bytes.length];
        byte[] inverse = new byte[256];
               
        for (int i = 0; i < key.length; i++) { //inverse mapping
            inverse[key[i] & 0xff] = (byte) i;
        }
        
        for (int i = 0; i < bytes.length; i++) { //use inverse mapping to reverse substitution
            decrypt[i] = inverse[bytes[i] & 0xff];
        }
        
        return new String(decrypt); //convert bytes to string and return 
    }
    
    public byte[] getKey() {
        return key;
    }

    private void randomizeArray(byte[] bytes) { //randomize byte array
        Random rand = new Random();
        int length = (bytes.length - 1);
        for (int i = length; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            byte temp = bytes[index];
            bytes[index] = bytes[i];
            bytes[i] = temp;
        }
    }
}
