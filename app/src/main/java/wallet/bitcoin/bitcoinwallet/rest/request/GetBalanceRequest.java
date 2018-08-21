package wallet.bitcoin.bitcoinwallet.rest.request;

import wallet.bitcoin.bitcoinwallet.helper.Constants;

public class GetBalanceRequest {

    public String access_token;


    public Params params;

    public class Params{
        public String currency;

        public int start;

        public int offset;
    }

    //TODO Params
//    public String currency;

    public GetBalanceRequest(String access_token, int offset) {
        this.access_token = access_token;
        this.params = new Params();

        this.params.currency = Constants.CURRENCY;
        this.params.start = offset;
        this.params.offset = Constants.OFFSET;
//        this.currency = Constants.CURRENCY;
    }
}
