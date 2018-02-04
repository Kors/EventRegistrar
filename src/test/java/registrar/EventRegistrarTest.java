package registrar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

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
    void getLastHourEventsCountt_correctResultReturns(int actualEventsCount) {
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
    void getLastDayEventsCountt_correctResultReturns(int actualEventsCount) {
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

    @BeforeEach
    void setUp() throws Exception {
        registrar = new EventRegistrar();
    }
}