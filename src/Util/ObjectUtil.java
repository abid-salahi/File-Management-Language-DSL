package Util;

public class ObjectUtil {

    /**
     * Returns true if both objects are null or equal
     */
    public static boolean nullOrEqual(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        }
        if (o1 == null) {
            return false;
        }
        return o1.equals(o2);
    }
}
