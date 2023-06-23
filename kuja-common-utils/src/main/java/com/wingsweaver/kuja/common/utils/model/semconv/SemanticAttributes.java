package com.wingsweaver.kuja.common.utils.model.semconv;

import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.model.tags.TagKeys;

/**
 * 语义类型属性的定义。<br>
 * 参照 OTEL SemConv v1.26.0-alpha 实现。<br>
 * 语义类型的资源包括：异常信息、HTTP 请求信息、线程信息、消息信息，
 * 也就是会随着处理逻辑会发生变化的数据。
 *
 * @author wingsweaver
 */
@SuppressWarnings("PMD.ClassNamingShouldBeCamelRule")
public interface SemanticAttributes {
    /**
     * SCHEMA URL。
     */
    String SCHEMA_URL = "https://opentelemetry.io/schemas/1.19.0";

    /**
     * 版本。
     */
    String VERSION = "1.26.0-alpha";

    /**
     * 异常相关信息。
     */
    interface Exception {
        /**
         * Key: 异常的类型。
         */
        String KEY_TYPE = "exception.type";

        /**
         * Tag: 异常的类型。
         */
        TagKey.StringKey TAG_TYPE = TagKeys.ofString(KEY_TYPE);

        /**
         * Key: 异常的消息。
         */
        String KEY_MESSAGE = "exception.message";

        /**
         * Tag: 异常的消息。
         */
        TagKey.StringKey TAG_MESSAGE = TagKeys.ofString(KEY_MESSAGE);

        /**
         * Key: 异常的调用栈。
         */
        String KEY_STACKTRACE = "exception.stacktrace";

        /**
         * Tag: 异常的调用栈。
         */
        TagKey.StringKey TAG_STACKTRACE = TagKeys.ofString(KEY_STACKTRACE);

        /**
         * Key: 异常是否要逃逸出本 SCOPE 的范围。
         */
        String KEY_ESCAPED = "exception.escaped";

        /**
         * Tag: 异常是否要逃逸出本 SCOPE 的范围。
         */
        TagKey.StringKey TAG_ESCAPED = TagKeys.ofString(KEY_ESCAPED);
    }

    /**
     * HTTP 相关信息。
     */
    interface Http {
        /**
         * Key: Http Method。
         */
        String KEY_METHOD = "http.method";

        /**
         * Tag: Http Method。
         */
        TagKey.StringKey TAG_METHOD = TagKeys.ofString(KEY_METHOD);

        /**
         * Key: Http 状态码。
         */
        String KEY_STATUS_CODE = "http.status_code";

        /**
         * Tag: Http 状态码。
         */
        TagKey.LongKey TAG_STATUS_CODE = TagKeys.ofLong(KEY_STATUS_CODE);

        /**
         * Key: Http 协议的类型。
         */
        String KEY_FLAVOR = "http.flavor";

        /**
         * Tag: Http 协议的类型。
         */
        TagKey.StringKey TAG_FLAVOR = TagKeys.ofString(KEY_FLAVOR);

        /**
         * HTTP 协议类型的定义。
         */
        interface Flavors {
            /**
             * HTTP/1.0。
             */
            String HTTP_1_0 = "1.0";

            /**
             * HTTP/1.1。
             */
            String HTTP_1_1 = "1.1";

            /**
             * HTTP/2。
             */
            String HTTP_2_0 = "2.0";

            /**
             * HTTP/3。
             */
            String HTTP_3_0 = "3.0";

            /**
             * SPDY protocol。
             */
            String SPDY = "SPDY";

            /**
             * QUIC protocol。
             */
            String QUIC = "QUIC";
        }

        /**
         * Key: Http 协议的 Scheme。
         */
        String KEY_SCHEME = "http.scheme";

        /**
         * Tag: Http 协议的 Scheme。
         */
        TagKey.StringKey TAG_SCHEME = TagKeys.ofString(KEY_SCHEME);

        /**
         * Key: Http 请求匹配的路由。
         */
        String KEY_ROUTE = "http.route";

        /**
         * Tag: Http 请求匹配的路由。
         */
        TagKey.StringKey TAG_ROUTE = TagKeys.ofString(KEY_ROUTE);

        /**
         * Key: Http 请求的 URL。
         */
        String KEY_URL = "http.url";

