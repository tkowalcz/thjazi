package pl.tkowalcz.tjahzi.stats;

public class EmptyMonitoringModule implements MonitoringModule {

    @Override
    public void incrementDroppedPuts() {
    }

    @Override
    public void incrementDroppedPuts(Throwable throwable) {
    }

    @Override
    public void incrementSentHttpRequests(int sizeBytes) {
    }

    @Override
    public void incrementFailedHttpRequests() {
    }

    @Override
    public void incrementRetriedHttpRequests() {
    }

    @Override
    public void addAgentError(Throwable throwable) {
    }

    @Override
    public void incrementHttpConnectAttempts() {
    }

    @Override
    public void addPipelineError(Throwable cause) {
    }

    @Override
    public void incrementChannelInactive() {
    }

    @Override
    public void incrementHttpResponses() {
    }

    @Override
    public void incrementHttpErrors(int status, String content) {
    }

    @Override
    public void recordResponseTime(long time) {
    }
}
