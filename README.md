腾讯云短信 Java SDK
===


# Overview

> 目前腾讯云短信为客户提供国内短信、海外短信和语音通知三大服务。

> 国内短信提供单发、群发、带模板ID单发、带模板ID群发以及短信回执与回复拉取。

> 海外短信和国内短信使用同一接口，只需替换相应的国家码与手机号码，每次请求群发接口手机号码需全部为国内或者海外手机号码。

> 语音通知目前支持语音验证码和语音通知功能。


# Getting Start

## 准备

- [ ] 申请APPID以及APPKey

> 在开始本教程之前，您需要先获取APPID和APPkey,如您尚未申请，请到https://console.qcloud.com/sms/smslist 中添加应用。应用添加成功后您将获得APPID以及APPKey,注意APPID是以14xxxxx开头。

- [ ] 申请签名

> 下发短信必须携带签名，在相应服务模块 *短信内容配置*  中进行申请。

- [ ] 申请模板

> 下发短信内容必须经过审核，在相应服务 *短信内容配置* 中进行申请。

完成以上三项便可开始代码开发。

## 安装

qcloudsms可以采用多种方式进行安装，我们提供以下三种方法供用户使用:

### maven

要使用qcloudsms功能，需要在pom.xml中添加如下依赖

```xml
<dependency>
  <groupId>com.github.qcloudsms</groupId>
  <artifactId>sms</artifactId>
  <version>1.0.0</version>
</dependency>
```

### sbt

```
libraryDependencies += "com.github.qcloudsms" % "sms" % "1.0.0"
```

### 其他

- 方法1

