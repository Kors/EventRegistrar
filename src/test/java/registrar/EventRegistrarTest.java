package registrar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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
        LocalDateTime time = LocalDateTime.now().minusMinutes(20);
        registrar.registerEvent(time);
        registrar.registerEvent(time.plusMinutes(1));
        registrar.registerEvent(time.plusMinutes(2));

        for (int i = 0; i < actualEventsCount; i++)
            registrar.registerEvent();

        assertEquals(actualEventsCount, registrar.getLastMinuteEventsCount());
        assertEquals(actualEventsCount + 3, registrar.getLastHourEventsCount());
        assertEquals(actualEventsCount + 3, registrar.getLastDayEventsCount());
    }

    @Test
    void getLastHourEventsCount() {
    }

    @Test
    void getLastDayEventsCount() {
    }

    @BeforeEach
    void setUp() {
        registrar = new EventRegistrar();
    }
}