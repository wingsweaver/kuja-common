# kuja-common
kuja 的通用组件库。

作为整个 kuja 的基石，为上层的 `kuja-cloud`、`kuja-biz`、`kuja-app` 等提供基础支撑。



## 1、功能说明

提供一组跟具体的业务逻辑无关的、通用的基础功能。

- 设计思路及概要说明，请参照 [Github 上的 kuja-docs](https://www.github.com/wingsweaver/kuja-docs)  或者 [Gitee 上的 kuja-docs](https://www.gitee.com/wingsweaver/kuja-docs) 。

具体内容包括：

- [x] 单机
  - [x] 统一错误处理
  - [x] 统一返回值处理
  - [x] 上下文传递
  - [x] 启动预热
- [ ] 可观测性
  - [ ] 度量的增强
  - [ ] 跟踪的增强
  - [ ] 日志的增强
- [ ] 其他
  - [x] ID 生成器
  - [x] 消息发送的封装
  - [x] 控制消息的广播

等等



## 2、编译打包

- java + maven

- 建议使用 JDK 17 及以上版本的 JDK 进行编译打包

  如果不使用 Spring Boot 3.x，也可以使用 JDK 8 进行编译打包

  ``` 
  此时 Spring Boot 3.x 专用的项目将不可见，这些项目的名称以 -jakarta 结尾
  ```

- 支持以下版本的 JDK 和 Spring Boot：
  
  | Spring Boot 版本 | JDK 版本 | 备注                                                         |
  | ---------------- | -------- | ------------------------------------------------------------ |
  | [2.4, 2.x)       | 1.8+     |                                                              |
  | 3.x              | 17+      | WebMvc 等显示使用了 Jakarta-API 的项目，需要引入对应的 `-jakarta` 项目 |
  
  
  
  

## 3、使用方式

### 3.1、在 `pom.xml` 中加入 `kuja-common-bom` 的依赖管理

``` xml
 <dependencyManagement>
    <dependencies>
		<dependency>
            <groupId>com.wingsweaver.kuja</groupId>
            <artifactId>kuja-common-bom</artifactId>
            <version>${kuja-common.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 3.2、在 `pom.xml` 中按需引入相关组件
具体参照后续章节。

- 推荐使用 `kuja-starter-common-` 系列组件库
  - 如引入 `kuja-starter-common-webmvc-jee` ，即可使用 `WebMvc` (Jee / Spring Boot 2.x) 响应的功能

- 深度使用时可以引入 `kuja-common-` 系列组件库，然后使用 `EnableKuja-` 系列注解、手动开启相关功能





## 4、项目一览

### 4.1、Starter 项目

Starter 项目只是对相应核心项目的封装，以便自动激活核心项目中的功能（而无需手动使用 `@EnableKujaCommon-` 系列注解）。

| 项目名称                              | 项目描述                                                     | 备注                                             |
| ------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------ |
| kuja-starter-common-boot              | 普通的 Spring Boot 应用对应的 starter 项目                   |                                                  |
| kuja-starter-common-webflux           | Spring Boot WebFlux 应用对应的 starter 项目                  |                                                  |
| kuja-starter-common-webmvc-jee        | Spring Boot WebMvc 应用对应的 starter 项目（Spring Boot 2.x 专用） |                                                  |
| kuja-starter-common-webmvc-jakarta    | Spring Boot WebMvc 应用对应的 starter 项目（Spring Boot 3.x 专用） |                                                  |
| kuja-starter-common-messaging         | 用于消息发送的 starter 项目 （支持 Spring Boot 2.x 和 Spring Boot 3.x*） |                                                  |
| kuja-starter-common-messaging-jakarta | 用于消息发送的 starter 项目（Spring Boot 3.x * 的专用补丁）  | 需要支持 Spring Boot 3.x 中的 JMS 时，才需要引用 |

### 4.2、核心项目

具体的使用方法，请参照 [Github 上的 kuja-docs](https://www.github.com/wingsweaver/kuja-docs)  或者 [Gitee 上的 kuja-docs](https://www.gitee.com/wingsweaver/kuja-docs) 中各个场景的使用说明。

| 项目名称                          | 项目描述                                                     | 主要功能                                                     |
| --------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| kuja-common-utils             | 基于 Spring 的通用组件库                                     | 日志增强<br>断言增强<br>ID 生成<br>ToString 处理（支持匿名化处理）<br>JSON Codec 封装 |
| kuja-common-boot             | 普通的 Spring Boot 应用对应的通用组件库                      | 应用程序设置增强<br>统一返回结果<br>错误定义 、错误处理（底层处理）、错误记录和上报<br>处理上下文的定义<br>应用程序启动预热 |
| kuja-common-web               | Web 应用（WebFlux、WebMvc）对应的通用组件库                  | AbstractController 的定义<br>HTTP 请求和响应的统一封装       |
| kuja-common-webflux           | WebFlux 应用对应的组件库                                     | 全局异常处理<br>处理上下文的生命周期管理和传递<br>动态日志级别调整<br>ResponseWriter |
| kuja-common-webmvc-jee        | Spring Boot WebMvc 应用对应的组件库（Spring Boot 2.x 专用）  | 全局异常处理<br/>处理上下文的生命周期管理和传递<br/>动态日志级别调整<br/>ResponseWriter |
| kuja-common-webmvc-jakarta    | Spring Boot WebMvc 应用对应的组件库（Spring Boot 3.x 专用）  | 全局异常处理<br/>处理上下文的生命周期管理和传递<br/>动态日志级别调整<br/>ResponseWriter |
| kuja-common-messaging         | 用于消息发送的组件库（支持 Spring Boot 2.x 和 Spring Boot 3.x*） | 消息发送的封装（内置支持：JMS (J2ee)、RabbitMQ、RocketMQ、Kafka、Redis）<br>控制消息的广播和处理（内置支持：刷新应用程序上下文、删除缓存、停止应用程序）<br>基于消息的错误上报 |
| kuja-common-messaging-jakarta | 用于消息发送的组件库（Spring Boot 3.x * 的专用补丁）<br>需要支持 Spring Boot 3.x 中的 JMS 时，才需要引用 | 消息发送的封装（内置支持：JMS (JakartaEE)） |





## 5、补充说明

### 5.1、备用 Maven Repository

- [Gitee](https://gitee.com/wingsweaver/kuja-repo) 上的备用 Repository（含 Snapshot 和 Release）

``` xml
	<repositories>
		<repository>
			<id>kuja-repo-gitee</id>
			<name>Kuja Repository on Gitee</name>
			<url>https://gitee.com/wingsweaver/kuja-repo/raw/main</url>
			<snapshots>
				<updatePolicy>always</updatePolicy>
			</snapshots>
		</repository>
	</repositories>
```

- [Github](https://github.com/wingsweaver/kuja-repo) 上的备用 Repository（含 Snapshot 和 Release，需要翻墙）

``` xml
<repositories>
    <repository>
        <id>kuja-repo-github</id>
        <name>Kuja Repository on Github</name>
			<url>https://raw.githubusercontent.com/wingsweaver/kuja-repo/main</url>
			<snapshots>
				<updatePolicy>always</updatePolicy>
			</snapshots>
    </repository>
</repositories>
```

### 5.2、核心项目之间的依赖关系

``` mermaid
flowchart TD
	subgraph Base
	    B(kuja-common-boot) --> A(kuja-common-utils)
	end
    C(kuja-common-web) --> B
    subgraph Web
        D(kuja-common-webflux) --> C
        E(kuja-common-webmvc-common) --> C
        F(kuja-common-webmvc-jee) --> E
        G(kuja-common-webmvc-jakarta) --> E
    end
    H(kuja-common-messaging) --> B
    subgraph Messaging
	    I(kuja-common-messaging-jakarta) --> H
    end
```

