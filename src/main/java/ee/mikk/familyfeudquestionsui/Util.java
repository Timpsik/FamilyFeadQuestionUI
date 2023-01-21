package ee.mikk.familyfeudquestionsui;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
