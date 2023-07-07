package slssdhs.xyz;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class dateTest {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d, EEEE, MMMM yyyy", Locale.ENGLISH);

        LocalDate testDate1 = LocalDate.of(2023, 9, 3);
        String testDateStr1 = testDate1.format(formatter);

        LocalDate testDate2 = LocalDate.of(2023, 7, 4);
        String testDateStr2 = testDate2.format(formatter);

        String this_xpath = "//td[contains(@aria-label,'" + testDateStr1 + "')]";
        String a = "aaa";
        String contains_a = "contains" + "'" + a + "'";
        System.out.println(this_xpath);


    }

    @org.jetbrains.annotations.NotNull
    public static String tt() {
        return null;
    }

}
