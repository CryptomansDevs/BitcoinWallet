package wallet.bitcoin.bitcoinwallet.rest.request;

public class GetAllTxsRequest {

    public String access_token;

    public Params params;

    public class Params{
        public String currency;

        public int min;

        public int max;
    }

    public GetAllTxsRequest(String access_token, String currency, int min, int max){
        this.access_token = access_token;
        this.params = new Params();
        this.params.currency = currency;
        this.params.min = min;
        this.params.max = max;
    }
}
