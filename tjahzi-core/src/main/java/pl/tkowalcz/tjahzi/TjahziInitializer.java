package pl.tkowalcz.tjahzi;

import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.SleepingMillisIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.ManyToOneRingBuffer;
import org.agrona.concurrent.ringbuffer.RingBufferDescriptor;
import pl.tkowalcz.tjahzi.http.NettyHttpClient;

import java.nio.ByteBuffer;

public class TjahziInitializer {

    public static final int MIN_BUFFER_SIZE_BYTES = 1024 * 1024;

    public LoggingSystem createLoggingSystem(
            NettyHttpClient httpClient,
            int bufferSizeBytes,
            boolean offHeap) {
        bufferSizeBytes = findNearestPowerOfTwo(bufferSizeBytes);
        ByteBuffer javaBuffer = allocateJavaBuffer(bufferSizeBytes, offHeap);

        ManyToOneRingBuffer logBuffer = new ManyToOneRingBuffer(
                new UnsafeBuffer(javaBuffer)
        );

        LogBufferAgent agent = new LogBufferAgent(
                logBuffer,
                httpClient
        );

        AgentRunner runner = new AgentRunner(
                new SleepingMillisIdleStrategy(),
                Throwable::printStackTrace,
                null,
                agent
        );

        AgentRunner.startOnThread(runner);

        return new LoggingSystem(
                logBuffer,
                runner,
                httpClient
        );
    }

    public static boolean isCorrectSize(int bufferSize) {
        return bufferSize >= MIN_BUFFER_SIZE_BYTES && Integer.bitCount(bufferSize) == 1;
    }

    // VisibleForTesting
    static int findNearestPowerOfTwo(int bufferSize) {
        if (!isCorrectSize(bufferSize)) {
            if (bufferSize < MIN_BUFFER_SIZE_BYTES) {
                return MIN_BUFFER_SIZE_BYTES;
            }

            long candidatePowerOfTwo = Long.highestOneBit(bufferSize) << 1;
            if (candidatePowerOfTwo + RingBufferDescriptor.TRAILER_LENGTH > Integer.MAX_VALUE) {
                return (int) (candidatePowerOfTwo >> 1);
            }

            return (int) candidatePowerOfTwo;
        }

        return bufferSize;
    }

    private ByteBuffer allocateJavaBuffer(
            int bufferSize,
            boolean offHeap) {
        int totalSize = bufferSize + RingBufferDescriptor.TRAILER_LENGTH;

        if (offHeap) {
            return ByteBuffer.allocateDirect(totalSize);
        }

        return ByteBuffer.allocate(totalSize);
    }
}