将[源代码](https://github.com/qcloudsms/qcloudsms_java/tree/master/src)直接引入到项目工程中。

- 方法2

 将[JAR包]( http://central.maven.org/maven2/com/github/qcloudsms/sms/0.9.2/qcloudsms-1.0.0.jar)直接引入到您的工程中。

>`Note`: 由于qcloudsms中需要使用以下四个依赖项目 [org.json](http://central.maven.org/maven2/org/json/json/20170516/json-20170516.jar) , [httpclient](http://central.maven.org/maven2/org/apache/httpcomponents/httpclient/4.5.3/httpclient-4.5.3.jar), [httpcore](http://central.maven.org/maven2/org/apache/httpcomponents/httpcore/4.4.7/httpcore-4.4.7.jar), [httpmine](http://central.maven.org/maven2/org/apache/httpcomponents/httpmime/4.5.3/httpmime-4.5.3.jar)
`采用方法1，2都需要将以上四个jar包导入工程`。

## 用法

> 若您对接口存在疑问，可以查阅[API开发指南](https://cloud.tencent.com/document/product/382/5808)和[API文档](https://qcloudsms.github.io/qcloudsms_java/)。

- **准备必要参数**

```java
int appid = 122333333;
String appkey = "9ff91d87c2cd7cd0ea762f141975d1df37481d48700d70ac37470aefc60f9bad";
String[] phoneNumbers = {"21212313123", "12345678902", "12345678903"};
int templateId = 7839;  // 模板id需要从在相应服务 *短信内容配置* 中进行申请
```

- **单发短信**

```java
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

try {
    SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
    SmsSingleSenderResult result = ssender.send(0, "86", phoneNumbers[0],
        "【腾讯】您的验证码是: 5678", "", "");
    System.out.print(result);
} catch (HTTPException e) {
    // HTTP响应码错误
    e.printStackTrace();
} catch (JSONException e) {
    // json解析错误
    e.printStackTrace();
} catch (IOException e) {
    // 网路IO错误
    e.printStackTrace();
}
```

> `Note`: 发送短信没有指定模板ID时，发送的内容需要与已审核通过的模板内容相匹配，才可能下发成功，否则返回失败。
> `Note`: 如需发送海外短信，同样可以使用此接口，只需将国家码"86"改写成对应国家码号。

- **指定模板ID单发短信**

```java
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

try {
    String[] params = {"5678"};
    SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
    SmsSingleSenderResult result = sendWithParam("86", phoneNumbers[0],
        templateId, params, "", "", "");
    System.out.print(result);
} catch (HTTPException e) {
    // HTTP响应码错误
    e.printStackTrace();
} catch (JSONException e) {
    // json解析错误
    e.printStackTrace();
} catch (IOException e) {
    // 网路IO错误
    e.printStackTrace();
}
```

> `Note:`无论单发短信还是指定模板ID单发短信都需要从控制台中申请模板并且模板已经审核通过，才可能下发成功，否则返回失败。

- **群发**

```java
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;

try {
    SmsMultiSender msender = new SmsMultiSender(appid, appkey);
    SmsMultiSenderResult result =  msender.send(0, "86", phoneNumbers,
        "【腾讯】您的验证码是: 5678", "", "");
    System.out.print(result);
} catch (HTTPException e) {
    // HTTP响应码错误
    e.printStackTrace();
} catch (JSONException e) {
    // json解析错误
    e.printStackTrace();
} catch (IOException e) {
    // 网路IO错误
    e.printStackTrace();
}
```
- **指定模板ID群发**

```java
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;

try {
    String[] params = {"5678"};
    SmsMultiSender msender = new SmsMultiSender(appid, appkey);
    SmsMultiSenderResult result =  msender..sendWithParam("86", phoneNumbers,
        templateId, params, "", "", "");
    System.out.print(result);
} catch (HTTPException e) {
    // HTTP响应码错误
    e.printStackTrace();
} catch (JSONException e) {
    // json解析错误
    e.printStackTrace();
} catch (IOException e) {
    // 网路IO错误
    e.printStackTrace();
}
```
> `Note: `群发一次请求最多支持200个号码，如有对号码数量有特殊需求请联系腾讯云短信技术支持(QQ:3012203387)。

- **发送语音验证码**

```java
import com.github.qcloudsms.SmsVoiceVerifyCodeSender;
import com.github.qcloudsms.SmsVoiceVerifyCodeSenderResult;

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
    // 网路IO错误
    e.printStackTrace();
}
```

>`Note`: 语音验证码发送只需提供验证码数字，例如在msg=“5678”，您收到的语音通知为“您的语音验证码是5678”，如需自定义内容，可以使用语音通知。

- **发送语音通知**

```java
import com.github.qcloudsms.SmsVoicePromptSender;
import com.github.qcloudsms.SmsVoicePromptSenderResult;

try {
    SmsVoicePromptSender vpsender = new SmsVoicePromptSender(appid, appkey);
    SmsVoicePromptSenderResult result = vpsender.send("86", phoneNumbers[0],
        2, 2, "5678", ""));
    System.out.print(result);
} catch (HTTPException e) {
    // HTTP响应码错误
    e.printStackTrace();
} catch (JSONException e) {
    // json解析错误
    e.printStackTrace();
} catch (IOException e) {
    // 网路IO错误
    e.printStackTrace();
}
```

- **拉取短信回执以及回复**

```java
import com.github.qcloudsms.SmsStatusPuller;
import com.github.qcloudsms.SmsStatusPullCallbackResult;
import com.github.qcloudsms.SmsStatusPullReplyResult;

try {
    SmsStatusPuller spuller = new SmsStatusPuller(appid, appkey);
    // 拉取短信回执
    SmsStatusPullCallbackResult callbackResult = spuller.pullCallback(10);
    System.out.println(callbackResult);

    // 拉取回复
    SmsStatusPullReplyResult replyResult = spuller.pullReply(10);
    System.out.println(replyResult);
} catch (HTTPException e) {
    // HTTP响应码错误
    e.printStackTrace();
} catch (JSONException e) {
    // json解析错误
    e.printStackTrace();
} catch (IOException e) {
    // 网路IO错误
    e.printStackTrace();
}
```

> `Note:` 短信拉取功能需要联系腾讯云短信技术支持(QQ:3012203387)，量大客户可以使用此功能批量拉取，其他客户不建议使用。

- **拉取单个手机短信状态**

```java
import com.github.qcloudsms.SmsMobileStatusPuller;
import com.github.qcloudsms.SmsStatusPullCallbackResult;
import com.github.qcloudsms.SmsStatusPullReplyResult;

try {
    SmsMobileStatusPuller mspuller = new SmsMobileStatusPuller(appid, appkey);
    // 拉取短信回执
    SmsStatusPullCallbackResult callbackResult = mspuller.pullCallback(10);
    System.out.println(callbackResult);

    // 拉取回复
    SmsStatusPullReplyResult replyResult = mspuller.pullReply(10);
    System.out.println(replyResult);
} catch (HTTPException e) {
    // HTTP响应码错误
    e.printStackTrace();
} catch (JSONException e) {
    // json解析错误
    e.printStackTrace();
} catch (IOException e) {
    // 网路IO错误
    e.printStackTrace();
}
```

- **发送国际短信**

国际短信参考单发短信
