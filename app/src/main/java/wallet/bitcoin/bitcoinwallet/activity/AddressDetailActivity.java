package wallet.bitcoin.bitcoinwallet.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.adapter.DetailsAdapter;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.Constants;
import wallet.bitcoin.bitcoinwallet.helper.OnFinishCallback;
import wallet.bitcoin.bitcoinwallet.helper.UIHelper;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.rest.request.GetKeysRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.GetTxsRequest;
import wallet.bitcoin.bitcoinwallet.rest.request.RemoveRequest;
import wallet.bitcoin.bitcoinwallet.rest.response.GetTxsResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.KeysResponse;
import wallet.bitcoin.bitcoinwallet.rest.response.RemoveResponse;
import wallet.bitcoin.bitcoinwallet.view.AppRecyclerView;

public class AddressDetailActivity extends AppCompatActivity {

    private static final int ADDRESS_ANIM_DURATION = 300;

    public static final String ADDRESS = "Address";
    public static final String BALANCE = "balance";
    public static final String CUR = "CUR";

    @BindView(R.id.rvAddresses)
    public AppRecyclerView rvAddresses;

    @BindView(R.id.tvBalance)
    public TextView tvBalance;

    @BindView(R.id.tvRemove)
    public TextView tvRemove;

    @BindView(R.id.tvBalanceInCurrency)
    public TextView tvBalanceInCurrency;

    @BindView(R.id.abName)
    public TextView abName;

    @BindView(R.id.ivDropDown)
    protected ImageView ivDropDown;

    @BindView(R.id.tvAddress)
    public TextView tvAddress;

    @BindView(R.id.rlProgress)
    public SmoothProgressBar rlProgress;

    @BindView(R.id.emptyView)
    public LinearLayout emptyView;

    @BindView(R.id.abBack)
    protected ImageView abBack;

    @BindView(R.id.llBalanceInner)
    protected RelativeLayout llBalanceInner;

    @BindView(R.id.tvPublicKeyValue)
    public TextView tvPublicKeyValue;

    @BindView(R.id.tvPrivateKeyValue)
    public TextView tvPrivateKeyValue;

    @BindView(R.id.tvAddressValue)
    public TextView tvAddressValue;

    @BindView(R.id.ivCopyPrivate)
    public ImageView ivCopyPrivate;

    @BindView(R.id.ivCopyPublic)
    public ImageView ivCopyPublic;

    @BindView(R.id.ivCopyAddress)
    public ImageView ivCopyAddress;

    @BindView(R.id.ivRemove)
    public ImageView ivRemove;

    @BindView(R.id.swiperefresh)
    public SwipeRefreshLayout mPullToRefreshView;

    private DetailsAdapter adapter;

    private String address;
    private String cur;
    private float balance;

    public int offsetIndex = 0;

    private boolean isDropDownOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarColor(this);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        address = getIntent().getStringExtra(ADDRESS);
        cur = getIntent().getStringExtra(CUR);
        balance = getIntent().getFloatExtra(BALANCE, 0f);