        /**
         * Tag: Http 请求的 URL。
         */
        TagKey.StringKey TAG_URL = TagKeys.ofString(KEY_URL);

        /**
         * Key: Http 请求的客户端真实 IP。
         */
        String KEY_CLIENT_IP = "http.client_ip";

        /**
         * Tag: Http 请求的客户端真实 IP。
         */
        TagKey.StringKey TAG_CLIENT_IP = TagKeys.ofString(KEY_CLIENT_IP);

        /**
         * Key: Http 服务器的名称。<br>
         * 已过期。使用 {@link Net#KEY_HOST_NAME} 代替。
         */
        @Deprecated
        String KEY_SERVER_NAME = "http.server_name";

        /**
         * Tag: Http 服务器的名称。
         */
        TagKey.StringKey TAG_SERVER_NAME = TagKeys.ofString(KEY_SERVER_NAME);

        /**
         * Key: Http 服务器的名称。<br>
         * 已过期。使用 {@link Net#KEY_HOST_NAME} 代替。
         */
        @Deprecated
        String KEY_HOST = "http.host";

        /**
         * Tag: Http 服务器的名称。
         */
        TagKey.StringKey TAG_HOST = TagKeys.ofString(KEY_HOST);

        /**
         * Key: Http 客户端的 User-Agent 信息。<br>
         * 已过期。使用 {@link UserAgent#KEY_ORIGINAL} 代替。
         */
        @Deprecated
        String KEY_USER_AGENT = "http.user_agent";

        /**
         * Tag: Http 客户端的 User-Agent 信息。
         */
        TagKey.StringKey TAG_USER_AGENT = TagKeys.ofString(KEY_USER_AGENT);
    }

    /**
     * User-Agent 信息。
     */
    interface UserAgent {
        /**
         * Key: Http 客户端的 User-Agent 信息。
         */
        String KEY_ORIGINAL = "user_agent.original";

        /**
         * Tag: Http 客户端的 User-Agent 信息。
         */
        TagKey.StringKey TAG_ORIGINAL = TagKeys.ofString(KEY_ORIGINAL);
    }

    /**
     * 事件相关信息。
     */
    interface Event {
        /**
         * Key: 事件的名称。
         */
        String KEY_NAME = "event.name";

        /**
         * Tag: 事件的名称。
         */
        TagKey.StringKey TAG_NAME = TagKeys.ofString(KEY_NAME);

        /**
         * Key: 事件的业务领域。
         */
        String KEY_DOMAIN = "event.domain";

        /**
         * Tag: 事件的业务领域。
         */
        TagKey.StringKey TAG_DOMAIN = TagKeys.ofString(KEY_DOMAIN);

        /**
         * 事件的业务领域的定义。
         */
        interface Domains {
            /**
             * Events from browser apps。
             */
            String BROWSER = "browser";

            /**
             * Events from mobile apps。
             */
            String DEVICE = "device";

            /**
             * Events from Kubernetes。
             */
            String K8S = "k8s";
        }
    }

    /**
     * 数据库相关信息。
     */
    interface Database {
        /**
         * Key: 数据库系统的名称。
         */
        String KEY_SYSTEM = "db.system";

        /**
         * Tag: 数据库系统的名称。
         */
        TagKey.StringKey TAG_SYSTEM = TagKeys.ofString(KEY_SYSTEM);

        /**
         * 数据库系统的定义。
         */
        interface Systems {
            /**
             * Some other SQL database. Fallback only. See notes。
             */
            String OTHER_SQL = "other_sql";

            /**
             * Microsoft SQL Server。
             */
            String MSSQL = "mssql";

            /**
             * Microsoft SQL Server Compact。
             */
            String MSSQLCOMPACT = "mssqlcompact";

            /**
             * MySQL。
             */
            String MYSQL = "mysql";

            /**
             * Oracle Database。
             */
            String ORACLE = "oracle";

            /**
             * IBM Db2。
             */
            String DB2 = "db2";

            /**
             * PostgreSQL。
             */
            String POSTGRESQL = "postgresql";

            /**
             * Amazon Redshift。
             */
            String REDSHIFT = "redshift";

            /**
             * Apache Hive。
             */
            String HIVE = "hive";

            /**
             * Cloudscape。
             */
            String CLOUDSCAPE = "cloudscape";

