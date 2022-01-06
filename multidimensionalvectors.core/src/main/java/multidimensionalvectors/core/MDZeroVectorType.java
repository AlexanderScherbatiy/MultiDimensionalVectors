package multidimensionalvectors.core;

public final class MDZeroVectorType implements MDVectorType {

    private final int dimension;

    public MDZeroVectorType(int dimension) {
        this.dimension = dimension;
    }

    public int getDimension() {
        return dimension;
    }
}
