package wallet.bitcoin.bitcoinwallet.dialog;

import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.AppPreferenceManager;
import wallet.bitcoin.bitcoinwallet.helper.TicketGenerator;

public class GameDialog extends BaseDialogHelper {

    private static final long TIME_GAME = 1531170000000L;
//private static final long TIME_GAME = 153240L;

    @BindView(R.id.ivTop)
    public ImageView ivTop;

    @BindView(R.id.tvTimeLeft)
    public TextView tvTimeLeft;

    @BindView(R.id.tvDescrSmall)
    public TextView tvDescrSmall;

    @BindView(R.id.llRate)
    public LinearLayout llRate;

    @BindView(R.id.tvDescrLink)
    public TextView tvDescrLink;

    @BindView(R.id.tvDescr)
    public TextView tvDescr;

    @BindView(R.id.tvRate)
    public TextView tvRate;

    private GameDialogCallback callback;

    public interface GameDialogCallback {
        void onOpenDescrLink();

        void onRateOnGP();
    }

    public static GameDialog show(Context context, GameDialogCallback callback) {
        return new GameDialog(context, callback);
    }

    private GameDialog(Context context, GameDialogCallback callback) {
        super(context);
        this.callback = callback;

        init(null);
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.game_dialog, null);
        ButterKnife.bind(this, view);

        return view;
    }

    public void init(String ticket) {
        if (System.currentTimeMillis() < TIME_GAME) {

            long diff = TIME_GAME - System.currentTimeMillis();

            int diffHours = (int)(diff / (60 * 60 * 1000) % 24);
            int diffDays = (int)(diff / (24 * 60 * 60 * 1000));

            tvTimeLeft.setText("" + diffDays + " " +  App.getContext().getResources().getQuantityString(R.plurals.day, diffDays) + " " +
                    diffHours + " " + App.getContext().getResources().getQuantityString(R.plurals.hours, diffHours));

            SpannableString content = new SpannableString(tvDescrLink.getText().toString());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tvDescrLink.setText(content);

            if (ticket == null) {
                ticket = AppPreferenceManager.getInstance().getTicket();
            }

            if (ticket != null && ticket.length() > 0) {
                llRate.setClickable(false);
                tvRate.setText("Your ticket: " + ticket);
            } else {

            }
        } else {
            //game was ended
            if (ticket == null) {
                ticket = AppPreferenceManager.getInstance().getTicket();
            }

            llRate.setVisibility(View.GONE);
            tvTimeLeft.setText("Winner : " + TicketGenerator.WIN_TICKET);
            if (ticket != null && ticket.length() > 0) {
                tvDescrSmall.setText("Your ticket number: " + ticket);
                tvDescr.setText(R.string.thanks);
            } else {
                tvDescrSmall.setText("You had no ticket");
                tvDescr.setText("Maybe, we would repeat the game in the future");
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                ((RelativeLayout.LayoutParams) tvDescrLink.getLayoutParams()).removeRule(RelativeLayout.BELOW);
            }
            ((RelativeLayout.LayoutParams) tvDescrLink.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.tvDescr);

        }
    }

    public void onReceiveTicket(){
        String ticket = TicketGenerator.generate();
        AppPreferenceManager.getInstance().setTicket(ticket);

        init(ticket);
    }

    @OnClick(R.id.llRate)
    public void llRateClick() {
        callback.onRateOnGP();
    }

    @OnClick(R.id.tvDescrLink)
    public void ontvDescrLinkClick() {
        callback.onOpenDescrLink();
    }


}
