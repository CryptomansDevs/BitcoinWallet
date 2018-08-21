package wallet.bitcoin.bitcoinwallet.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class AppRecyclerView extends RecyclerView {

    private View emptyView;

    private OnAdapterSizeChanged onAdapterSizeChanged;

    public interface OnAdapterSizeChanged{
        void onEmptyViewVisible(boolean isEmptyViewVisible);
    }

    public void setOnAdapterSizeChanged(OnAdapterSizeChanged onAdapterSizeChanged){
        this.onAdapterSizeChanged = onAdapterSizeChanged;
    }

    private final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public AppRecyclerView(Context context) {
        super(context);
    }

    public AppRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible = getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);

            if (onAdapterSizeChanged != null) {
                onAdapterSizeChanged.onEmptyViewVisible(emptyViewVisible);
            }
//            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }
}


