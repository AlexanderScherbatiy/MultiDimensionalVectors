package multidimensionalvectors.weakreferences;

import multidimensionalvectors.MDAbstractVectorOperationCreateTest;
import multidimensionalvectors.core.MDVectorOperation;
import multidimensionalvectors.linearmemory.MDLinearMemoryVectorOperation;

public class MDWeakReferencesVectorOperationCreateTest extends MDAbstractVectorOperationCreateTest {

    private static final int memorySize = 1000;

    @Override
    public MDVectorOperation getVectorOperation(int dimension) {
        return new MDWeakReferencesVectorOperation(dimension, memorySize);
    }
}
