package wallet.bitcoin.bitcoinwallet.rest.request;

import java.util.List;

public class SendFromAddressRequest {

    public String access_token;

    public Params params;

    public static class Params{
        public String from;

        public List<ToAddresses> to;

        public String fee;

        public boolean new_leftover = true;
    }

    public static class ToAddresses{

        public String address;

        public float amount;
    }

    public SendFromAddressRequest(String access_token, String from, String fee){
        this.access_token = access_token;

        params = new Params();
        this.params.from = from;
        this.params.fee = fee;

    }
}
