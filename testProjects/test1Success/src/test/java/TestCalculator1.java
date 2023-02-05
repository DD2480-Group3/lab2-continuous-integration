import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for Calculator.java class
 * */
public class TestCalculator1 {

    /**
     * Tests sum method
     * */
    @Test
    public void testSum() {
        Calculator calculator = new Calculator();
        assertEquals(10, calculator.add(5, 5));

    }

    }