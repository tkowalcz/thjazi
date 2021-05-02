package pl.tkowalcz.tjahzi.log4j2;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.impl.JdkMapAdapterStringMap;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.junit.jupiter.api.Test;
import pl.tkowalcz.tjahzi.LoggingSystem;
import pl.tkowalcz.tjahzi.TjahziLogger;

import java.nio.ByteBuffer;
import java.util.Map;

import static org.mockito.Mockito.*;

class AppenderLogicTest {

    @Test
    void shouldWorkWithNoDynamicLabels() {
        // Given
        TjahziLogger tjahziLogger = mock(TjahziLogger.class);

        LoggingSystem loggingSystem = mock(LoggingSystem.class);
        when(loggingSystem.createLogger()).thenReturn(tjahziLogger);

        long timestamp = 42L;
        String logLevelLabel = "log_level";
        Level logLevel = Level.INFO;

        AppenderLogic logic = new AppenderLogic(
                loggingSystem,
                logLevelLabel,
                Map.of()
        );

        MutableLogEvent logEvent = new MutableLogEvent();
        logEvent.setLevel(logLevel);
        logEvent.setLoggerName("foobar");
        logEvent.setTimeMillis(timestamp);
        logEvent.setContextData(new JdkMapAdapterStringMap(Map.of()));

        // When
        logic.accept(logEvent, ByteBuffer.allocate(1024));

        // Then
        verify(tjahziLogger).log(
                eq(timestamp),
                eq(Map.of()),
                eq(logLevelLabel),
                eq("INFO"),
                any()
        );
    }

    @Test
    void shouldPerformSubstitutionOnDynamicLabels() {
        // Given
        TjahziLogger tjahziLogger = mock(TjahziLogger.class);

        LoggingSystem loggingSystem = mock(LoggingSystem.class);
        when(loggingSystem.createLogger()).thenReturn(tjahziLogger);

        long timestamp = 42L;
        String logLevelLabel = "log_level";
        Level logLevel = Level.INFO;

        Map<String, String> dynamicLabels = Map.of(
                "id", "foo_${ctx:foo}_bar_${ctx:bar}",
                "tennant", "${ctx:tennant}",
                "region", "region_${ctx:region}"
        );

        Map<String, String> contextMap = Map.of(
                "foo", "123",
                // "bar"... - missing bar
                "tennant", "david",
                "region", "us_east_1"
        );

        Map<String, String> expected = Map.of(
                "id", "foo_123_bar_${ctx:bar}",
                "tennant", "david",
                "region", "region_us_east_1"
        );

        AppenderLogic logic = new AppenderLogic(
                loggingSystem,
                logLevelLabel,
                dynamicLabels
        );

        MutableLogEvent logEvent = new MutableLogEvent();
        logEvent.setLevel(logLevel);
        logEvent.setLoggerName("foobar");
        logEvent.setTimeMillis(timestamp);
        logEvent.setContextData(new JdkMapAdapterStringMap(contextMap));

        // When
        logic.accept(logEvent, ByteBuffer.allocate(1024));

        // Then
        verify(tjahziLogger).log(
                eq(timestamp),
                eq(expected),
                eq(logLevelLabel),
                eq("INFO"),
                any()
        );
    }
}
