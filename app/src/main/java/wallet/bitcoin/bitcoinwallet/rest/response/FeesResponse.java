package wallet.bitcoin.bitcoinwallet.rest.response;

import com.google.gson.annotations.SerializedName;

public class FeesResponse extends BaseResponse {

    public class Result {

        @SerializedName("fast")
        public int fast;

        @SerializedName("optimal")
        public int optimal;

        @SerializedName("slow")
        public int slow;
    }

    @SerializedName("result")
    public Result result;
}
