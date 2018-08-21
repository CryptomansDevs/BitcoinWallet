package wallet.bitcoin.bitcoinwallet.rest.request;

public class GetTxsRequest {

    public String access_token;

    public Params params;

    public class Params{
        public String address;

        public int min;

        public int max;
    }

    public GetTxsRequest(String access_token, String address, int min, int max){
        this.access_token = access_token;
        this.params = new Params();
        this.params.address = address;
        this.params.min = min;
        this.params.max = max;
    }
}
