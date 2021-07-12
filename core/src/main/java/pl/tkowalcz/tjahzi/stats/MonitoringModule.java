package pl.tkowalcz.tjahzi.stats;

import java.time.Clock;

public interface MonitoringModule {

    default Clock getClock() {
        return Clock.systemUTC();
    }

    void incrementDroppedPuts();

    void incrementDroppedPuts(Throwable throwable);

    void incrementSentHttpRequests(int sizeBytes);

    void incrementFailedHttpRequests();

    void incrementRetriedHttpRequests();

    void addAgentError(Throwable throwable);

    void incrementHttpConnectAttempts();

    void addPipelineError(Throwable cause);

    void incrementChannelInactive();

    void incrementHttpResponses();

    void incrementHttpErrors(int status, String content);

    void recordResponseTime(long time);
}
