package wallet.bitcoin.bitcoinwallet.rest.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import wallet.bitcoin.bitcoinwallet.rest.request.AccessTokenRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.FeesRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.GetAllTxsRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.GetBalanceRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.GetKeysRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.GetNewAddressRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.GetTxsRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.ImportAddressRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.ImportWalletRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.RatesRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.RemoveRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.SendFromAddressRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.SendFromWalletRequest;
import wallet.bitcoin.bitcoinwallet.rest.response.BalanceResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.FeesResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.GetNewAddressResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.GetRatesResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.GetTxsResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.ImportWalletResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.KeysResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.RemoveResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.SeedResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.SendFromWalletResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.TotalBalanceResponse;

public interface ApiService {

    @POST("/api/v1.0/GetBalance")
    public Call<BalanceResponse> getBalance(@Body GetBalanceRequest request);

    @POST("/api/v1.0/GetTotalBalance")
    public Call<TotalBalanceResponse> getTotalBalance(@Body AccessTokenRequest request);

    @POST("/api/v1.0/GetNewAddress")
    public Call<GetNewAddressResponse> getNewAddress(@Body GetNewAddressRequest request);

    @POST("/api/v1.0/GetTxs")
    public Call<GetTxsResponse> getTxs(@Body GetTxsRequest request);

    @POST("/api/v1.0/GetAllTxs")
    public Call<GetTxsResponse> getAllTxs(@Body GetAllTxsRequest request);

    @POST("/api/v1.0/GetKeys")
    public Call<KeysResponse> getKeys(@Body GetKeysRequest request);

    @POST("/api/v1.0/GetRates")
    public Call<GetRatesResponse> getRates(@Body RatesRequest request);

    @POST("/api/v1.0/ImportAddress")
    public Call<GetNewAddressResponse> importAddress(@Body ImportAddressRequest request);

    @POST("/api/v1.0/RemoveAddress")
    public Call<RemoveResponse> removeAddress(@Body RemoveRequest request);

    @POST("/api/v1.0/GetFees")
    public Call<FeesResponse> getFees(@Body FeesRequest request);

    @POST("/api/v1.0/SendFromWallet")
    public Call<SendFromWalletResponse> sendFromWallet(@Body SendFromWalletRequest request);

    @POST("/api/v1.0/SendFromAddress")
    public Call<SendFromWalletResponse> sendFromAddress(@Body SendFromAddressRequest request);

    @POST("/api/v1.0/ExportSeed")
    public Call<SeedResponse> exportSeed(@Body AccessTokenRequest request);

    @POST("/api/v1.0/ImportWallet")
    public Call<ImportWalletResponse> importWallet(@Body ImportWalletRequest request);

}
