package wallet.bitcoin.bitcoinwallet.helper;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wallet.bitcoin.bitcoinwallet.events.RatesUpdateEvent;
import wallet.bitcoin.bitcoinwallet.model.User;
import wallet.bitcoin.bitcoinwallet.rest.request.FeesRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.RatesRequest;
import wallet.bitcoin.bitcoinwallet.rest.response.FeesResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.GetRatesResponse;

public class RatesHelper {

    public static void getRates() {
        Call<GetRatesResponse> balanceRequestCall = App.getRestClient().getApiService().
                getRates(new RatesRequest());

        balanceRequestCall.enqueue(new Callback<GetRatesResponse>() {
            @Override
            public void onResponse(Call<GetRatesResponse> call, Response<GetRatesResponse> response) {
                if (response.isSuccessful()) {

                    GetRatesResponse responseBody = response.body();
                    if (responseBody.success && responseBody.result != null && responseBody.result.size() > 0 &&
                            responseBody.result.get(0).rates != null) {

                        User user = App.getCurrentUser();
                        user.rateUsd = responseBody.result.get(0).rates.usd;
                        user.rateEuro = responseBody.result.get(0).rates.eur;
                        user.rateRur = responseBody.result.get(0).rates.rub;
                        App.updateUser();

                        EventBus.getDefault().postSticky(new RatesUpdateEvent());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetRatesResponse> call, Throwable t) {
            }
        });

        getFees();
    }

    private static void getFees(){
        Call<FeesResponse> balanceRequestCall = App.getRestClient().getApiService().
                getFees(new FeesRequest());

        balanceRequestCall.enqueue(new Callback<FeesResponse>() {
            @Override
            public void onResponse(Call<FeesResponse> call, Response<FeesResponse> response) {
                if (response.isSuccessful()) {

                    FeesResponse responseBody = response.body();
                    if (responseBody.success && responseBody.result != null) {

                        User user = App.getCurrentUser();
                        user.fastFee = responseBody.result.fast;
                        user.optimalFee = responseBody.result.optimal;
                        user.slowFee = responseBody.result.slow;
                        App.updateUser();
                    }
                }
            }

            @Override
            public void onFailure(Call<FeesResponse> call, Throwable t) {
            }
        });
    }
}
