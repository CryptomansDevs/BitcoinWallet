package wallet.bitcoin.bitcoinwallet.rest.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TotalBalanceResponse extends BaseResponse {

    public class Result {

        @SerializedName("balance")
        public float balance;

        @SerializedName("currency")
        public String currency;
    }

    @SerializedName("result")
    public List<Result> result;
}
