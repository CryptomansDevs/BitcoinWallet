package wallet.bitcoin.bitcoinwallet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.activity.BuyBitcoinActivity;
import wallet.bitcoin.bitcoinwallet.activity.CameraActivity;
import wallet.bitcoin.bitcoinwallet.activity.MainActivity;
import wallet.bitcoin.bitcoinwallet.adapter.SendAdapter;
import wallet.bitcoin.bitcoinwallet.dialog.ConfirmSendDialogHelper;
import wallet.bitcoin.bitcoinwallet.dialog.SentDialogHelper;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.BtcParcerHelper;
import wallet.bitcoin.bitcoinwallet.helper.Constants;
import wallet.bitcoin.bitcoinwallet.helper.OnFinishCallback;
import wallet.bitcoin.bitcoinwallet.helper.UIHelper;
import wallet.bitcoin.bitcoinwallet.model.WalletAddress;
import wallet.bitcoin.bitcoinwallet.rest.request.SendFromAddressRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.SendFromWalletRequest;
import wallet.bitcoin.bitcoinwallet.rest.response.SendFromWalletResponse;

import static android.app.Activity.RESULT_OK;

public class SendFragment extends BaseFragment implements SendAdapter.OnSendAdapterCallback, ConfirmSendDialogHelper.OnConfirmedCallback {

    private static final int QR_CODE_REQEST = 11;

    @BindView(R.id.rvSend)
    public RecyclerView rvSend;

    private SendAdapter adapter;

    public static SendFragment newInstance() {
        SendFragment instance = new SendFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) LayoutInflater.from(App.getContext()).inflate(R.layout.fragment_send, container, false);
        ButterKnife.bind(this, mRootView);

        LinearLayoutManager quickLinkLayoutManager = new LinearLayoutManager(rvSend.getContext(), LinearLayoutManager.VERTICAL, false);
        rvSend.setLayoutManager(quickLinkLayoutManager);
        rvSend.setHasFixedSize(true);

        adapter = new SendAdapter(this);
        rvSend.setAdapter(adapter);
        List<SendAdapter.SendModel> list = new ArrayList<>();
        list.add(SendAdapter.SendModel.createHeader());
        list.add(SendAdapter.SendModel.create());
        list.add(SendAdapter.SendModel.createFooter());

        adapter.setItems(list);

        showProgress(true);

