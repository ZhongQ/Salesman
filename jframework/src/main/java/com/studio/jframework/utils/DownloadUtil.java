package com.studio.jframework.utils;

import android.content.Context;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

/**
 * 文件上传下载
 * Created by ZQ on 2016/8/16 0016.
 */
public class DownloadUtil {
    private static final int TIME_OUT = 30 * 1000; //超时时间

    private static final String CHARSET = "utf-8"; //设置编码

    private DownloadUtil() {
        throw new UnsupportedOperationException("DownloadUtil cannot be instantiated");
    }

    /**
     * @param file       上传文件
     * @param RequestURL 上传文件URL
     * @return 服务器返回的信息，如果出错则返回为null
     */
    public static String uploadFile(File file, String RequestURL) {
        String BOUNDARY = UUID.randomUUID().toString(); //边界标识 随机生成 String PREFIX = "--" , LINE_END = "\r\n";
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; //内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false); //不允许使用缓存
            conn.setRequestMethod("POST"); //请求方式
            conn.setRequestProperty("Charset", CHARSET);
            // 此处代码有待研究
//            conn.setRequestProperty("Cookie", "PHPSESSID=" + App.getSessionId());
            //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file != null) {
                /** * 当文件不为空，把文件包装并且上传 */
                OutputStream outputSteam = conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputSteam);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功
                 * 当响应成功，获取响应的流
                 */
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                InputStream resultStream = conn.getInputStream();
                len = -1;
                byte[] buffer = new byte[1024 * 8];
                while ((len = resultStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                resultStream.close();
                bos.flush();
                bos.close();
                String info = new String(bos.toByteArray());
                int res = conn.getResponseCode();
                if (res == 200) {
                    return info;
                } else {
                    return null;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] download(String urlStr) {
        HttpURLConnection conn = null;
        InputStream is = null;
        byte[] result = null;
        ByteArrayOutputStream bos = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIME_OUT);
            conn.setReadTimeout(TIME_OUT);
            conn.setDoInput(true);
            conn.setUseCaches(false);//不使用缓存
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                byte[] buffer = new byte[1024 * 8];
                int len;
                bos = new ByteArrayOutputStream();
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                result = bos.toByteArray();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (is != null) {
                    is.close();
                }
                if (conn != null) conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取缓存文件
     */
    public static File getDiskCacheFile(Context context, String filename, boolean isExternal) {
        if (isExternal && (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            return new File(context.getExternalCacheDir(), filename);
        } else {
            return new File(context.getCacheDir(), filename);
        }
    }

    /**
     * 获取缓存文件目录
     */
    public static File getDiskCacheDirectory(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return context.getExternalCacheDir();
        } else {
            return context.getCacheDir();
        }
    }
}
