## 项目介绍
Frpc 利用netty编写的轻量级RPC框架，目前使用的注册中心为Nacos

## 模块介绍
frcp-common 通用异常工具类
frpc-codec  编解码相关代码
frpc-client 服务调用方核心逻辑
frpc-server 服务提供方核心逻辑
frpc-spring-boot-starter 自定义starter，方便使用
frpc-starter-test-server 测试服务端使用Frpc
frpc-starter-test-client 测试客户端使用Frpc

## 客户端实现思路
自定义注解 @RpcReference 注解上加上@Autowired注解（方便让spring帮我们从容器中找到对应的bean），编写RpcReferenceBeanPostProcessor通过指定扫描路径，
扫描出所有带有@RpcReference注解的属性，然后将这些属性对应的类型封装为ReferenceBean（工厂bean）注入到容器中。当注入工厂bean时实际调用其getObject方法来获取bean。
在getObject方法里创建代理对象，在执行目标方法时，解析方法信息构造请求通过channel像指定服务端发送请求

## 服务端实现思路
自定义@RpcService，注解@Service注解（方便spring帮我们注入到容器）当容器初始化完成时，扫描指定路径获取所有@Service注解的类，根据类型从spring容器中
查找出对应的bean，然后将方法信息作为key,将methord和bean缓存起来。当收到请求时解析出方法信息通过反射调用目标bean执行对应方法，返回结果。
todo: 扫描@Service 应该实现beanPostProcessor，在重载方法里判断bean是否带某个注解

## starter实现思路
自定义@EnableFrpc注解（注解上配置扫描路径），此注解通过import注入FrpcComponentScanRegistrar，该类负责将我们处理注解的postProcessor、监听器等注入到容器中。
监听器：判断若容器中含有ReferenceBean则启动客户端、若方法缓存（存放带有@Service的bean）不为空则启动服务端
自动配置：在resources/META-INF/spring.factories文件里配置自动配置类的全路径名。在此类中根据配置创建FrpcServerBootStrap、FrpcClientBootStrap


