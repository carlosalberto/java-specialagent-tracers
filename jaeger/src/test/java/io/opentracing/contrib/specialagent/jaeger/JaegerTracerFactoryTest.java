package io.opentracing.contrib.specialagent.jaeger;

import java.io.File;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Tracer;
import io.opentracing.contrib.specialagent.common.Configuration;
import io.opentracing.contrib.specialagent.common.Utils;

import static org.junit.Assert.assertTrue;

public class JaegerTracerFactoryTest {
  final static String SERVICE_NAME_KEY = "JAEGER_SERVICE_NAME";

  Tracer tracer;

  @Before
  public void beforeTest() {
    Properties systemProps = System.getProperties();
    for (String propName: systemProps.stringPropertyNames()) {
      if (propName.startsWith(TracerParameters.JAEGER_PREFIX))
        System.clearProperty(propName);
    }
  }

  @After
  public void afterTest() {
    if (tracer != null) {
      ((JaegerTracer)tracer).close();
      tracer = null;
    }
  }

  @Test(expected=IllegalArgumentException.class)
  public void getTracer_noServiceName() {
    new JaegerTracerFactory().getTracer();
  }

  @Test
  public void getTracer_serviceNameViaProperty() {
    System.setProperty(SERVICE_NAME_KEY, "MyService");
    tracer = new JaegerTracerFactory().getTracer();
    assertTrue(tracer instanceof JaegerTracer);
  }

  @Test
  public void getTracer_serviceNameViaConfigFile() throws Exception {
    Properties props = new Properties();
    props.setProperty(JaegerTracerFactoryTest.SERVICE_NAME_KEY, "MyService");

    File file = null;
    try {
      file = Utils.savePropertiesToTempFile(props);
      System.setProperty(Configuration.CONFIGURATION_FILE_KEY, file.getAbsolutePath());

      tracer = new JaegerTracerFactory().getTracer();
      assertTrue(tracer instanceof JaegerTracer);

    } finally {
      if (file != null)
        file.delete();
    }
  }
}
