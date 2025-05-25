import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class BoardFieldPossibilitiesTest {
    @Test
    void testSimpleAndFastMostImportantFeatures() {
        BoardFieldPossibilities options = new BoardFieldPossibilities(2);

        options.setAllValuesAsUnknown();
        assertArrayEquals(new int[] {}, options.getValuesPossible());
        assertArrayEquals(new int[] {}, options.getValuesImpossible());
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, options.getValuesUnknown());

        options.setAllValuesAsImpossible();
        assertArrayEquals(new int[] {}, options.getValuesPossible());
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, options.getValuesImpossible());
        assertArrayEquals(new int[] {}, options.getValuesUnknown());

        options.setAllValuesAsPossible();
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, options.getValuesPossible());
        assertArrayEquals(new int[] {}, options.getValuesImpossible());
        assertArrayEquals(new int[] {}, options.getValuesUnknown());

        options.setValueAsImpossible(2);
        assertArrayEquals(new int[] { 1, 3, 4 }, options.getValuesPossible());
        assertArrayEquals(new int[] { 2 }, options.getValuesImpossible());
        assertArrayEquals(new int[] {}, options.getValuesUnknown());

        options.setValueAsUnknown(1);
        options.setValueAsUnknown(3);
        options.setValueAsUnknown(4);
        options.setValueAsPossible(3);
        assertArrayEquals(new int[] { 3 }, options.getValuesPossible());
        assertArrayEquals(new int[] { 2 }, options.getValuesImpossible());
        assertArrayEquals(new int[] { 1, 4 }, options.getValuesUnknown());

        options.setAllValuesAsUnknown();
        options.setManyValuesAsImpossible(Set.of(1, 2, 4));
        assertArrayEquals(new int[] {}, options.getValuesPossible());
        assertArrayEquals(new int[] { 1, 2, 4 }, options.getValuesImpossible());
        assertArrayEquals(new int[] { 3 }, options.getValuesUnknown());
    }

    @Test
    void testClearValuesImpossible() {
        // TODO > uzupełnić testy klasy
    }

    @Test
    void testClearValuesPossible() {

    }

    @Test
    void testClearValuesUnknown() {

    }

    @Test
    void testGetValuesImpossible() {

    }

    @Test
    void testGetValuesPossible() {

    }

    @Test
    void testGetValuesUnknown() {

    }

    @Test
    void testIsValueImpossible() {

    }

    @Test
    void testIsValuePossible() {

    }

    @Test
    void testIsValueUnknown() {

    }

    @Test
    void testSetAllValuesAsImpossible() {

    }

    @Test
    void testSetAllValuesAsPossible() {

    }

    @Test
    void testSetAllValuesAsUnknown() {

    }

    @Test
    void testSetManyValuesAsImpossible() {

    }

    @Test
    void testSetValueAsImpossible() {

    }

    @Test
    void testSetValueAsPossible() {

    }

    @Test
    void testSetValueAsUnknown() {

    }
}
