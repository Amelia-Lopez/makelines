package amylopez.makelines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import amylopez.makelines.gui.MainWindow;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Main entry point of the Makelines app
 */
public class MakelinesApp {

    private static Logger log = LoggerFactory.getLogger(MakelinesApp.class);

    public static void main(String[] args) throws Exception {
        log.info("Application started");

        // set up main Guice module
        Injector injector = Guice.createInjector(new MainModule());

        // read config
        Configuration config = injector.getInstance(Configuration.class);
        log.info("Configuration loaded");

        // initialize configured Guice modules
        String[] moduleClassNames = config.getStringArray("modules/module");
        log.debug("Modules to load: " + Arrays.toString(moduleClassNames));
        List<Module> modules = new ArrayList<>();
        for (String moduleClassName : moduleClassNames) {
            modules.add((Module) injector.getInstance(Class.forName(moduleClassName)));
        }
        injector = injector.createChildInjector(modules);

        // start application
        MainWindow window = injector.getInstance(MainWindow.class);
        window.setupGUI();
    }
}
