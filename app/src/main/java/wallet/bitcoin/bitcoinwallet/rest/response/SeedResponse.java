package wallet.bitcoin.bitcoinwallet.rest.response;

import com.google.gson.annotations.SerializedName;

public class SeedResponse extends BaseResponse {

    public class Result {

        @SerializedName("seed")
        public String seed;

        @SerializedName("xpriv")
        public String xpriv;

        @SerializedName("currency")
        public String currency;
    }

    @SerializedName("result")
    public Result result;
}
