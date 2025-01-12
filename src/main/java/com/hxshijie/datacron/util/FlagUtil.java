package com.hxshijie.datacron.util;

/**
 * 使用位掩码代替多个boolean类型flag工具
 * <p>
 * 需要自己定义某一位代表什么
 * @author hxshijie
 * */
public class FlagUtil {

    private static final String initMask = "00000000000000000000000000000000";

    /**
     * 设置位掩码
     * @param flag 业务中的flag
     * @param index 要更改第几位的值，索引值必须是1~31之间的数字
     * @param value 是否具有这个值
     * @return 更新后的flag
     * @throws IndexOutOfBoundsException index超出长度限制时抛出
     * */
    public static int setFlag(int flag, int index, boolean value) throws IndexOutOfBoundsException {
        if (index > 31 || index <= 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", range: 1 ~ 31");
        }

        StringBuilder maskBuilder = new StringBuilder(initMask);
        maskBuilder.replace(initMask.length() - index, initMask.length() - index + 1, "1");
        int mask = Integer.parseInt(maskBuilder.toString(), 2);

        if (value) {
            return flag | mask;
        } else {
            return flag & (~mask);
        }
    }

    /**
     * 设置位掩码
     * <p>
     * 默认value为true具有这个值
     * @param flag 业务中的flag
     * @param index 要更改第几位的值，索引值必须是1~31之间的数字
     * @return 更新后的flag
     * @throws IndexOutOfBoundsException index超出长度限制时抛出
     * */
    public static int setFlag(int flag, int index) throws IndexOutOfBoundsException {
        return setFlag(flag, index, true);
    }

    /**
     * 验证位掩码
     * @param flag 业务中的flag
     * @param index 要更改第几位的值，索引值必须是1~31之间的数字
     * @return 返回是否具有这个值
     * @throws IndexOutOfBoundsException index超出长度限制时抛出
     * */
    public static boolean getFlag(int flag, int index) throws IndexOutOfBoundsException {
        return flag == setFlag(flag, index);
    }
}
