package wallet.bitcoin.bitcoinwallet.rest.response;

import com.google.gson.annotations.SerializedName;

public class KeysResponse extends BaseResponse {

    public class Result {
        @SerializedName("private")
        public String privateKey;

        @SerializedName("public")
        public String publicKey;
    }

    @SerializedName("result")
    public Result result;
}
