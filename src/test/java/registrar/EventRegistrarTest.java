package registrar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kors
 */
class EventRegistrarTest {

    private IEventRegistrar registrar;

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

    @Test
    void getLastMinuteEventsCount() {
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