        init();
    }

    private void init() {
        Utility.setDrawableColor(abBack, App.getContext().getResources().getColor(R.color.white));
        Utility.setDrawableColor(ivCopyAddress, App.getContext().getResources().getColor(R.color.colorPrimary));
        Utility.setDrawableColor(ivCopyPrivate, App.getContext().getResources().getColor(R.color.colorPrimary));
        Utility.setDrawableColor(ivCopyPublic, App.getContext().getResources().getColor(R.color.colorPrimary));
        Utility.setDrawableColor(ivRemove, App.getContext().getResources().getColor(R.color.colorPrimary));

        tvAddressValue.setText(address);
        tvBalance.setText(Utility.getDoubleStringFormatNoSign(balance)+ " " + Constants.CURRENCY);
        float curRate = App.getCurrentUser().getRate();
        tvBalanceInCurrency.setText(Utility.getDoubleStringFormatForCurrency(curRate * balance) + " " + App.getCurrentUser().getRateName());

        LinearLayoutManager quickLinkLayoutManager = new LinearLayoutManager(rvAddresses.getContext(), LinearLayoutManager.VERTICAL, false);
        rvAddresses.setLayoutManager(quickLinkLayoutManager);
        rvAddresses.setHasFixedSize(true);
        rvAddresses.setEmptyView(emptyView);

        adapter = new DetailsAdapter(address);
        rvAddresses.setAdapter(adapter);

        RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {}

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager mg = (LinearLayoutManager) rvAddresses.getLayoutManager();

                int totalCount = mg.getItemCount();
                int lastVisible = mg.findLastVisibleItemPosition();

                if (lastVisible < 0) {
                    return;
                }

                boolean loadMore = (lastVisible == (totalCount - 1));

                if (adapter.getItemCount() < Constants.OFFSET + offsetIndex){
                    loadMore = false;
                }

                if (loadMore) {
                    offsetIndex += Constants.OFFSET;
                    getTransactions(null, offsetIndex);
                }
            }
        };
        rvAddresses.addOnScrollListener(mScrollListener);


        mPullToRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offsetIndex = 0;

                getTransactions(new OnFinishCallback() {
                    @Override
                    public void onFinished() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, offsetIndex);
            }
        });
        mPullToRefreshView.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getTransactions(null, offsetIndex);
        requestKeys();

        initDropDown(false);
    }

    @OnClick(R.id.tvAddress)
    public void ontvDropDownClick() {
        isDropDownOpened = !isDropDownOpened;
        initDropDown(true);
    }

    @OnClick(R.id.ivDropDown)
    public void onivDropDownClick() {
        isDropDownOpened = !isDropDownOpened;
        initDropDown(true);
    }

    private void initDropDown(boolean animate) {
        if (isDropDownOpened) {
            ivDropDown.setImageResource(R.drawable.arrowupgrey);

            if (animate) {
                ViewGroup.LayoutParams layoutParams = llBalanceInner.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                llBalanceInner.setLayoutParams(layoutParams);
                llBalanceInner.setVisibility(View.VISIBLE);
            } else {
                ViewGroup.LayoutParams layoutParams = llBalanceInner.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                llBalanceInner.setLayoutParams(layoutParams);
                llBalanceInner.setVisibility(View.VISIBLE);
            }

        } else {
            ivDropDown.setImageResource(R.drawable.arrowdowngrey);

            if (animate) {
                ValueAnimator anim = ValueAnimator.ofInt(llBalanceInner.getMeasuredHeight(), 0);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = llBalanceInner.getLayoutParams();
                        layoutParams.height = val;
                        llBalanceInner.setLayoutParams(layoutParams);
                    }
                });
                anim.setDuration(ADDRESS_ANIM_DURATION);
                anim.start();
                anim.addListener(new AnimatorListenerAdapter() {
                                     @Override
                                     public void onAnimationEnd(Animator animation) {
                                         super.onAnimationEnd(animation);

                                         llBalanceInner.setVisibility(View.GONE);
                                     }
                                 }
                );
            } else {
                llBalanceInner.setVisibility(View.GONE);
            }

        }
    }

    private void requestKeys() {
        showProgress(true);

        Call<KeysResponse> balanceRequestCall = App.getRestClient().getApiService().
                getKeys(new GetKeysRequest(App.getCurrentUser().accessToken, address));

        balanceRequestCall.enqueue(new Callback<KeysResponse>() {
            @Override
            public void onResponse(Call<KeysResponse> call, Response<KeysResponse> response) {
                if (response.isSuccessful()) {

                    KeysResponse responseBody = response.body();
                    if (responseBody.success && responseBody.result != null) {
                        onReceivedKeys(responseBody.result.publicKey, responseBody.result.privateKey);
                    } else {
                        Toasty.error(AddressDetailActivity.this, responseBody.error_msg, Toast.LENGTH_LONG, true).show();
                    }

                    showProgress(false);
                } else {
                    Toasty.error(AddressDetailActivity.this, response.message(), Toast.LENGTH_LONG, true).show();
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<KeysResponse> call, Throwable t) {
                Toasty.error(AddressDetailActivity.this, t.toString(), Toast.LENGTH_LONG, true).show();
                showProgress(false);
            }
        });
    }

    private void onReceivedKeys(String pubKey, String privKey) {
        tvPublicKeyValue.setText(pubKey);
        tvPrivateKeyValue.setText(privKey);
    }

    private void getTransactions(final OnFinishCallback callback, final int offset){
        if (offset == 0) {
            showProgress(true);
        }

        if (offset != 0) {
            adapter.showFooter();
        }

        Call<GetTxsResponse> getTxsRequestCall = App.getRestClient().getApiService().
                getTxs(new GetTxsRequest(App.getCurrentUser().accessToken, address, offset, offset + Constants.OFFSET));

        getTxsRequestCall.enqueue(new Callback<GetTxsResponse>() {
            @Override
            public void onResponse(Call<GetTxsResponse> call, Response<GetTxsResponse> response) {
                if (response.isSuccessful()) {

                    GetTxsResponse responseBody = response.body();
                    if (responseBody.success) {

                        List<GetTxsResponse.Result> results = responseBody.result;
                        if (offset == 0) {
                            adapter.setItems(results);
                            if (results != null && results.size() < 2){
                                isDropDownOpened = true;
                                initDropDown(false);
                            }
                        } else {
                            adapter.addItems(results);
                            adapter.hideFooter();
                        }
                    } else {
                        Toasty.error(AddressDetailActivity.this, responseBody.error_msg, Toast.LENGTH_LONG, true).show();
                    }

                    showProgress(false);
                } else {
                    Toasty.error(AddressDetailActivity.this, response.message(), Toast.LENGTH_LONG, true).show();
                    showProgress(false);
                }

                if (callback != null) {
                    callback.onFinished();
                }
            }

            @Override
            public void onFailure(Call<GetTxsResponse> call, Throwable t) {
                Toasty.error(AddressDetailActivity.this, t.toString(), Toast.LENGTH_LONG, true).show();
                showProgress(false);

                if (callback != null) {
                    callback.onFinished();
                }
            }
        });
    }

    private void showProgress(boolean progressShown) {
        if (progressShown) {
            rlProgress.setVisibility(View.VISIBLE);
        } else {
            rlProgress.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.llRemove)
    public void ontvRemoveCLicked(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.remove_address)
                .setMessage(R.string.remove_address_descr)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        showProgress(true);

                        Call<RemoveResponse> call = App.getRestClient().getApiService().
                                removeAddress(new RemoveRequest(App.getCurrentUser().accessToken, address));

                        call.enqueue(new Callback<RemoveResponse>() {
                            @Override
                            public void onResponse(Call<RemoveResponse> call, Response<RemoveResponse> response) {
                                if (response.isSuccessful()) {

                                    RemoveResponse responseBody = response.body();
                                    if (responseBody.success && responseBody.result) {
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        Toasty.error(AddressDetailActivity.this, responseBody.error_msg, Toast.LENGTH_LONG, true).show();
                                    }

                                    showProgress(false);
                                } else {
                                    Toasty.error(AddressDetailActivity.this, response.message(), Toast.LENGTH_LONG, true).show();
                                    showProgress(false);
                                }
                            }

                            @Override
                            public void onFailure(Call<RemoveResponse> call, Throwable t) {
                                Toasty.error(AddressDetailActivity.this, t.toString(), Toast.LENGTH_LONG, true).show();
                                showProgress(false);
                            }
                        });
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }


    @OnClick(R.id.abBack)
    public void onBackClick(){
        onBackPressed();
    }

    @OnClick(R.id.ivCopyPublic)
    public void onCopyivCopyPublicClicked(){
        Utility.copy(tvPublicKeyValue.getText().toString());
        Toasty.success(this, getResources().getString(R.string.copied)).show();
    }

    @OnClick(R.id.ivCopyPrivate)
    public void onCopyivCopyPrivateClicked(){
        Utility.copy(tvPrivateKeyValue.getText().toString());
        Toasty.success(this, getResources().getString(R.string.copied)).show();
    }

    @OnClick(R.id.ivCopyAddress)
    public void onivCopyAddressClicked(){
        Utility.copy(tvAddressValue.getText().toString());
        Toasty.success(this, getResources().getString(R.string.copied)).show();
    }
}
