import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertEquals;

/**
 * Tests for Calculator.java class
 * */
public class CalculatorTester {

    Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    /**
     * Tests sum method
     * */
    @Test
    public void testSum() {

        assertEquals(100, calculator.add(5, 5));

    }

    }