package registrar;

/**
 * @author Kors
 */
public interface IEventRegistrar {
    void registerEvent();

    long getLastMinuteEventsCount();

    long getLastHourEventsCount();

    long getLastDayEventsCount();
}
