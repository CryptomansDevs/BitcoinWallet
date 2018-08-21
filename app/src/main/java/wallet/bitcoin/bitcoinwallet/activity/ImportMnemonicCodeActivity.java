package wallet.bitcoin.bitcoinwallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import wallet.bitcoin.bitcoinwallet.dialog.ImortWalletDoneDialog;
import wallet.bitcoin.bitcoinwallet.dialog.ImportWalletWarnDialog;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.rest.request.ImportWalletRequest;
import wallet.bitcoin.bitcoinwallet.rest.response.ImportWalletResponse;

public class ImportMnemonicCodeActivity extends AppCompatActivity {

    @BindView(R.id.etMnemonic)
    public EditText etMnemonic;

    @BindView(R.id.abBack)
    public ImageView abBack;

    @BindView(R.id.abName)
    public TextView abName;

    @BindView(R.id.rlProgress)
    public SmoothProgressBar rlProgress;

    @BindView(R.id.btnImport)
    public Button btnImport;

    private boolean disabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarColor(this);
        setContentView(R.layout.import_mnemonic_code_activity);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        Utility.setDrawableColor(abBack, App.getContext().getResources().getColor(R.color.white));
        showProgress(false);
    }

    private void importMnemonicCode(String seed) {
        showProgress(true);

        Call<ImportWalletResponse> importMyMnemonicCall = App.getRestClient().getApiService().
                importWallet(new ImportWalletRequest(App.getCurrentUser().accessToken, seed));

        importMyMnemonicCall.enqueue(new Callback<ImportWalletResponse>() {
            @Override
            public void onResponse(Call<ImportWalletResponse> call, Response<ImportWalletResponse> response) {
                if (response.isSuccessful()) {

                    ImportWalletResponse responseBody = response.body();
                    if (responseBody.success && responseBody.result != null) {
                        onMnemonicCodeImported(responseBody.result.imported);
                    } else {
                        Toasty.error(ImportMnemonicCodeActivity.this, responseBody.error_msg, Toast.LENGTH_LONG, true).show();
                    }

                    showProgress(false);
                } else {
                    Toasty.error(ImportMnemonicCodeActivity.this, response.message(), Toast.LENGTH_LONG, true).show();
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<ImportWalletResponse> call, Throwable t) {
                Toasty.error(ImportMnemonicCodeActivity.this, t.toString(), Toast.LENGTH_LONG, true).show();
                showProgress(false);
            }
        });
    }

    private void onMnemonicCodeImported(int imported) {
        ImortWalletDoneDialog.show(this, new ImortWalletDoneDialog.OnFinished() {
            @Override
            public void onFinished() {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @OnClick(R.id.btnImport)
    public void onbtnImportClick() {
        ImportWalletWarnDialog.show(this, new ImportWalletWarnDialog.ImportWalletWarnDialogCallback() {
            @Override
            public void onDoImport() {
                importMnemonicCode(etMnemonic.getText().toString());
            }

            @Override
            public void onCancelImport() {
                onBackPressed();
            }
        });
    }

    @OnClick(R.id.abBack)
    public void onBackClick() {
        onBackPressed();
    }

    private void showProgress(boolean progressShown) {
        if (progressShown) {
            rlProgress.setVisibility(View.VISIBLE);

            disabled = true;
            btnImport.setEnabled(false);
        } else {
            rlProgress.setVisibility(View.GONE);

            disabled = false;
            btnImport.setEnabled(true);

        }
    }

    @Override
    public void onBackPressed() {
        if (disabled){
            return;
        }

        super.onBackPressed();
    }
}

