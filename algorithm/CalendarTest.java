package algorithm;

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
    @Test
    public void testRamdomGenerateList1() {
        int[] ls = {1, 2, 3, 4, 6, 8, 10, 23, 1};
        int[] test1;
        int[] test2;

        test1 = Calendar.randomSort(ls);
        test2 = Calendar.randomSort(ls);

        for (int i = 0; i < ls.length; i++) {
            System.out.println("test2: " + test1[i] + " test2: " + test2[i] + " ls: " + ls[i]);
        }
    }
    @Test
    public void testRamdomGenerateList2() {
        int[] ls = {0, 1, 2, 3, 4};
        int[] test1;
        int[] test2;

        test1 = Calendar.randomSort(ls);
        test2 = Calendar.randomSort(ls);

        for (int i = 0; i < 5; i++) {
            System.out.println("test3: " + test1[i] + " test2: " + test2[i] + " ls: " + ls[i]);
        }
    }
    @Test
    public void testRamdomGenerateList3() {
        int[] ls = {1};
        int[] test1;
        int[] test2;

        test1 = Calendar.randomSort(ls);
        test2 = Calendar.randomSort(ls);

        for (int i = 0; i < 1; i++) {
            System.out.println("test4: " + test1[i] + " test2: " + test2[i] + " ls: " + ls[i]);
        }
    }

    @Test
    public void testRamdomGenerateList4() {
        int[] ls = {6, 5, 4, 3, 2, 1};
        int[] test1;
        int[] test2;

        test1 = Calendar.randomSort(ls);
        test2 = Calendar.randomSort(ls);

        for (int i = 0; i < 1; i++) {
            System.out.println("test4: " + test1[i] + " test2: " + test2[i] + " ls: " + ls[i]);
        }
    }

    @Test
    public void testRamdomGenerateList14() {
        int[] ls = {6, 5, 4, 3, 2, 1};
        int[] test1;
        int[] test2;

        test1 = Calendar.randomSort(ls);
        test2 = Calendar.randomSort(ls);

        for (int i = 0; i < 1; i++) {
            System.out.println("test4: " + test1[i] + " test2: " + test2[i] + " ls: " + ls[i]);
        }
    }

    @Test
    public void testRamdomGenerateList5() {
        int[] ls = {6, 5, 4, 3, 2, 1};
        int[] test1;
        int[] test2;

        test1 = Calendar.randomSort(ls);
        test2 = Calendar.randomSort(ls);

        for (int i = 0; i < 1; i++) {
            System.out.println("test4: " + test1[i] + " test2: " + test2[i] + " ls: " + ls[i]);
        }
    }

    @Test
    public void testRamdomGenerateList6() {
        int[] ls = {6, 5, 4, 3, 2, 1, 5, 7};
        int[] test1;
        int[] test2;

        test1 = Calendar.randomSort(ls);
        test2 = Calendar.randomSort(ls);

        for (int i = 0; i < 1; i++) {
            System.out.println("test4: " + test1[i] + " test2: " + test2[i] + " ls: " + ls[i]);
        }
    }

    @Test
    public void testRamdomGenerateListr() {
        int[] ls = {6, 5, 4, 3, 2, 1, 1000, 3};
        int[] test1;
        int[] test2;

        test1 = Calendar.randomSort(ls);
        test2 = Calendar.randomSort(ls);

        for (int i = 0; i < 1; i++) {
            System.out.println("test4: " + test1[i] + " test2: " + test2[i] + " ls: " + ls[i]);
        }
    }
}