import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsStatusPuller;
import com.github.qcloudsms.SmsMobileStatusPuller;
import com.github.qcloudsms.SmsStatusPullCallbackResult;
import com.github.qcloudsms.SmsStatusPullReplyResult;
import com.github.qcloudsms.SmsVoiceVerifyCodeSender;
import com.github.qcloudsms.SmsVoiceVerifyCodeSenderResult;
import com.github.qcloudsms.SmsVoicePromptSender;
import com.github.qcloudsms.SmsVoicePromptSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

import org.json.JSONException;

import java.io.IOException;


public class QcloudSmsTest {

    public static void main(String[] args) {
        // 短信应用SDK AppID
        int appid = 1400009099; // 1400开头

        // 短信应用SDK AppKey
        String appkey = "9ff91d87c2cd7cd0ea762f141975d1df37481d48700d70ac37470aefc60f9bad";

        // 需要发送短信的手机号码
        String[] phoneNumbers = {"21212313123", "12345678902", "12345678903"};

        // 短信模板ID，需要在短信应用中申请
        // NOTE: 这里的模板ID`7839`只是一个示例，
        // 真实的模板ID需要在短信控制台中申请
        int templateId = 7839;

        // 签名
        // NOTE: 这里的签名"腾讯云"只是一个示例，
        // 真实的签名需要在短信控制台中申请，另外
        // 签名参数使用的是`签名内容`，而不是`签名ID`
        String smsSign = "腾讯云";

        // 单发短信
        try {
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.send(0, "86", phoneNumbers[0],
                "【腾讯云】您的验证码是: 5678", "", "");
            System.out.print(result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }

        // 指定模板ID单发短信
        try {
            String[] params = {"5678"};
            SmsMultiSender msender = new SmsMultiSender(appid, appkey);
            SmsMultiSenderResult result =  msender.sendWithParam("86", phoneNumbers,
                templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.print(result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }

        // 发送语音验证码
        try {
            SmsVoiceVerifyCodeSender vvcsender = new SmsVoiceVerifyCodeSender(appid,appkey);
            SmsVoiceVerifyCodeSenderResult result = vvcsender.send("86", phoneNumbers[0],
                "5678", 2, "");
            System.out.print(result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }

        // 发送语音通知
        try {
            SmsVoicePromptSender vpsender = new SmsVoicePromptSender(appid, appkey);
            SmsVoicePromptSenderResult result = vpsender.send("86", phoneNumbers[0],
                2, 2, "5678", "");
            System.out.print(result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }

        // 拉取短信回执以及回复
        try {
            // Note: 短信拉取功能需要联系腾讯云短信技术支持(QQ:3012203387)开通权限
            int maxNum = 10;  // 单次拉取最大量
            SmsStatusPuller spuller = new SmsStatusPuller(appid, appkey);

            // 拉取短信回执
            SmsStatusPullCallbackResult callbackResult = spuller.pullCallback(maxNum);
            System.out.println(callbackResult);

            // 拉取回复
            SmsStatusPullReplyResult replyResult = spuller.pullReply(naxNum);
            System.out.println(replyResult);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }

        // 拉取单个手机短信状态
        try {
            int beginTime = 1511125600;  // 开始时间(unix timestamp)
            int endTime = 1511841600;    // 结束时间(unix timestamp)
            int maxNum = 10;             // 单次拉取最大量
            SmsMobileStatusPuller mspuller = new SmsMobileStatusPuller(appid, appkey);

            // 拉取短信回执
            SmsStatusPullCallbackResult callbackResult = mspuller.pullCallback("86",
                phoneNumbers[0], beginTime, endTime, maxNum);
            System.out.println(callbackResult);

            // 拉取回复
            SmsStatusPullReplyResult replyResult = mspuller.pullReply("86",
                phoneNumbers[0], beginTime, endTime, maxNum);
            System.out.println(replyResult);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
    }
}
