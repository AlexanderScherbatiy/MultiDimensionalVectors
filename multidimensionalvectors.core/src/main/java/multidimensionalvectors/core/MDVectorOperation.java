package multidimensionalvectors.core;

public interface MDVectorOperation {

    MDVector create(MDVectorType type, int dimension, double... values);

    MDVector create(MDVectorType type, int dimension, int index, double... values);

    double getValue(MDVector vector, int index);
}
