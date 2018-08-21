package wallet.bitcoin.bitcoinwallet.rest.response;

import com.google.gson.annotations.SerializedName;

public class RemoveResponse extends BaseResponse {

    @SerializedName("result")
    public boolean result;
}
