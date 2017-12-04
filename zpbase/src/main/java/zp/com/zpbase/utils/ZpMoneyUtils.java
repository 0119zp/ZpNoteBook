package zp.com.zpbase.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Administrator on 2017/9/26 0026.
 */

public class ZpMoneyUtils {

    public static final int UNIT_FEN = 1;
    public static final int UNIT_YUAN = 2;

    /**
     * 返回要求的货币形式 ex: 1000.000 --》 1,000.00 货币format
     *
     * @param money 金额数目
     * @param unit  单位
     * @return
     */
    private static String getMoney(double money, String unit) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
        currencyFormat.setMaximumFractionDigits(2);
        return currencyFormat.format(money).substring(1) + unit;
    }

    /**
     * 返回要求的货币形式 ex: 1000.000 --》 1,000.00 货币format
     *
     * @param money 金额数目（分）
     * @param unit  单位
     * @return
     */
    public static String getMoneyString(double money, String unit) {
        return getMoney(money / 100.00, unit);
    }

    /**
     * 返回要求的货币形式 ex: 1000.000 --》 1,000.00 货币format
     *
     * @param money    金额数目
     * @param unitFlag 金额数目单位
     * @param unit     单位
     * @return
     */
    public static String getMoneyString(double money, int unitFlag, String unit) {
        if (unitFlag == UNIT_FEN) {
            money = money / 100.00;
        }
        return getMoney(money, unit);
    }

    /**
     * 返回货币符号, "$", "￥"
     *
     * @return
     */
    public static String getMoneyCharacter(Locale local) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(local);
        currencyFormat.setMaximumFractionDigits(2);
        return currencyFormat.format(1).substring(0, 1);
    }

}