            /**
             * HyperSQL DataBase。
             */
            String HSQLDB = "hsqldb";

            /**
             * Progress Database。
             */
            String PROGRESS = "progress";

            /**
             * SAP MaxDB。
             */
            String MAXDB = "maxdb";

            /**
             * SAP HANA。
             */
            String HANADB = "hanadb";

            /**
             * Ingres。
             */
            String INGRES = "ingres";

            /**
             * FirstSQL。
             */
            String FIRSTSQL = "firstsql";

            /**
             * EnterpriseDB。
             */
            String EDB = "edb";

            /**
             * InterSystems Caché。
             */
            String CACHE = "cache";

            /**
             * Adabas (Adaptable Database System)。
             */
            String ADABAS = "adabas";

            /**
             * Firebird。
             */
            String FIREBIRD = "firebird";

            /**
             * Apache Derby。
             */
            String DERBY = "derby";

            /**
             * FileMaker。
             */
            String FILEMAKER = "filemaker";

            /**
             * Informix。
             */
            String INFORMIX = "informix";

            /**
             * InstantDB。
             */
            String INSTANTDB = "instantdb";

            /**
             * InterBase。
             */
            String INTERBASE = "interbase";

            /**
             * MariaDB。
             */
            String MARIADB = "mariadb";

            /**
             * Netezza。
             */
            String NETEZZA = "netezza";

            /**
             * Pervasive PSQL。
             */
            String PERVASIVE = "pervasive";

            /**
             * PointBase。
             */
            String POINTBASE = "pointbase";

            /**
             * SQLite。
             */
            String SQLITE = "sqlite";

            /**
             * Sybase。
             */
            String SYBASE = "sybase";

            /**
             * Teradata。
             */
            String TERADATA = "teradata";

            /**
             * Vertica。
             */
            String VERTICA = "vertica";

            /**
             * H2。
             */
            String H2 = "h2";

            /**
             * ColdFusion IMQ。
             */
            String COLDFUSION = "coldfusion";

            /**
             * Apache Cassandra。
             */
            String CASSANDRA = "cassandra";

            /**
             * Apache HBase。
             */
            String HBASE = "hbase";

            /**
             * MongoDB。
             */
            String MONGODB = "mongodb";

            /**
             * Redis。
             */
            String REDIS = "redis";

            /**
             * Couchbase。
             */
            String COUCHBASE = "couchbase";

            /**
             * CouchDB。
             */
            String COUCHDB = "couchdb";

            /**
             * Microsoft Azure Cosmos DB。
             */
            String COSMOSDB = "cosmosdb";

            /**
             * Amazon DynamoDB。
             */
            String DYNAMODB = "dynamodb";

            /**
             * Neo4j。
             */
            String NEO4J = "neo4j";

            /**
             * Apache Geode。
             */
            String GEODE = "geode";

            /**
             * Elasticsearch。
             */
            String ELASTICSEARCH = "elasticsearch";

            /**
             * Memcached。
             */
            String MEMCACHED = "memcached";

            /**
             * CockroachDB。
             */
            String COCKROACHDB = "cockroachdb";

            /**
             * OpenSearch。
             */
            String OPENSEARCH = "opensearch";

            /**
             * ClickHouse。
             */
            String CLICKHOUSE = "clickhouse";

            /**
             * Cloud Spanner。
             */
            String SPANNER = "spanner";
        }

        /**
         * Key: 数据库的连接字符串。
         */
        String KEY_CONNECTION_STRING = "db.connection_string";

        /**
         * Tag: 数据库的连接字符串。
         */
        TagKey.StringKey TAG_CONNECTION_STRING = TagKeys.ofString(KEY_CONNECTION_STRING);

        /**
         * Key: 数据库的使用用户。
         */
        String KEY_USER = "db.user";

        /**
         * Tag: 数据库的使用用户。
         */
        TagKey.StringKey TAG_USER = TagKeys.ofString(KEY_USER);

        /**
         * Key: 数据库的 JDBC 驱动类的名称。
         */
        String KEY_JDBC_DRIVER_CLASSNAME = "db.jdbc.driver_classname";

        /**
         * Tag: 数据库的 JDBC 驱动类的名称。
         */
        TagKey.StringKey TAG_JDBC_DRIVER_CLASSNAME = TagKeys.ofString(KEY_JDBC_DRIVER_CLASSNAME);

