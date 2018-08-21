package wallet.bitcoin.bitcoinwallet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.Constants;
import wallet.bitcoin.bitcoinwallet.helper.UIHelper;
import wallet.bitcoin.bitcoinwallet.helper.Utility;
import wallet.bitcoin.bitcoinwallet.model.WalletAddress;

public class SpinnerAdapter extends ArrayAdapter<WalletAddress> {

    private static final int textSize = 12;
    private static final int padding = UIHelper.getPixel(8);

    private Context context;
    private List<WalletAddress> contents;
    private boolean isSend;
    private float balance;

    public SpinnerAdapter(boolean isSend, float balance, Context context, int textViewResourceId, List<WalletAddress> values) {
        super(context, textViewResourceId, values);
        this.isSend = isSend;
        this.balance = balance;

        this.context = context;
        this.contents = values;
    }

    @Override
    public int getCount() {
        if (isSend && contents == null) {
            return 1;
        }

        if (!isSend && contents == null) {
            return 0;
        }

        return isSend ? 1 + contents.size() : contents.size();
    }

    @Override
    public WalletAddress getItem(int position) {
        return contents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Spannable text;
        if (isSend) {
            position -= 1;
        }

        if (position < 0) {
            text = getTotalWalletString(balance);
        } else {
            text = getWalletString(contents.get(position));
        }

        TextView txt = new TextView(context);
        txt.setPadding(padding, padding, padding, padding);
        txt.setTextSize(textSize);
        txt.setBackgroundColor(Color.WHITE);
        txt.setTextColor(App.getContext().getResources().getColor(R.color.text_color));

        if (isSend && position < 0) {
            txt.setTextColor(App.getContext().getResources().getColor(R.color.colorAccent));
        }

        txt.setText(text);
        return txt;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewgroup) {
        Spannable text;
        if (isSend) {
            position -= 1;
        }

        if (position < 0) {
            text = getTotalWalletString(balance);
        } else {
            text = getWalletString(contents.get(position));
        }


        TextView txt = new TextView(context);
        txt.setGravity(Gravity.LEFT);
//        if (isSend){
        txt.setTextColor(App.getContext().getResources().getColor(R.color.text_color));
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_36dp, 0);
//        } else {
//            txt.setBackgroundColor(App.getContext().getResources().getColor(R.color.action_bar_color));
//            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_white_36dp, 0);
//        }

        txt.setTextColor(App.getContext().getResources().getColor(R.color.colorAccent));

        txt.setTextSize(textSize);

        txt.setText(text);
        return txt;
    }

    private Spannable getWalletString(WalletAddress preset) {
        StringBuilder poolAppearance = new StringBuilder()
                .append(preset.id)
                .append("\n")
                .append(App.getContext().getResources().getString(R.string.balance))
                .append(": ");

        int len1 = poolAppearance.length();

        poolAppearance.append(Utility.getDoubleStringFormatNoSign(preset.balance)).append(" ").append(Constants.RATE_CURRENCY);

        ForegroundColorSpan spanWhite = new ForegroundColorSpan(App.getContext().getResources().getColor(R.color.text_color));
        ForegroundColorSpan spanFab = new ForegroundColorSpan(App.getContext().getResources().getColor(R.color.colorAccent));

        Spannable spannable = new SpannableString(poolAppearance.toString());
        spannable.setSpan(spanWhite, 0, len1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(spanFab, len1, poolAppearance.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    private Spannable getTotalWalletString(float balance) {
        StringBuilder poolAppearance = new StringBuilder()
                .append(App.getContext().getResources().getString(R.string.send_from_wallet))
                .append("\n")
                .append(App.getContext().getResources().getString(R.string.balance))
                .append(": ");

        int len1 = poolAppearance.length();

        poolAppearance.append(Utility.getDoubleStringFormatNoSign(balance)).append(" ").append(Constants.RATE_CURRENCY);

        ForegroundColorSpan spanWhite = new ForegroundColorSpan(App.getContext().getResources().getColor(R.color.text_color));
        ForegroundColorSpan spanFab = new ForegroundColorSpan(App.getContext().getResources().getColor(R.color.colorAccent));

        Spannable spannable = new SpannableString(poolAppearance.toString());
        spannable.setSpan(spanWhite, 0, len1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(spanFab, len1, poolAppearance.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }
}