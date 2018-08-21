package wallet.bitcoin.bitcoinwallet.rest.request;

public class ImportAddressRequest {

    public String access_token;

    public Params params;

    public class Params{
        public String currency;

        public String private_key;
    }

    public ImportAddressRequest(String access_token, String currency, String private_key){
        this.access_token = access_token;
        this.params = new Params();
        this.params.currency = currency;
        this.params.private_key = private_key;
    }
}
