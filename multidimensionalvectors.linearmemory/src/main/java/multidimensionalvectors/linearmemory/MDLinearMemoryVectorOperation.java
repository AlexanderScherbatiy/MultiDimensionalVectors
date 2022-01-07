package multidimensionalvectors.linearmemory;

import multidimensionalvectors.core.MDOutOfMemoryException;
import multidimensionalvectors.core.MDUnitVectorType;
import multidimensionalvectors.core.MDUnsupportedClassException;
import multidimensionalvectors.core.MDUnsupportedTypeException;
import multidimensionalvectors.core.MDVector;
import multidimensionalvectors.core.MDVectorOperation;
import multidimensionalvectors.core.MDVectorType;
import multidimensionalvectors.core.MDZeroVectorType;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MDLinearMemoryVectorOperation implements MDVectorOperation {

    private final int dimension;
    private final double[] memory;

    private int freeMemorySize;
    private final Lock memoryBlocksLock = new ReentrantLock();
    private final List<MemoryBlock> memoryBlocks = new ArrayList<>();

    private static final Set<MDVectorWeakReference> weakReferenceSet = ConcurrentHashMap.newKeySet();
    private static final ReferenceQueue<MDLinearMemoryVector> referenceQueue = new ReferenceQueue<>();

    static {
        Thread referenceQueueThread = new Thread(() -> {
            while (true) {
                try {
                    MDVectorWeakReference vectorRef = (MDVectorWeakReference) referenceQueue.remove();
                    vectorRef.freeVector();
                    weakReferenceSet.remove(vectorRef);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        referenceQueueThread.setDaemon(true);
        referenceQueueThread.start();
    }

    public MDLinearMemoryVectorOperation(int dimension) {
        this(dimension, 100_000);
    }

    public MDLinearMemoryVectorOperation(int dimension, int linearMemorySize) {
        this.dimension = dimension;
        this.memory = new double[linearMemorySize];
        this.freeMemorySize = linearMemorySize;
        memoryBlocksLock.lock();
        try {
            memoryBlocks.add(new MemoryBlock(0, this.memory.length));
        } finally {
            memoryBlocksLock.unlock();
        }
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

        MDLinearMemoryVector vector = new MDLinearMemoryVector(requestBase());
        weakReferenceSet.add(new MDVectorWeakReference(vector, this));
        return vector;
    }

    private int requestBase() {
        boolean isLowSpace = false;
        memoryBlocksLock.lock();
        try {

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
            isLowSpace = (freeMemorySize < 0.1 * memory.length);
            return base;
        } finally {
            memoryBlocksLock.unlock();
            if (isLowSpace) {
                System.gc();
            }
        }
    }

    private void freeVector(int base) {
        memoryBlocksLock.lock();
        try {
            memoryBlocks.add(new MemoryBlock(base, dimension));
            freeMemorySize += dimension;
        } finally {
            memoryBlocksLock.unlock();
        }
    }

    private static class MDVectorWeakReference extends WeakReference<MDLinearMemoryVector> {

        private final int base;
        private final MDLinearMemoryVectorOperation vectorOperation;

        public MDVectorWeakReference(MDLinearMemoryVector vector, MDLinearMemoryVectorOperation vectorOperation) {
            super(vector, referenceQueue);
            this.base = vector.base;
            this.vectorOperation = vectorOperation;
        }

        private void freeVector() {
            vectorOperation.freeVector(base);
        }
    }


    private static class MemoryBlock {

        private final int base;
        private final int size;

        public MemoryBlock(int base, int size) {
            this.base = base;
            this.size = size;
        }
    }

    private static class MDLinearMemoryVector implements MDVector {
        private final int base;

        public MDLinearMemoryVector(int base) {
            this.base = base;
        }
    }
}
