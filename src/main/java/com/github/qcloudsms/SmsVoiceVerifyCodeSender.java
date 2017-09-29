package com.github.qcloudsms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import org.json.JSONObject;

public class SmsVoiceVerifyCodeSender {
	int appid;
	String appkey;
    String url = "https://yun.tim.qq.com/v5/tlsvoicesvr/sendvoice";
    SmsSenderUtil util = new SmsSenderUtil();

    public SmsVoiceVerifyCodeSender(int appid, String appkey) {
    	this.appid = appid;
    	this.appkey = appkey;
    }

    /*
     * 发送语音短信
     * @param nationCode 国家码，如 86 为中国
     * @param phoneNumber 不带国家码的手机号
     * @param msg 消息类型
     * @param playtimes 播放次数
     * @param ext 服务端原样返回的参数，可填空
     * @return {@link}SmsVoiceVerifyCodeSenderResult
     * @throws Exception
     */
    public SmsVoiceVerifyCodeSenderResult send(
    		String nationCode,
    		String phoneNumber,
    		String msg,
    		int playtimes,
    		String ext) throws Exception {

		if (null == ext) {
			ext = "";
		}

        long random = util.getRandom();
        long curTime = System.currentTimeMillis()/1000;

        ArrayList<String> phoneNumbers = new ArrayList<String>();
    	phoneNumbers.add(phoneNumber);
    	String sig = util.calculateSigForTempl(appkey, random, curTime, phoneNumbers);

		// 按照协议组织 post 请求包体
		JSONObject data = new JSONObject();

        JSONObject tel = new JSONObject();
        tel.put("nationcode", nationCode);
        tel.put("mobile", phoneNumber);

        data.put("tel", tel);
        data.put("msg", msg);
        data.put("playtimes", playtimes);
        data.put("sig", sig);
        data.put("time", curTime);
        data.put("ext", ext);

        // 与上面的 random 必须一致
		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", url, appid,random);
        HttpURLConnection conn = util.getPostHttpConn(wholeUrl);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        System.out.println(data.toString());
        wr.write(data.toString());
        wr.flush();

        // 显示 POST 请求返回的内容
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
        SmsVoiceVerifyCodeSenderResult result;
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JSONObject json = new JSONObject(sb.toString());
            result = util.jsonToSmsVoiceVerifyCodeSenderResult(json);
        } else {
        	result = new SmsVoiceVerifyCodeSenderResult();
        	result.result = httpRspCode;
        	result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        }

        return result;
    }
}
