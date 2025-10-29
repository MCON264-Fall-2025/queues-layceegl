import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("QueueInterface Conformance Tests")
public class ParameterizedQueueTest {

    // Providers for ANY queue implementation (bounded or unbounded)
    static Stream<Arguments> queueProviders() {
        return Stream.of(
                Arguments.of("ArrayBoundedQueue", (Supplier<QueueInterface<Integer>>) () -> new ArrayBoundedQueue<>(10)),
                Arguments.of("LinkedQueue", (Supplier<QueueInterface<Integer>>) LinkedQueue::new)
        );
    }

    @Nested
    @DisplayName("Common behavior (all implementations)")
    class CommonBehavior {
        @ParameterizedTest(name = "{0} — create empty")
        @MethodSource("ParameterizedQueueTest#queueProviders")
        void test_create(String name, Supplier<QueueInterface<Integer>> factory) {
            QueueInterface<Integer> queue = factory.get();
            assertTrue(queue.isEmpty());
        }

        @ParameterizedTest(name = "{0} — enqueue")
        @MethodSource("ParameterizedQueueTest#queueProviders")
        void test_enqueue(String name, Supplier<QueueInterface<Integer>> factory) {
            QueueInterface<Integer> queue = factory.get();
            queue.enqueue(42);
            // TODO: Add assertions to verify enqueue
            assertFalse(queue.isEmpty());
            assertEquals(1, queue.size());
        }

        @ParameterizedTest(name = "{0} — dequeue")
        @MethodSource("ParameterizedQueueTest#queueProviders")
        void test_dequeue(String name, Supplier<QueueInterface<Integer>> factory) {
            QueueInterface<Integer> queue = factory.get();
            queue.enqueue(99);
            Integer val = queue.dequeue();
            // TODO: Add assertions to verify dequeue
            assertEquals(99, val);
            assertTrue(queue.isEmpty());
        }

        @ParameterizedTest(name = "{0} — isFull")
        @MethodSource("ParameterizedQueueTest#queueProviders")
        void test_isFull(String name, Supplier<QueueInterface<Integer>> factory) {
            QueueInterface<Integer> queue = factory.get();
            // TODO: Fill queue if possible and assert isFull
            if (queue instanceof ArrayBoundedQueue) {
                int capacity = 10; // hardcoded capacity to match constructor argument
                for (int i = 0; i < capacity; i++) {
                    queue.enqueue(i);
                }
                assertTrue(queue.isFull());
            } else {
                for (int i = 0; i < 1000; i++) {
                    queue.enqueue(i);
                }
                assertFalse(queue.isFull());
            }
        }

        @ParameterizedTest(name = "{0} — isEmpty")
        @MethodSource("ParameterizedQueueTest#queueProviders")
        void test_isEmpty(String name, Supplier<QueueInterface<Integer>> factory) {
            QueueInterface<Integer> queue = factory.get();
            // TODO: verify isEmpty before and after enqueue
            assertTrue(queue.isEmpty());
            queue.enqueue(10);
            assertFalse(queue.isEmpty());
        }

        @ParameterizedTest(name = "{0} — size")
        @MethodSource("ParameterizedQueueTest#queueProviders")
        void test_size(String name, Supplier<QueueInterface<Integer>> factory) {
            QueueInterface<Integer> queue = factory.get();
            assertEquals(0, queue.size());
            queue.enqueue(1);
            assertEquals(1, queue.size());
            queue.enqueue(2);
            queue.enqueue(3);
            assertEquals(3, queue.size());
            queue.dequeue();
            assertEquals(2, queue.size());
        }

        @ParameterizedTest(name = "{0} — enqueue/dequeue")
        @MethodSource("ParameterizedQueueTest#queueProviders")
        void test_enqueue_dequeue(String name, Supplier<QueueInterface<Integer>> factory) {
            QueueInterface<Integer> queue = factory.get();
            // TODO: Verify that enqueue(1) makes the queue non-empty,
            //  dequeue() returns 1, and the queue becomes empty again
            queue.enqueue(1);
            assertFalse(queue.isEmpty());
            Integer val = queue.dequeue();
            assertEquals(1, val);
            assertTrue(queue.isEmpty());
        }

        @ParameterizedTest(name = "{0} — underflow on empty dequeue")
        @MethodSource("ParameterizedQueueTest#queueProviders")
        void test_underflow(String name, Supplier<QueueInterface<Integer>> factory) {
            QueueInterface<Integer> queue = factory.get();
            assertThrows(QueueUnderflowException.class, queue::dequeue);
        }
    }

    @Nested
    @DisplayName("ArrayBoundedQueue-specific behavior")
    class ArrayBoundedQueueSpecific {
        @Test
        @DisplayName("ArrayBoundedQueue — overflow exception")
        void test_overflow() {
            ArrayBoundedQueue<Integer> queue = new ArrayBoundedQueue<>(2);
            // TODO: enqueue elements and assert overflow behavior
            queue.enqueue(1);
            queue.enqueue(2);
            assertThrows(QueueOverflowException.class, () -> queue.enqueue(3));
        }
    }

    @Nested
    @DisplayName("LinkedQueue-specific behavior")
    class LinkedQueueSpecific {
        @Test
        @DisplayName("LinkedQueue — test specific feature")
        void test_linkedQueueSpecific() {
            LinkedQueue<Integer> queue = new LinkedQueue<>();
            // TODO: Add LinkedQueue-specific assertions
            assertFalse(queue.isFull());
            queue.enqueue(5);
            queue.enqueue(10);
            assertEquals(2, queue.size());
            assertEquals(5, queue.dequeue());
            assertEquals(10, queue.dequeue());
            assertTrue(queue.isEmpty());
        }
        // Add more LinkedQueue-specific tests here
    }
}
