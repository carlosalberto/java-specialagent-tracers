package io.opentracing.contrib.specialagent.jaeger;

import javax.annotation.Priority;

import io.opentracing.Tracer;
import io.opentracing.contrib.tracerresolver.TracerFactory;

@Priority(1) // Higher priority than the original factory (which we wrap).
public class JaegerTracerFactory implements TracerFactory
{
  io.jaegertracing.tracerresolver.internal.JaegerTracerFactory factory;

  public JaegerTracerFactory() {
    TracerParameters.loadParameters();
    this.factory = new io.jaegertracing.tracerresolver.internal.JaegerTracerFactory();
  }

  @Override
  public Tracer getTracer() {
    return factory.getTracer();
  }
}
