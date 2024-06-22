package Application;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigLoader {
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;
    private static final Logger logger = Logger.getLogger(ConfigLoader.class.getName());

    static {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
            logger.log(Level.INFO, "Configurações carregadas com sucesso:");
            properties.forEach((key, value) -> logger.log(Level.INFO, key + ": " + value));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao carregar o arquivo de configuração: " + e.getMessage(), e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
