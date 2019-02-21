package io.opentracing.contrib.specialagent.jaeger;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TracerParametersTest {
  final static String SERVICE_NAME_KEY = "JAEGER_SERVICE_NAME";

  @Before
  public void beforeTest() {
    Properties systemProps = System.getProperties();
    for (String propName: systemProps.stringPropertyNames()) {
      if (propName.startsWith(TracerParameters.JAEGER_PREFIX))
        System.clearProperty(propName);
    }
  }

  @Test
  public void loadParameters_fromConfigurationFile() throws Exception {
    Properties props = new Properties();
    props.setProperty(SERVICE_NAME_KEY, "MyService");
    props.setProperty("NOT_RELATED", "SomeValue");

    File file = null;
    try {
      file = File.createTempFile("myconfig", "properties");
      System.setProperty(TracerParameters.CONFIGURATION_FILE_KEY, file.getAbsolutePath());

      try (FileOutputStream stream = new FileOutputStream(file)) {
        props.store(stream, "");
      }

      TracerParameters.loadParameters();
      assertEquals("MyService", System.getProperty(SERVICE_NAME_KEY));
      assertNull(System.getProperty("NOT_RELATED"));

    } finally {
      if (file != null)
        file.delete();
    }
  }
}