## 参考dubbo
#### RPC 框架中请求与响应匹配
在 RPC 框架中，client 端需要将每个请求和其对应的响应进行匹配。一般而言，有以下几种方式可以实现：
- 使用唯一标识符：在发起请求时，client 端可以自动生成一个唯一标识符，并将其附加在请求中一并发送给 server 端。在返回响应时，server 端需要将该标识符一同返回。这样，client 端就可以通过该标识符将响应和请求进行匹配。
- 序列号和响应码：client 端在发送请求时，可以为其分配一个序列号，同时也为响应分配一个响应码。server 端返回响应时，同时返回相应的序列号和响应码。client 端通过判断响应码和序列号是否匹配来确定响应是属于哪个请求的。
- 保持连接状态：在某些情况下，client 端和 server 端可以保持连接状态，即 client 端向 server 端建立一个长连接，所有请求和响应都在该连接上进行。此时，client 端可以通过检查哪个请求触发了该连接上的响应来将其与对应的请求进行匹配。
#### Dubbo 请求与响应对应处理
在 Dubbo RPC 框架中，client 端通过 Netty 发送请求到 server 端，并且通过一个唯一的 Request ID 来标识每个请求。server 端在响应时将该 Request ID 一并返回给 client 端，client 端可以通过检查该 Request ID 来确定响应是来自哪个请求的。
具体来说，当 client 端发送请求时，它会生成一个唯一的 Request ID，并将该 ID 附加在请求的消息头中。server 端在接收到请求后，将处理结果打包成一个响应，并将该 Request ID 一并返回给 client 端。
client 端的 Netty Channel 接收到响应后，会将响应交给对应的 ResponseFuture 对象处理。ResponseFuture 类型的对象通过反序列化响应消息中的 Request ID 字段，将对应的响应与原请求进行匹配，并将响应保存到相应的位置上。这样，client 端就能够知道每个响应对应的请求是哪个。
需要注意的是，在使用 Dubbo RPC 框架时，client 端和 server 端之间的连接可能是长连接或短连接，因此在匹配请求和响应时需要考虑到连接的状态。同时，Dubbo 还提供了一些高级特性，例如异步调用和多协议支持，其请求和响应匹配的实现方式有所不同，需要根据具体情况进行处理。
#### Dubbo 请求管理
在 Dubbo RPC 框架中，client 端发送的未收到响应的请求会被存储在一个叫做 PendingFutureManager 的容器中。具体来说，当 client 端发送一个请求后，会创建一个 ResponseFuture 对象，并将其保存在 PendingFutureManager 容器中。如果 server 端在一定时间内未能返回响应，那么 ResponseFuture 对象就会设置超时状态，并从 PendingFutureManager 容器中移除。
PendingFutureManager 容器使用 ConcurrentHashMap 实现，它的 Key 是 Request ID，Value 是对应的 ResponseFuture 对象。因为 ConcurrentHashMap 支持并发读写，所以 PendingFutureManager 可以用于处理多线程环境下的请求和响应。
另外，在 Dubbo RPC 框架中，还可以通过配置 timeout 参数来设置请求超时时间。timeout 参数的默认值为 3000ms，即如果 server 端在 3 秒内没有返回响应，client 端就会认为该请求超时。如果希望改变超时时间，可以在配置文件中进行配置。
总之，Dubbo RPC 框架通过 PendingFutureManager 容器实现了请求和响应的异步处理，使得 client 端能够更好地处理未收到响应的情况。同时，还提供了 timeout 参数，避免了请求长时间等待而无响应的问题。
#### Dubbo 连接池
在 Dubbo RPC 框架中，client 端使用了连接池来管理和复用网络连接。具体来说，Dubbo 客户端通过 Netty ChannelPool 来管理 client 端的连接数和连接复用。
ChannelPool 是 Netty 提供的一种机制，它可以管理 Channel（网络连接）的生命周期，并根据需要创建、保存和回收 Channel，从而实现 Channel 的复用。在 Dubbo RPC 框架中，client 端在初始化时会创建一个 ChannelPool 对象，并设置最大连接数、最小连接数以及连接空闲时间等参数。当请求需要与 server 端通信时，client 端会向 ChannelPool 请求一个可用的 Channel。如果 ChannelPool 中已经存在可用的 Channel，则将其返回给 client 端；否则客户端会创建一个新的 Channel，并将其添加到 ChannelPool 中，以备下次请求使用。
使用连接池的好处是可以避免频繁地创建与关闭网络连接，从而提高网络性能并降低资源消耗。同时，连接池还能够控制网络连接的数量，避免因过多的网络连接造成服务器压力过大。
在 Dubbo RPC 框架中，client 端使用了 Netty ChannelPool 来实现连接池的功能。Netty 是一个基于事件驱动的网络编程框架，提供了 ChannelPool 接口和一些实现类，用于管理网络连接。
在 Dubbo RPC 中，client 端通过实现自定义的 NettyClient 客户端，并重写它的 doOpen()、doClose() 方法来创建和销毁 PoolableChannel 对象。PoolableChannel 实现了 Channel 接口，并包含了一个状态字段，用来标识该 Channel 是否正在被使用。当 client 端需要向 server 端发送请求时，就会向 ChannelPool 请求一个可用的 PoolableChannel 对象，如果当前没有可用的 Channel，则会创建一个新的 Channel，并将其添加到 ChannelPool 中，以供后续请求使用。
具体实现方式如下：
初始化时创建 ChannelPool 对象，并设置最大连接数、最小连接数、连接空闲时间等参数。
在 doOpen() 方法中，使用 Netty 的 Bootstrap 和 EventLoopGroup 创建和初始化一个新的 Channel，并将其封装为 PoolableChannel 对象并添加到 ChannelPool 中。
当 client 端需要发送请求时，调用 ChannelPool 的 acquire() 方法获取一个可用的 Channel，如果当前没有可用的 Channel，则根据最大连接数限制创建一个新的 Channel，并将其添加到 ChannelPool 中。
当请求处理完成后，client 端调用 ChannelPool 的 release() 方法将 Channel 设置为可用状态并返回到连接池中，用于后续请求的复用。
如果 ChannelPool 中的 Channel 空闲时间超过指定的连接空闲时间，Netty 会自动将其关闭并从 ChannelPool 中移除。
总之，Dubbo RPC 框架使用 Netty ChannelPool 来管理网络连接，减少了因频繁创建和关闭网络连接而导致的性能和资源开销。同时，ChannelPool 还能控制网络连接的数量，避免流量过多造成服务器压力过大。
#### Netty ChannelPool使用案例
Netty ChannelPool 是用于管理网络连接池的接口，它提供了 acquire()、release() 和 close() 等方法，可以方便地获取可用的连接、回收不再使用的连接、以及关闭连接池。它是属于 Netty 框架的一部分，因此在使用之前需要先引入 Netty jars。
下面是一个简单的示例代码，演示了如何使用 Netty ChannelPool：

```java
// 创建 ClientBootstrap 对象，并设置相关参数
ClientBootstrap bootstrap = new ClientBootstrap();
bootstrap.group(new NioEventLoopGroup());
bootstrap.channel(NioSocketChannel.class);
bootstrap.option(ChannelOption.TCP_NODELAY, true);
bootstrap.handler(new ChannelInitializer<SocketChannel>() {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 添加编解码器和业务逻辑处理器
        ch.pipeline().addLast(new MyEncoder(), new MyDecoder(), new MyHandler());
    }
});

// 创建 ChannelPool 对象
ChannelPool channelPool = new FixedChannelPool(bootstrap, new MyChannelPoolHandler(), 10);

// 获取 Channel 对象，发送请求
Channel channel = channelPool.acquire().sync().getNow();
channel.writeAndFlush(new Request());

// 回收 Channel 对象
channelPool.release(channel);

// 关闭 ChannelPool 对象
channelPool.close();

```
上述代码中，首先创建了一个 ClientBootstrap 客户端对象，用于创建并初始化客户端 Channel。然后创建了一个 ChannelPool 对象，通过 FixedChannelPool 类指定了 ChannelPool 的类型，设置了最大连接数为 10，同时指定了 MyChannelPoolHandler 对象，用于处理新建 Channel 时的初始化操作。接着通过 acquire() 方法获取一个可用的 Channel 对象，并使用该 Channel 发送请求。请求处理完成后，调用 release() 方法将该 Channel 对象标记为可用，然后最后再关闭 ChannelPool。
