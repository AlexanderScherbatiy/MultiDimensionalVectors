package multidimensionalvectors.memory;

import multidimensionalvectors.core.MDVector;
import multidimensionalvectors.core.MDVectorBaseType;
import multidimensionalvectors.core.MDVectorOperation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MDMemoryVectorOperationCreateTest {

    private static final double DELTA = 0.001;

    @Test
    public void testCreate() {
        MDVectorOperation vectorOperation = new MDMemoryVectorOperation();
        MDVector vector = vectorOperation.create(MDVectorBaseType.ZERO, 1);

        assertEquals(0.0, vectorOperation.getValue(vector, 0), DELTA);
    }
}
