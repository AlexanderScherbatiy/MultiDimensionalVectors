package multidimensionalvectors.core;

public class MDUnitVectorType implements MDVectorType {

    private final int dimension;
    private final int index;

    public MDUnitVectorType(int dimension, int index) {
        this.dimension = dimension;
        this.index = index;
    }

    public int getDimension() {
        return dimension;
    }

    public int getIndex() {
        return index;
    }
}
