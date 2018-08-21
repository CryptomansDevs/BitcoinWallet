package wallet.bitcoin.bitcoinwallet.rest.response;

import com.google.gson.annotations.SerializedName;

public class SendFromWalletResponse extends BaseResponse {

    public static class Result {
        @SerializedName("txid")
        public String txid;

        @SerializedName("new_address")
        public NewAddress newAddress;
    }

    public static class NewAddress {
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
