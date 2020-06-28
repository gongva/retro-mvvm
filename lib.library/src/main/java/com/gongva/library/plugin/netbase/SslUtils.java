package com.gongva.library.plugin.netbase;

import android.content.Context;
import android.content.res.AssetManager;

import com.hik.core.android.api.LogCat;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * @author
 * @date 2019/5/27
 * @email
 */
public class SslUtils {

    private static final String TAG = "SslUtils";
    private static CustomerX509TrustManager sCustomerX509TrustManager;

    /**
     * 设置证书检查策略
     *
     * @param canCheckCertificate           是否启用http证书检查
     * @param canCheckCertificateExpiration 是否检查证书有效性
     */
    public static void setCheckCertificateStrategy(boolean canCheckCertificate, boolean canCheckCertificateExpiration) {
        if (sCustomerX509TrustManager != null) {
            sCustomerX509TrustManager.setCheckCertificateStrategy(canCheckCertificate, canCheckCertificateExpiration);
        }
    }

    //读取证书内容到KeyStore中
    private static KeyStore getKeyStore(Context context, String... fileNames) {
        KeyStore keyStore = null;
        try {
            AssetManager assetManager = context.getAssets();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            String keyStoreType = KeyStore.getDefaultType();
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            for (String fileName : fileNames) {
                InputStream caInput = assetManager.open(fileName);
                Certificate ca;
                try {
                    ca = cf.generateCertificate(caInput);
                    LogCat.d(TAG, "ca=" + ((X509Certificate) ca).getSubjectDN());
                } finally {
                    caInput.close();
                }

                keyStore.setCertificateEntry(fileName, ca);
            }
        } catch (Exception e) {
            LogCat.e(TAG, "Error during getting keystore", e);
        }
        return keyStore;
    }

    //生成SSLContext以便用于Retrofit的配置中
    public static SSLContext getSslContextForCertificateFile(Context context, String... fileNames) {
        try {
            KeyStore keyStore = SslUtils.getKeyStore(context, fileNames);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            //TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            //trustManagerFactory.init(keyStore);
            //sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            sCustomerX509TrustManager = new CustomerX509TrustManager(keyStore);
            TrustManager[] trustManagers = {sCustomerX509TrustManager};
            sslContext.init(null, trustManagers, new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            String msg = "Error during creating SslContext for certificate from assets";
            LogCat.e(TAG, msg, e);
            throw new RuntimeException(msg);
        }
    }
}
