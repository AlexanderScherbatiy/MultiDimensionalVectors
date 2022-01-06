package multidimensionalvectors.core;

public interface MDVectorOperation {

    MDVector create(MDVectorType vectorType);

    double getValue(MDVector vector, int index);
}
