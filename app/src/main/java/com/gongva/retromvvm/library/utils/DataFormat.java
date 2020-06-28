package com.gongva.retromvvm.library.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 数据格式化方法集合
 *
 * @author gongwei 2018/12/18.
 */
public class DataFormat {

    /**
     * 人民币分转元
     * 返回形如12 or 12.1 or 12.12
     *
     * @param cent
     * @return
     */
    public static String centToYuan(long cent) {
        return round(cent / 100f, 2).stripTrailingZeros().toPlainString();
    }

    /**
     * 格式化文件大小
     */
    public static String formatFileSize(long sizeByte) {
        if (sizeByte < 1024 * 1024 * 1024) {
            return (round(sizeByte / 1024 / 1024f, 1)) + "MB";
        } else {
            return (round(sizeByte / 1024 / 1024 / 1024f, 1)) + "GB";
        }
    }

    /**
     * 四舍五入.
     *
     * @param number  原数
     * @param decimal 保留几位小数
     * @return 四舍五入后的值
     */
    public static BigDecimal round(double number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 将map<String, String>的value进行String返回，中间逗号分隔
     *
     * @param map
     * @return
     */
    public static String getStringFromMapValue(HashMap<String, String> map) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : map.keySet()) {
            stringBuffer.append(map.get(key));
            stringBuffer.append(",");
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    /**
     * 手机号中建4位变*号
     *
     * @param phone
     * @return
     */
    public static String hidePhoneStars(String phone) {
        String result = null;
        if (!TextUtils.isEmpty(phone)) {
            result = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return result;
    }

    /**
     * URL后面追加参数
     *
     * @param url
     * @param paramKey   参数key
     * @param paramValue 参数value
     * @return
     */
    public static String addParamToUrl(String url, String paramKey, String paramValue) {
        if (!TextUtils.isEmpty(url)) {
            String concat = url.lastIndexOf('?') != -1 ? "&" : "?";
            url = url + concat + paramKey + "=" + paramValue;
        }
        return url;
    }

    /**
     * 将人民币格式化为带前缀¥格式
     *
     * @param rmb
     * @return
     */
    public static String formatRMBWithSymbol(String rmb) {
        String result = "";
        if (!TextUtils.isEmpty(rmb) && !rmb.startsWith("¥")) {
            result = String.format("¥%s", rmb.contains("¥") ? rmb.replace("¥", "") : rmb);
        }
        return result;
    }

    /**
     * 把图整成横向满屏的
     *
     * @param data
     * @return
     */
    public static String getSingleColumnPicWebData(String data) {
        if (data != null) {
            if (data.contains("<img") || data.contains("< img")) {
                data = data.replace("<img", "<img width='100%' height='auto'");
                data = data.replace("< img", "< img width='100%' height='auto'");
            } else {
                data = data.replace("\u003cimg", "\u003cimg width='100%' height='auto'");
                data = data.replace("\u003c img", "\u003c img width='100%' height='auto'");
            }
            data = data.replaceAll("width:\\s*\\d*\\.*\\d*px;", "");
        }
        return data;
    }

    /**
     * 把秒转换为格式 HH:mm:ss
     *
     * @param second
     * @return
     */
    public static String formatSecondToTime(long second) {
        long hours = second / 3600;
        second = second % 3600;

        long minutes = second / 60;
        second = second % 60;

        StringBuffer result = new StringBuffer();
        if (hours > 0) {
            result.append((hours < 10) ? "0" + hours : hours);
            result.append(":");
        }
        if (minutes > 0) {
            result.append((minutes < 10) ? "0" + minutes : minutes);
        } else {
            result.append("00");
        }
        result.append(":");
        if (second > 0) {
            result.append((second < 10) ? "0" + second : second);
        } else {
            result.append("00");
        }
        return result.toString();
    }
}
