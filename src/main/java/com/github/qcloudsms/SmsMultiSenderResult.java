package com.github.qcloudsms;

import java.util.ArrayList;

public class SmsMultiSenderResult {
/*
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
	public class Detail {
		public int result;
		public String errMsg = "";
		public String phoneNumber = "";
		public String nationCode = "";
		public String sid = "";
		public int fee;

		public String toString() {
			if (0 == result) {
				return String.format(
						"Detail result %d\nerrMsg %s\nphoneNumber %s\nnationCode %s\nsid %s\nfee %d",
						result, errMsg, phoneNumber, nationCode, sid, fee);
			} else {
				return String.format(
						"result %d\nerrMsg %s\nphoneNumber %s\nnationCode %s",
						result, errMsg, phoneNumber, nationCode);
			}
		}
	}

	public int result;
	public String errMsg = "";
	public String ext = "";
	public ArrayList<Detail> details;

	public String toString() {
		return String.format(
				"SmsMultiSenderResult\nresult %d\nerrMsg %s\next %s\ndetails %s",
				result, errMsg, ext, details);
	}
}
