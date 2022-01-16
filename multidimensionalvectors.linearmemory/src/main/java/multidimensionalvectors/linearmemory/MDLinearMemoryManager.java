package multidimensionalvectors.linearmemory;

import multidimensionalvectors.core.MDOutOfMemoryException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MDLinearMemoryManager {

    final int dimension;
    final double[] memory;

    int freeMemorySize;
    final List<MemoryBlock> memoryBlocks = new ArrayList<>();

    public MDLinearMemoryManager(int dimension, int memorySize) {
        this.dimension = dimension;
        this.freeMemorySize = memorySize;
        this.memory = new double[memorySize];
        memoryBlocks.add(new MemoryBlock(0, this.memory.length));

    }

    public double getValue(int base, int index) {
        return memory[base + index];
    }

    public void setValue(int base, int index, double value) {
        memory[base + index] = value;
    }

    public int getMemorySize() {
        return memory.length;
    }

    public int getFreeMemorySize() {
        return freeMemorySize;
    }

    public int requestBase() {
        if (memoryBlocks.isEmpty()) {
            String msg = String.format("MDLinearMemory is empty (total memory: %d, free memory: %d)",
                    memory.length, freeMemorySize);
            throw new MDOutOfMemoryException(msg);
        }

        MemoryBlock memoryBlock = memoryBlocks.remove(0);
        int base = memoryBlock.base;
        int newBase = memoryBlock.base + dimension;
        int newSize = memoryBlock.size - dimension;

        if (newSize >= dimension) {
            memoryBlocks.add(new MemoryBlock(newBase, newSize));
        }

        freeMemorySize -= dimension;
        return base;
    }

    public void freeVector(int base) {
        memoryBlocks.add(new MemoryBlock(base, dimension));
        freeMemorySize += dimension;
        Arrays.fill(memory, base, base + dimension, 0.0);
    }


    static class MemoryBlock {

        int base;
        int size;

        public MemoryBlock(int base, int size) {
            this.base = base;
            this.size = size;
        }
    }
}
