package com.gongva.library.plugin.netbase;

import com.hik.core.android.api.LogCat;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @author
 * @date 2019/5/16
 * @email
 */
public class CustomerX509TrustManager implements X509TrustManager {

    private List<String> mClientEncodeds;
    private List<String> mClientSubjects;
    private List<String> mClientIssUsers;
    private volatile boolean mCanCheckCertificate;
    private volatile boolean mCanCheckCertificateExpiration;

    public CustomerX509TrustManager(KeyStore keyStore) {
        mClientEncodeds = new ArrayList<>();
        mClientSubjects = new ArrayList<>();
        mClientIssUsers = new ArrayList<>();
        initClientCertificate(keyStore);
        mCanCheckCertificate = true;//默认开启证书校验
        mCanCheckCertificateExpiration = true;//默认开启证书时间过期校验
    }

    public void setCheckCertificateStrategy(boolean canCheckCertificate, boolean canCheckCertificateExpiration) {
        this.mCanCheckCertificate = canCheckCertificate;
        this.mCanCheckCertificateExpiration = canCheckCertificateExpiration;
    }

    private void initClientCertificate(KeyStore keyStore) {
        try {
            if (keyStore != null && keyStore.size() > 0) {
                Enumeration<String> aliases = keyStore.aliases();
                while (aliases.hasMoreElements()) {
                    String alias = aliases.nextElement();
                    X509Certificate clientCertificate = (X509Certificate) keyStore.getCertificate(alias);

                    mClientEncodeds.add(new BigInteger(1, clientCertificate.getPublicKey().getEncoded()).toString(16));
                    mClientSubjects.add(clientCertificate.getSubjectDN().getName());
                    mClientIssUsers.add(clientCertificate.getIssuerDN().getName());
                }
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (!mCanCheckCertificate) {
            LogCat.d("close certificate verify");
            return;
        }
        if (chain == null) {
            throw new CertificateException("checkServerTrusted: X509Certificate array is null");
        }
        if (chain.length < 1) {
            throw new CertificateException("checkServerTrusted: X509Certificate is empty");
        }
        if (!(null != authType && authType.equals("ECDHE_RSA"))) {
            throw new CertificateException("checkServerTrusted: AuthType is not ECDHE_RSA");
        }

        //检查所有证书
        if (mCanCheckCertificateExpiration) {
            try {
                TrustManagerFactory factory = TrustManagerFactory.getInstance("X509");
                factory.init((KeyStore) null);
                for (TrustManager trustManager : factory.getTrustManagers()) {
                    ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
                }
            } catch (NoSuchAlgorithmException | KeyStoreException e) {
                e.printStackTrace();
            }
        } else {
            LogCat.d("close certificate expire time verify");
        }

        //获取网络中的证书信息
        X509Certificate certificate = chain[0];
        PublicKey publicKey = certificate.getPublicKey();
        String serverEncoded = new BigInteger(1, publicKey.getEncoded()).toString(16);

        if (!mClientEncodeds.contains(serverEncoded)) {
            throw new CertificateException("server's PublicKey is not equals to client's PublicKey");
        }
        String subject = certificate.getSubjectDN().getName();
        if (!mClientSubjects.contains(subject)) {
            throw new CertificateException("server's subject is not equals to client's subject");
        }
        String issuser = certificate.getIssuerDN().getName();
        if (!mClientIssUsers.contains(issuser)) {
            throw new CertificateException("server's issuser is not equals to client's issuser");
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}