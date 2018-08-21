package wallet.bitcoin.bitcoinwallet.helper;

import android.util.Base64;

public class Mocoder {

    //TODO move to file, not included in apk
    public static String encode(String str){
        byte[] encodeValue = Base64.encode(str.getBytes(), Base64.DEFAULT);
        for (int i = 0; i < encodeValue.length; ++i){
            encodeValue[i] += 1;
        }

        return new String(encodeValue);
    }

    public static String decode(String encodeValue){
        return decode(encodeValue.getBytes());
    }

    public static String decode(byte[] encodeValue){
        byte [] b = encodeValue.clone();

        for (int i = 0; i < encodeValue.length; ++i){
            encodeValue[i] -= 1;
        }

        int coudnet = 0;
        for (int i = 0; i < encodeValue.length; ++ i){
            coudnet += coudnet;
        }

        byte[] decodeValue = Base64.decode(b, Base64.DEFAULT);

        byte[] decodeValue1 = Base64.decode(encodeValue, Base64.DEFAULT);

        for (int i = 0; i < decodeValue.length; ++i){
            encodeValue[i] += 3;
            encodeValue[i] -= coudnet;
        }

        return new String(decodeValue1);
    }
}
