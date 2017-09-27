package com.github.qcloudsms;

public class SmsStatusPullerResult {
	public class Data {
		int success;
		int status;
		int status_success;
		int status_fail;
		int status_fail_0;
		int status_fail_1;
		int status_fail_2;
		int status_fail_3;
		int status_fail_4;
	}

	int result;
	String errmsg = "";
	Data data;

	public String toString() {
		if (null != data) {
			return String.format("SmsStatusPullerResult:\n"
					+"result %d\n"
					+"errmsg %s\n"
					+"data\n"
					+"\tsuccess %d\n"
					+"\tstatus %d\n"
					+"\tstatus_success %d\n"
					+"\tstatus_fail %d\n"
					+"\tstatus_fail_0 %d\n"
					+"\tstatus_fail_1 %d\n"
					+"\tstatus_fail_2 %d\n"
					+"\tstatus_fail_3 %d\n"
					+"\tstatus_fail_4 %d\n",
					result, errmsg, data.success, data.status,
					data.status_success, data.status_fail,
					data.status_fail_0, data.status_fail_1,
					data.status_fail_2, data.status_fail_3,
					data.status_fail_4);
		}
		return String.format("SmsStatusPullerResult:\n"
				+"result %d errmsg %s", result, errmsg);
	}
}
