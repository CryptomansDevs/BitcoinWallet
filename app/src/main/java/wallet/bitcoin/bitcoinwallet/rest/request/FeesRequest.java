package wallet.bitcoin.bitcoinwallet.rest.request;

import wallet.bitcoin.bitcoinwallet.helper.Constants;

public class FeesRequest {
    public String currency;

    public FeesRequest(){
        currency = Constants.RATE_CURRENCY;
    }
}
