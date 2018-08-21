package wallet.bitcoin.bitcoinwallet.rest.response;

import com.google.gson.annotations.SerializedName;

public abstract class BaseResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("error")
    public String error;

    @SerializedName("error_msg")
    public String error_msg;

}
