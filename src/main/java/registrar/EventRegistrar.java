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
 * @author Kors
 */
public class EventRegistrar implements IEventRegistrar {

    private final Lock lock = new ReentrantLock();
    private final List<LocalDateTime> events = new ArrayList<>();

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

    @Override
    public long getLastMinuteEventsCount() {
        return getEventsCount(ChronoUnit.MINUTES);
    }

    @Override
    public long getLastHourEventsCount() {
        return getEventsCount(ChronoUnit.HOURS);
    }

    @Override
    public long getLastDayEventsCount() {
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
