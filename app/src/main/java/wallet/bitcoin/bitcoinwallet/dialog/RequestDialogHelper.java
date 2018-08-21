package wallet.bitcoin.bitcoinwallet.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wallet.bitcoin.bitcoinwallet.R;

public class RequestDialogHelper extends BaseDialogHelper{

    @BindView(R.id.btnOk)
    public Button btnOk;

    private RequestDialogHelperCallback listenner;

    public interface RequestDialogHelperCallback{
        void onRequestDialogOkPressed();
    }

    public static void show(Context context, RequestDialogHelperCallback listenner){
        new RequestDialogHelper(context, listenner);
    }

    private RequestDialogHelper(Context context, RequestDialogHelperCallback listenner){
        super(context);
        this.listenner = listenner;

        init();
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.camera_perm_dlg, null);
        ButterKnife.bind(this, view);

        return view;
    }

    private void init(){
        initAnimation(btnOk, 100);
    }

    @OnClick(R.id.btnOk)
    public void btnOkClick(){

        listenner.onRequestDialogOkPressed();

        dismiss();
    }

}

