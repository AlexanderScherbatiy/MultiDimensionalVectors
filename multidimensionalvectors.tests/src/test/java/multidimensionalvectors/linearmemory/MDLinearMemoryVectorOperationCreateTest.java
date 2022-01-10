package multidimensionalvectors.linearmemory;

import multidimensionalvectors.MDAbstractVectorOperationCreateTest;
import multidimensionalvectors.core.MDVectorOperation;

public class MDLinearMemoryVectorOperationCreateTest extends MDAbstractVectorOperationCreateTest {

    private static final int memorySize = 1000;

    @Override
    public MDVectorOperation getVectorOperation(int dimension) {
        return new MDLinearMemoryVectorOperation(dimension, memorySize);
    }
}
