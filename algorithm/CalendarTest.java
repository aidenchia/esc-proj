

import org.junit.Test;

import static org.junit.Assert.*;

public class CalendarTest {
    @Test
    public void testRamdomGenerateList() {
        int[] ls = {1, 2, 3, 4};
        int[] test1;
        int[] test2;

        test1 = Calendar.randomSort(ls);
        test2 = Calendar.randomSort(ls);

        for (int i = 0; i < 4; i++) {
            System.out.println("test1: " + test1[i] + " test2: " + test2[i] + " ls: " + ls[i]);
        }
    }
}