package wallet.bitcoin.bitcoinwallet.helper;

import java.util.Random;

public class TicketGenerator {

    public static final String WIN_TICKET = "KDUC";

    public static final int TICKET_LEN = 4;

    public static String generate(){
        int length = TICKET_LEN;
        final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJLMNOPQRSTUVWXYZ1234567890";
        StringBuilder result = new StringBuilder();
        while(length > 0) {
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            length--;
        }
        String gen = result.toString().toUpperCase();

        if (gen.equalsIgnoreCase(WIN_TICKET)){
            return generate();
        }

        return gen;
    }
}
