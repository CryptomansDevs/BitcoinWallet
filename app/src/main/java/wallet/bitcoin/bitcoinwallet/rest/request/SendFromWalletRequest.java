package wallet.bitcoin.bitcoinwallet.rest.request;

import java.util.List;

public class SendFromWalletRequest {

    public String access_token;

    public Params params;

    public static class Params{
        public String currency;

        public List<ToAddresses> to;

        public String fee;
    }

    public static class ToAddresses{

        public String address;

        public float amount;
    }

    public SendFromWalletRequest(String access_token, String currency, String fee){
        this.access_token = access_token;

        params = new Params();
        this.params.currency = currency;
        this.params.fee = fee;

    }
}
