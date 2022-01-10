package multidimensionalvectors.memory;

import multidimensionalvectors.core.MDVector;
import multidimensionalvectors.core.MDVectorType;
import multidimensionalvectors.core.MDVectorOperation;
import multidimensionalvectors.core.MDZeroVectorType;
import multidimensionalvectors.core.MDUnitVectorType;
import multidimensionalvectors.core.MDUnsupportedClassException;
import multidimensionalvectors.core.MDUnsupportedTypeException;

public class MDMemoryVectorOperation implements MDVectorOperation {

    @Override
    public MDVector create(MDVectorType vectorType) {

        if (vectorType instanceof MDZeroVectorType) {
            MDZeroVectorType zeroVectorType = (MDZeroVectorType) vectorType;
            return new MDMemoryVector(zeroVectorType.getDimension());
        }

        if (vectorType instanceof MDUnitVectorType) {
            MDUnitVectorType unitVectorType = (MDUnitVectorType) vectorType;
            MDMemoryVector vector = new MDMemoryVector(unitVectorType.getDimension());
            vector.values[unitVectorType.getIndex()] = 1.0;
            return vector;
        }

        throw new MDUnsupportedTypeException(vectorType);
    }

    @Override
    public double getValue(MDVector vector, int index) {
        if (vector instanceof MDMemoryVector) {
            MDMemoryVector memoryVector = (MDMemoryVector) vector;
            return memoryVector.values[index];
        }

        throw new MDUnsupportedClassException(vector);
    }

    private static class MDMemoryVector implements MDVector {

        private final double[] values;

        MDMemoryVector(int dimension) {
            values = new double[dimension];
        }

        @Override
        public void close() {
        }
    }
}
