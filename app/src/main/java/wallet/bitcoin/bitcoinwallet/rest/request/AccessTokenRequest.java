package wallet.bitcoin.bitcoinwallet.rest.request;

import wallet.bitcoin.bitcoinwallet.helper.Constants;

public class AccessTokenRequest {

    public String access_token;

    public Params params;

    public class Params{
        public String currency;
    }

    public AccessTokenRequest(String access_token){
        this.access_token = access_token;
        this.params = new Params();
        this.params.currency = Constants.CURRENCY;
    }
}
