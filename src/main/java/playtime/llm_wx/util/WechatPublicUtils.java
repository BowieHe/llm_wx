package playtime.llm_wx.util;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
public class WechatPublicUtils {

    /**
     * 校验签名
     *
     * @param signature 微信签名
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @param token     我们在公众号平台「基本配置」里定义的token
     * @return 验证结果
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce, String token) {
        // 将token、timestamp、nonce 进行字典序排序
        String[] arr = new String[]{token, timestamp, nonce};
        Arrays.sort(arr);
        // 字符串拼接
        StringBuilder content = new StringBuilder();
        for (String s : arr) {
            content.append(s);
        }
        String tmpStr = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            // sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error("failed to encrypt content", e);
        }
        content = null;
        // sha1加密后的字符串与signature对比
        return tmpStr != null && tmpStr.equals(signature.toUpperCase());
    }

    public static String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }
        return sb.toString();
    }

    //sha1加密
    public static String getSha1(String str) {
        if (null == str || str.isEmpty()) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes(StandardCharsets.UTF_8));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            log.error("exception occurred while get sha1", e);
        }
        return "";
    }

    private static String byteToStr(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();
        for (byte b : byteArray) {
            strDigest.append(byteToHexStr(b));
        }
        return strDigest.toString();
    }

    private static String byteToHexStr(byte mByte) {
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = digit[mByte & 0X0F];
        return new String(tempArr);
    }
}