        /**
         * Key: 数据库的名称。
         */
        String KEY_NAME = "db.name";

        /**
         * Tag: 数据库的名称。
         */
        TagKey.StringKey TAG_NAME = TagKeys.ofString(KEY_NAME);

        /**
         * Key: 数据库操作的语句。
         */
        String KEY_STATEMENT = "db.statement";

        /**
         * Tag: 数据库操作的语句。
         */
        TagKey.StringKey TAG_STATEMENT = TagKeys.ofString(KEY_STATEMENT);

        /**
         * Key: 数据库操作的名称。
         */
        String KEY_OPERATION = "db.operation";

        /**
         * Tag: 数据库操作的名称。
         */
        TagKey.StringKey TAG_OPERATION = TagKeys.ofString(KEY_OPERATION);

        /**
         * Key: 数据库操作的数据表的名称。
         */
        String KEY_SQL_TABLE = "db.sql.table";

        /**
         * Tag: 数据库操作的数据表的名称。
         */
        TagKey.StringKey TAG_SQL_TABLE = TagKeys.ofString(KEY_SQL_TABLE);
    }

    /**
     * 网络相关信息。
     */
    interface Net {
        /**
         * Key: 远程主机的名称。
         */
        String KEY_PEER_NAME = "net.peer.name";

        /**
         * Tag: 远程主机的名称。
         */
        TagKey.StringKey TAG_PEER_NAME = TagKeys.ofString(KEY_PEER_NAME);

        /**
         * Key: 远程主机的端口。
         */
        String KEY_PEER_PORT = "net.peer.port";

        /**
         * Tag: 远程主机的端口。
         */
        TagKey.LongKey TAG_PEER_PORT = TagKeys.ofLong(KEY_PEER_PORT);

        /**
         * Key: 本地主机的名称。
         */
        String KEY_HOST_NAME = "net.host.name";

        /**
         * Tag: 本地主机的名称。
         */
        TagKey.StringKey TAG_HOST_NAME = TagKeys.ofString(KEY_HOST_NAME);

        /**
         * Key: 本地主机的端口。
         */
        String KEY_HOST_PORT = "net.host.port";

        /**
         * Tag: 本地主机的端口。
         */
        TagKey.LongKey TAG_HOST_PORT = TagKeys.ofLong(KEY_HOST_PORT);
    }

    /**
     * 终端用户相关信息。
     */
    interface EndUser {
        /**
         * Key: 终端用户的 ID。
         */
        String KEY_ID = "enduser.id";

        /**
         * Tag: 终端用户的 ID。
         */
        TagKey.StringKey TAG_ID = TagKeys.ofString(KEY_ID);

        /**
         * Key: 终端用户的角色。
         */
        String KEY_ROLE = "enduser.role";

        /**
         * Tag: 终端用户的角色。
         */
        TagKey.StringKey TAG_ROLE = TagKeys.ofString(KEY_ROLE);

        /**
         * Key: 终端用户的访问范围。
         */
        String KEY_SCOPE = "enduser.scope";

        /**
         * Tag: 终端用户的访问范围。
         */
        TagKey.StringKey TAG_SCOPE = TagKeys.ofString(KEY_SCOPE);
    }

    /**
     * 线程相关信息。
     */
    interface Thread {
        /**
         * Key: 线程 ID。
         */
        String KEY_ID = "thread.id";

        /**
         * Tag: 线程 ID。
         */
        TagKey.LongKey TAG_ID = TagKeys.ofLong(KEY_ID);

        /**
         * Key: 线程名称。
         */
        String KEY_NAME = "thread.name";

        /**
         * Tag: 线程名称。
         */
        TagKey.StringKey TAG_NAME = TagKeys.ofString(KEY_NAME);
    }

    /**
     * 代码相关信息。
     */
    interface Code {
        /**
         * Key: 代码的函数名称。
         */
        String KEY_FUNCTION = "code.function";

        /**
         * Tag: 代码的函数名称。
         */
        TagKey.StringKey TAG_FUNCTION = TagKeys.ofString(KEY_FUNCTION);

        /**
         * Key: 代码的命名空间。
         */
        String KEY_NAMESPACE = "code.namespace";

