package com.example.framework.manager;

import com.example.framework.utils.SHA1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * FileName: HttpManager
 * Founder: LiuGuiLin
 * Profile: OkHttp
 */
public class HttpManager {

    private static volatile HttpManager mInstnce = null;
    private OkHttpClient mOkHttpClient;

    private HttpManager() {
        mOkHttpClient = new OkHttpClient();
    }

    public static HttpManager getInstance() {
        if (mInstnce == null) {
            synchronized (HttpManager.class) {
                if (mInstnce == null) {
                    mInstnce = new HttpManager();
                }
            }
        }
        return mInstnce;
    }

    /**
     * 请求融云Token
     *
     * @param map
     */
//    public String postCloudToken(HashMap<String, String> map) {
//
//        //参数
//        String Timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//        String Nonce = String.valueOf(Math.floor(Math.random() * 100000));
//        String Signature = SHA1.sha1(CloudManager.CLOUD_SECRET + Nonce + Timestamp);
//
//        //参数填充
//        FormBody.Builder builder = new FormBody.Builder();
//        for (String key : map.keySet()) {
//            builder.add(key, map.get(key));
//        }
//        RequestBody requestBody = builder.build();
//        //添加签名规则
//        Request request = new Request.Builder()
//                .url(CloudManager.TOKEN_URL)
//                .addHeader("Timestamp", Timestamp)
//                .addHeader("App-Key", CloudManager.CLOUD_KEY)
//                .addHeader("Nonce", Nonce)
//                .addHeader("Signature", Signature)
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .post(requestBody)
//                .build();
//        try {
//            return mOkHttpClient.newCall(request).execute().body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    /**
     * 下载 是否要增加一个定时清理Meet文件夹的机制？
     * @param url
     * @param saveDir
     * @param listener
     */
    public void download(final String url, final String saveDir, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onDownloadFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                //储存下载文件的目录
                //String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    //不用从url 直接从path
                    File file = new File(saveDir);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    //下载完成
                    listener.onDownloadSuccess(file.getAbsolutePath());
                } catch (Exception e) {
                    listener.onDownloadFailed(e);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {

                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {

                    }
                }
            }
        });
    }

    /**
     * 判断文件下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * 从路径获取文件名
     */
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 下载进度监听
     */
    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(String path);

        /**
         * 下载进度
         *
         * @param progress
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed(Exception e);
    }
}
