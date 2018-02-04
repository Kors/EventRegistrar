package registrar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kors
 */
class EventRegistrarTest {

    IEventRegistrar registrar;

    @Test
    void registerEvent_eventRegistered() {
        registrar.registerEvent();

        assertEquals(1, registrar.getLastMinuteEventsCount());
        assertEquals(1, registrar.getLastHourEventsCount());
        assertEquals(1, registrar.getLastDayEventsCount());
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