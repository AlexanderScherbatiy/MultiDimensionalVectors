package multidimensionalvectors.core;

public interface MDVectorOperation {

    MDVector create(MDVectorType type, int dimension, double... values);

    double getValue(MDVector vector, int index);
}
