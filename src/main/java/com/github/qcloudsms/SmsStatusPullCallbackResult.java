package com.github.qcloudsms;

import java.util.ArrayList;

public class SmsStatusPullCallbackResult {
	 class Callback{
		String user_receive_time;
		String nationcode;
		String mobile;
		String report_status;
		String errmsg;
		String description;
		String sid;
		public String toString(){
			if (0 == result) {
				return String.format("\t"
								+ "user_receive_time:%s\t"
								+ "nationcode:%s\t"
								+ "mobile:%s\t"
								+ "errmsg:%s\t"
								+ "report_status:%s\t"
								+ "description:%s\t"
								+ "sid:%s\t",
						user_receive_time,
						nationcode,
						mobile,
						report_status,
						errmsg,
						description,
						sid);
			} else {
				return "";
			}
		}
	}

	int result;
	String errmsg;
	int count;
	ArrayList<Callback> callbacks;

	public String toString() {
		if (0 != result) {
			return String.format("SmsStatusPullCallbackResult:\n"
							+"result:%d\n"
							+"errmsg:%s\n", result, errmsg);
		} else {
			return String.format("SmsStatusPullCallbackResult:\n"
							+ "result:%d\n"
							+ "errmsg:%s\n"
							+ "count:%d\n"
							+ "callbacks:%s\n",
					result,
					errmsg,
					count,
					callbacks.toString());
		}
	}
}