        return mRootView;
    }

    @Override
    public void onVisibleOnScreen() {
        super.onVisibleOnScreen();
    }

    public void setCurrentAddress(List<WalletAddress> addresses, float balance) {
        showProgress(false);
        adapter.initHeader(balance, addresses);
    }

    @Override
    public void showProgress(boolean progressShown) {
        super.showProgress(progressShown);
        adapter.showProgress(progressShown);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case QR_CODE_REQEST:
                if (resultCode == RESULT_OK) {
                    String barcode = BtcParcerHelper.parceWallet(data.getStringExtra(CameraActivity.BARCODE));
                    int pos = data.getIntExtra(CameraActivity.ADDRESS_POS, 0);

                    adapter.updateItem(pos, barcode);
                }
                break;
        }
    }

    @Override
    public void onQrClicked(int pos) {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra(CameraActivity.ADDRESS_POS, pos);
        startActivityForResult(intent, QR_CODE_REQEST);
    }

    @Override
    public void hideKeyboard() {
        if (getActivity() != null) {
            UIHelper.hideKeyboard(getActivity());
        }
    }

    public void showConfirmSendDialog() {
        if (getActivity() != null) {
            UIHelper.hideKeyboard(getActivity());

            ConfirmSendDialogHelper confirmSendDlgHelper = ConfirmSendDialogHelper.show(getActivity(), this);
        }
    }

    private void sendMoneyFromWallet(String feeParam) {
        showProgress(true);

        SendFromWalletRequest request = new SendFromWalletRequest(App.getCurrentUser().accessToken, Constants.CURRENCY, feeParam);
        List<SendFromWalletRequest.ToAddresses> sentAddresses = new ArrayList<>();

        List<SendAdapter.SendModel> list = adapter.getItems();
        for (SendAdapter.SendModel model : list) {
            if (model.isModel()) {
                SendFromWalletRequest.ToAddresses address = new SendFromWalletRequest.ToAddresses();
                address.address = model.address;
                address.amount = model.amount;

                sentAddresses.add(address);
            }
        }

        request.params.to = sentAddresses;

        Call<SendFromWalletResponse> sendMoneyFromWalletCall = App.getRestClient().getApiService().
                sendFromWallet(request);

        sendMoneyFromWalletCall.enqueue(new Callback<SendFromWalletResponse>() {
            @Override
            public void onResponse(Call<SendFromWalletResponse> call, Response<SendFromWalletResponse> response) {
                if (response.isSuccessful()) {

                    SendFromWalletResponse responseBody = response.body();
                    if (responseBody.success) {

                        showSentDialog(responseBody);

                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).getBalance(new OnFinishCallback() {
                                @Override
                                public void onFinished() {
                                    showProgress(false);
                                }
                            });
                        }
                        return;

                    } else {
                        if (getActivity() != null) {
                            Toasty.error(getActivity(), responseBody.error_msg, Toast.LENGTH_LONG, true).show();
                        }
                    }

                    showProgress(false);
                } else {
                    if (getActivity() != null) {
                        Toasty.error(getActivity(), response.message(), Toast.LENGTH_LONG, true).show();
                    }
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<SendFromWalletResponse> call, Throwable t) {
                if (getActivity() != null) {
                    Toasty.error(getActivity(), t.toString(), Toast.LENGTH_LONG, true).show();
                }
                showProgress(false);
            }
        });
    }

    private void sendMoneyFromAddress(String sendingAddress, String feeParam) {
        showProgress(true);

        SendFromAddressRequest request = new SendFromAddressRequest(App.getCurrentUser().accessToken, sendingAddress, feeParam);
        List<SendFromAddressRequest.ToAddresses> sentAddresses = new ArrayList<>();

        List<SendAdapter.SendModel> list = adapter.getItems();
        for (SendAdapter.SendModel model : list) {
            if (model.isModel()) {
                SendFromAddressRequest.ToAddresses address = new SendFromAddressRequest.ToAddresses();
                address.address = model.address;
                address.amount = model.amount;

                sentAddresses.add(address);
            }
        }

        request.params.new_leftover = adapter.getIsLeftOver();
        request.params.to = sentAddresses;

        Call<SendFromWalletResponse> sendMoneyFromWalletCall = App.getRestClient().getApiService().
                sendFromAddress(request);

        sendMoneyFromWalletCall.enqueue(new Callback<SendFromWalletResponse>() {
            @Override
            public void onResponse(Call<SendFromWalletResponse> call, Response<SendFromWalletResponse> response) {
                if (response.isSuccessful()) {

                    SendFromWalletResponse responseBody = response.body();
                    if (responseBody.success) {

                        showSentDialog(responseBody);
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).getBalance(new OnFinishCallback() {
                                @Override
                                public void onFinished() {
                                    showProgress(false);
                                }
                            });
                        }
                        return;

                    } else {
                        if (getActivity() != null) {
                            Toasty.error(getActivity(), responseBody.error_msg, Toast.LENGTH_LONG, true).show();
                        }
                    }

                    showProgress(false);
                } else {
                    if (getActivity() != null) {
                        Toasty.error(getActivity(), response.message(), Toast.LENGTH_LONG, true).show();
                    }
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<SendFromWalletResponse> call, Throwable t) {
                if (getActivity() != null) {
                    Toasty.error(getActivity(), t.toString(), Toast.LENGTH_LONG, true).show();
                }
                showProgress(false);
            }
        });
    }

    private void showSentDialog(SendFromWalletResponse response) {
        if (getActivity() != null) {
            SentDialogHelper.show(getActivity(), response.result.txid);
        }
    }

//    @Override
//    public void onBuyBitcoinsClicked() {
//        if (getActivity() != null) {
//            Intent intent = new Intent(getActivity(), BuyBitcoinActivity.class);
//            startActivity(intent);
//        }
//    }
//
//    @Override
//    public void onShowBitcoinsAddressClicked() {
//        if (getActivity() != null) {
//            ((MainActivity) getActivity()).onSetCurrentViewPagerItem(MainActivity.FOURTH_TAB_NUM);
//        }
//    }

    @Override
    public void onConfirm(String feeParam) {
        String sendingAddress = adapter.getSelectedAddress();
        if (sendingAddress == null) {
            sendMoneyFromWallet(feeParam);
        } else {
            sendMoneyFromAddress(sendingAddress, feeParam);
        }
    }

    @Override
    public void onCancel() {

    }
}