        /**
         * Tag: 代码的命名空间。
         */
        TagKey.StringKey TAG_NAMESPACE = TagKeys.ofString(KEY_NAMESPACE);

        /**
         * Key: 代码的文件路径。
         */
        String KEY_FILEPATH = "code.filepath";

        /**
         * Tag: 代码的文件路径。
         */
        TagKey.StringKey TAG_FILEPATH = TagKeys.ofString(KEY_FILEPATH);

        /**
         * Key: 代码的代码行号。
         */
        String KEY_LINENO = "code.lineno";

        /**
         * Tag: 代码的代码行号。
         */
        TagKey.LongKey TAG_LINENO = TagKeys.ofLong(KEY_LINENO);

        /**
         * Key: 代码的列编号。
         */
        String KEY_COLUMN = "code.column";

        /**
         * Tag: 代码的列编号。
         */
        TagKey.LongKey TAG_COLUMN = TagKeys.ofLong(KEY_COLUMN);
    }

    /**
     * 消息系统（MQ）相关信息。
     */
    interface Messaging {
        /**
         * Key: 消息 ID。
         */
        String KEY_MESSAGE_ID = "messaging.message.id";

        /**
         * Tag: 消息 ID。
         */
        TagKey.StringKey TAG_MESSAGE_ID = TagKeys.ofString(KEY_MESSAGE_ID);

        /**
         * Key: 消息的会话 ID。
         */
        String KEY_MESSAGE_CONVERSATION_ID = "messaging.message.conversation_id";

        /**
         * Tag: 消息的会话 ID。
         */
        TagKey.StringKey TAG_MESSAGE_CONVERSATION_ID = TagKeys.ofString(KEY_MESSAGE_CONVERSATION_ID);

        /**
         * Key: 消息的数据大小（byte）。
         */
        String KEY_MESSAGE_PAYLOAD_SIZE_BYTES = "messaging.message.payload_size_bytes";

        /**
         * Tag: 消息的数据大小（byte）。
         */
        TagKey.LongKey TAG_MESSAGE_PAYLOAD_SIZE_BYTES = TagKeys.ofLong(KEY_MESSAGE_PAYLOAD_SIZE_BYTES);

        /**
         * Key: 消息的目标类型。
         */
        String KEY_DESTINATION_KIND = "messaging.destination.kind";

        /**
         * Tag: 消息的目标类型。
         */
        TagKey.StringKey TAG_DESTINATION_KIND = TagKeys.ofString(KEY_DESTINATION_KIND);

        /**
         * 目标类型的定义。
         */
        interface DestinationKinds {
            /**
             * A message sent to a queue。
             */
            String QUEUE = "queue";

            /**
             * A message sent to a topic。
             */
            String TOPIC = "topic";
        }

        /**
         * Key: 消息的目标名称。
         */
        String KEY_DESTINATION_NAME = "messaging.destination.name";

        /**
         * Tag: 消息的目标名称。
         */
        TagKey.StringKey TAG_DESTINATION_NAME = TagKeys.ofString(KEY_DESTINATION_NAME);

        /**
         * Key: 消息的目的地名称的低基数表示。
         */
        String KEY_DESTINATION_TEMPLATE = "messaging.destination.template";

        /**
         * Tag: 消息的目的地名称的低基数表示。
         */
        TagKey.StringKey TAG_DESTINATION_TEMPLATE = TagKeys.ofString(KEY_DESTINATION_TEMPLATE);

        /**
         * Key: 消息的目的地是否是临时的。
         */
        String KEY_DESTINATION_TEMPORARY = "messaging.destination.temporary";

        /**
         * Tag: 消息的目的地是否是临时的。
         */
        TagKey.BooleanKey TAG_DESTINATION_TEMPORARY = TagKeys.ofBoolean(KEY_DESTINATION_TEMPORARY);

        /**
         * Key: 消息的目的地是否是匿名的。
         */
        String KEY_DESTINATION_ANONYMOUS = "messaging.destination.anonymous";

        /**
         * Tag: 消息的目的地是否是匿名的。
         */
        TagKey.BooleanKey TAG_DESTINATION_ANONYMOUS = TagKeys.ofBoolean(KEY_DESTINATION_ANONYMOUS);

        /**
         * Key: 消息的来源类型。
         */
        String KEY_SOURCE_KIND = "messaging.source.kind";

