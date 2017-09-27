package com.github.qcloudsms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import org.json.JSONObject;

public class SmsStatusPuller {

	String url = "https://yun.tim.qq.com/v5/tlssmssvr/pullstatus";
	int appid;
	String appkey;

	SmsSenderUtil util = new SmsSenderUtil();

	public SmsStatusPuller(int appid, String appkey) {
		this.appid = appid;
		this.appkey = appkey;
	}

	public HttpURLConnection constructConnection(int type,int max) throws Exception{
		/*
		{
			"sig": "xxxxxx", // sha256(appkey=$appkey&rand=$rand&time=$time)
			"type": 0, // 类型
			"max": 10, //�?大条�?
			"time": 1464624000 //unix时间戳，请求发起时间，如果和系统时间相差超过10分钟则会拉取失败
		}
		*/
		// 按照协议组织 post 请求包体
		long random = util.getRandom();
		long curTime = System.currentTimeMillis() / 1000;

		JSONObject data = new JSONObject();
		data.put("sig", util.strToHash(String.format(
				"appkey=%s&random=%d&time=%d", appkey, random,curTime)));
		data.put("time", curTime);
		data.put("type", type);
		data.put("max", max);

		// 与上面的 random 必须�?�?
		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", url, appid, random);
		HttpURLConnection conn = util.getPostHttpConn(wholeUrl);

		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
		wr.write(data.toString());
		wr.flush();

		return conn;
	}
	/**
	 * 拉取回执结果
	 * @param max �?大条�? �?�?100
	 * @return {@link}pullCallback
	 * @throws Exception
	 */
	public SmsStatusPullCallbackResult pullCallback(int max) throws Exception {

		HttpURLConnection conn = constructConnection(0,max);//1表示短信回执

		// 显示 POST 请求返回的内�?
		StringBuilder sb = new StringBuilder();
		int httpRspCode = conn.getResponseCode();
		SmsStatusPullCallbackResult result;
		if (httpRspCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			JSONObject json = new JSONObject(sb.toString());
			System.out.println(sb.toString());
			result = util.jsonToSmsStatusPullCallbackrResult(json);
		} else {
			result = new SmsStatusPullCallbackResult();
			result.result = httpRspCode;
			result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
		}

		return result;
	}


	/**
	 * 拉取回复信息
	 * @param max �?大条�? �?�?100
	 * @return {@link}pullReply
	 * @throws Exception
	 */
	public SmsStatusPullReplyResult pullReply(int max) throws Exception {

		HttpURLConnection conn = constructConnection(1,max);//1表示回复

		// 显示 POST 请求返回的内�?
		StringBuilder sb = new StringBuilder();
		int httpRspCode = conn.getResponseCode();
		SmsStatusPullReplyResult result;
		if (httpRspCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			JSONObject json = new JSONObject(sb.toString());
			result = util.jsonToSmsStatusPullReplyResult(json);
		} else {
			result = new SmsStatusPullReplyResult();
			result.result = httpRspCode;
			result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
		}

		return result;
	}

}
