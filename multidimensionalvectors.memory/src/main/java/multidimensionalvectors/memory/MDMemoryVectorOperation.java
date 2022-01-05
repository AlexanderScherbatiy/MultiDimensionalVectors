package multidimensionalvectors.memory;

import multidimensionalvectors.core.MDVector;
import multidimensionalvectors.core.MDVectorOperation;
import multidimensionalvectors.core.MDVectorType;

public class MDMemoryVectorOperation implements MDVectorOperation {

    @Override
    public MDVector create(MDVectorType type, int dimension, double[] values) {
        return null;
    }

    @Override
    public double getValue(MDVector vector, int index) {
        return 0;
    }
}
