package browserbot.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Методы помогающие при работе с номером телефона.
 */
public class PhoneNumber {

    /**
     * Валидирует номер телефона.
     *
     * @param phoneNumber телефонный номер.
     * @return true - валидный номер, иначе - false.
     * //@todo hibernate validation messages
     */
    public static boolean validate(String phoneNumber) {


        //@Todo message from Hibernate validator
        Pattern pattern = Pattern.compile("^\\+7\\d{10}$");

        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }
}
