package wallet.bitcoin.bitcoinwallet.helper;

public class BtcParcerHelper {

    final public static String CRYPT_PREF = "bitcoin:";
    final public static String CRYPT_PREF_2 = "btc:";

    public static String parceWallet(String newWallet){
        if (newWallet.toLowerCase().startsWith(CRYPT_PREF)) {
            // at least monero-wallet-gui.exe has this prefixed in its QR generated
            newWallet = newWallet.substring(CRYPT_PREF.length());
        }

        if (newWallet.toLowerCase().startsWith(CRYPT_PREF_2)) {
            // at least monero-wallet-gui.exe has this prefixed in its QR generated
            newWallet = newWallet.substring(CRYPT_PREF_2.length());
        }

        return newWallet;
    }
}
