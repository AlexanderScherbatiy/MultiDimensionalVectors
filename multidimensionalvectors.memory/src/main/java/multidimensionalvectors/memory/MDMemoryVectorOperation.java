package multidimensionalvectors.memory;

import multidimensionalvectors.core.MDException;
import multidimensionalvectors.core.MDUnsupportedClassException;
import multidimensionalvectors.core.MDUnsupportedTypeException;
import multidimensionalvectors.core.MDVector;
import multidimensionalvectors.core.MDVectorBaseType;
import multidimensionalvectors.core.MDVectorOperation;
import multidimensionalvectors.core.MDVectorType;

public class MDMemoryVectorOperation implements MDVectorOperation {

    @Override
    public MDVector create(MDVectorType type, int dimension, double... values) {

        if (type instanceof MDVectorBaseType) {
            switch ((MDVectorBaseType) type) {
                case ZERO:
                    return new MDMemoryVector(dimension);
                default:
                    throw new MDUnsupportedTypeException(type);
            }
        }

        throw new MDUnsupportedTypeException(type);
    }

    @Override
    public MDVector create(MDVectorType type, int dimension, int index, double... values) {
        if (type instanceof MDVectorBaseType) {
            switch ((MDVectorBaseType) type) {
                case UNIT: {
                    MDMemoryVector vector = new MDMemoryVector(dimension);
                    vector.values[index] = 1.0;
                    return vector;

                }
                default:
                    throw new MDUnsupportedTypeException(type);
            }
        }

        throw new MDUnsupportedTypeException(type);
    }

    @Override
    public double getValue(MDVector vector, int index) {
        if (vector instanceof MDMemoryVector) {
            MDMemoryVector memoryVector = (MDMemoryVector) vector;
            return memoryVector.values[index];
        }

        throw new MDUnsupportedClassException(vector);
    }

    private static class MDMemoryVector implements MDVector {

        private final double[] values;

        MDMemoryVector(int dimension) {
            values = new double[dimension];
        }
    }
}
