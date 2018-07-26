package cn.jk.kaoyandanci.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.Word;
import cn.jk.kaoyandanci.model.WordDao;
import cn.jk.kaoyandanci.ui.activity.MyWordListActivity;
import cn.jk.kaoyandanci.util.ToastUtil;

/**
 * Created by jack on 2017/11/24.
 */

public class AddWordDialog extends DialogFragment {

    MyWordListActivity context;
    WordDao wordDao;
    @BindView(R.id.englishTxt)
    EditText englishTxt;
    @BindView(R.id.chineseTxt)
    EditText chineseTxt;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = (MyWordListActivity) getActivity();
        wordDao = context.getWordDao();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_word, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                addWord();
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        ButterKnife.bind(this, view);
        //异步联网拿到中文.考完在做.
//        englishTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                String english = englishTxt.getText().toString();
//                if (!hasFocus) {
//
//                    if (english == null || english.isEmpty()) {
//                        return;
//                    }
//                }
//                String queryUrl = Constant.cibaUrl + english.toLowerCase();
//                JSONObject queryResult = new JSONObject();
//                try {
//                    queryResult = JsonReader.readJsonFromUrl(queryUrl);
//                    queryResult
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        return builder.create();

    }

    private void addWord() {
        String english = englishTxt.getText().toString();
        if (english == null || english.isEmpty()) {
            ToastUtil.showShort(context, "英文不能为空");
            return;
        }
        boolean alreadyExist = (1 == wordDao.queryBuilder().where(WordDao.Properties.English.eq(english)).list().size());
        if (alreadyExist) {
            ToastUtil.showShort(context, "单词表中已经有这个单词了");
            return;
        }
        String chinese = chineseTxt.getText().toString();
        if (chinese == null || chinese.isEmpty()) {
            ToastUtil.showShort(context, "中文不能为空");
            return;
        }
        Word word = new Word();
        word.setEnglish(english);
        word.setChinese(chinese);
        word.setCollect(1);
        word.setEasy(false);
        wordDao.insert(word);
        context.addWord(word);
        ToastUtil.showShort(context, "添加成功");
    }


}
