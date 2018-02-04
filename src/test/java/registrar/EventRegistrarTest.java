package registrar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Kors
 */
class EventRegistrarTest {

    private EventRegistrar registrar;

    @DisplayName("Registrar return as many events as registered")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 15})
    void registerEvent_eventRegistered(int eventsCount) {
        for (int i = 0; i < eventsCount; i++)
            registrar.registerEvent();

        assertEquals(eventsCount, registrar.getLastMinuteEventsCount());
        assertEquals(eventsCount, registrar.getLastHourEventsCount());
        assertEquals(eventsCount, registrar.getLastDayEventsCount());
    }

    @DisplayName("Check last minute events")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 15})
    void getLastMinuteEventsCount_correctResultReturns(int actualEventsCount) {
        LocalDateTime time = LocalDateTime.now().minusMinutes(4).minusSeconds(1);
        registrar.registerEvent(time);
        registrar.registerEvent(time.plusMinutes(1));
        registrar.registerEvent(time.plusMinutes(2));

        for (int i = 0; i < actualEventsCount; i++)
            registrar.registerEvent();

        assertEquals(actualEventsCount, registrar.getLastMinuteEventsCount());
        assertEquals(actualEventsCount + 3, registrar.getLastHourEventsCount());
        assertEquals(actualEventsCount + 3, registrar.getLastDayEventsCount());
    }

    @DisplayName("Check last hour events")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 15})
    void getLastHourEventsCount_correctResultReturns(int actualEventsCount) {
        LocalDateTime time = LocalDateTime.now().minusHours(3).minusMinutes(1);
        registrar.registerEvent(time);
        registrar.registerEvent(time.plusHours(1));
        registrar.registerEvent(time.plusHours(2));
        registrar.registerEvent(LocalDateTime.now().minusMinutes(30));

        for (int i = 0; i < actualEventsCount; i++)
            registrar.registerEvent();

        assertEquals(actualEventsCount, registrar.getLastMinuteEventsCount());
        assertEquals(actualEventsCount + 1, registrar.getLastHourEventsCount());
        assertEquals(actualEventsCount + 4, registrar.getLastDayEventsCount());
    }

    @DisplayName("Check last day events")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 15})
    void getLastDayEventsCount_correctResultReturns(int actualEventsCount) {
        LocalDateTime time = LocalDateTime.now().minusDays(3).minusMinutes(1);
        registrar.registerEvent(time);
        registrar.registerEvent(time.plusDays(1));
        registrar.registerEvent(time.plusDays(2));
        registrar.registerEvent(LocalDateTime.now().minusHours(12));

        for (int i = 0; i < actualEventsCount; i++)
            registrar.registerEvent();

        assertEquals(actualEventsCount, registrar.getLastMinuteEventsCount());
        assertEquals(actualEventsCount, registrar.getLastHourEventsCount());
        assertEquals(actualEventsCount + 1, registrar.getLastDayEventsCount());
    }

    @DisplayName("Multithreading test")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 15, 10000})
    void registerManyEvents_allEventsRegistred(int actualEventsCount) throws Exception {
        LocalDateTime oldData = LocalDateTime.now().minusDays(2);
        for (int i = 0; i < 1_000_000; i++)
            registrar.registerEvent(oldData);

        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        List<Future<Void>> futures = new ArrayList<>();
        for (int i = 0; i < actualEventsCount; i++) {
            futures.add(CompletableFuture.runAsync(
                    () -> registrar.registerEvent(),
                    threadPool
            ));
            futures.add(CompletableFuture.runAsync(
                    () -> registrar.getLastMinuteEventsCount(),
                    threadPool
            ));
        }
        for (Future<Void> future : futures) {
            future.get();
        }

        assertEquals(actualEventsCount, registrar.getLastMinuteEventsCount());
        assertEquals(actualEventsCount, registrar.getLastHourEventsCount());
        assertEquals(actualEventsCount, registrar.getLastDayEventsCount());
    }

    @BeforeEach
    void setUp() {
        registrar = new EventRegistrar();
    }
}