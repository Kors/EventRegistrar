package registrar;

/**
 * @author Kors
 */
public interface IEventRegistrar {
    void registerEvent();

    int getLastMinuteEventsCount();

    int getLastHourEventsCount();

    int getLastDayEventsCount();
}
