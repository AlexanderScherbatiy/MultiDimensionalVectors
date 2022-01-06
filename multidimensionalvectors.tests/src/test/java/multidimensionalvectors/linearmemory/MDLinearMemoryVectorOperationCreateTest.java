package multidimensionalvectors.linearmemory;

import multidimensionalvectors.MDAbstractVectorOperationCreateTest;
import multidimensionalvectors.core.MDVectorOperation;
import multidimensionalvectors.memory.MDMemoryVectorOperation;

public class MDLinearMemoryVectorOperationCreateTest extends MDAbstractVectorOperationCreateTest {

    @Override
    public MDVectorOperation getVectorOperation(int dimension) {
        return new MDLinearMemoryVectorOperation(dimension);
    }
}
