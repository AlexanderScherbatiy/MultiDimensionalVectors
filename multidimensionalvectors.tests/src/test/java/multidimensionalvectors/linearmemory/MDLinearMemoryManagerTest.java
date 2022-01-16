package multidimensionalvectors.linearmemory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MDLinearMemoryManagerTest {

    @Test
    public void testConstructor() {

        int dimension = 10;
        int memorySize = 30;
        MDLinearMemoryManager memoryManager = new MDLinearMemoryManager(dimension, memorySize);
        Assertions.assertEquals(dimension, memoryManager.dimension);
        Assertions.assertEquals(memorySize, memoryManager.freeMemorySize);
        Assertions.assertEquals(memorySize, memoryManager.freeMemorySize);
        Assertions.assertEquals(1, memoryManager.memoryBlocks.size());
        Assertions.assertEquals(0, memoryManager.memoryBlocks.get(0).base);
        Assertions.assertEquals(memorySize, memoryManager.memoryBlocks.get(0).size);
    }

    @Test
    public void testRequestBase1() {

        int dimension = 10;
        int memorySize = 50;
        MDLinearMemoryManager memoryManager = new MDLinearMemoryManager(dimension, memorySize);
        int base = memoryManager.requestBase();
        Assertions.assertEquals(0, base);

        Assertions.assertEquals(memorySize - dimension, memoryManager.getFreeMemorySize());
        Assertions.assertEquals(1, memoryManager.memoryBlocks.size());
        Assertions.assertEquals(dimension, memoryManager.memoryBlocks.get(0).base);
        Assertions.assertEquals(memorySize - dimension, memoryManager.memoryBlocks.get(0).size);
    }

    @Test
    public void testRequestBase2() {

        int dimension = 10;
        int memorySize = 50;
        MDLinearMemoryManager memoryManager = new MDLinearMemoryManager(dimension, memorySize);
        int base = memoryManager.requestBase();
        base = memoryManager.requestBase();


        Assertions.assertEquals(dimension, base);
        int freeMemorySize = memorySize - 2 * dimension;

        Assertions.assertEquals(freeMemorySize, memoryManager.getFreeMemorySize());
        Assertions.assertEquals(1, memoryManager.memoryBlocks.size());
        Assertions.assertEquals(2 * dimension, memoryManager.memoryBlocks.get(0).base);
        Assertions.assertEquals(freeMemorySize, memoryManager.memoryBlocks.get(0).size);
    }
}
