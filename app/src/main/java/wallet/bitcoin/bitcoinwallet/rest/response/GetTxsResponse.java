package wallet.bitcoin.bitcoinwallet.rest.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTxsResponse extends BaseResponse {

    public class Result {

        @SerializedName("confirmed")
        public boolean confirmed;

        @SerializedName("date")
        public String date;

        @SerializedName("inputs")
        public List<InputOutput> inputs;

        @SerializedName("outputs")
        public List<InputOutput> outputs;

        @SerializedName("txid")
        public String txid;

        @SerializedName("amount")
        public double amount;

        @SerializedName("fees")
        public double fees;
    }

    public class InputOutput {
        public String address;

        public float amount;
    }

    @SerializedName("result")
    public List<Result> result;
}
