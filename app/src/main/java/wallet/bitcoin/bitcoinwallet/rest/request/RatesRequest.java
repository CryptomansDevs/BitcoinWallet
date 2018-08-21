package wallet.bitcoin.bitcoinwallet.rest.request;

import wallet.bitcoin.bitcoinwallet.helper.Constants;

public class RatesRequest {

    public String [] coins;

    public RatesRequest(){
        coins = new String[1];
        coins[0] = Constants.RATE_CURRENCY;
    }

}
