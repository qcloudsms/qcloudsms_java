package com.github.qcloudsms;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import org.json.JSONObject;


public class SmsMultiSender {
	int appid;
	String appkey;
    String url = "https://yun.tim.qq.com/v5/tlssmssvr/sendmultisms2";

	SmsSenderUtil util = new SmsSenderUtil();

	public SmsMultiSender(int appid, String appkey) throws Exception {
		this.appid = appid;
		this.appkey = appkey;
	}

	/*
	 * 普通群发，明确指定内容，如果有多个签名，请在内容中以【】的方式添加到信息内容中，否则系统将使用默认签名
	 * 【注意】海外短信无群发功能
	 * @param type 短信类型，0 为普通短信，1 营销短信
	 * @param nationCode 国家码，如 86 为中国
	 * @param phoneNumbers 不带国家码的手机号列表
	 * @param msg 信息内容，必须与申请的模板格式一致，否则将返回错误
	 * @param extend 扩展码，可填空
	 * @param ext 服务端原样返回的参数，可填空
	 * @return {@link}SmsMultiSenderResult
	 *  @throws Excetion null
	 */
	public SmsMultiSenderResult send(
			int type,
			String nationCode,
			ArrayList<String> phoneNumbers,
			String msg,
			String extend,
			String ext) throws Exception {
/*
请求包体
{
    "tel": [
        {
            "nationcode": "86",
            "mobile": "13788888888"
        },
        {
            "nationcode": "86",
            "mobile": "13788888889"
        }
    ],
    "type": 0,
    "msg": "你的验证码是1234",
    "sig": "fdba654e05bc0d15796713a1a1a2318c",
    "time": 1479888540,
    "extend": "",
    "ext": ""
}
应答包体
{
    "result": 0,
    "errmsg": "OK",
    "ext": "",
    "detail": [
        {
            "result": 0,
            "errmsg": "OK",
            "mobile": "13788888888",
            "nationcode": "86",
            "sid": "xxxxxxx",
            "fee": 1
        },
        {
            "result": 0,
            "errmsg": "OK",
            "mobile": "13788888889",
            "nationcode": "86",
            "sid": "xxxxxxx",
            "fee": 1
        }
    ]
}
*/
		// 校验 type 类型
		if (0 != type && 1 != type) {
			throw new Exception("type " + type + " error");
		}

		if (null == extend) {
			extend = "";
		}

		if (null == ext) {
			ext = "";
		}

		long random = util.getRandom();
		long curTime = System.currentTimeMillis()/1000;

		// 按照协议组织 post 请求包体
		JSONObject data = new JSONObject();
        data.put("tel", util.phoneNumbersToJSONArray(nationCode, phoneNumbers));
        data.put("type", type);
        data.put("msg", msg);
        data.put("sig", util.calculateSig(appkey, random, msg, curTime, phoneNumbers));
        data.put("time", curTime);
        data.put("extend", extend);
        data.put("ext", ext);

		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", url, appid, random);
        HttpURLConnection conn = util.getPostHttpConn(wholeUrl);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        wr.write(data.toString());
        wr.flush();

        // 显示 POST 请求返回的内容
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
        SmsMultiSenderResult result;
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JSONObject json = new JSONObject(sb.toString());
            result = util.jsonToSmsMultiSenderResult(json);
        } else {
        	result = new SmsMultiSenderResult();
        	result.result = httpRspCode;
        	result.errMsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        }

        return result;
	}

	/*
	 * 指定模板群发
	 * 【注意】海外短信无群发功能
	 * @param nationCode 国家码，如 86 为中国
	 * @param phoneNumbers 不带国家码的手机号列表
	 * @param templId 模板 id
	 * @param params 模板参数列表
	 * @param sign 签名，如果填空，系统会使用默认签名
	 * @param extend 扩展码，可以填空
	 * @param ext 服务端原样返回的参数，可以填空
	 * @return {@link}SmsMultiSenderResult
	 * @throws Exception
	 */
	public SmsMultiSenderResult sendWithParam(
			String nationCode,
			ArrayList<String> phoneNumbers,
			int templId,
			ArrayList<String> params,
			String sign,
			String extend,
			String ext) throws Exception {
/*
请求包体
{
    "tel": [
        {
            "nationcode": "86",
            "mobile": "13788888888"
        },
        {
            "nationcode": "86",
            "mobile": "13788888889"
        }
    ],
    "sign": "腾讯云",
    "tpl_id": 19,
    "params": [
        "验证码",
        "1234",
        "4"
    ],
    "sig": "fdba654e05bc0d15796713a1a1a2318c",
    "time": 1479888540,
    "extend": "",
    "ext": ""
}
应答包体
{
    "result": 0,
    "errmsg": "OK",
    "ext": "",
    "detail": [
        {
            "result": 0,
            "errmsg": "OK",
            "mobile": "13788888888",
            "nationcode": "86",
            "sid": "xxxxxxx",
            "fee": 1
        },
        {
            "result": 0,
            "errmsg": "OK",
            "mobile": "13788888889",
            "nationcode": "86",
            "sid": "xxxxxxx",
            "fee": 1
        }
    ]
}
*/
		if (null == nationCode || 0 == nationCode.length()) {
			nationCode = "86";
		}
		if (0 == phoneNumbers.size()) {
			throw new Exception("phoneNumbers size error");
		}
		if (null == params) {
			params = new ArrayList<String>();
		}
		if (null == sign) {
			sign = "";
		}
		if (null == extend) {
			extend = "";
		}
		if (null == ext) {
			ext = "";
		}

		long random = util.getRandom();
		long curTime = System.currentTimeMillis()/1000;

		// 按照协议组织 post 请求包体
		JSONObject data = new JSONObject();
        data.put("tel", util.phoneNumbersToJSONArray(nationCode, phoneNumbers));
        data.put("sign", sign);
        data.put("tpl_id", templId);
        data.put("params", util.smsParamsToJSONArray(params));
        data.put("sig", util.calculateSigForTempl(appkey, random, curTime, phoneNumbers));
        data.put("time", curTime);
        data.put("extend", extend);
        data.put("ext", ext);

		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", url, appid, random);
        HttpURLConnection conn = util.getPostHttpConn(wholeUrl);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        wr.write(data.toString());
        wr.flush();

        // 显示 POST 请求返回的内容
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
        SmsMultiSenderResult result;
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JSONObject json = new JSONObject(sb.toString());
            result = util.jsonToSmsMultiSenderResult(json);
        } else {
        	result = new SmsMultiSenderResult();
        	result.result = httpRspCode;
        	result.errMsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        }

        return result;
	}
}

