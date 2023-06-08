package lh.wordtree.comm.utils;

public abstract class ChineseNumberUtils {
    public static String translateToChineseNumerals(int number) {
        String[] chineseNumerals = {"��", "һ", "��", "��", "��", "��", "��", "��", "��", "��", "ʮ"};
        if (number == 0) {
            return chineseNumerals[0];
        }
        StringBuilder sb = new StringBuilder();
        int digit;
        while (number > 0) {
            digit = number % 10;
            sb.insert(0, chineseNumerals[digit]);
            number /= 10;
        }
        return sb.toString();
    }
}
