package wallet.bitcoin.bitcoinwallet.rest.response;

import com.google.gson.annotations.SerializedName;

public class ImportWalletResponse extends BaseResponse {

    public class Result {

        @SerializedName("imported")
        public int imported;
    }

    @SerializedName("result")
    public Result result;

}