        /**
         * Tag: 消息的来源类型。
         */
        TagKey.StringKey TAG_SOURCE_KIND = TagKeys.ofString(KEY_SOURCE_KIND);

        /**
         * 来源类型的定义。
         */
        interface SourceKinds {
            /**
             * A message sent to a queue。
             */
            String QUEUE = "queue";

            /**
             * A message sent to a topic。
             */
            String TOPIC = "topic";
        }

        /**
         * Key: 消息的来源名称。
         */
        String KEY_SOURCE_NAME = "messaging.source.name";

        /**
         * Tag: 消息的来源名称。
         */
        TagKey.StringKey TAG_SOURCE_NAME = TagKeys.ofString(KEY_SOURCE_NAME);

        /**
         * Key: 消息的来源名称的低基数表示。
         */
        String KEY_SOURCE_TEMPLATE = "messaging.source.template";

        /**
         * Tag: 消息的来源名称的低基数表示。
         */
        TagKey.StringKey TAG_SOURCE_TEMPLATE = TagKeys.ofString(KEY_SOURCE_TEMPLATE);

        /**
         * Key: 消息的来源是否是临时的。
         */
        String KEY_SOURCE_TEMPORARY = "messaging.source.temporary";

        /**
         * Tag: 消息的来源是否是临时的。
         */
        TagKey.BooleanKey TAG_SOURCE_TEMPORARY = TagKeys.ofBoolean(KEY_SOURCE_TEMPORARY);

        /**
         * Key: 消息的来源是否是匿名的。
         */
        String KEY_SOURCE_ANONYMOUS = "messaging.source.anonymous";

        /**
         * Tag: 消息的来源是否是匿名的。
         */
        TagKey.BooleanKey TAG_SOURCE_ANONYMOUS = TagKeys.ofBoolean(KEY_SOURCE_ANONYMOUS);

        /**
         * Key: 消息系统的名称。
         */
        String KEY_SYSTEM = "messaging.system";

        /**
         * Tag: 消息系统的名称。
         */
        TagKey.StringKey TAG_SYSTEM = TagKeys.ofString(KEY_SYSTEM);

        /**
         * Key: 消息操作的名称。
         */
        String KEY_OPERATION = "messaging.operation";

        /**
         * Tag: 消息操作的名称。
         */
        TagKey.StringKey TAG_OPERATION = TagKeys.ofString(KEY_OPERATION);

        /**
         * 消息操作的定义。
         */
        interface Operations {
            /**
             * publish。
             */
            String PUBLISH = "publish";

            /**
             * receive。
             */
            String RECEIVE = "receive";

            /**
             * process。
             */
            String PROCESS = "process";
        }

        /**
         * Key: 消息消费者的 ID。
         */
        String KEY_CONSUMER_ID = "messaging.consumer.id";

        /**
         * Tag: 消息消费者的 ID。
         */
        TagKey.StringKey TAG_CONSUMER_ID = TagKeys.ofString(KEY_CONSUMER_ID);

        /**
         * Kafka 消息相关信息。
         */
        interface Kafka {
            /**
             * Key: Kafka 消息的 Key。
             */
            String KEY_MESSAGE_KEY = "messaging.kafka.message.key";

            /**
             * Tag: Kafka 消息的 Key。
             */
            TagKey.StringKey TAG_MESSAGE_KEY = TagKeys.ofString(KEY_MESSAGE_KEY);

            /**
             * Key: Kafka 消息消费者的 Group。
             */
            String KEY_CONSUMER_GROUP = "messaging.kafka.consumer.group";

            /**
             * Tag: Kafka 消息消费者的 Group。
             */
            TagKey.StringKey TAG_CONSUMER_GROUP = TagKeys.ofString(KEY_CONSUMER_GROUP);

            /**
             * Key: Kafka 客户端的 ID。
             */
            String KEY_CLIENT_ID = "messaging.kafka.client_id";

            /**
             * Tag: Kafka 客户端的 ID。
             */
            TagKey.StringKey TAG_CLIENT_ID = TagKeys.ofString(KEY_CLIENT_ID);

            /**
             * Key: Kafka 消息的目标分区。
             */
            String KEY_DESTINATION_PARTITION = "messaging.kafka.destination.partition";

