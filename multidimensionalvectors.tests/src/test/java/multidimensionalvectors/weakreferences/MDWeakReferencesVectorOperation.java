package multidimensionalvectors.weakreferences;

import multidimensionalvectors.core.MDUnitVectorType;
import multidimensionalvectors.core.MDUnsupportedClassException;
import multidimensionalvectors.core.MDUnsupportedTypeException;
import multidimensionalvectors.core.MDVector;
import multidimensionalvectors.core.MDVectorOperation;
import multidimensionalvectors.core.MDVectorType;
import multidimensionalvectors.core.MDZeroVectorType;
import multidimensionalvectors.linearmemory.MDLinearMemoryManager;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MDWeakReferencesVectorOperation implements MDVectorOperation {

    private final Lock memoryLock = new ReentrantLock();
    private final MDLinearMemoryManager memoryManager;

    private static final Set<MDVectorWeakReference> weakReferenceSet = ConcurrentHashMap.newKeySet();
    private static final ReferenceQueue<MDWeakReferenceVector> referenceQueue = new ReferenceQueue<>();

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

    public MDWeakReferencesVectorOperation(int dimension, int linearMemorySize) {
        memoryLock.lock();
        try {
            this.memoryManager = new MDLinearMemoryManager(dimension, linearMemorySize);
        } finally {
            memoryLock.unlock();
        }
    }

    @Override
    public MDVector create(MDVectorType vectorType) {

        if (vectorType instanceof MDZeroVectorType) {
            return createEmptyVector();
        }

        if (vectorType instanceof MDUnitVectorType) {
            MDUnitVectorType unitVectorType = (MDUnitVectorType) vectorType;
            MDWeakReferenceVector vector = createEmptyVector();
            int base = vector.base;
            int index = unitVectorType.getIndex();
            memoryManager.setValue(base, index, 1.0);
            return vector;
        }

        throw new MDUnsupportedTypeException(vectorType);
    }

    @Override
    public double getValue(MDVector vector, int index) {
        if (vector instanceof MDWeakReferenceVector) {
            MDWeakReferenceVector linearMemoryVector = (MDWeakReferenceVector) vector;
            int base = linearMemoryVector.base;
            return memoryManager.getValue(base, index);
        }
        throw new MDUnsupportedClassException(vector);
    }

    private MDWeakReferenceVector createEmptyVector() {

        int base = -1;

        memoryLock.lock();
        try {
            base = memoryManager.requestBase();
        } finally {
            memoryLock.unlock();
        }

        if (memoryManager.getFreeMemorySize() < 0.1 * memoryManager.getMemorySize()) {
            System.gc();
        }

        MDWeakReferenceVector vector = new MDWeakReferenceVector(base);
        weakReferenceSet.add(new MDVectorWeakReference(vector, this));
        return vector;
    }

    private void freeVector(int base) {
        memoryLock.lock();
        try {
            memoryManager.freeVector(base);
        } finally {
            memoryLock.unlock();
        }
    }

    private static class MDVectorWeakReference extends WeakReference<MDWeakReferenceVector> {

        private final int base;
        private final MDWeakReferencesVectorOperation vectorOperation;

        public MDVectorWeakReference(MDWeakReferenceVector vector, MDWeakReferencesVectorOperation vectorOperation) {
            super(vector, referenceQueue);
            this.base = vector.base;
            this.vectorOperation = vectorOperation;
        }

        private void freeVector() {
            vectorOperation.freeVector(base);
        }
    }

    private static class MDWeakReferenceVector implements MDVector {
        private final int base;

        public MDWeakReferenceVector(int base) {
            this.base = base;
        }

        @Override
        public void close() {
        }
    }
}
