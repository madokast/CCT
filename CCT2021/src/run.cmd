"C:\Program Files\jdk-15\bin\java.exe" -Dfile.encoding=UTF-8 -classpath .\target\classes;.\maven_repo\io\jenetics\jenetics\6.1.0\jenetics-6.1.0.jar;.\maven_repo\io\jenetics\jenetics.ext\6.1.0\jenetics.ext-6.1.0.jar;.\maven_repo\org\springframework\boot\spring-boot-starter-webflux\2.2.4.RELEASE\spring-boot-starter-webflux-2.2.4.RELEASE.jar;.\maven_repo\org\springframework\boot\spring-boot-starter\2.2.4.RELEASE\spring-boot-starter-2.2.4.RELEASE.jar;.\maven_repo\org\springframework\boot\spring-boot\2.2.4.RELEASE\spring-boot-2.2.4.RELEASE.jar;.\maven_repo\org\springframework\spring-context\5.2.3.RELEASE\spring-context-5.2.3.RELEASE.jar;.\maven_repo\org\springframework\spring-aop\5.2.3.RELEASE\spring-aop-5.2.3.RELEASE.jar;.\maven_repo\org\springframework\spring-expression\5.2.3.RELEASE\spring-expression-5.2.3.RELEASE.jar;.\maven_repo\org\springframework\boot\spring-boot-autoconfigure\2.2.4.RELEASE\spring-boot-autoconfigure-2.2.4.RELEASE.jar;.\maven_repo\org\springframework\boot\spring-boot-starter-logging\2.2.4.RELEASE\spring-boot-starter-logging-2.2.4.RELEASE.jar;.\maven_repo\ch\qos\logback\logback-classic\1.2.3\logback-classic-1.2.3.jar;.\maven_repo\ch\qos\logback\logback-core\1.2.3\logback-core-1.2.3.jar;.\maven_repo\org\apache\logging\log4j\log4j-to-slf4j\2.12.1\log4j-to-slf4j-2.12.1.jar;.\maven_repo\org\apache\logging\log4j\log4j-api\2.12.1\log4j-api-2.12.1.jar;.\maven_repo\org\slf4j\jul-to-slf4j\1.7.30\jul-to-slf4j-1.7.30.jar;.\maven_repo\jakarta\annotation\jakarta.annotation-api\1.3.5\jakarta.annotation-api-1.3.5.jar;.\maven_repo\org\yaml\snakeyaml\1.25\snakeyaml-1.25.jar;.\maven_repo\org\springframework\boot\spring-boot-starter-json\2.2.4.RELEASE\spring-boot-starter-json-2.2.4.RELEASE.jar;.\maven_repo\com\fasterxml\jackson\core\jackson-databind\2.10.2\jackson-databind-2.10.2.jar;.\maven_repo\com\fasterxml\jackson\core\jackson-annotations\2.10.2\jackson-annotations-2.10.2.jar;.\maven_repo\com\fasterxml\jackson\core\jackson-core\2.10.2\jackson-core-2.10.2.jar;.\maven_repo\com\fasterxml\jackson\datatype\jackson-datatype-jdk8\2.10.2\jackson-datatype-jdk8-2.10.2.jar;.\maven_repo\com\fasterxml\jackson\datatype\jackson-datatype-jsr310\2.10.2\jackson-datatype-jsr310-2.10.2.jar;.\maven_repo\com\fasterxml\jackson\module\jackson-module-parameter-names\2.10.2\jackson-module-parameter-names-2.10.2.jar;.\maven_repo\org\springframework\boot\spring-boot-starter-reactor-netty\2.2.4.RELEASE\spring-boot-starter-reactor-netty-2.2.4.RELEASE.jar;.\maven_repo\io\projectreactor\netty\reactor-netty\0.9.4.RELEASE\reactor-netty-0.9.4.RELEASE.jar;.\maven_repo\io\netty\netty-codec-http\4.1.45.Final\netty-codec-http-4.1.45.Final.jar;.\maven_repo\io\netty\netty-common\4.1.45.Final\netty-common-4.1.45.Final.jar;.\maven_repo\io\netty\netty-buffer\4.1.45.Final\netty-buffer-4.1.45.Final.jar;.\maven_repo\io\netty\netty-transport\4.1.45.Final\netty-transport-4.1.45.Final.jar;.\maven_repo\io\netty\netty-resolver\4.1.45.Final\netty-resolver-4.1.45.Final.jar;.\maven_repo\io\netty\netty-codec\4.1.45.Final\netty-codec-4.1.45.Final.jar;.\maven_repo\io\netty\netty-codec-http2\4.1.45.Final\netty-codec-http2-4.1.45.Final.jar;.\maven_repo\io\netty\netty-handler\4.1.45.Final\netty-handler-4.1.45.Final.jar;.\maven_repo\io\netty\netty-handler-proxy\4.1.45.Final\netty-handler-proxy-4.1.45.Final.jar;.\maven_repo\io\netty\netty-codec-socks\4.1.45.Final\netty-codec-socks-4.1.45.Final.jar;.\maven_repo\io\netty\netty-transport-native-epoll\4.1.45.Final\netty-transport-native-epoll-4.1.45.Final-linux-x86_64.jar;.\maven_repo\io\netty\netty-transport-native-unix-common\4.1.45.Final\netty-transport-native-unix-common-4.1.45.Final.jar;.\maven_repo\org\glassfish\jakarta.el\3.0.3\jakarta.el-3.0.3.jar;.\maven_repo\org\springframework\boot\spring-boot-starter-validation\2.2.4.RELEASE\spring-boot-starter-validation-2.2.4.RELEASE.jar;.\maven_repo\jakarta\validation\jakarta.validation-api\2.0.2\jakarta.validation-api-2.0.2.jar;.\maven_repo\org\hibernate\validator\hibernate-validator\6.0.18.Final\hibernate-validator-6.0.18.Final.jar;.\maven_repo\org\jboss\logging\jboss-logging\3.4.1.Final\jboss-logging-3.4.1.Final.jar;.\maven_repo\com\fasterxml\classmate\1.5.1\classmate-1.5.1.jar;.\maven_repo\org\springframework\spring-web\5.2.3.RELEASE\spring-web-5.2.3.RELEASE.jar;.\maven_repo\org\springframework\spring-beans\5.2.3.RELEASE\spring-beans-5.2.3.RELEASE.jar;.\maven_repo\org\springframework\spring-webflux\5.2.3.RELEASE\spring-webflux-5.2.3.RELEASE.jar;.\maven_repo\org\synchronoss\cloud\nio-multipart-parser\1.1.0\nio-multipart-parser-1.1.0.jar;.\maven_repo\org\synchronoss\cloud\nio-stream-storage\1.1.3\nio-stream-storage-1.1.3.jar;.\maven_repo\org\springframework\spring-core\5.2.3.RELEASE\spring-core-5.2.3.RELEASE.jar;.\maven_repo\org\springframework\spring-jcl\5.2.3.RELEASE\spring-jcl-5.2.3.RELEASE.jar;.\maven_repo\io\projectreactor\reactor-core\3.3.2.RELEASE\reactor-core-3.3.2.RELEASE.jar;.\maven_repo\org\reactivestreams\reactive-streams\1.0.3\reactive-streams-1.0.3.jar;.\maven_repo\org\reflections\reflections\0.9.11\reflections-0.9.11.jar;.\maven_repo\com\google\guava\guava\20.0\guava-20.0.jar;.\maven_repo\org\javassist\javassist\3.21.0-GA\javassist-3.21.0-GA.jar;.\maven_repo\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;.\maven_repo\cn\hutool\hutool-all\4.0.9\hutool-all-4.0.9.jar;.\maven_repo\net\java\dev\jna\jna\4.5.0\jna-4.5.0.jar;.\maven_repo\net\java\dev\jna\jna-platform\4.5.0\jna-platform-4.5.0.jar;.\maven_repo\com\github\oshi\oshi-json\3.6.1\oshi-json-3.6.1.jar;.\maven_repo\com\github\oshi\oshi-core\3.6.1\oshi-core-3.6.1.jar;.\maven_repo\org\threeten\threetenbp\1.3.6\threetenbp-1.3.6.jar;.\maven_repo\org\glassfish\javax.json\1.0.4\javax.json-1.0.4.jar;.\maven_repo\org\slf4j\slf4j-api\1.7.30\slf4j-api-1.7.30.jar;.\maven_repo\org\jetbrains\annotations\20.1.0\annotations-20.1.0.jar cn.edu.hust.zrx.cct.study.V双极点坐标系机架.C20201102外部算法