package registrar;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Reusable utility class that provides events registration.
 * This realization useful in case of:
 * - it could be a lot of registration operations (>10000 per second)
 * - getting stats are rare operations
 * - locking time for finding is not important for registering process
 *
 * @author Kors
 */
public class EventRegistrar implements IEventRegistrar {

    private final Lock lock = new ReentrantLock();
    private final List<LocalDateTime> events = new ArrayList<>();

    /**
     * Method registers new event.
     * As soon as events are identical, no event information stored, only registration time.
     */
    @Override
    public void registerEvent() {
        lock.lock();
        try {
            registerEvent(LocalDateTime.now());
        } finally {
            lock.unlock();
        }
    }

    // добавлен и не сделан private чтобы не мудрить в тестах с reflection, предполагается что использоваться не будет.
    void registerEvent(LocalDateTime time) {
        events.add(time);
    }

    /**
     * Method returns count of events registered during the last minute
     */
    @Override
    public int getLastMinuteEventsCount() {
        return getEventsCount(ChronoUnit.MINUTES);
    }

    /**
     * Method returns count of events registered during the last hour
     */
    @Override
    public int getLastHourEventsCount() {
        return getEventsCount(ChronoUnit.HOURS);
    }

    /**
     * Method returns count of events registered during the last day
     */
    @Override
    public int getLastDayEventsCount() {
        return getEventsCount(ChronoUnit.DAYS);
    }

    private int getEventsCount(TemporalUnit unit) {
        int size, val;
        lock.lock();
        try {
            size = events.size();
            val = Collections.binarySearch(events, LocalDateTime.now().minus(1, unit));
        } finally {
            lock.unlock();
        }
        return val > 0 ?
                size - val :
                size + val + 1;
    }
}
