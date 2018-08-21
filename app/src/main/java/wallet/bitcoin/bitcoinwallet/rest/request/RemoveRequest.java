package wallet.bitcoin.bitcoinwallet.rest.request;

public class RemoveRequest {

    public String access_token;

    public Params params;

    public class Params{
        public String address;
    }

    public RemoveRequest(String access_token, String address){
        this.access_token = access_token;
        this.params = new Params();
        this.params.address = address;
    }
}
