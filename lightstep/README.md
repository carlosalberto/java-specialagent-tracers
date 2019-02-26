# SpecialAgent LightStep Tracer plugin

Usage:

```sh
mvn package
java -cp MY_CLASSPATH:target/lightstep.jar \
	-Dtracer.configurationFile=myconfig.properties \
	-javaagent:specialagent.jar com.mycompany.MainClass
```

Configuration file defaults to `tracer.properties`, and can be overriden with `tracer.configurationFile` as shown above. Configuration options can be specified as system properties as well.

Sample configuration file:

```properties
-- ls.accessToken is the only required parameter --
ls.accessToken=myaccesstoken
ls.componentName=MyApplication
ls.collectorHost=127.0.0.1
ls.collectorProtocol=https
ls.collectorPort=66631
``` 
