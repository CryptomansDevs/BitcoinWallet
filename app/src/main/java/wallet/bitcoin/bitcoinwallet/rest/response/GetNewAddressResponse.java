package wallet.bitcoin.bitcoinwallet.rest.response;

import com.google.gson.annotations.SerializedName;

public class GetNewAddressResponse extends BaseResponse {

    public class Result {

        @SerializedName("address")
        public String address;

        @SerializedName("private")
        public String privateKey;

        @SerializedName("public")
        public String publicKey;

        @SerializedName("currency")
        public String currency;
    }

    @SerializedName("result")
    public Result result;
}
