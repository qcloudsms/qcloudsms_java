package com.github.qcloudsms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import org.json.JSONObject;

public class SmsSingleVoiceSender {
	int appid;
	String appkey;
    String url = "https://test.tim.qq.com/v3/tlsvoicesvr/sendvoiceprompt";
    SmsSenderUtil util = new SmsSenderUtil();

    public SmsSingleVoiceSender(int appid, String appkey) {
    	this.appid = appid;
    	this.appkey = appkey;
    }

    /**
     * 发�?�语音短�?
     * @param nationCode 国家码，�? 86 为中�?
     * @param phoneNumber 不带国家码的手机�?
     * @param type 类型，目前只�? 3
     * @param fileName 上传后生成的文件地址{@link}SmsVoiceUploader
     * @param ext 服务端原样返回的参数，可填空
     * @return {@link}SmsSingleVoiceSenderResult
     * @throws Exception
     */
    public SmsSingleVoiceSenderResult send(
    		String nationCode,
    		String phoneNumber,
    		int type,
    		String fileName,
    		String ext) throws Exception {

		// 校验 type 类型
		if (3 != type) {
			throw new Exception("type " + type + " error");
		}
		if (null == ext) {
			ext = "";
		}

		// 按照协议组织 post 请求包体
		JSONObject data = new JSONObject();

        JSONObject tel = new JSONObject();
        tel.put("nationcode", nationCode);
        tel.put("phone", phoneNumber);

        data.put("tel", tel);
        data.put("prompttype", type);
        data.put("promptfile", fileName);
        data.put("sig", util.stringMD5(appkey+phoneNumber));
        data.put("ext", ext);

        // 与上面的 random 必须�?�?
		String wholeUrl = String.format("%s?sdkappid=%d", url, appid);
        HttpURLConnection conn = util.getPostHttpConn(wholeUrl);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        System.out.println(data.toString());
        wr.write(data.toString());
        wr.flush();

        // 显示 POST 请求返回的内�?
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
        SmsSingleVoiceSenderResult result;
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JSONObject json = new JSONObject(sb.toString());
            result = util.jsonToSmsSingleVoiceSenderResult(json);
        } else {
        	result = new SmsSingleVoiceSenderResult();
        	result.result = httpRspCode;
        	result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        }

        return result;
    }
}
