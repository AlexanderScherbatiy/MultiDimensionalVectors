package multidimensionalvectors.core;

public interface MDAutoClosable extends AutoCloseable {
    @Override
    default void close() throws MDException {
    }
}
