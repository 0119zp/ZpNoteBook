package zp.com.zpbase.utils;

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/9/26 0026.
 */

public class ZpIsUtils {

    public static boolean isPhoneNumber(String number) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(number)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为有效的手机号,此方法只对输入的手机号位数做限制,必须为8-11位纯数字
     *
     * @param number
     * @return
     */
    public static boolean isValidPhoneNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }

        try {
            Pattern p = Pattern.compile("^[0-9]{8,11}$");
            Matcher m = p.matcher(number);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 以1开头的13位数字。可能4G会有新的号码段或者170等开头的虚拟电话，故没采用更严格的正则过滤
     *
     * @param number
     * @return
     */
    public static boolean isChinesePhoneNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }

        try {
            // Pattern p = Pattern.compile("^13[0-9]{9}|15[012356789][0-9]{8}|18[0256789][0-9]{8}|147[0-9]{8}$");
            Pattern p = Pattern.compile("^1[0-9]{10}$");
            Matcher m = p.matcher(number);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //验证号码 手机号 固话均可
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;

        String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        CharSequence inputStr = phoneNumber;

        Pattern pattern = Pattern.compile(expression);

        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;

    }

    public static boolean isNumber(String number) {
        try {
            Pattern p = Pattern.compile("^[0-9]*$");
            Matcher m = p.matcher(number);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isNumberOrLetter(String number) {
        try {
            Pattern p = Pattern.compile("^[0-9a-zA-Z]*$");
            Matcher m = p.matcher(number);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isLetter(String number) {
        try {
            Pattern p = Pattern.compile("^[a-zA-Z]*$");
            Matcher m = p.matcher(number);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isChineseOrLetterOrNum(String str) {
        try {
            Pattern p = Pattern.compile("^[\u4e00-\u9fa5a-zA-Z0-9]+$");
            Matcher m = p.matcher(str);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否为身份证号码, 支持15位和18位身份证号码验证, 暂不支持地区, 性别校验<br/>
     * 复杂匹配可参考 http://liuzidong.iteye.com/blog/1122417
     *
     * @param id 身份证号
     * @return
     */
    public static boolean isIDNo(String id) {
        if (TextUtils.isEmpty(id)) {
            return false;
        }

        // Pattern pattern = Pattern.compile("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$");
        Pattern pattern = Pattern.compile("^(^\\d{18}$|^\\d{17}(\\d|X|x))$");
        Matcher matcher = pattern.matcher(id);

        return matcher.matches();
    }

    /**
     * 验证姓名是否符合标准，汉字2-10位
     *
     * @param name
     * @return
     */
    public static boolean isChineseName(String name) {
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]{2,10}");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * 验证姓名是否符合标准，汉字或字母2-10位
     *
     * @param name
     * @return
     */
    public static boolean isName(String name) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\u4e00-\u9fa5]{2,10}");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * 验证密码是否符合校验规则，字母和数字组成，6-20位
     *
     * @param passwordStr
     * @return
     */
    public static boolean isPassWord(String passwordStr) {
        Pattern pattern = Pattern.compile("(?!^\\d+$)(?!^[a-zA-Z]+$)[0-9a-zA-Z]{6,20}");
        Matcher matcher = pattern.matcher(passwordStr);
        return matcher.matches();
    }

    /**
     * 验证银行卡号码是否符合规范，必须为数字，16~21位正整数
     *
     * @param number
     * @return
     */
    public static boolean isBankCardNumber(String number) {
        Pattern pattern = Pattern.compile("^[0-9]{16,21}");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

}
