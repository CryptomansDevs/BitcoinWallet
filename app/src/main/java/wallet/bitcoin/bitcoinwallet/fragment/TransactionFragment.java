package wallet.bitcoin.bitcoinwallet.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
import wallet.bitcoin.bitcoinwallet.rest.request.GetAllTxsRequest;
import wallet.bitcoin.bitcoinwallet.rest.response.GetTxsResponse;
import wallet.bitcoin.bitcoinwallet.view.AppRecyclerView;

public class TransactionFragment extends BaseFragment{

    public static final int OFFSET = Constants.OFFSET;

    @BindView(R.id.rlProgress)
    public SmoothProgressBar rlProgress;

    @BindView(R.id.rvAddresses)
    public AppRecyclerView rvAddresses;

    @BindView(R.id.emptyView)
    public LinearLayout emptyView;

    private DetailsAdapter adapter;

    public int offsetIndex = 0;

    public static TransactionFragment newInstance(){
        TransactionFragment instance = new TransactionFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) LayoutInflater.from(App.getContext()).inflate(R.layout.fragment_transactions, container, false);
        ButterKnife.bind(this, mRootView);

        init();
        return mRootView;
    }

    private void init(){
        LinearLayoutManager quickLinkLayoutManager = new LinearLayoutManager(rvAddresses.getContext(), LinearLayoutManager.VERTICAL, false);
        rvAddresses.setLayoutManager(quickLinkLayoutManager);
        rvAddresses.setHasFixedSize(true);
        rvAddresses.setEmptyView(emptyView);

        adapter = new DetailsAdapter(null);
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

                if (adapter.getItemCount() < OFFSET + offsetIndex){
                    loadMore = false;
                }

                if (loadMore) {
                    offsetIndex += OFFSET;
                    getTransactions(null, offsetIndex);
                }
            }
        };
        rvAddresses.addOnScrollListener(mScrollListener);
    }

    public void getAllTxs(OnFinishCallback callback){
        offsetIndex = 0;

        getTransactions(callback, offsetIndex);
    }

    public void onRequestAllTxs(){
        offsetIndex = 0;

        getTransactions(null, offsetIndex);
    }

    private void getTransactions(final OnFinishCallback callback, final int offset){
        if (offset == 0) {
            showProgress(true);
        }

        if (offset != 0) {
            adapter.showFooter();
        }

        Call<GetTxsResponse> balanceRequestCall = App.getRestClient().getApiService().
                getAllTxs(new GetAllTxsRequest(App.getCurrentUser().accessToken, Constants.CURRENCY, offset, offset + OFFSET));

        balanceRequestCall.enqueue(new Callback<GetTxsResponse>() {
            @Override
            public void onResponse(Call<GetTxsResponse> call, Response<GetTxsResponse> response) {
                if (response.isSuccessful()) {

                    GetTxsResponse responseBody = response.body();
                    if (responseBody.success) {

                        List<GetTxsResponse.Result> results = responseBody.result;
                        if (offset == 0) {
                            if (adapter != null) {
                                adapter.setItems(results);
                            }
                        } else {
                            if (adapter != null) {
                                adapter.addItems(results);
                                adapter.hideFooter();
                            }
                        }
                    } else {
                        if (getActivity() != null) {
                            Toasty.error(getActivity(), responseBody.error_msg, Toast.LENGTH_LONG, true).show();
                        }
                    }

                    showProgress(false);
                } else {
                    if (getActivity() != null){
                        Toasty.error(getActivity(), response.message(), Toast.LENGTH_LONG, true).show();
                    }
                    showProgress(false);
                }

                if (callback != null) {
                    callback.onFinished();
                }
            }

            @Override
            public void onFailure(Call<GetTxsResponse> call, Throwable t) {
                if (getActivity() != null) {
                    Toasty.error(getActivity(), t.toString(), Toast.LENGTH_LONG, true).show();
                }
                showProgress(false);

                if (callback != null) {
                    callback.onFinished();
                }
            }
        });
    }

    @Override
    public void showProgress(boolean progressShown) {
        if (rlProgress == null){
            return;
        }

        if (progressShown) {
            rlProgress.setVisibility(View.VISIBLE);
        } else {
            rlProgress.setVisibility(View.GONE);
        }
    }
}
