package multidimensionalvectors.linearmemory;

import multidimensionalvectors.core.MDOutOfMemoryException;
import multidimensionalvectors.core.MDUnitVectorType;
import multidimensionalvectors.core.MDUnsupportedClassException;
import multidimensionalvectors.core.MDUnsupportedTypeException;
import multidimensionalvectors.core.MDVector;
import multidimensionalvectors.core.MDVectorOperation;
import multidimensionalvectors.core.MDVectorType;
import multidimensionalvectors.core.MDZeroVectorType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MDLinearMemoryVectorOperation implements MDVectorOperation {

    private final int dimension;
    private final double[] memory;

    private int freeMemorySize;
    private final List<MemoryBlock> memoryBlocks = new ArrayList<>();

    public MDLinearMemoryVectorOperation(int dimension) {
        this(dimension, 100_000);
    }

    public MDLinearMemoryVectorOperation(int dimension, int linearMemorySize) {
        this.dimension = dimension;
        this.memory = new double[linearMemorySize];
        this.freeMemorySize = linearMemorySize;
        memoryBlocks.add(new MemoryBlock(0, this.memory.length));
    }

    public int getDimension() {
        return dimension;
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
            memory[base + index] = 1.0;
            return vector;
        }

        throw new MDUnsupportedTypeException(vectorType);
    }

    @Override
    public double getValue(MDVector vector, int index) {
        if (vector instanceof MDLinearMemoryVector) {
            MDLinearMemoryVector linearMemoryVector = (MDLinearMemoryVector) vector;
            int base = linearMemoryVector.base;
            return memory[base + index];
        }
        throw new MDUnsupportedClassException(vector);
    }

    private MDLinearMemoryVector createEmptyVector() {
        return new MDLinearMemoryVector(requestBase());
    }

    private int requestBase() {
        if (memoryBlocks.isEmpty()) {
            String msg = String.format("MDLinearMemory is empty (total memory: %d, free memory: %d)",
                    memory.length, freeMemorySize);
            throw new MDOutOfMemoryException(msg);
        }

        MemoryBlock memoryBlock = memoryBlocks.remove(0);
        int base = memoryBlock.base;
        int newBase = memoryBlock.base + dimension;
        int newSize = memoryBlock.size - dimension;

        if (newSize >= dimension) {
            memoryBlocks.add(new MemoryBlock(newBase, newSize));
        }

        freeMemorySize -= dimension;
        return base;
    }

    private void freeVector(int base) {
        memoryBlocks.add(new MemoryBlock(base, dimension));
        freeMemorySize += dimension;
        Arrays.fill(memory, base, base + dimension, 0.0);
    }

    private static class MemoryBlock {

        private final int base;
        private final int size;

        public MemoryBlock(int base, int size) {
            this.base = base;
            this.size = size;
        }
    }

    private class MDLinearMemoryVector implements MDVector {
        private final int base;

        public MDLinearMemoryVector(int base) {
            this.base = base;
        }

        @Override
        public void close() {
            freeVector(base);
        }
    }
}
