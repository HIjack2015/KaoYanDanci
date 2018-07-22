package cn.jk.kaoyandanci.util;

import android.content.Context;
import android.util.Log;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.Region;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.utils.StringUtils;
import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider;
import com.tencent.qcloud.core.auth.BasicQCloudCredentials;
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <pre>
 *     author : jiakang
 *     e-mail : 1079153785@qq.com
 *     time   : 2018/07/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TencentCloudService {

    static final String bucketName = "kydc-record";
    static final String prefix = "http://kydc-record-1253381150.cos.ap-chengdu.myqcloud.com";
    public static String please = "这个存储空间里只有用户的学习记录备份,这个appId和secret只有cos的读写权限.所以对您来说没有任何价值," +
            "而且这个账户里边也没有钱.所以请不要乱搞,多谢.";
    public static String notice = "诶.我也知道这样不安全.但我实在是太懒了.:>,PS: 腾讯云文档真不好用";


    public static CosXmlService getService(Context context) {
        String appid = "1253381150";
        String region = Region.AP_Chengdu.getRegion();

        String secretId = "AKIDqaMzdAcoxyqaDiTJPPHz0viMsGVc2P6f";
        String secretKey = "a2MYZTCl9PVP8f1CBzc4LRPErKcjfQ1U";
        long keyDuration = 600; //SecretKey 的有效时间，单位秒

//创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion(appid, region)
                .setDebuggable(true)
                .builder();

//创建获取签名类(请参考下面的生成签名示例，或者参考 sdk中提供的ShortTimeCredentialProvider类）
        LocalCredentialProvider localCredentialProvider = new LocalCredentialProvider(secretId, secretKey, keyDuration);


        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, localCredentialProvider);
        return cosXmlService;
    }

    static class LocalCredentialProvider extends BasicLifecycleCredentialProvider {
        private String secretKey;
        private long keyDuration;
        private String secretId;

        public LocalCredentialProvider(String secretId, String secretKey, long keyDuration) {
            this.secretId = secretId;
            this.secretKey = secretKey;
            this.keyDuration = keyDuration;
        }

        /**
         * 返回 BasicQCloudCredentials
         */
        @Override
        public QCloudLifecycleCredentials fetchNewCredentials() throws CosXmlClientException {
            long current = System.currentTimeMillis() / 1000L;
            long expired = current + 112222;
            String keyTime = current + ";" + expired;
            return new BasicQCloudCredentials(secretId, secretKeyToSignKey(secretKey, keyTime), keyTime);
        }

        private String secretKeyToSignKey(String secretKey, String keyTime) {
            String signKey = null;
            try {
                if (secretKey == null) {
                    throw new IllegalArgumentException("secretKey is null");
                }
                if (keyTime == null) {
                    throw new IllegalArgumentException("qKeyTime is null");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            try {
                byte[] byteKey = secretKey.getBytes("utf-8");
                SecretKey hmacKey = new SecretKeySpec(byteKey, "HmacSHA1");
                Mac mac = Mac.getInstance("HmacSHA1");
                mac.init(hmacKey);
                signKey = StringUtils.toHexString(mac.doFinal(keyTime.getBytes("utf-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            return signKey;
        }
    }


    public static boolean checkFileExist(String fileName) {
        try {

            URL url = new URL(getPathByName(fileName));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();

            return (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception exception) {
            Log.d("aaaa", exception.getStackTrace().toString());
            return false;
        }
    }

    public static final String uploadFile(String fileName, String localPath, Context context) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, localPath);
        try {
            getService(context).putObject(putObjectRequest);
            return "上传成功";
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
    }

    public static String downloadFile(String fileName, String savePath, Context context) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName, savePath);
     
        try {
            GetObjectResult getObjectResult = getService(context).getObject(getObjectRequest);
            return "下载成功";
        } catch (CosXmlClientException e) {
            Log.w("TEST", "CosXmlClientException =" + e.toString());
            return "下载失败" + e.getMessage();
        } catch (CosXmlServiceException e) {
            Log.w("TEST", "CosXmlServiceException =" + e.toString());
            return "下载失败" + e.getMessage();
        }


    }

    public static final String getPathByName(String fileName) {
        return prefix + "/" + fileName;
    }
}
