package com.github.qcloudsms;

public class SmsSingleVoiceSenderResult {

/*
{
    "result": 0, //0表示成功，非0表示失败
    "errmsg": "", //result�?0时的具体错误信息
    "ext": "some msg", //可�?�字段，用户的session内容，腾讯server回包中会原样返回
    "callid": "xxxx" //标识本次发�?�id
}

*/
	public int result;
	public String errmsg;
	public String ext = "";
	public String callid;

	public String toString() {
		if (0 == result) {
			return String.format(
					"SmsSingleVoiceSenderResult\nresult %d\nerrmsg %s\next %s\ncallid %s",
					result, errmsg, ext, callid);
		} else {
			return String.format(
					"SmsSingleVoiceSenderResult\nresult %d\nerrmsg %s\next %s",
					result, errmsg, ext);
		}
	}
}
