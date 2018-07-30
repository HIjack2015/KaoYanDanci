package cn.jk.kaoyandanci.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.jk.kaoyandanci.InitApplication;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.DaoSession;
import cn.jk.kaoyandanci.model.Word;
import cn.jk.kaoyandanci.model.WordDao;
import cn.jk.kaoyandanci.ui.activity.AdvanceSettingActivity;
import cn.jk.kaoyandanci.ui.activity.MainActivity;
import cn.jk.kaoyandanci.ui.activity.WordListActivity;
import cn.jk.kaoyandanci.ui.dialog.DownloadDialog;
import cn.jk.kaoyandanci.ui.dialog.PleaseDonateDialog;
import cn.jk.kaoyandanci.util.Constant;
import cn.jk.kaoyandanci.util.DateLongGson;
import cn.jk.kaoyandanci.util.FileUtil;
import cn.jk.kaoyandanci.util.MD5;
import cn.jk.kaoyandanci.util.NetWorkUtil;
import cn.jk.kaoyandanci.util.TencentCloudService;
import cn.jk.kaoyandanci.util.ToastUtil;

import static cn.jk.kaoyandanci.util.Constant.WORD_LIST_LBL;

/**
 * A simple {@link Fragment} subclass.
 */

public class AdvanceSettingFragment extends PreferenceFragment {
    Gson mGson = DateLongGson.gson;
    DownloadDialog downloadDialog;
    AdvanceSettingActivity context;
    MaterialDialog loadingDialog;
    public AdvanceSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.advance_setting);
        context = (AdvanceSettingActivity) getActivity();
        loadingDialog = new MaterialDialog.Builder(getActivity()).content(R.string.please_wait).build();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Preference pieWordPref = findPreference(Constant.PIE_WORD);
        pieWordPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                MainActivity.DATA_CHANGED=true;
                return true;
            }
        });
        final SwitchPreference coreModePreference = (SwitchPreference) findPreference(getString(R.string.core_mode));
        coreModePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                MainActivity.DATA_CHANGED=true;
                return true;
            }
        });
        final SwitchPreference easyModePreference = (SwitchPreference) findPreference(getString(R.string.hide_easy));
        easyModePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                MainActivity.DATA_CHANGED=true;
                return true;
            }
        });

        Preference exportWordPref = findPreference(getString(R.string.export_word));
        exportWordPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                new MaterialDialog.Builder(context)
                        .title(R.string.please_select_export_type)
                        .items(R.array.word_type)
                        .positiveText(R.string.confirm)
                        .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, final Integer[] which, CharSequence[] text) {
                                if (which.length == 0) {
                                    ToastUtil.showShort(context, "你得至少选一项啊...");
                                    return false;
                                }

                                ToastUtil.showShort(context, "正在将单词导出为到sdcard/kaoyandanci/word.txt,请稍候.");

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        exportWord(which);
                                        context.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.showShort(context, "已经导出到sdcard/kaoyandanci/word.txt.");
                                            }
                                        });
                                    }
                                }).start();
                                return true;
                            }
                        })
                        .show();


                return true;
            }
        });
        Preference downloadVoicePackPref = findPreference(getString(R.string.download_voice_pack));
        downloadVoicePackPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (!NetWorkUtil.isOnline(getActivity())) {
                    ToastUtil.showShort(getActivity(), "请先联网");
                    return false;
                }
                new PleaseDonateDialog().show(getFragmentManager(), "pleaseDonate");
                return false;
            }
        });
        Preference easyWordListPref = findPreference(getString(R.string.easy_word_list));
        easyWordListPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(context, WordListActivity.class);
                intent.putExtra(WORD_LIST_LBL, Constant.EASY);
                startActivity(intent);
                return false;
            }
        });
        Preference backupPref = findPreference(getString(R.string.back_to_server));
        backupPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (!NetWorkUtil.isOnline(getActivity())) {
                    ToastUtil.showShort(getActivity(), "请先联网");
                    return false;
                }
                new MaterialDialog.Builder(context)
                        .title(R.string.please_input_record_name_to_backup).
                        inputType(InputType.TYPE_CLASS_TEXT)
                        .input(R.string.sure_to_unique, R.string.input_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                backUpByName(input.toString(), true);
                            }
                        })
                        .positiveText(R.string.confirm)
                        .negativeText(R.string.cancel)
                        .show();

                return false;
            }
        });
        Preference restorePref = findPreference(getString(R.string.restore_record));
        restorePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (!NetWorkUtil.isOnline(getActivity())) {
                    ToastUtil.showShort(getActivity(), "请先联网");
                    return false;
                }
                new MaterialDialog.Builder(context)
                        .title(R.string.please_input_record_name_to_restore).
                        inputType(InputType.TYPE_CLASS_TEXT)
                        .input(R.string.will_clear_local, R.string.input_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                restoreByName(input.toString());
                            }
                        })
                        .positiveText(R.string.confirm)
                        .negativeText(R.string.cancel)
                        .show();

                return false;
            }
        });
    }

    void restoreByName(final String recordName) {
        if (TextUtils.isEmpty(recordName)) {
            showResultMsg("名字不能为空");
            return;
        }
        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean exist = TencentCloudService.checkFileExist(recordName);

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (exist) {
                            restoreToLocal(recordName);
                        } else {
                            showResultMsg("我没有找到这个名字对应的备份记录...");
                        }

                    }
                });
            }
        }).start();
    }

    private void restoreToLocal(final String recordName) {
        final String savePath = context.getFilesDir().getAbsolutePath();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result = TencentCloudService.downloadFile(recordName, savePath, context);

                context.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (result.equals("下载成功")) {

                            restoreFromLocal(recordName);
                        } else {
                            showResultMsg(result);
                        }

                    }
                });
            }
        }).start();

    }

    private void restoreFromLocal(String recordName) {

        try {
            String content = FileUtil.getStringFromFile(context.getFilesDir() + "/" + recordName);
            Type listType = new TypeToken<List<Word>>() {
            }.getType();

            List<Word> words = mGson.fromJson(content, listType);

            DaoSession daoSession = ((InitApplication) context.getApplicationContext()).getDaoSession();
            WordDao wordDao = daoSession.getWordDao();

            wordDao.updateInTx(words);

            context.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    showResultMsg("恢复成功");
                    MainActivity.DATA_CHANGED=true;

                }
            });
        } catch (final Exception e) {

            showResultMsg(e.getMessage());
        }
    }

    private void showResultMsg(final String msg) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
                ToastUtil.showShort(context, msg);
            }
        });


    }

    /**
     * 检查参数并调用真正的备份 ,TODO 这个写的太乱了,我需要rx java.
     *
     * @param recordName
     */
    void backUpByName(final String recordName, final boolean checkExist) {
        if (TextUtils.isEmpty(recordName)) {
            ToastUtil.showShort(context, "名字不能为空");
            return;
        }
        loadingDialog.setTitle("正在备份,请稍候...");
        loadingDialog.show();
        if (checkExist) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final boolean exist = TencentCloudService.checkFileExist(recordName);

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (exist) {
                                loadingDialog.dismiss();
                                new MaterialDialog.Builder(context)
                                        .title(R.string.confirm_to_overwrite).positiveText(R.string.confirm)
                                        .negativeText(R.string.cancel).onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        backUpByName(recordName, false);
                                    }
                                })
                                        .show();

                            } else {
                                backUpByName(recordName, false);
                            }
                        }
                    });
                }
            }).start();
            return;

        }


        new

                Thread(new Runnable() {
            @Override
            public void run() {
                final String result = backRecordToServer(recordName);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(context, result);
                        loadingDialog.dismiss();

                    }
                });
            }
        }).

                start();


    }

    /**
     * 将学习记录转成json文件存起来,然后上传到云端.
     *
     * @param recordName
     * @return 要提示给用户的消息.
     */
    private String backRecordToServer(String recordName) {
        QueryBuilder<Word> queryBuilder = context.getWordDao().queryBuilder();
        List<Word> words = queryBuilder.where(WordDao.Properties.LastLearnTime.isNotNull()).list();
        //   List<WordRecord> wordRecords = new ArrayList<>();
//        for (Word word : words) {
//            wordRecords.add(new WordRecord(word));
//        }
        String wordRecordJsonStr = mGson.toJson(words);
        File file = new File(context.getFilesDir(), recordName);
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(recordName, Context.MODE_PRIVATE);
            outputStream.write(wordRecordJsonStr.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return TencentCloudService.uploadFile(recordName, file.getAbsolutePath(), context);


    }


    /**
     * 导出单词至sdcard/kaoyandanci/word.txt.完成后提示
     *
     * @param which <item>已掌握</item>
     *              <item>已认识</item>
     *              <item>不认识</item
     */
    void exportWord(Integer[] which) {

        QueryBuilder<Word> queryBuilder = context.getWordDao().queryBuilder();
        List<WhereCondition> whereConditions = new ArrayList<>();
        for (int i = 0; i < which.length; i++) {
            int value = which[i];

            switch (value) {
                case 0:
                    whereConditions.add(WordDao.Properties.NeverShow.isNotNull());
                    break;
                case 1:
                    whereConditions.add(WordDao.Properties.KnowTime.isNotNull());
                    break;
                case 2:
                    whereConditions.add(WordDao.Properties.UnknownTime.isNotNull());
                    break;
            }
        }
            if (which.length == 1) {
                queryBuilder.where(whereConditions.get(0));
            } else if (which.length == 2) {
                queryBuilder.whereOr(whereConditions.get(0), whereConditions.get(1));
            } else {
                queryBuilder.whereOr(whereConditions.get(0), whereConditions.get(1), whereConditions.get(2));
            }


        List<Word> learnedWords =
                queryBuilder.list();
        StringBuilder sb = new StringBuilder();
        sb.append("英文 中文 已经掌握 认识次数 不认识次数\n");
        for (Word word : learnedWords) {
            int knowTime = word.getKnowTime() == null ? 0 : word.getKnowTime();
            int neverShow = word.getNeverShow() == null ? 0 : word.getNeverShow();
            int unknownTime = word.getUnknownTime() == null ? 0 : word.getUnknownTime();
            sb.append(String.format("%16s %31s,%2d %2d %2d\n",
                    word.getEnglish(), word.getChinese(), neverShow, knowTime, unknownTime));
        }
        String filePath = Environment.getExternalStorageDirectory()
                + File.separator + "kaoyandanci" + File.separator + "word.txt";
        FileUtil.saveString(context, sb.toString(), filePath);
    }


    public void startDownload() {
        downloadDialog = new DownloadDialog();
        downloadDialog.show(getFragmentManager(), "downloadProgressDialog");

        final AsyncTask<String, String, String> asyncTask = new DownloadFileFromURL();
        downloadDialog.setTask(asyncTask);
        asyncTask.execute(new String[]{Constant.downloadVoicePackUrl});
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {


        /**
         * A
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {

            File downloadZip = new File(Environment
                    .getExternalStorageDirectory().toString()
                    + Constant.ENGLISH_ZIP_PATH);
            boolean exist = MD5.checkMD5(Constant.VOICE_PACK_MD5, downloadZip);
            if (exist) {
                publishProgress("exists");
                this.cancel(true);
                return null;
            }


            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                new File(Environment
                        .getExternalStorageDirectory().toString()
                        + Constant.ENGLISH_FILE_DIR).mkdirs();
                // Output stream
                OutputStream output = new FileOutputStream(downloadZip);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            if ("exists".equals(progress[0])) {
                ToastUtil.showShort(getActivity(), getActivity().getString(R.string.already_have_pack));
                downloadDialog.dismiss();
            } else {
                downloadDialog.setProgress(Integer.parseInt(progress[0]));
            }


        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            if (isCancelled()) {
                return;
            }
            // dismiss the dialog after the file was downloaded
            final File zipFile = new File(Environment
                    .getExternalStorageDirectory().toString()
                    + Constant.ENGLISH_ZIP_PATH);
            final File unzipPath = new File(Environment
                    .getExternalStorageDirectory().toString()
                    + Constant.ENGLISH_FILE_DIR);
            if (context != null && context.visible) {
                downloadDialog.dismiss();
                ToastUtil.showShort(getActivity(), getActivity().getString(R.string.unziping));
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileUtil.unzip(zipFile, unzipPath);
                    if (context != null && context.visible) {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downloadDialog.dismiss();
                                ToastUtil.showShort(context, context.getString(R.string.unzip_finished));
                            }
                        });
                    }
                }
            }).start();


        }

    }

}
