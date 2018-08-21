package wallet.bitcoin.bitcoinwallet.model;

import wallet.bitcoin.bitcoinwallet.rest.response.BalanceResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.GetNewAddressResponse;

public class WalletAddress {

    public String id;
    public String currency;

    public float balance;

    public static WalletAddress create(BalanceResponse.Result result) {
        WalletAddress address = new WalletAddress();
        address.id = result.address;
        address.balance = result.balance;
        address.currency = result.currency;

        return address;
    }

    public static WalletAddress create(GetNewAddressResponse.Result result) {
        WalletAddress address = new WalletAddress();
        address.id = result.address;
        address.balance = 0f;
        address.currency = result.currency;

        return address;
    }
}
