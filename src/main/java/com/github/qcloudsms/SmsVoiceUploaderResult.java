package com.github.qcloudsms;

public class SmsVoiceUploaderResult {
/*
{
    "result": 0,		// 0  表示成功，非 0  表示失败
    "msg": "xxx", 		// result �? 0 时的具体错误信息
	"file":"aaaaaaa" 	// result �? 0 时，存储的文件名
}
*/
	public int result;
	public String msg;
	public String file;

	public String toString() {
		if (0 == result) {
			return String.format(
					"SmsVoiceUploaderResult:result %d\nmsg %s\nfile %s", result, msg, file);
		} else {
			return String.format("SmsVoiceUploaderResult:result %d\nmsg %s", result, msg);
		}
	}
}
