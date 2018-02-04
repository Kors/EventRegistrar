package registrar;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Kors
 */
public class EventRegistrar implements IEventRegistrar {

    private CopyOnWriteArrayList<LocalDateTime> events = new CopyOnWriteArrayList<>(); // todo модифицировать для случая частых модификаций

    @Override
    public void registerEvent() {
        // так как минимально мы считаем за минуту и доли секунд не являются существенными,
        // будем считать такую запись в список упорядоченной, хотя в реальности могут быть небольшие отклонения.
        registerEvent(LocalDateTime.now());
    }

    void registerEvent(LocalDateTime time) {
        events.add(time);
    }

    @Override
    public long getLastMinuteEventsCount() {
        return findApproximateEventsCount(ChronoUnit.MINUTES);
    }

    @Override
    public long getLastHourEventsCount() {
        return findApproximateEventsCount(ChronoUnit.HOURS);
    }

    @Override
    public long getLastDayEventsCount() {
        return findApproximateEventsCount(ChronoUnit.DAYS);
    }

    private int findApproximateEventsCount(TemporalUnit unit) {
        int size = events.size();
        int val = Collections.binarySearch(events, LocalDateTime.now().minus(1, unit));
        return val > 0 ?
                size - val :
                size + val + 1;
    }
}
