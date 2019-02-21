package io.opentracing.contrib.specialagent.jaeger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TracerParameters {
  private TracerParameters() {}

  final static String JAEGER_PREFIX = "JAEGER_";

  private final static Logger logger = Logger.getLogger(TracerParameters.class.getName());

  final static String DEFAULT_CONFIGURATION_FILE_PATH = "tracer.properties";
  final static String CONFIGURATION_FILE_KEY = "tracer.configurationFile";

  public static void loadParameters() {
    String filePath = System.getProperty(CONFIGURATION_FILE_KEY);
    if (filePath == null)
      filePath = DEFAULT_CONFIGURATION_FILE_PATH;

    Properties props = loadPropertiesFile(filePath);
    if (props != null)
      loadParametersIntoSystemProperties(props);
  }

  static Properties loadPropertiesFile(String path) {
    File file = new File(path);
    if (!file.isFile()) {
      return null;
    }

    Properties props = new Properties();

    try (FileInputStream stream = new FileInputStream(file)) {
      props.load(stream);
    } catch (IOException e) {
      logger.log(Level.WARNING, "Failed to read the Tracer configuration file '" + path + "'");
      logger.log(Level.WARNING, e.toString());
      return null;
    }

    logger.log(Level.INFO, "Successfully loaded Tracer configuration file " + path);
    return props;
  }

  static void loadParametersIntoSystemProperties(Properties props) {
    for (String propName: props.stringPropertyNames()) {
      // Only load the parameter if it is not *already* defined as a System property.
      if (!propName.startsWith(JAEGER_PREFIX) || System.getProperty(propName) != null)
        continue;

      String propValue = props.getProperty(propName);
      System.setProperty(propName, propValue);
      logger.log(Level.INFO, "Set System property " + propName + "=" + propValue + " from Tracer configuration file");
    }
  }
}
