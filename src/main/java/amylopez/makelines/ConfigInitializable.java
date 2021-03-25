package amylopez.makelines;

public interface ConfigInitializable {

    /**
     * Initialize the class using the specified base path for configuration
     * @param configPath String base path of the configuration this instance should use
     */
    void init(String configPath);
}
