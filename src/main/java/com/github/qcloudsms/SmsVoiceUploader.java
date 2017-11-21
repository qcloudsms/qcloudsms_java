package com.github.qcloudsms;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class SmsVoiceUploader {
	String appkey;
	int appid;
	//String url = "https://test.tim.qq.com/v3/tlsvoicesvr/upload_voice";
	String url = "https://yun.tim.qq.com/v3/tlsvoicesvr/upload_voice";

	SmsSenderUtil util = new SmsSenderUtil();

	public SmsVoiceUploader(int appid, String appkey) {
		this.appid = appid;
		this.appkey = appkey;
	}

	/*
	 * 上传文件
	 * @param filePath �?要上传文件本地路�?
	 * @return {@link}SmsVoiceUploaderResult
	 * @throws Exception
	 */
	public SmsVoiceUploaderResult upload(String filePath) throws Exception {

		String wholeUrl = String.format("%s?sdkappid=%d", url, appid);
		String random = "" + util.getRandom();
		String curTime = "" + System.currentTimeMillis()/1000;

		File voiceFile = new File(filePath);

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(wholeUrl);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		ContentType contentType = ContentType.create("application/octet-stream;\r\nContent-Length: " + voiceFile.length());
		builder.addBinaryBody("file", voiceFile, contentType, voiceFile.getName());
		builder.addTextBody("sig", util.strToHash(String.format("appkey=%s&rand=%s&time=%s", appkey, random, curTime)));
		builder.addTextBody("rand", random);
		builder.addTextBody("time", curTime);
		HttpEntity multipart = builder.build();
		httpPost.setEntity(multipart);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		multipart.writeTo(bos);
		System.out.println(bos.toString());

		SmsVoiceUploaderResult result;
		int httpRspCode;
		try {
			HttpResponse response = httpClient.execute(httpPost);
			httpRspCode = response.getStatusLine().getStatusCode();
			if (200 == httpRspCode) {
				BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				JSONObject json = new JSONObject(sb.toString());
				result = util.jsonToSmsVoiceUploaderResult(json);
				System.out.println(sb.toString());
			} else {
				result = new SmsVoiceUploaderResult();
				result.result = -1;
				result.msg = "http error " + httpRspCode;
			}
		} finally {
			httpClient.close();
		}

		return result;
	}
}
