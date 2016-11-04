package com.salesman.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URL;

public class HttpServiceUtil {

    private static final String TAG = "zzxwifi2";

    private static String apMac = "";
    private static String apIp = "";
    private static String apSn = "";
    private static String apUsb = "";

    private static boolean bzzxwifi = false;
    private static String allString = "";

    /***
     * 根据当前mac与设备mac进行上网操作
     *
     * @param
     * @param
     * @return
     */
    public static String postMAC(String appMAc, String wlanApMac, String serverIp) {
        if (!TextUtils.isEmpty(appMAc)) {
            // loginAp();
//			String apMAC = getMAC();
            // logoutAp();
            apMac = wlanApMac;
            URL url = null;
            HttpURLConnection urlConn = null;
            String resultData = "";
            InputStreamReader in = null;
            try {
//				
                String SERVICE_URL = serverIp + "/sdb_v2/common_Portal_apCertificate2.action?user=1&pwd=1&id=1&gid=1&wlanusermac=" + appMAc + "&wlanapmac=" + apMac + "&redir=1&client=android_apk";
                //String SERVICE_URL = "http://172.16.0.126:8080/sdb_v2/common_Portal_apCertificate2.action?user=1&pwd=1&id=1&gid=1&wlanusermac=" + appMAc + "&wlanapmac=" + apMac + "&redir=1&client=android_apk";
                Log.e("postMAC", "SERVICE_URL==" + SERVICE_URL);
                url = new URL(SERVICE_URL);
                urlConn = (HttpURLConnection) url.openConnection();
                Log.e("openConnection", "openConnection>>>>>>>>>>");
                in = new InputStreamReader(urlConn.getInputStream());
                BufferedReader buffer = new BufferedReader(in);
                String inputLine = null;
                while (((inputLine = buffer.readLine()) != null)) {
                    resultData += inputLine + "\n";

                    Log.e("postMAC", "resultData==11" + resultData);
                }
//				Log.d(GPSData.TAG, "postMAC:resultData=" + resultData);

                Log.e("postMAC", "resultData==22" + resultData);

                if (resultData.contains("ok")) {
                    Log.e("postMAC", "apMac==" + apMac);
                    return apMac;
                }
            } catch (IOException e) {
                Log.d(TAG, "ERROR=" + e);
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != urlConn) {
                    urlConn.disconnect();
                }
            }
        }
        return "";
    }

    /***
     * 判断是否连上zzxwifi，通过连接的mac进行判断的
     *
     * @return
     */
    public static boolean isConnectedZzxwifi(Context context) {
//		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//		final WifiInfo wifiInfo = wm.getConnectionInfo();
//		
//		
//		if (null != wifiInfo && null != wifiInfo.getSSID()) {
//			
//			String name = wifiInfo.getSSID().toUpperCase().substring(1, 4);
//			if(name.equals("蜘蛛匣")){
//				return true;
//			}else{
//				return false;
//			}
//		}
        return bzzxwifi;
    }

    public static void setConnectedZzxwifi(boolean bwifi) {
        bzzxwifi = bwifi;
    }


    public static String getAppmarket_getUsbAppList(Context context) {

        String appList = "";

        MulticastSocket mSocket = null;

        try {
            mSocket = new MulticastSocket(5000);
            mSocket.setSoTimeout(3000);// 设置超时时间

            InetAddress group = getBroadcastAddress(context);
            String str = "Appmarket_getUsbAppList";
            byte[] buff = str.getBytes("utf-8");// 设定多播报文的数据
            mSocket.setTimeToLive(4);

            DatagramPacket packet = new DatagramPacket(buff, buff.length, group, 5000);
            mSocket.send(packet);// 发送报文
            byte[] buffer = new byte[8192];
            long time = System.currentTimeMillis();

            while (true) {
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);//接受报文
                mSocket.receive(dp);
                String s = new String(dp.getData(), 0, dp.getLength()).toUpperCase(); // 5.解码组播数据包提取信息，并依据得到的信息作出响应

                if (System.currentTimeMillis() - time > 3000) {
                    return "";
                }

                if (s.contains("Appmarket_getUsbAppList")) {
                    return s;
                }

            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            if (mSocket != null) {
                mSocket.close();
                mSocket = null;
            }
        }

        return "";
    }

    public static String getApMac(Context context, String wlanMac, String userId) {

        bzzxwifi = false;

        if (TextUtils.isEmpty(wlanMac)) {
            return "";
        }

        wlanMac = wlanMac.toUpperCase();
        MulticastSocket mSocket = null;
        try {
            mSocket = new MulticastSocket(5000);
            mSocket.setSoTimeout(3000);// 设置超时时间

            // 生成套接字并绑定5000端口
            InetAddress group = getBroadcastAddress(context);
            String ip = getIpAddress(context);

            String androidId = getAndroidId(context);
            String str = "network_info_request" + " ip=" + ip + " uid=" + androidId + " device=Android userId=" + userId;
            byte[] buff = str.getBytes("utf-8");// 设定多播报文的数据
            mSocket.setTimeToLive(4);// 设定TTL

            // 设定UDP报文（内容，内容长度，多播组，端口）
            DatagramPacket packet = new DatagramPacket(buff, buff.length, group, 5000);
            mSocket.send(packet);// 发送报文
            byte[] buffer = new byte[8192];
            long time = System.currentTimeMillis();
            while (true) {
//				DatagramPacket dp = new DatagramPacket(buffer, buffer.length, group, 5000);
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);//接受报文
                mSocket.receive(dp);
                String s = new String(dp.getData(), 0, dp.getLength()).toUpperCase(); // 5.解码组播数据包提取信息，并依据得到的信息作出响应
                if (System.currentTimeMillis() - time > 3000) {
                    return "";
                }


                if (s.contains(wlanMac)) {
                    String[] lines = s.split("\n");
                    for (String line : lines) {
                        String temp = line;

                        if (temp.contains("SN")) {

                            apSn = temp.split("SN=")[1].substring(0, 15);

                            if (temp.contains("USB")) {
                                apUsb = temp.split("USB=")[1].substring(0, 2);
                            }
                        }

                        if (line.contains("BR-LAN")) {
                            String mac = line.split("MAC=")[1].replaceAll(";", "");
                            apMac = mac;
                            bzzxwifi = true;
//							if(apIp.equals(",")){
//								temp = line;
//								String t =  temp.split("IP=")[1];
//								
//								
//								apIp = t.substring(0,t.indexOf(";"));
//							}
                            //Log.w(GPSData.TAG, "getApMac apip=" + apIp);

                            if (temp.contains("IP") && temp.contains(".") && apIp.equals("")) {
                                temp = line;
                                String t = temp.split("IP=")[1];
                                apIp = t.substring(0, t.lastIndexOf("MAC") - 1);
                            }

                        }
                    }
                    return apMac;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mSocket != null) {
                mSocket.close();
                mSocket = null;
            }
        }
        return "";
    }

    public static String getIp() {
        return apIp;
    }

    public static String getSN() {
        return apSn;
    }

    public static boolean getapUsb() {
        return apUsb.equals("YE");
    }

    private static InetAddress getBroadcastAddress(Context context) throws IOException {
        WifiManager myWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo myDhcpInfo = myWifiManager.getDhcpInfo();
        if (myDhcpInfo == null) {
            System.out.println("Could not get broadcast address");
            return null;
        }
        int broadcast = (myDhcpInfo.ipAddress & myDhcpInfo.netmask) | ~myDhcpInfo.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

    private static String getIpAddress(Context context) throws IOException {
        WifiManager myWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo myDhcpInfo = myWifiManager.getDhcpInfo();
        if (myDhcpInfo == null) {
            System.out.println("Could not get broadcast address");
            return null;
        }
        return Formatter.formatIpAddress(myDhcpInfo.ipAddress);
    }

    public static String getAndroidId(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID).toUpperCase();
    }

    /***
     * 添加图片地址转16进制字符串方法
     *
     * @param context
     * @param uri
     * @return
     */
    public static byte[] Uri2byte(Context context, String uri) {
        byte[] data = null;
        InputStream input = null;
        try {
            input = context.getContentResolver().openInputStream(Uri.parse(uri.toString()));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    /***
     * 高效转换成16进制
     *
     * @param data
     * @return
     */
    public static String byte2Hex(byte[] data) {
        char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_UPPER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_UPPER[0x0F & data[i]];
        }
        return new String(out);
    }

    /***
     * 低效转换成16进制
     *
     * @param
     * @return
     */
    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            // if (n<b.length-1) hs=hs+":";
        }
        return hs.toUpperCase();
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


}
