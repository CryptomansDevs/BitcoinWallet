package wallet.bitcoin.bitcoinwallet.rest.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetRatesResponse extends BaseResponse {

    public class Rates {

        @SerializedName("eur")
        public float eur;

        @SerializedName("rub")
        public float rub;

        @SerializedName("usd")
        public float usd;
    }

    public class Result {

        @SerializedName("currency")
        public String currency;

        @SerializedName("rates")
        public Rates rates;
    }

    @SerializedName("result")
    public List<Result> result;
}