            /**
             * Tag: Kafka 消息的目标分区。
             */
            TagKey.LongKey TAG_DESTINATION_PARTITION = TagKeys.ofLong(KEY_DESTINATION_PARTITION);

            /**
             * Key: Kafka 消息的来源分区。
             */
            String KEY_SOURCE_PARTITION = "messaging.kafka.source.partition";

            /**
             * Tag: Kafka 消息的来源分区。
             */
            TagKey.LongKey TAG_SOURCE_PARTITION = TagKeys.ofLong(KEY_SOURCE_PARTITION);

            /**
             * Key: Kafka 消息的偏移量。
             */
            String KEY_MESSAGE_OFFSET = "messaging.kafka.message.offset";

            /**
             * Tag: Kafka 消息的偏移量。
             */
            TagKey.LongKey TAG_MESSAGE_OFFSET = TagKeys.ofLong(KEY_MESSAGE_OFFSET);

            /**
             * Key: 是否是 Kafka 墓碑消息。
             */
            String KEY_MESSAGE_TOMBSTONE = "messaging.kafka.message.tombstone";

            /**
             * Tag: 是否是 Kafka 墓碑消息。
             */
            TagKey.BooleanKey TAG_MESSAGE_TOMBSTONE = TagKeys.ofBoolean(KEY_MESSAGE_TOMBSTONE);
        }

        /**
         * RabbitMQ 消息相关信息。
         */
        interface RabbitMQ {
            /**
             * Key: RabbitMQ 消息的目标路由的 Key。
             */
            String KEY_DESTINATION_ROUTING_KEY = "messaging.rabbitmq.destination.routing_key";

            /**
             * Tag: RabbitMQ 消息的目标路由的 Key。
             */
            TagKey.StringKey TAG_DESTINATION_ROUTING_KEY = TagKeys.ofString(KEY_DESTINATION_ROUTING_KEY);
        }

        /**
         * RocketMQ 消息相关信息。
         */
        interface RocketMQ {
            /**
             * Key: RocketMQ 的命名空间。
             */
            String KEY_NAMESPACE = "messaging.rocketmq.namespace";

            /**
             * Tag: RocketMQ 的命名空间。
             */
            TagKey.StringKey TAG_NAMESPACE = TagKeys.ofString(KEY_NAMESPACE);

            /**
             * Key: RocketMQ 的客户端分组。
             */
            String KEY_CLIENT_GROUP = "messaging.rocketmq.client_group";

            /**
             * Tag: RocketMQ 的客户端分组。
             */
            TagKey.StringKey TAG_CLIENT_GROUP = TagKeys.ofString(KEY_CLIENT_GROUP);

            /**
             * Key: RocketMQ 的客户端 ID。
             */
            String KEY_CLIENT_ID = "messaging.rocketmq.client_id";

            /**
             * Tag: RocketMQ 的客户端 ID。
             */
            TagKey.StringKey TAG_CLIENT_ID = TagKeys.ofString(KEY_CLIENT_ID);

            /**
             * Key: RocketMQ 消息投递的时间戳。
             */
            String KEY_MESSAGE_DELIVERY_TIMESTAMP = "messaging.rocketmq.message.delivery_timestamp";

            /**
             * Tag: RocketMQ 消息投递的时间戳。
             */
            TagKey.LongKey TAG_MESSAGE_DELIVERY_TIMESTAMP = TagKeys.ofLong(KEY_MESSAGE_DELIVERY_TIMESTAMP);

            /**
             * Key: RocketMQ 消息的延迟级别。
             */
            String KEY_MESSAGE_DELAY_TIME_LEVEL = "messaging.rocketmq.message.delay_time_level";

            /**
             * Tag: RocketMQ 消息的延迟级别。
             */
            TagKey.LongKey TAG_MESSAGE_DELAY_TIME_LEVEL = TagKeys.ofLong(KEY_MESSAGE_DELAY_TIME_LEVEL);

            /**
             * Key: RocketMQ 消息的分组。
             */
            String KEY_MESSAGE_GROUP = "messaging.rocketmq.message.group";

            /**
             * Tag: RocketMQ 消息的分组。
             */
            TagKey.StringKey TAG_MESSAGE_GROUP = TagKeys.ofString(KEY_MESSAGE_GROUP);

