package multidimensionalvectors;

import multidimensionalvectors.core.MDVectorOperation;

public abstract class MDAbstractVectorTest {

    public static final double DELTA = 0.001;

    public abstract MDVectorOperation getVectorOperation(int dimension);
}
