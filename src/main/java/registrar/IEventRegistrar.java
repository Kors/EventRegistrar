package registrar;

/**
 * Interface that provides simple identical events registration.
 *
 * @author Kors
 */
public interface IEventRegistrar {
    void registerEvent();

    int getLastMinuteEventsCount();

    int getLastHourEventsCount();

    int getLastDayEventsCount();
}
