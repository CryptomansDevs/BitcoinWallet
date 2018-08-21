package wallet.bitcoin.bitcoinwallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.rest.request.AccessTokenRequest;
import wallet.bitcoin.bitcoinwallet.rest.response.SeedResponse;

public class MyMnemonicCodeActivity extends AppCompatActivity {

    @BindView(R.id.tvPublicKeyValue)
    public TextView tvMnemonicKeyValue;

    @BindView(R.id.tvPrivateKeyValue)
    public TextView tvXPrevKeyValue;

    @BindView(R.id.ivCopyPrivate)
    public ImageView ivCopyXPrev;

    @BindView(R.id.ivCopyPublic)
    public ImageView ivCopyMnemonic;

    @BindView(R.id.abBack)
    public ImageView abBack;

    @BindView(R.id.abName)
    public TextView abName;

    @BindView(R.id.rlProgress)
    public SmoothProgressBar rlProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarColor(this);
        setContentView(R.layout.mnemonic_code_activity);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        Utility.setDrawableColor(ivCopyXPrev, App.getContext().getResources().getColor(R.color.colorPrimary));
        Utility.setDrawableColor(ivCopyMnemonic, App.getContext().getResources().getColor(R.color.colorPrimary));
        Utility.setDrawableColor(abBack, App.getContext().getResources().getColor(R.color.white));

        requestMnemonicCode();
    }

    private void requestMnemonicCode() {
        showProgress(true);

        Call<SeedResponse> getMyMnemonicCall = App.getRestClient().getApiService().
                exportSeed(new AccessTokenRequest(App.getCurrentUser().accessToken));

        getMyMnemonicCall.enqueue(new Callback<SeedResponse>() {
            @Override
            public void onResponse(Call<SeedResponse> call, Response<SeedResponse> response) {
                if (response.isSuccessful()) {

                    SeedResponse responseBody = response.body();
                    if (responseBody.success && responseBody.result != null) {
                        onMnemonicCodeReceived(responseBody.result.seed, responseBody.result.xpriv);
                    } else {
                        Toasty.error(MyMnemonicCodeActivity.this, responseBody.error_msg, Toast.LENGTH_LONG, true).show();
                    }

                    showProgress(false);
                } else {
                    Toasty.error(MyMnemonicCodeActivity.this, response.message(), Toast.LENGTH_LONG, true).show();
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<SeedResponse> call, Throwable t) {
                Toasty.error(MyMnemonicCodeActivity.this, t.toString(), Toast.LENGTH_LONG, true).show();
                showProgress(false);
            }
        });
    }

    private void onMnemonicCodeReceived(String seed, String xpriv){
        tvMnemonicKeyValue.setText(seed);
        tvXPrevKeyValue.setText(xpriv);
    }

    @OnClick(R.id.abBack)
    public void onBackClick() {
        onBackPressed();
    }

    @OnClick(R.id.ivCopyPublic)
    public void onCopyivCopyPublicClicked(){
        Utility.copy(tvMnemonicKeyValue.getText().toString());
        Toasty.success(this, getResources().getString(R.string.copied)).show();
    }

    @OnClick(R.id.ivCopyPrivate)
    public void onCopyivCopyPrivateClicked(){
        Utility.copy(tvXPrevKeyValue.getText().toString());
        Toasty.success(this, getResources().getString(R.string.copied)).show();
    }

    private void showProgress(boolean progressShown) {
        if (progressShown) {
            rlProgress.setVisibility(View.VISIBLE);
        } else {
            rlProgress.setVisibility(View.GONE);
        }
    }
}
