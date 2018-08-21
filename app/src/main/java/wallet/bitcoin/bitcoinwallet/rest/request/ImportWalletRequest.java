package wallet.bitcoin.bitcoinwallet.rest.request;

import wallet.bitcoin.bitcoinwallet.helper.Constants;

public class ImportWalletRequest {

    public String access_token;

    public Params params;

    public class Params {
        public String seed;

        public String currency;
    }

    public ImportWalletRequest(String access_token, String seed) {
        this.access_token = access_token;
        this.params = new Params();
        this.params.currency = Constants.CURRENCY;
        this.params.seed = seed;
    }

}
