package cn.jk.kaoyandanci.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import cn.jk.kaoyandanci.R;

/**
 * Created by Administrator on 2017/7/22.
 */

public class DownloadDialog extends DialogFragment {

    public ProgressDialog progressDialog;
    private AsyncTask asyncTask;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("下载语音包中");
        progressDialog.setCancelable(true);
        progressDialog.setMax(100);

        return progressDialog;
    }

    public void setTask(AsyncTask asyncTask) {
        this.asyncTask = asyncTask;
    }

    public void setProgress(int progress) {
        if (progress == 99) {
            progressDialog.setMessage(getString(R.string.unziping));
        } else {
            progressDialog.setMessage("下载语音包中" + progress + "/100");
        }
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        asyncTask.cancel(true);
    }


    public void setMessage(String s) {
        progressDialog.setMessage(s);
    }
}
