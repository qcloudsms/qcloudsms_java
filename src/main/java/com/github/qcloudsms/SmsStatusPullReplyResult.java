package com.github.qcloudsms;

import java.util.ArrayList;

public class SmsStatusPullReplyResult {
	public class Reply{
		String nationcode;
		String mobile;
		String text;
		String sign;
		long time;
		public String toString(){
			return String.format(
					"nationcode:%s\t"
					+"mobile:%s\t"
					+"text:%s\t"
					+"sign:%s\t"
					+"time:%d\n",
					nationcode,
					mobile,
					text,
					sign,
					time
			  );
		}
	}

	int result;
	String errmsg;
	int count;
	ArrayList<Reply> replys;

	public String toString() {
		if (0 == result) {
			return String.format("SmsStatusReplyResult:\n"
							+ "result:%d\n"
							+ "errmsg:%s\n"
							+ "count:%d\n"
							+ "replys:%s\n",
					result,
					errmsg,
					count,
					replys.toString()
			);
		} else {
			return String.format("SmsStatusReplyResult:\n"
							+ "result:%d\n"
							+ "errmsg:%s\n", result, errmsg);
		}
	}
}

