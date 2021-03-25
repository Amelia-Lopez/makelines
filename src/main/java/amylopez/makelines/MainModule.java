package amylopez.makelines;

import javax.inject.Singleton;

import amylopez.makelines.gui.BlockBoardView;
import amylopez.makelines.gui.MainWindow;
import amylopez.makelines.gui.Paintable;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class MainModule extends AbstractModule {

    private static Logger log = LoggerFactory.getLogger(MainModule.class);

    @Override
    protected void configure() {
        bind(Paintable.class)
            .annotatedWith(Names.named("All"))
            .to(MainWindow.class);

        bind(Paintable.class)
            .annotatedWith(Names.named("Board"))
            .to(BlockBoardView.class);
    }

    @Provides @Singleton
    public Configuration providesConfiguration() {
        CombinedConfiguration config = null;

        try {
            DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder("config/config.xml");
            config = builder.getConfiguration(true);
            config.setExpressionEngine(new XPathExpressionEngine());
        } catch (Throwable t) {
            // don't bother running the application if we can't load configuration
            log.error("Unable to load configuration", t);
            System.exit(1);
        }

        return config;
    }
}
