package pl.tkowalcz.tjahzi.stats;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

public class DropwizardMonitoringModule implements MonitoringModule {

    private final Counter droppedPuts;
    private final Counter httpConnectAttempts;

    private final Counter sentHttpRequests;
    private final Meter sentBytes;

    private final Counter failedHttpRequests;
    private final Counter retriedHttpRequests;
    private final Counter httpResponses;
    private final Timer responseTimes;

    private final Counter channelInactive;
    private final Counter agentErrors;
    private final Counter responseErrors;

    public DropwizardMonitoringModule(MetricRegistry metricRegistry, String prefix) {
        droppedPuts = metricRegistry.counter(prefix + ".droppedPuts");
        httpConnectAttempts = metricRegistry.counter(prefix + ".httpConnectAttempts");

        sentHttpRequests = metricRegistry.counter(prefix + ".sentHttpRequests");
        sentBytes = metricRegistry.meter(prefix + ".sentBytes");

        failedHttpRequests = metricRegistry.counter(prefix + ".failedHttpRequests");
        retriedHttpRequests = metricRegistry.counter(prefix + ".retriedHttpRequests");
        httpResponses = metricRegistry.counter(prefix + ".httpResponses");
        responseTimes = metricRegistry.timer(prefix + ".responseTimes");

        channelInactive = metricRegistry.counter(prefix + ".channelInactive");
        agentErrors = metricRegistry.counter(prefix + ".agentErrors");
        responseErrors = metricRegistry.counter(prefix + ".responseErrors");
    }

    @Override
    public Clock getClock() {
        return Clock.systemUTC();
    }

    @Override
    public void incrementDroppedPuts() {
        droppedPuts.inc();
    }

    @Override
    public void incrementDroppedPuts(Throwable throwable) {
        incrementDroppedPuts();
    }

    @Override
    public void incrementSentHttpRequests(int sizeBytes) {
        sentHttpRequests.inc();
        sentBytes.mark(sizeBytes);
    }

    @Override
    public void incrementFailedHttpRequests() {
        failedHttpRequests.inc();
    }

    @Override
    public void incrementRetriedHttpRequests() {
        retriedHttpRequests.inc();
    }

    @Override
    public void addAgentError(Throwable throwable) {
        agentErrors.inc();
    }

    @Override
    public void incrementHttpConnectAttempts() {
        httpConnectAttempts.inc();
    }

    @Override
    public void incrementChannelInactive() {
        channelInactive.inc();
    }

    @Override
    public void incrementHttpResponses() {
        httpResponses.inc();
    }

    @Override
    public void addPipelineError(Throwable cause) {
        responseErrors.inc();
    }

    @Override
    public void incrementHttpErrors(int status, String content) {
        responseErrors.inc();
    }

    @Override
    public void recordResponseTime(long time) {
        responseTimes.update(time, TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString() {
        return "StandardMonitoringModule{" +
                "droppedPuts=" + droppedPuts.getCount() +
                ", httpConnectAttempts=" + httpConnectAttempts +
                ", sentHttpRequests=" + sentHttpRequests +
                ", sentKilobytes=" + (sentBytes.getCount() / 1024) +
                ", failedHttpRequests=" + failedHttpRequests +
                ", retriedHttpRequests=" + retriedHttpRequests +
                ", httpResponses=" + httpResponses +
                ", channelInactive=" + channelInactive +
                ", agentErrors=" + agentErrors +
                '}';
    }
}
