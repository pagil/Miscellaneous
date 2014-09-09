package au.test.doj;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import au.test.doj.App;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    @Test
    public void testCurrentFinYearViktar() throws Exception {
        Date[] dates = App.getCurrentFinYearViktar();
        for (Date d : dates) {
            System.out.print(d.getTime() + " ");
            System.out.println(d);
        }
        dates = App.getCurrentFinYearZankar();
        for (Date d : dates) {
            System.out.print(d.getTime() + " ");
            System.out.println(d);
        }
    }
}
