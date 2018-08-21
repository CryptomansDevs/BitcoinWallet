package wallet.bitcoin.bitcoinwallet.rest.request;

public class GetNewAddressRequest {

    public String access_token;

    public Params params;

    public class Params{
        public String currency;
    }

    public GetNewAddressRequest(String access_token, String currency){
        this.access_token = access_token;
        this.params = new Params();
        this.params.currency = currency;
    }
}
