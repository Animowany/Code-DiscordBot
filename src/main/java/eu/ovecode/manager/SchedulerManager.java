package eu.ovecode.manager;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SchedulerManager {

    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(6);
    @Getter(lazy = true)
    private static final SchedulerManager instance = new SchedulerManager();

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }

}
