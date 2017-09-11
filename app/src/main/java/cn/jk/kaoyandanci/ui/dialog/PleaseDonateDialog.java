package cn.jk.kaoyandanci.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.ui.fragment.AdvanceSettingFragment;

/**
 * Created by Administrator on 2017/6/29.
 */

public class PleaseDonateDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.pleaseDonate)
                .setPositiveButton(R.string.confirm_use_voice_pack, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AdvanceSettingFragment fragment = (AdvanceSettingFragment) getActivity().getFragmentManager().findFragmentById(R.id.content_frames);
                        fragment.startDownload();
                    }
                })
                .setNegativeButton(R.string.cancel_use_voice_pack, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}