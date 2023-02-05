import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for Calculator.java class
 * */
public class TestCalculator3 {

    /**
     * Tests sum method
     * */
    @Test
    public void testSum() {
        Calculator calculator = new Calculator();
        assertEquals(100, calculator.add(5, 5));

    }

    }