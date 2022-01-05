package multidimensionalvectors.core;

public class MDUnsupportedClassException extends MDException{

    public MDUnsupportedClassException(Object obj) {
        super(String.format("Unsupported class: %s%n", obj.getClass()));
    }
}
