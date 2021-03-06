package pl.tkowalcz.tjahzi.log4j2;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "header", category = Node.CATEGORY, printObject = true)
public class Header extends Property {

    private static final Logger LOGGER = StatusLogger.getLogger();

    protected Header(String name, String value) {
        super(name, value);
    }

    @PluginFactory
    public static Header createHeader(
            @PluginAttribute("name") String name,
            @PluginValue("value") String value) {
        if (name == null) {
            LOGGER.error("Property name cannot be null");
        }

        return new Header(name, value);
    }
}
