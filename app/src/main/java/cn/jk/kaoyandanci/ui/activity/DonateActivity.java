package cn.jk.kaoyandanci.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jk.kaoyandanci.R;
import cn.jk.kaoyandanci.util.FileUtil;
import cn.jk.kaoyandanci.util.ToastUtil;

public class DonateActivity extends BaseActivity {

    public static final String weixin = "微信";
    public static final String alipay = "支付宝";
    @BindView(R.id.alipayBtn)
    Button alipayBtn;
    @BindView(R.id.weixinBtn)
    Button weixinBtn;
    @BindView(R.id.openAlipayBtn)
    Button openAlipayBtn;
    @BindView(R.id.donateImg)
    ImageView donateImg;
    @BindView(R.id.saveDonateImgBtn)
    Button saveDonateImgBtn;
    String currentImgType = weixin;

    final int requestPermissionCode = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("捐赠开发者");
    }

    @OnClick(R.id.alipayBtn)
    public void onAlipayBtnClicked() {
        openAlipayBtn.setVisibility(View.VISIBLE);
        donateImg.setImageResource(R.drawable.donate_alipay);
        currentImgType = alipay;
    }

    @OnClick(R.id.weixinBtn)
    public void onWeixinBtnClicked() {
        openAlipayBtn.setVisibility(View.INVISIBLE);
        donateImg.setImageResource(R.drawable.donate_weixin);
        currentImgType = weixin;
    }

    @OnClick(R.id.openAlipayBtn)
    public void onOpenAlipayBtnClicked() {
        String alipayUrl = getString(R.string.alipayUrl);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(alipayUrl));
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case requestPermissionCode: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveQrImg();
                } else {
                    ToastUtil.showShort(context, "请给我存储权限QAQ");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void saveQrImg() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestPermissionCode
            );
            return;
        }

        int resId = currentImgType.equals(weixin) ? R.drawable.donate_weixin : R.drawable.donate_alipay;
        Bitmap donateBm = BitmapFactory.decodeResource(getResources(), resId);

        String fileName = currentImgType.equals(weixin) ? "IMG_donateByWeiXin.png" : "IMG_donateByAlipay.png";

        FileUtil.saveImg(context, fileName, donateBm);

        ToastUtil.showShort(context, String.format("已将二维码保存至本地,请打开%s扫一扫", currentImgType));
    }

    @OnClick(R.id.saveDonateImgBtn)
    public void onViewClicked() {
        saveQrImg();
    }
}
