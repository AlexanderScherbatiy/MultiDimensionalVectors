package multidimensionalvectors;

import multidimensionalvectors.core.MDUnitVectorType;
import multidimensionalvectors.core.MDVector;
import multidimensionalvectors.core.MDVectorOperation;
import multidimensionalvectors.core.MDZeroVectorType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class MDAbstractVectorOperationCreateTest extends MDAbstractVectorTest {

    @Test
    public void testCreateZero1() {

        MDVectorOperation vectorOperation = getVectorOperation(1);
        MDVector vector = vectorOperation.create(new MDZeroVectorType(1));
        assertEquals(0.0, vectorOperation.getValue(vector, 0), DELTA);
    }

    @Test
    public void testCreateZero2() {

        MDVectorOperation vectorOperation = getVectorOperation(2);
        MDVector vector = vectorOperation.create(new MDZeroVectorType(2));
        assertEquals(0.0, vectorOperation.getValue(vector, 0), DELTA);
        assertEquals(0.0, vectorOperation.getValue(vector, 1), DELTA);
    }

    @Test
    public void testCreateZero100() {

        int N = 1000;
        int dimension = 10;

        MDVectorOperation vectorOperation = getVectorOperation(dimension);

        for (int n = 0; n < N; n++) {
            MDVector vector = vectorOperation.create(new MDZeroVectorType(dimension));
            for (int i = 0; i < dimension; i++) {
                assertEquals(0.0, vectorOperation.getValue(vector, i), DELTA);
            }
        }
    }

    @Test
    public void testCreateUnit1() {

        MDVectorOperation vectorOperation = getVectorOperation(1);
        MDVector vector = vectorOperation.create(new MDUnitVectorType(1, 0));

        assertEquals(1.0, vectorOperation.getValue(vector, 0), DELTA);
    }

    @Test
    public void testCreateUnit2() {

        MDVectorOperation vectorOperation = getVectorOperation(2);

        MDVector vector1 = vectorOperation.create(new MDUnitVectorType(2, 0));
        assertEquals(1.0, vectorOperation.getValue(vector1, 0), DELTA);
        assertEquals(0.0, vectorOperation.getValue(vector1, 1), DELTA);

        MDVector vector2 = vectorOperation.create(new MDUnitVectorType(2, 1));
        assertEquals(0.0, vectorOperation.getValue(vector2, 0), DELTA);
        assertEquals(1.0, vectorOperation.getValue(vector2, 1), DELTA);
    }
}
