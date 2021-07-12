package pl.tkowalcz.tjahzi.stats;

public class MutableMonitoringModuleWrapper implements MonitoringModule {

    private MonitoringModule monitoringModule;

    public void setMonitoringModule(MonitoringModule monitoringModule) {
        this.monitoringModule = monitoringModule;
    }

    @Override
    public void incrementDroppedPuts() {
        monitoringModule.incrementDroppedPuts();
    }

    @Override
    public void incrementDroppedPuts(Throwable throwable) {
        monitoringModule.incrementDroppedPuts(throwable);
    }

    @Override
    public void incrementSentHttpRequests(int sizeBytes) {
        monitoringModule.incrementSentHttpRequests(sizeBytes);
    }

    @Override
    public void incrementFailedHttpRequests() {
        monitoringModule.incrementFailedHttpRequests();
    }

    @Override
    public void incrementRetriedHttpRequests() {
        monitoringModule.incrementRetriedHttpRequests();
    }

    @Override
    public void addAgentError(Throwable throwable) {
        monitoringModule.addAgentError(throwable);
    }

    @Override
    public void incrementHttpConnectAttempts() {
        monitoringModule.incrementHttpConnectAttempts();
    }

    @Override
    public void addPipelineError(Throwable cause) {
        monitoringModule.addPipelineError(cause);
    }

    @Override
    public void incrementChannelInactive() {
        monitoringModule.incrementChannelInactive();
    }

    @Override
    public void incrementHttpResponses() {
        monitoringModule.incrementHttpResponses();
    }

    @Override
    public void incrementHttpErrors(int status, String content) {
        monitoringModule.incrementHttpErrors(status, content);
    }

    @Override
    public void recordResponseTime(long time) {
        monitoringModule.recordResponseTime(time);
    }
}
