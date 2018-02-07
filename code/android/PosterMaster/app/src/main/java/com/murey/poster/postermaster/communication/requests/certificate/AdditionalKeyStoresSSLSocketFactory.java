package com.murey.poster.postermaster.communication.requests.certificate;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Allows you to trust certificates from additional KeyStores in addition to
 * the default KeyStore
 *
 *  * HOW TO CREATE OWN CERTIFICATE
 *
 * PRECONDITION
 *      - download the bcprov-jdk15on-157.jar to create your own keystore
 *
 * ///////
 * // 1 //  Generate all the certificate for "webservice.you.want.fr" in the file "mycert.pem" on the current repository
 * ///////
 * echo | openssl s_client -connect webservice.you.want.fr:443 2>&1 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > mycert.pem
 *
 * ///////
 * // 2 //  Generate your own KeyStore file using "keytool" in the directory you want (for me it's "/c/Users/guillaume.m/Downloads/Programmes/mystore.bks")
 * ///////  Use the storepass you want and the alias you want. Don't forget to set the providerpath where the bcprov-jdk15on-157.jar is.
 *
 * "/c/Program Files/Java/jre1.8.0_144/bin/keytool.exe" \
 *      -genkey -alias com.murey.postermaster \
 *      -keystore /c/Users/guill/Downloads/Programme/mystore.bks \
 *      -storepass 123456 \
 *      -storetype BKS-v1 \
 *      -provider org.bouncycastle.jce.provider.BouncyCastleProvider \
 *      -providerpath /c/Users/guill/Downloads/Programme/bcprov-jdk15on-157.jar \
 *
 * ///////
 * // 3 // Enrich the keystore with the alias you have with the "mycert.pem" file
 * ///////
 *
 * "/c/Program Files/Java/jre1.8.0_144/bin/keytool.exe" \
 *      -import \
 *      -v \
 *      -trustcacerts \
 *      -alias com.murey.postermaster \
 *      -file /c/Users/guill/Downloads/root.crt \
 *      -keystore /c/Users/guill/Downloads/Programme/mystore.bks \
 *      -storetype BKS-v1 \
 *      -provider org.bouncycastle.jce.provider.BouncyCastleProvider \
 *      -providerpath /c/Users/guill/Downloads/Programme/bcprov-jdk15on-157.jar \
 *      -storepass 123456
 *
 */
public class AdditionalKeyStoresSSLSocketFactory extends SSLSocketFactory {
    protected SSLContext sslContext = SSLContext.getInstance("TLSv1");

    public AdditionalKeyStoresSSLSocketFactory(KeyStore keyStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super();
        sslContext.init(null, new TrustManager[]{new AdditionalKeystoresTrustManager(keyStore)}, null);
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return new String[0];
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return null;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        return null;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return null;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return null;
    }
}
