package multidimensionalvectors.linearmemory;

import multidimensionalvectors.core.MDUnitVectorType;
import multidimensionalvectors.core.MDUnsupportedClassException;
import multidimensionalvectors.core.MDUnsupportedTypeException;
import multidimensionalvectors.core.MDVector;
import multidimensionalvectors.core.MDVectorOperation;
import multidimensionalvectors.core.MDVectorType;
import multidimensionalvectors.core.MDZeroVectorType;

public class MDLinearMemoryVectorOperation implements MDVectorOperation {

    private final MDLinearMemoryManager memoryManager;

    public MDLinearMemoryVectorOperation(int dimension) {
        this(dimension, 100_000);
    }

    public MDLinearMemoryVectorOperation(int dimension, int linearMemorySize) {
        this.memoryManager = new MDLinearMemoryManager(dimension, linearMemorySize);
    }

    @Override
    public MDVector create(MDVectorType vectorType) {

        if (vectorType instanceof MDZeroVectorType) {
            MDLinearMemoryVector vector = createEmptyVector();
            return vector;
        }

        if (vectorType instanceof MDUnitVectorType) {
            MDUnitVectorType unitVectorType = (MDUnitVectorType) vectorType;
            MDLinearMemoryVector vector = createEmptyVector();
            int base = vector.base;
            int index = unitVectorType.getIndex();
            memoryManager.setValue(base, index, 1.0);
            return vector;
        }

        throw new MDUnsupportedTypeException(vectorType);
    }

    @Override
    public double getValue(MDVector vector, int index) {
        if (vector instanceof MDLinearMemoryVector) {
            MDLinearMemoryVector linearMemoryVector = (MDLinearMemoryVector) vector;
            int base = linearMemoryVector.base;
            return memoryManager.getValue(base, index);
        }
        throw new MDUnsupportedClassException(vector);
    }

    private MDLinearMemoryVector createEmptyVector() {
        return new MDLinearMemoryVector(memoryManager.requestBase());
    }

    private class MDLinearMemoryVector implements MDVector {
        private final int base;

        public MDLinearMemoryVector(int base) {
            this.base = base;
        }

        @Override
        public void close() {
            memoryManager.freeVector(base);
        }
    }
}
