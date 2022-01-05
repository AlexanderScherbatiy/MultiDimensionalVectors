package multidimensionalvectors.core;

public class MDUnsupportedTypeException extends MDException {

    public MDUnsupportedTypeException(MDType type) {
        super(String.format("Unsupported type: %s", type));
    }
}
