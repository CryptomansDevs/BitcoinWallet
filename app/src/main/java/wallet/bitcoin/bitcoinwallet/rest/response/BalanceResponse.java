package wallet.bitcoin.bitcoinwallet.rest.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BalanceResponse extends BaseResponse {

    public class Result {
        @SerializedName("address")
        public String address;

        @SerializedName("balance")
        public float balance;

        @SerializedName("currency")
        public String currency;
    }

    @SerializedName("result")
    public List<Result> result;
}
