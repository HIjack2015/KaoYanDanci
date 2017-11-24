package cn.jk.kaoyandanci.ui.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.model.Word;
import cn.jk.kaoyandanci.model.WordDao;
import cn.jk.kaoyandanci.ui.activity.AdvanceSettingActivity;
import cn.jk.kaoyandanci.ui.activity.WordListActivity;
import cn.jk.kaoyandanci.ui.dialog.DownloadDialog;
import cn.jk.kaoyandanci.ui.dialog.PleaseDonateDialog;
import cn.jk.kaoyandanci.util.Constant;
import cn.jk.kaoyandanci.util.FileUtil;
import cn.jk.kaoyandanci.util.MD5;
import cn.jk.kaoyandanci.util.NetWordUtil;
import cn.jk.kaoyandanci.util.ToastUtil;

import static cn.jk.kaoyandanci.util.Constant.WORD_LIST_LBL;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvanceSettingFragment extends PreferenceFragment {

    DownloadDialog downloadDialog;
    AdvanceSettingActivity context;

    public AdvanceSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.advance_setting);
        context = (AdvanceSettingActivity) getActivity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Preference pieWordPref = findPreference(Constant.PIE_WORD);
        pieWordPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Constant.DATA_CHANGED = true;
                return true;
            }
        });
        final SwitchPreference coreModePreference = (SwitchPreference) findPreference(getString(R.string.core_mode));
        coreModePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Constant.DATA_CHANGED = true;
                return true;
            }
        });
        final SwitchPreference easyModePreference = (SwitchPreference) findPreference(getString(R.string.hide_easy));
        easyModePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Constant.DATA_CHANGED = true;
                return true;
            }
        });

        Preference exportWordPref = findPreference(getString(R.string.export_word));
        exportWordPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ToastUtil.showShort(context, "正在将单词导出为到sdcard/kaoyandanci/word.txt,请稍候.");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        exportWord();
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
        });
        Preference downloadVoicePackPref = findPreference(getString(R.string.download_voice_pack));
        downloadVoicePackPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (!NetWordUtil.isOnline(getActivity())) {
                    ToastUtil.showShort(getActivity(), "请先联网");
                    return false;
                }
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //有时间写onActivity result.反正接下来也有dialog.
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10
                    );

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
    }


    /**
     * 导出单词至sdcard/kaoyandanci/word.txt.完成后提示
     */
    private void exportWord() {
        List<Word> learnedWords = context.getWordDao().queryBuilder().
                where(WordDao.Properties.KnowTime.isNotNull()).list();
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
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ToastUtil.showShort(context, "请给我存储权限");
            return;
        }
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
                ToastUtil.showShort(getContext(), getContext().getString(R.string.already_have_pack));
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
