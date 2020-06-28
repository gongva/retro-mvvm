package com.gongva.retromvvm.library.utils;

import android.text.TextUtils;

import com.hik.core.java.tools.IDCardValidateUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据验证Util
 *
 * @author gongwei 2018.12.25
 */
public class ValidateUtil {

    private static final String PROVINCE_SHORT = "京,津,沪,渝,晋,蒙,吉,黑,苏,浙,皖,闽,赣,鲁,豫,鄂,湘,粤,桂,琼,川,云,藏,青,宁,新,港,澳,台,甘,辽,冀,贵,陕";

    private static final String PROVINCE_CODE = "Q,W,E,R,T,Y,U,O,P,A,S,D,F,G,H,J,K,L,Z,X,C,V,B,N,M";

    public static boolean validatePlateNum(String plateNum) {
        if (TextUtils.isEmpty(plateNum)) return false;

        //车牌号必须是7位或者8位
        if (plateNum.length() < 7 || plateNum.length() > 8) return false;

        String provinceShort = plateNum.substring(0, 1);
        String provinceCode = plateNum.substring(1, 2);
        return PROVINCE_SHORT.contains(provinceShort) && PROVINCE_CODE.contains(provinceCode);
    }

    /**
     * 身份证验证
     *
     * @return boolean
     */
    public static boolean validateIdCard(String idCard) {
        if (TextUtils.isEmpty(idCard)) return false;
        idCard = idCard.replace("x", "X");
        return IDCardValidateUtil.checkFormat(idCard);
    }

    /**
     * 手机号验证
     *
     * @param mobile
     * @return
     */
    public static boolean validateMobile(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return false;
        }
        String phoneRegex = "[1]\\d{10}";
        if (!mobile.matches(phoneRegex)) {
            return false;
        }
        return true;
    }

    /**
     * 邮箱验证
     *
     * @param mail
     * @return
     */
    public static boolean validateMail(String mail) {
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(RULE_EMAIL);
        Matcher m = p.matcher(mail);
        return m.matches();
    }

    /**
     * 判断字符串是否为相同的字符构成
     *
     * @param text
     * @return
     */
    private static boolean isSameCharacters(String text) {
        if (TextUtils.isEmpty(text)) return false;
        if (text.length() == 1) return true;
        char first = text.charAt(0);
        for (int i = 1; i < text.length(); i++) {
            if (first != text.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否连续数字
     * 纯数字(数字0-9,对应ASCII为48-57)
     *
     * @param text
     * @return true：是连续数字
     */
    public static boolean isContinuousDigit(String text) {
        for (int i = 0; i < text.length() - 1; i++) {
            //当前ascii值
            int currentAscii = Integer.valueOf(text.charAt(i));
            //下一个ascii值
            int nextAscii = Integer.valueOf(text.charAt(i + 1));
            //满足区间进行判断
            if (rangeInDefined(currentAscii, 48, 57) && rangeInDefined(nextAscii, 48, 57)) {
                //计算两数之间差一位则为连续
                if (Math.abs((nextAscii - currentAscii)) != 1) {
                    return false;
                } else {
                    continue;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个数字是否在某个区间
     *
     * @param current 当前比对值
     * @param min     最小范围值
     * @param max     最大范围值
     * @return
     */
    private static boolean rangeInDefined(int current, int min, int max) {
        return Math.max(min, current) == Math.min(current, max);
    }

    /**
     * 匹配金额是否符合要求（99999999.99）
     *
     * @param money 金额字符串
     * @return true= 符合 false=不符合
     */
    public static boolean isMoney(String money) {
        String regex = "(^[1-9][0-9]{0,7}(\\.[0-9]{0,2})?)|(^0(\\.[0-9]{0,2})?)";
        return isMatches(money, regex);
    }

    private static boolean isMatches(String text, String format) {
        Pattern pattern = Pattern.compile(format);
        Matcher m = pattern.matcher(text);
        return m.matches();
    }

}
