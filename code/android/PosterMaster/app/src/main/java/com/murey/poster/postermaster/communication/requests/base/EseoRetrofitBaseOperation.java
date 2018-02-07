package com.murey.poster.postermaster.communication.requests.base;

import android.content.Context;
import android.content.DialogInterface;

import com.afollestad.materialdialogs.MaterialDialog;
import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.communication.requests.certificate.AdditionalKeyStoresSSLSocketFactory;
import com.murey.poster.postermaster.communication.requests.certificate.AdditionalKeystoresTrustManager;
import com.murey.poster.postermaster.communication.message.WebMessage;
import com.murey.poster.postermaster.model.constant.Constant;
import com.murey.poster.postermaster.utils.DialogUtils;
import com.murey.poster.postermaster.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public abstract class EseoRetrofitBaseOperation implements retrofit2.Callback{
    protected String requestId;
    protected ArrayList<EseoRetrofitRequestObserver> requestObservers = new ArrayList<>();
    protected Retrofit retrofit;
    protected Call call;
    protected Converter.Factory converterFactory;
    protected Map<String, String> headers;

    protected boolean isCancelable = true;

    protected boolean isShowLoadingDialog;
    protected Context context;
    protected MaterialDialog dialog;

    public EseoRetrofitBaseOperation(String requestId) {
        this.requestId = requestId;
    }

    public EseoRetrofitBaseOperation(String requestId, boolean isShowLoadingDialog, Context context) {
        this.requestId = requestId;
        this.isShowLoadingDialog = isShowLoadingDialog;
        this.context = context;
    }

    protected Object initRequest(String baseUrl, Class serviceInterface, Map<String, String> headers){
        this.headers = headers == null ? new HashMap<String, String>() : headers;
        converterFactory = getConverterFactory();

        OkHttpClient httpClient = getNewHttpClient(baseUrl);

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(converterFactory != null ? converterFactory : GsonConverterFactory.create())
                .build();
        Object interfaceObject = retrofit.create(serviceInterface);
        return interfaceObject;
    }

    private OkHttpClient getNewHttpClient(String baseUrl) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null)
                .writeTimeout(Constant.TW_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constant.TW_READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(Constant.TW_CONNECTION_SERVER, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request.Builder builder = chain.request().newBuilder();
                        for (Map.Entry<String, String> entry : EseoRetrofitBaseOperation.this.headers.entrySet()) {
                            builder.addHeader(entry.getKey(), entry.getValue());
                        }
                        return chain.proceed(builder.build());
                    }
                });

        return enableEseoCertificate(client).build();
    }

    public OkHttpClient.Builder enableEseoCertificate(OkHttpClient.Builder client) {
        try{
            client.sslSocketFactory(new AdditionalKeyStoresSSLSocketFactory(getKeyStore()),new AdditionalKeystoresTrustManager(getKeyStore()));
        }catch(Exception e){
            LogUtils.e(LogUtils.DEBUG_TAG,"Exception sslSocketFactory => ",e);
        }
        return client;
    }

    private KeyStore getKeyStore(){
        try {
            final KeyStore ks = KeyStore.getInstance("BKS");
            // the bks file we generated above
            final InputStream in = context.getResources().openRawResource(R.raw.mystore);
            try {
                ks.load(in, "123456".toCharArray());
            } finally {
                in.close();
            }
            return ks;
        } catch( Exception e ) {
            LogUtils.e(LogUtils.DEBUG_TAG,"ERROR KEYSTORE ",e);
        }
        return null;
    }

    public void addRequestObserver(EseoRetrofitRequestObserver observer){
        requestObservers.add(observer);
    }

    protected void cancel(){

    }

    public void setIsCancelable(boolean isCancelable){
        this.isCancelable = isCancelable;
    }

    public void showLoadingDialog(){
        if (isShowLoadingDialog) {
            if(dialog==null) {
                dialog = DialogUtils.getWaitingDialog(context, isCancelable);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        LogUtils.d(LogUtils.DEBUG_TAG, "Dialog Canceled ");
                        cancel();
                    }
                });

                try {
                    if (!dialog.isShowing())
                        dialog.show();
                } catch (Exception e) {}
                // Show dialog on activity killed
            }
        }
    }

    protected static String bodyToString(Call call) {
        try {
            final RequestBody copy = call.request().body();
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "";
        }
    }

    public String getRequestId(){
        return requestId;
    }

    protected abstract void onSuccess(int responseCode, WebMessage responseBody);

    protected abstract String getBaseUrl();

    protected abstract Class getServiceInterface();

    protected abstract Map<String,String> getHeaders();

    protected abstract Call createCall(Object interfaceObject);

    protected abstract Converter.Factory getConverterFactory();

    public void startRequest(){
        Object interfaceObject = initRequest(getBaseUrl(), getServiceInterface(), getHeaders());
        call = createCall(interfaceObject);
        call.enqueue(this);
    }
}
