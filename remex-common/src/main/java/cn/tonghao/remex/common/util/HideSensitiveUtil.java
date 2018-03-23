package cn.tonghao.remex.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 敏感信息处理
 */
public class HideSensitiveUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HideSensitiveUtil.class);

    private static final String BANK_CARD = "\\\"(\\d{19})\\\"|\\\"(\\d{18})\\\"|\\\"(\\d{16})\\\"";   //银行卡
    private static final String ID_CARD = "\\\"(\\d{18})\\\"|\\\"(\\d{17}X)\\\"";              //身份证
    private static final String MOBILE_EXPRESSION = "\\\"\\d{11}\\\"";                 //电话号码
    private static final String ACC_NAME_EXPRESSION = "\\\"[\\u4e00-\\u9fa5]{2,5}\\\"";   //账户名称
    private static final String EMAIL_EXPRESSION = "\\\"[\\w\\-\\.]+\\@[\\w\\-\\.]{2,}\\.[\\w\\-\\.]+\\\""; //邮箱
    private static final int[] ID_INT_ARR_0 = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final int[] ID_INT_ARR_1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int[] ID_INT_ARR_2 = {1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2};
    private static final Pattern P_BANK_CARD = Pattern.compile(BANK_CARD); // 正则表达式
    private static final Pattern P_ID_CARD = Pattern.compile(ID_CARD); // 正则表达式
    private static final Pattern P_MOBILE = Pattern.compile(MOBILE_EXPRESSION);//正则表达式
    private static final Pattern P_ACC_NAME = Pattern.compile(ACC_NAME_EXPRESSION);//正则表达式
    private static final Pattern P_EMAIL = Pattern.compile(EMAIL_EXPRESSION);//正则表达式
    private static final String DEFAULT_SENSITIVE_PLACE_HOLDER = "*";

    private static final int BANK_CARD_DEFAULT_PREFIX_COUNT = 6;    //银行卡默认显示前缀位数
    private static final int BANK_CARD_DEFAULT_SUFFIX_COUNT = 4;    //银行卡默认显示后缀位数

    private static final int ID_CARD_DEFAULT_PREFIX_COUNT = 6;    //身份证号默认显示前缀位数
    private static final int ID_CARD_DEFAULT_SUFFIX_COUNT = 4;    //身份证默认显示后缀位数

    private static final int MOBILE_DEFAULT_PREFIX_COUNT = 3;    //手机号默认显示前缀位数
    private static final int MOBILE_DEFAULT_SUFFIX_COUNT = 4;    //手机号默认显示后缀位数

    private static final int ACC_NAME_DEFAULT_PREFIX_COUNT = 1;    //账号默认显示前缀位数
    private static final int ACC_NAME_DEFAULT_SUFFIX_COUNT = 1;    //账号默认显示后缀位数

    private static final int EMAIL_DEFAULT_PREFIX_COUNT = 4;    //账号默认显示前缀位数
    private static final int EMAIL_DEFAULT_SUFFIX_COUNT = 2;    //账号默认显示后缀位数

    public static String hideBankAndIdCard(String sensitiveInfo) {
        if (StringUtils.isEmpty(sensitiveInfo)) {
            return sensitiveInfo;
        }
        sensitiveInfo = hideBankCard(sensitiveInfo);
        sensitiveInfo = hideIdCard(sensitiveInfo);
        return sensitiveInfo;
    }

    /**
     * 校验身份证号
     *
     * @param cardId
     * @return
     */
    public static boolean checkIdCard(String cardId) {
        int sum = 0;
        for (int i = 0; i < ID_INT_ARR_0.length; i++) {
            // 2.将这17位数字和系数相乘的结果相加。
            sum += Character.digit(cardId.charAt(i), 10) * ID_INT_ARR_0[i];
        }
        // 3.用加出来和除以11，看余数是多少？
        int mod = sum % 11;
        // 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2。
        String matchDigit = "";
        for (int i = 0; i < ID_INT_ARR_1.length; i++) {
            int j = ID_INT_ARR_1[i];
            if (j == mod) {
                matchDigit = String.valueOf(ID_INT_ARR_2[i]);
                if (ID_INT_ARR_2[i] > 57) {
                    matchDigit = String.valueOf((char) ID_INT_ARR_2[i]);
                }
            }
        }
        if (matchDigit.equals(cardId.substring(cardId.length() - 1))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 显示前3后4位手机号
     *
     * @param mobile
     * @return
     */
    public static String mobileFormat(String mobile) {

        return mobileFormat(mobile, MOBILE_DEFAULT_PREFIX_COUNT, MOBILE_DEFAULT_SUFFIX_COUNT);
    }

    public static String mobileFormat(String mobile, int prefixCount, int suffixCount) {

        return mobileFormat(mobile, prefixCount, suffixCount, DEFAULT_SENSITIVE_PLACE_HOLDER);
    }

    public static String mobileFormat(String mobile, int prefixCount, int suffixCount, String placeHolder) {
        if (StringUtils.isBlank(mobile) || mobile.length() <= (prefixCount + suffixCount)) {
            return mobile;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(mobile.substring(0, prefixCount));
        for (int i = 0, length = (mobile.length() - (prefixCount + suffixCount)); i < length; i++) {
            sb.append(placeHolder);
        }
        sb.append(mobile.substring(mobile.length() - suffixCount));
        return sb.toString();
    }

    public static String hideMobile(String sensitiveInfo) {
        if (StringUtils.isEmpty(sensitiveInfo)) {
            return sensitiveInfo;
        }
        Matcher phoneM = P_MOBILE.matcher(sensitiveInfo); // 操作的字符串
        while (phoneM.find()) {
            String matcherStr = sensitiveInfo.substring(phoneM.start() + 1, phoneM.end() - 1);
            sensitiveInfo = sensitiveInfo.replace(matcherStr, mobileFormat(matcherStr));
        }
        return sensitiveInfo;
    }

    /**
     * 显示前6后4位卡号
     *
     * @param bankCard
     * @return
     */
    public static String bankCardFormat(String bankCard) {

        return bankCardFormat(bankCard, BANK_CARD_DEFAULT_PREFIX_COUNT, BANK_CARD_DEFAULT_SUFFIX_COUNT);
    }

    public static String bankCardFormat(String bankCard, int prefixCount, int suffixCount) {

        return bankCardFormat(bankCard, prefixCount, suffixCount, DEFAULT_SENSITIVE_PLACE_HOLDER);
    }

    public static String bankCardFormat(String bankCard, int prefixCount, int suffixCount, String placeHolder) {
        if (StringUtils.isBlank(bankCard) || bankCard.length() < prefixCount + suffixCount) {
            return bankCard;
        }

        StringBuilder sb = new StringBuilder();
        String prefix = bankCard.substring(0, prefixCount);
        sb.append(prefix);
        for (int i = 0, length = (bankCard.length() - (prefixCount + suffixCount)); i < length; i++) {
            sb.append(placeHolder);
        }
        String suffix = bankCard.substring(bankCard.length() - suffixCount);
        sb.append(suffix);
        return sb.toString();
    }

    public static String hideBankCard(String sensitiveInfo) {
        if (StringUtils.isEmpty(sensitiveInfo)) {
            return sensitiveInfo;
        }
        Matcher mBankCard = P_BANK_CARD.matcher(sensitiveInfo);
        while (mBankCard.find()) {
            String matcher = sensitiveInfo.substring(mBankCard.start() + 1, mBankCard.end() - 1);
            sensitiveInfo = sensitiveInfo.replace(matcher, bankCardFormat(matcher));
        }
        return sensitiveInfo;
    }

    /**
     * 显示前6后4位证件号
     *
     * @param idCard
     * @return
     */
    public static String idCardFormat(String idCard) {

        return idCardFormat(idCard, ID_CARD_DEFAULT_PREFIX_COUNT, ID_CARD_DEFAULT_SUFFIX_COUNT);
    }

    public static String idCardFormat(String idCard, int prefixCount, int suffixCount) {

        return idCardFormat(idCard, prefixCount, suffixCount, DEFAULT_SENSITIVE_PLACE_HOLDER);
    }

    public static String idCardFormat(String idCard, int prefixCount, int suffixCount, String placeHolder) {
        if (StringUtils.isBlank(idCard) || idCard.length() <= prefixCount + suffixCount) {
            return idCard;
        }
        StringBuilder sb = new StringBuilder();
        String prefix = idCard.substring(0, prefixCount);
        sb.append(prefix);
        for (int i = 0, length = idCard.length() - (prefixCount + suffixCount); i < length; i++) {
            sb.append(placeHolder);
        }
        String suffix = idCard.substring(idCard.length() - suffixCount);
        sb.append(suffix);
        return sb.toString();
    }

    public static String hideIdCard(String sensitiveInfo) {
        if (StringUtils.isEmpty(sensitiveInfo)) {
            return sensitiveInfo;
        }
        Matcher mIdCard = P_ID_CARD.matcher(sensitiveInfo); // 操作的字符串
        while (mIdCard.find()) {
            String matcher = sensitiveInfo.substring(mIdCard.start() + 1, mIdCard.end() - 1);
            if (checkIdCard(matcher)) {
                sensitiveInfo = sensitiveInfo.replace(matcher, idCardFormat(matcher));
            }
        }
        return sensitiveInfo;
    }

    /**
     * 显示姓名第一位和最后一位，两个字只显示第一位
     *
     * @param accName
     * @return
     */
    public static String accNameFormat(String accName) {

        return accNameFormat(accName, ACC_NAME_DEFAULT_PREFIX_COUNT, ACC_NAME_DEFAULT_SUFFIX_COUNT);
    }

    public static String accNameFormat(String accName, int prefixCount, int suffixCount) {

        return accNameFormat(accName, prefixCount, suffixCount, DEFAULT_SENSITIVE_PLACE_HOLDER);
    }

    public static String accNameFormat(String accName, int prefixCount, int suffixCount, String placeHolder) {
        if (StringUtils.isBlank(accName) || accName.length() < (prefixCount + suffixCount)) {
            return placeHolder + accName;
        } else if (accName.length() == (prefixCount + suffixCount)) {
            return placeHolder + accName.charAt(1);
        } else {
            StringBuilder sb = new StringBuilder();
            String prefix = accName.substring(0, prefixCount);
            sb.append(placeHolder);
            for (int i = 0, length = (accName.length() - (prefixCount + suffixCount)); i < length; i++) {
                sb.append(placeHolder);
            }
            String suffix = accName.substring(accName.length() - suffixCount);
            sb.append(suffix);
            return sb.toString();
        }
    }

    public static String hideAccName(String sensitiveInfo) {
        if (StringUtils.isEmpty(sensitiveInfo)) {
            return sensitiveInfo;
        }
        Matcher m = P_ACC_NAME.matcher(sensitiveInfo);
        while (m.find()) {
            String matcher = sensitiveInfo.substring(m.start() + 1, m.end() - 1);
            sensitiveInfo = sensitiveInfo.replace(matcher, accNameFormat(matcher));
        }
        return sensitiveInfo;
    }


    public static String emailFormat(String email) {

        return emailFormat(email, EMAIL_DEFAULT_PREFIX_COUNT, EMAIL_DEFAULT_SUFFIX_COUNT);
    }

    public static String emailFormat(String email, int prefixCount, int suffixCount) {

        return emailFormat(email, prefixCount, suffixCount, DEFAULT_SENSITIVE_PLACE_HOLDER);
    }

    public static String emailFormat(String email, int prefixCount, int suffixCount, String placeHolder) {
        if (StringUtils.isBlank(email) || email.length() < (prefixCount + suffixCount)) {
            return email + placeHolder + placeHolder;
        }
        String[] emailGroup = email.split("@");
        String emailContent = emailGroup[0];
        String emailAddress = emailGroup[1];

        StringBuilder sb = new StringBuilder();
        String prefix = emailContent.substring(0, prefixCount);
        sb.append(prefix);
        for (int i = 0, length = (emailContent.length() - (prefixCount + suffixCount)); i < length; i++) {
            sb.append(placeHolder);
        }
        String suffix = emailContent.substring(emailContent.length() - suffixCount);
        sb.append(suffix);
        sb.append("@");
        sb.append(emailAddress);
        return sb.toString();
    }


    public static String hideEmail(String sensitiveInfo) {
        if (StringUtils.isEmpty(sensitiveInfo)) {
            return sensitiveInfo;
        }
        Matcher m = P_EMAIL.matcher(sensitiveInfo);
        while (m.find()) {
            String matcher = sensitiveInfo.substring(m.start() + 1, m.end() - 1);
            sensitiveInfo = sensitiveInfo.replace(matcher, emailFormat(matcher));
        }
        return sensitiveInfo;
    }

    public static String toString(Object object) {

        String sensitiveInfo = JsonUtil.toString(object);
        return hideSensitiveInfo(sensitiveInfo);
    }

    /**
     * 根据特定的正则表达式加密卡号和手机号以及姓名
     *
     * @param sensitiveInfo
     * @return
     */
    public static String hideSensitiveInfo(String sensitiveInfo) {
        if (StringUtils.isEmpty(sensitiveInfo)) {
            return sensitiveInfo;
        }
        sensitiveInfo = hideBankCard(sensitiveInfo);
        sensitiveInfo = hideIdCard(sensitiveInfo);
        sensitiveInfo = hideMobile(sensitiveInfo);
        sensitiveInfo = hideAccName(sensitiveInfo);
        sensitiveInfo = hideEmail(sensitiveInfo);
        return sensitiveInfo;
    }

}
