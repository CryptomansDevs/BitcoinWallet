package wallet.bitcoin.bitcoinwallet.auth;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import wallet.bitcoin.bitcoinwallet.R;
import wallet.bitcoin.bitcoinwallet.helper.App;
import wallet.bitcoin.bitcoinwallet.helper.UIHelper;
import wallet.bitcoin.bitcoinwallet.helper.Utility;

public class IndicatorController {

    @BindView(R.id.indicator)
    public View indicator;

    @BindView(R.id.vDots)
    public View vDots;

    public View activity_tutorial;

    private static final int color1 = App.getContext().getResources().getColor(R.color.onboadingback_1);
    private static final int color2 = App.getContext().getResources().getColor(R.color.onboadingback_2);
    private static final int color3 = App.getContext().getResources().getColor(R.color.onboadingback_3);
    private static final int color4 = Color.parseColor("#141414");
    private static final int color5 = Color.parseColor("#141414");

    public IndicatorController(View activity_tutorial){
        ButterKnife.bind(this, activity_tutorial);
        this.activity_tutorial = activity_tutorial;

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {Color.parseColor("#3f3f3f"), Color.parseColor("#141414")});
        gd.setCornerRadius(0f);
        activity_tutorial.setBackgroundDrawable(gd);
        Utility.setBackgroundColor(vDots, Color.parseColor("#141414"));
    }

    public void onPageScrolled(float positionOffset, int position){
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) indicator.getLayoutParams();
        params.leftMargin = (int)((positionOffset + position) * UIHelper.getPixel(18f));
        indicator.requestLayout();

//        int color;
//        switch (position){
//            case 0:
//                color = UIHelper.blendColors(color1, color2, positionOffset);
//                break;
//            case 1:
//                color = UIHelper.blendColors(color2, color3, positionOffset);
//                break;
//            case 2:
//                color = UIHelper.blendColors(color3, color4, positionOffset);
//                break;
//            default:
//                color = UIHelper.blendColors(color4, color5, positionOffset);
//                break;
//        }
//
////        Utility.setBackgroundColor(vDots, color);
////        activity_tutorial.setBackgroundColor(color);
//
//        if (position == 3){
//
//        }
    }
}
