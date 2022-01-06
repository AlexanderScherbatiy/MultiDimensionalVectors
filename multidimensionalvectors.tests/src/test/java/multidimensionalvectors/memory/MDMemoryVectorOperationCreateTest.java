package multidimensionalvectors.memory;

import multidimensionalvectors.MDAbstractVectorOperationCreateTest;
import multidimensionalvectors.core.MDVectorOperation;

public class MDMemoryVectorOperationCreateTest extends MDAbstractVectorOperationCreateTest {

    @Override
    public MDVectorOperation getVectorOperation(int dimension) {
        return new MDMemoryVectorOperation();
    }
}
