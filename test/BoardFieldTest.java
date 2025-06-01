import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BoardFieldTest {
    @Test
    void testSimpleAndFastMostImportantFeatures() {
        BoardField field;
        field = new BoardField(2);
        assertArrayEquals(new int[] {}, field.possibilities.getValuesPossible());
        assertArrayEquals(new int[] {}, field.possibilities.getValuesImpossible());
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, field.possibilities.getValuesUnknown());
        assertEquals(BoardBase.EMPTY_FIELD, field.getValue());
        assertNotEquals(3, field.getValue());
        assertEquals(false, field.isConstField());
        assertNotEquals(true, field.isConstField());

        field = new BoardField(2, 1);
        assertArrayEquals(new int[] {}, field.possibilities.getValuesPossible());
        assertArrayEquals(new int[] {}, field.possibilities.getValuesImpossible());
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, field.possibilities.getValuesUnknown());
        assertEquals(1, field.getValue());
        assertNotEquals(3, field.getValue());
        assertEquals(true, field.isConstField());
        assertNotEquals(false, field.isConstField());
    }

    @Test
    void testGetValue() {

    }

    @Test
    void testIsFieldConst() {

    }

    @Test
    void testRemoveValue() {

    }

    @Test
    void testSetValue() {

    }
}