            /**
             * Key: RocketMQ 消息的类型。
             */
            String KEY_MESSAGE_TYPE = "messaging.rocketmq.message.type";

            /**
             * Tag: RocketMQ 消息的分组。
             */
            TagKey.StringKey TAG_MESSAGE_TYPE = TagKeys.ofString(KEY_MESSAGE_TYPE);

            /**
             * Key: RocketMQ 消息的 Tag。
             */
            String KEY_MESSAGE_TAG = "messaging.rocketmq.message.tag";

            /**
             * Tag: RocketMQ 消息的 Tag。
             */
            TagKey.StringKey TAG_MESSAGE_TAG = TagKeys.ofString(KEY_MESSAGE_TAG);

            /**
             * Key: RocketMQ 消息的 Key。
             */
            String KEY_MESSAGE_KEYS = "messaging.rocketmq.message.keys";

            /**
             * Tag: RocketMQ 消息的 Key。
             */
            TagKey.StringArrayKey KEYS_MESSAGE_KEYS = TagKeys.ofStringArray(KEY_MESSAGE_KEYS);

            /**
             * Key: RocketMQ 的消费模式。
             */
            String KEY_CONSUMPTION_MODEL = "messaging.rocketmq.consumption_model";

            /**
             * Tag: RocketMQ 的消费模式。
             */
            TagKey.StringKey TAG_CONSUMPTION_MODEL = TagKeys.ofString(KEY_CONSUMPTION_MODEL);
        }
    }

    /**
     * RPC 相关信息。
     */
    interface Rpc {
        /**
         * Key: RPC 系统名称。
         */
        String KEY_SYSTEM = "rpc.system";

        /**
         * Tag: RPC 系统名称。
         */
        TagKey.StringKey TAG_SYSTEM = TagKeys.ofString(KEY_SYSTEM);

        /**
         * RPC 系统定义。
         */
        interface Systems {
            /**
             * gRPC。
             */
            String GRPC = "grpc";

            /**
             * Java RMI。
             */
            String JAVA_RMI = "java_rmi";

            /**
             * .NET WCF。
             */
            String DOTNET_WCF = "dotnet_wcf";

            /**
             * Apache Dubbo。
             */
            String APACHE_DUBBO = "apache_dubbo";

            /**
             * Connect RPC。
             */
            String CONNECT_RPC = "connect_rpc";
        }

        /**
         * Key: 被调用服务的完整（逻辑）名称，包括其包名称（如果适用的话）。
         */
        String KEY_SERVICE = "rpc.service";

        /**
         * Tag: 被调用服务的完整（逻辑）名称，包括其包名称（如果适用的话）。
         */
        TagKey.StringKey TAG_SERVICE = TagKeys.ofString(KEY_SERVICE);

        /**
         * Key: 被调用的（逻辑）方法的名称，必须等于 span 中的 $method 部分。
         */
        String KEY_METHOD = "rpc.method";

        /**
         * Tag: 被调用的（逻辑）方法的名称，必须等于 span 中的 $method 部分。
         */
        TagKey.StringKey TAG_METHOD = TagKeys.ofString(KEY_METHOD);
    }

    /**
     * 消息（内容）相关信息。
     */
    interface Message {
        /**
         * Key: 消息的类型。
         */
        String KEY_TYPE = "message.type";

        /**
         * Tag: 消息的类型。
         */
        TagKey.StringKey TAG_TYPE = TagKeys.ofString(KEY_TYPE);

        /**
         * 消息类型的定义。
         */
        interface Types {
            /**
             * sent。
             */
            String SENT = "SENT";

            /**
             * received。
             */
            String RECEIVED = "RECEIVED";
        }

        /**
         * Key: 消息的ID。
         */
        String KEY_ID = "message.id";

        /**
         * Tag: 消息的ID。
         */
        TagKey.StringKey TAG_ID = TagKeys.ofString(KEY_ID);

        /**
         * Key: 消息的未压缩的大小。
         */
        String KEY_UNCOMPRESSED_SIZE = "message.uncompressed_size";

        /**
         * Tag: 消息的未压缩的大小。
         */
        TagKey.StringKey TAG_UNCOMPRESSED_SIZE = TagKeys.ofString(KEY_UNCOMPRESSED_SIZE);
    }
}
