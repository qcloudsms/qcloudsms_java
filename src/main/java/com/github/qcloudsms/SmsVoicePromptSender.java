package com.github.qcloudsms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import org.json.JSONObject;

public class SmsVoicePromptSender {
	int appid;
	String appkey;
    String url = "https://yun.tim.qq.com/v5/tlsvoicesvr/sendvoiceprompt";
    SmsSenderUtil util = new SmsSenderUtil();

    public SmsVoicePromptSender(int appid, String appkey) {
    	this.appid = appid;
    	this.appkey = appkey;
    }

    /**
     * 发�?�语音短�?
     * @param nationCode 国家码，�? 86 为中�?
     * @param phoneNumber 不带国家码的手机�?
     * @param prompttype 类型，目前固定�?�为2
     * @param playtimes 播放次数
     * @param msg 语音通知消息内容
     * @param ext  "扩展字段，原样返�?"
     * @return {@link}SmsSingleVoiceSenderResult
     * @throws Exception
     */
    public SmsVoicePromptSenderResult send(
    		String nationCode,
    		String phoneNumber,
    		int prompttype,
    		int playtimes,
    		String msg,
    		String ext) throws Exception {

		if (null == ext) {
			ext = "";
		}

        long random = util.getRandom();
        long curTime = System.currentTimeMillis()/1000;

        ArrayList<String> phoneNumbers = new ArrayList<String>();
    	phoneNumbers.add(phoneNumber);
    	String sig = util.calculateSig(appkey, random, msg,curTime, phoneNumbers);

		// 按照协议组织 post 请求包体
		JSONObject data = new JSONObject();

        JSONObject tel = new JSONObject();
        tel.put("nationcode", nationCode);
        tel.put("mobile", phoneNumber);

        data.put("tel", tel);
        data.put("prompttype", prompttype);
        data.put("promptfile", msg);
        data.put("playtimes", playtimes);
        data.put("sig",sig);
        data.put("time", curTime);
        data.put("ext", ext);

        // 与上面的 random 必须�?�?
		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", url, appid,random);
        HttpURLConnection conn = util.getPostHttpConn(wholeUrl);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        System.out.println(data.toString());
        wr.write(data.toString());
        wr.flush();

        // 显示 POST 请求返回的内�?
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
        SmsVoicePromptSenderResult result;
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JSONObject json = new JSONObject(sb.toString());
            result = util.jsonToSmsVoicePromptSenderResult(json);
        } else {
        	result = new SmsVoicePromptSenderResult();
        	result.result = httpRspCode;
        	result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        }

        return result;
    }
}
