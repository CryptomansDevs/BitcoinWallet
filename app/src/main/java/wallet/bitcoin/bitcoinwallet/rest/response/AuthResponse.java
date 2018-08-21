package wallet.bitcoin.bitcoinwallet.rest.response;

import com.google.gson.annotations.SerializedName;

public class AuthResponse extends BaseResponse {

    public class Result {
        @SerializedName("access_token")
        public String access_token;
    }

    @SerializedName("result")
    public Result result;
}
