package com.wingsweaver.kuja.common.utils.model.semconv;

import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.model.tags.TagKeys;

/**
 * 资源类型属性的定义。<br>
 * 参照 OTEL SemConv v1.26.0-alpha 实现。<br>
 * 资源类型的属性包括：云平台信息、容器信息、设备信息、操作系统信息等,
 * 也就是在应用程序（或者服务）运行过程中不变的数据。
 *
 * @author wingsweaver
 */
@SuppressWarnings("PMD.ClassNamingShouldBeCamelRule")
public interface ResourceAttributes {
    /**
     * SCHEMA URL。
     */
    String SCHEMA_URL = "https://opentelemetry.io/schemas/1.19.0";

    /**
     * 版本。
     */
    String VERSION = "1.26.0-alpha";

    /**
     * 浏览器相关信息。
     */
    interface Browser {
        /**
         * KEY: 浏览器品牌名称和版本。<br>
         * 从 User-Agent 字段中解析。
         */
        String KEY_BRANDS = "browser.brands";

        /**
         * TAG: 浏览器品牌名称和版本。<br>
         * 从 User-Agent 字段中解析。
         */
        TagKey.StringArrayKey TAG_BRANDS = TagKeys.ofStringArray(KEY_BRANDS);

        /**
         * KEY: 浏览器的平台。<br>
         * 从 navigator.userAgentData.platform 获取，
         * 如果不存在的从 navigator.platform 获取。
         */
        String KEY_PLATFORM = "browser.platform";

        /**
         * TAG: 浏览器的平台。
         */
        TagKey.StringKey TAG_PLATFORM = TagKeys.ofString(KEY_PLATFORM);

        /**
         * KEY: 浏览器是否运行在移动设备上。<br>
         * 从 User-Agent 中获取。
         */
        String KEY_MOBILE = "browser.mobile";

        /**
         * TAG: 浏览器是否运行在移动设备上。
         */
        TagKey.BooleanKey TAG_MOBILE = TagKeys.ofBoolean(KEY_MOBILE);

        /**
         * KEY: 浏览器的语言。<br>
         * 从 navigator.language 中获取。
         */
        String KEY_LANGUAGE = "browser.language";

        /**
         * TAG: 浏览器的语言。
         */
        TagKey.StringKey TAG_LANGUAGE = TagKeys.ofString(KEY_LANGUAGE);
    }

    /**
     * 云平台相关信息。
     */
    interface Cloud {
        /**
         * Key: 云服务商的名称。
         */
        String KEY_PROVIDER = "cloud.provider";

        /**
         * Tag: 云服务商的名称。
         */
        TagKey.StringKey TAG_PROVIDER = TagKeys.ofString(KEY_PROVIDER);

        /**
         * 云服务商的名称的列表。
         */
        interface Providers {
            /**
             * Alibaba Cloud。
             */
            String ALIBABA_CLOUD = "alibaba_cloud";

            /**
             * Amazon Web Services。
             */
            String AWS = "aws";

            /**
             * Microsoft Azure。
             */
            String AZURE = "azure";

            /**
             * Google Cloud Platform。
             */
            String GCP = "gcp";

            /**
             * Heroku Platform as a Service。
             */
            String HEROKU = "heroku";

            /**
             * IBM Cloud。
             */
            String IBM_CLOUD = "ibm_cloud";

            /**
             * Tencent Cloud。
             */
            String TENCENT_CLOUD = "tencent_cloud";
        }

        /**
         * Key: 云账户的 ID。
         */
        String KEY_ACCOUNT_ID = "cloud.account.id";

        /**
         * Tag: 云账户的 ID。
         */
        TagKey.StringKey TAG_ACCOUNT_ID = TagKeys.ofString(KEY_ACCOUNT_ID);

        /**
         * Key: 云服务所在的区域。
         */
        String KEY_REGION = "cloud.region";

        /**
         * Tag: 云服务所在的区域。
         */
        TagKey.StringKey TAG_REGION = TagKeys.ofString(KEY_REGION);

        /**
         * Key: 云资源的 ID。
         */
        String KEY_RESOURCE_ID = "cloud.resource_id";

        /**
         * Tag: 云资源的 ID。
         */
        TagKey.StringKey TAG_RESOURCE_ID = TagKeys.ofString(KEY_RESOURCE_ID);

        /**
         * Key: 云服务所在的可用区。
         */
        String KEY_AVAILABILITY_ZONE = "cloud.availability_zone";

        /**
         * Tag: 云服务所在的可用区。
         */
        TagKey.StringKey TAG_AVAILABILITY_ZONE = TagKeys.ofString(KEY_AVAILABILITY_ZONE);

        /**
         * Key: 使用的云平台。<br>
         * 对应数据的前缀应该跟 {@link #KEY_PROVIDER} 对应数据一致。
         */
        String KEY_PLATFORM = "cloud.platform";

        /**
         * Tag: 使用的云平台。
         */
        TagKey.StringKey TAG_PLATFORM = TagKeys.ofString(KEY_PLATFORM);

        /**
         * 云平台的类型定义。
         */
        interface Platforms {
            /**
             * Alibaba Cloud Elastic Compute Service。
             */
            String ALIBABA_CLOUD_ECS = "alibaba_cloud_ecs";

            /**
             * Alibaba Cloud Function Compute。
             */
            String ALIBABA_CLOUD_FC = "alibaba_cloud_fc";

            /**
             * Red Hat OpenShift on Alibaba Cloud。
             */
            String ALIBABA_CLOUD_OPENSHIFT = "alibaba_cloud_openshift";

            /**
             * AWS Elastic Compute Cloud。
             */
            String AWS_EC2 = "aws_ec2";

            /**
             * AWS Elastic Container Service。
             */
            String AWS_ECS = "aws_ecs";

            /**
             * AWS Elastic Kubernetes Service。
             */
            String AWS_EKS = "aws_eks";

            /**
             * AWS Lambda。
             */
            String AWS_LAMBDA = "aws_lambda";

            /**
             * AWS Elastic Beanstalk。
             */
            String AWS_ELASTIC_BEANSTALK = "aws_elastic_beanstalk";

            /**
             * AWS App Runner。
             */
            String AWS_APP_RUNNER = "aws_app_runner";

            /**
             * Red Hat OpenShift on AWS (ROSA)。
             */
            String AWS_OPENSHIFT = "aws_openshift";

            /**
             * Azure Virtual Machines。
             */
            String AZURE_VM = "azure_vm";

            /**
             * Azure Container Instances。
             */
            String AZURE_CONTAINER_INSTANCES = "azure_container_instances";

            /**
             * Azure Kubernetes Service。
             */
            String AZURE_AKS = "azure_aks";

            /**
             * Azure Functions。
             */
            String AZURE_FUNCTIONS = "azure_functions";

            /**
             * Azure App Service。
             */
            String AZURE_APP_SERVICE = "azure_app_service";

            /**
             * Azure Red Hat OpenShift。
             */
            String AZURE_OPENSHIFT = "azure_openshift";

            /**
             * Google Cloud Compute Engine (GCE)。
             */
            String GCP_COMPUTE_ENGINE = "gcp_compute_engine";

            /**
             * Google Cloud Run。
             */
            String GCP_CLOUD_RUN = "gcp_cloud_run";

            /**
             * Google Cloud Kubernetes Engine (GKE)。
             */
            String GCP_KUBERNETES_ENGINE = "gcp_kubernetes_engine";

            /**
             * Google Cloud Functions (GCF)。
             */
            String GCP_CLOUD_FUNCTIONS = "gcp_cloud_functions";

            /**
             * Google Cloud App Engine (GAE)。
             */
            String GCP_APP_ENGINE = "gcp_app_engine";

            /**
             * Red Hat OpenShift on Google Cloud。
             */
            String GCP_OPENSHIFT = "gcp_openshift";

            /**
             * Red Hat OpenShift on IBM Cloud。
             */
            String IBM_CLOUD_OPENSHIFT = "ibm_cloud_openshift";

            /**
             * Tencent Cloud Cloud Virtual Machine (CVM)。
             */
            String TENCENT_CLOUD_CVM = "tencent_cloud_cvm";

            /**
             * Tencent Cloud Elastic Kubernetes Service (EKS)。
             */
            String TENCENT_CLOUD_EKS = "tencent_cloud_eks";

            /**
             * Tencent Cloud Serverless Cloud Function (SCF)。
             */
            String TENCENT_CLOUD_SCF = "tencent_cloud_scf";
        }
    }

    /**
     * 容器相关信息。
     */
    interface Container {
        /**
         * Key: 容器名称。
         */
        String KEY_NAME = "container.name";

        /**
         * Tag: 容器名称。
         */
        TagKey.StringKey TAG_NAME = TagKeys.ofString(KEY_NAME);

        /**
         * Key: 容器 ID。
         */
        String KEY_ID = "container.id";

        /**
         * Key: 容器 ID。
         */
        TagKey.StringKey TAG_ID = TagKeys.ofString(KEY_ID);

        /**
         * Key: 容器运行时。
         */
        String KEY_RUNTIME = "container.runtime";

        /**
         * Tag: 容器运行时。
         */
        TagKey.StringKey TAG_RUNTIME = TagKeys.ofString(KEY_RUNTIME);

        /**
         * Key: 容器镜像的名称。
         */
        String KEY_IMAGE_NAME = "container.image.name";

        /**
         * Tag: 容器镜像的名称。
         */
        TagKey.StringKey TAG_IMAGE_NAME = TagKeys.ofString(KEY_IMAGE_NAME);

        /**
         * Key: 容器镜像的版本。
         */
        String KEY_IMAGE_TAG = "container.image.tag";

        /**
         * Tag: 容器镜像的版本。
         */
        TagKey.StringKey TAG_IMAGE_TAG = TagKeys.ofString(KEY_IMAGE_TAG);
    }

    /**
     * 发布相关信息。
     */
    interface Deployment {
        /**
         * Key: 发布环境。<br>
         * 参照 <a href="https://en.wikipedia.org/wiki/environment">发布环境</a>。
         */
        String KEY_ENVIRONMENT = "deployment.environment";

        /**
         * Tag: 发布环境。
         */
        TagKey.StringKey TAG_ENVIRONMENT = TagKeys.ofString(KEY_ENVIRONMENT);
    }

    /**
     * 设备相关信息。
     */
    interface Device {
        /**
         * Key: 设备的唯一标识。
         */
        String KEY_ID = "device.id";

        /**
         * Tag: 设备的唯一标识。
         */
        TagKey.StringKey TAG_ID = TagKeys.ofString(KEY_ID);

        /**
         * Key: 设备机型的标识。
         */
        String KEY_MODEL_IDENTIFIER = "device.model.identifier";

        /**
         * Tag: 设备机型的标识。
         */
        TagKey.StringKey TAG_MODEL_IDENTIFIER = TagKeys.ofString(KEY_MODEL_IDENTIFIER);

        /**
         * Key: 设备机型的名称。
         */
        String KEY_MODEL_NAME = "device.model.name";

        /**
         * Key: 设备机型的名称。
         */
        TagKey.StringKey TAG_MODEL_NAME = TagKeys.ofString(KEY_MODEL_NAME);

        /**
         * Key: 设备制造商的名称。
         */
        String KEY_MANUFACTURER = "device.manufacturer";

        /**
         * Tag: 设备制造商的名称。
         */
        TagKey.StringKey TAG_MANUFACTURER = TagKeys.ofString(KEY_MANUFACTURER);
    }

    /**
     * 主机相关信息。
     */
    interface Host {
        /**
         * Key: 主机的唯一标识。<br>
         * 在云环境中，必须使用云服务商分配给主机的实例 ID 作为唯一标识。<br>
         * 在其他环境中，应该使用主机 ID。
         */
        String KEY_ID = "host.id";

        /**
         * Tag: 主机的唯一标识。
         */
        TagKey.StringKey TAG_ID = TagKeys.ofString(KEY_ID);

        /**
         * Key: 主机的名称。
         */
        String KEY_NAME = "host.name";

        /**
         * Tag: 主机的名称。
         */
        TagKey.StringKey TAG_NAME = TagKeys.ofString(KEY_NAME);

        /**
         * Key: 主机的 IP 地址。
         */
        String KEY_ADDRESS = "host.address";

        /**
         * Tag: 主机的 IP 地址。
         */
        TagKey.StringKey TAG_ADDRESS = TagKeys.ofString(KEY_ADDRESS);

        /**
         * Key: 主机的规格型号。
         */
        String KEY_TYPE = "host.type";

        /**
         * Tag: 主机的规格型号。
         */
        TagKey.StringKey TAG_TYPE = TagKeys.ofString(KEY_TYPE);

        /**
         * Key: 主机的架构。
         */
        String KEY_ARCH = "host.arch";

        /**
         * Tag: 主机的架构。
         */
        TagKey.StringKey TAG_ARCH = TagKeys.ofString(KEY_ARCH);

        /**
         * Key: 主机的架构。
         */
        String KEY_PROCESSOR_ARCH = "host.processor.arch";

        /**
         * Tag: 主机的架构。
         */
        TagKey.StringKey TAG_PROCESSOR_ARCH = TagKeys.ofString(KEY_PROCESSOR_ARCH);

        /**
         * Key: 主机的架构。
         */
        String KEY_PROCESSOR_TYPE = "host.processor.type";

        /**
         * Tag: 主机的架构。
         */
        TagKey.StringKey TAG_PROCESSOR_TYPE = TagKeys.ofString(KEY_PROCESSOR_TYPE);

        /**
         * 主机架构的定义。
         */
        interface Archs {
            /**
             * AMD64。
             */
            String AMD64 = "amd64";

            /**
             * ARM32。
             */
            String ARM32 = "arm32";

            /**
             * ARM64。
             */
            String ARM64 = "arm64";

            /**
             * Itanium。
             */
            String IA64 = "ia64";

            /**
             * 32-bit PowerPC。
             */
            String PPC32 = "ppc32";

            /**
             * 64-bit PowerPC。
             */
            String PPC64 = "ppc64";

            /**
             * IBM z/Architecture。
             */
            String S390X = "s390x";

            /**
             * 32-bit x86。
             */
            String X86 = "x86";
        }

        /**
         * Key: 主机的镜像名称。
         */
        String KEY_IMAGE_NAME = "host.image.name";

        /**
         * Tag: 主机的镜像名称。
         */
        TagKey.StringKey TAG_IMAGE_NAME = TagKeys.ofString(KEY_IMAGE_NAME);

        /**
         * Key: 主机的镜像 ID。
         */
        String KEY_IMAGE_ID = "host.image.id";

        /**
         * Tag: 主机的镜像 ID。
         */
        TagKey.StringKey TAG_IMAGE_ID = TagKeys.ofString(KEY_IMAGE_ID);
    }

    /**
     * K8s 相关信息。
     */
    interface K8S {
        /**
         * Key: K8S 的集群名称。
         */
        String KEY_CLUSTER_NAME = "k8s.cluster.name";

        /**
         * Tag: K8S 的集群名称。
         */
        TagKey.StringKey TAG_CLUSTER_NAME = TagKeys.ofString(KEY_CLUSTER_NAME);

        /**
         * Key: K8S 的节点名称。
         */
        String KEY_NODE_NAME = "k8s.node.name";

        /**
         * Tag: K8S 的节点名称。
         */
        TagKey.StringKey TAG_NODE_NAME = TagKeys.ofString(KEY_NODE_NAME);

        /**
         * Key: K8S 的节点 UID。
         */
        String KEY_NODE_UID = "k8s.node.uid";

        /**
         * Tag: K8S 的节点 UID。
         */
        TagKey.StringKey TAG_NODE_UID = TagKeys.ofString(KEY_NODE_UID);

        /**
         * Key: K8S Pod 的命名空间的名称。
         */
        String KEY_NAMESPACE_NAME = "k8s.namespace.name";

        /**
         * Tag: K8S Pod 的命名空间的名称。
         */
        TagKey.StringKey TAG_NAMESPACE_NAME = TagKeys.ofString(KEY_NAMESPACE_NAME);

        /**
         * Key: K8S Pod 的 UID。
         */
        String KEY_POD_UID = "k8s.pod.uid";

        /**
         * Tag: K8S Pod 的 UID。
         */
        TagKey.StringKey TAG_POD_UID = TagKeys.ofString(KEY_POD_UID);

        /**
         * Key: K8S Pod 的名称。
         */
        String KEY_POD_NAME = "k8s.pod.name";

        /**
         * Tag: K8S Pod 的名称。
         */
        TagKey.StringKey TAG_POD_NAME = TagKeys.ofString(KEY_POD_NAME);

        /**
         * Key: K8S Pod 中的运行时名称。
         */
        String KEY_CONTAINER_NAME = "k8s.container.name";

        /**
         * Tag: K8S Pod 中的运行时名称。
         */
        TagKey.StringKey TAG_CONTAINER_NAME = TagKeys.ofString(KEY_CONTAINER_NAME);

        /**
         * Key: K8S Pod 中的运行时的重启次数。
         */
        String KEY_CONTAINER_RESTART_COUNT = "k8s.container.restart_count";

        /**
         * Tag: K8S Pod 中的运行时的重启次数。
         */
        TagKey.LongKey TAG_CONTAINER_RESTART_COUNT = TagKeys.ofLong(KEY_CONTAINER_RESTART_COUNT);

        /**
         * Key: K8S ReplicatSet 的 UID。
         */
        String KEY_REPLICASET_UID = "k8s.replicatset.uid";

        /**
         * Tag: K8S ReplicatSet 的 UID。
         */
        TagKey.StringKey TAG_REPLICASET_UID = TagKeys.ofString(KEY_REPLICASET_UID);

        /**
         * Key: K8S ReplicatSet 的名称。
         */
        String KEY_REPLICASET_NAME = "k8s.replicatset.name";

        /**
         * Tag: K8S ReplicatSet 的名称。
         */
        TagKey.StringKey TAG_REPLICASET_NAME = TagKeys.ofString(KEY_REPLICASET_NAME);

        /**
         * Key: K8S Deployment 的 UID。
         */
        String KEY_DEPLOYMENT_UID = "k8s.deployment.uid";

        /**
         * Tag: K8S Deployment 的 UID。
         */
        TagKey.StringKey TAG_DEPLOYMENT_UID = TagKeys.ofString(KEY_DEPLOYMENT_UID);

        /**
         * Key: K8S Deployment 的名称。
         */
        String KEY_DEPLOYMENT_NAME = "k8s.deployment.name";

        /**
         * Tag: K8S Deployment 的名称。
         */
        TagKey.StringKey TAG_DEPLOYMENT_NAME = TagKeys.ofString(KEY_DEPLOYMENT_NAME);

        /**
         * Key: K8S StatefulSet 的 UID。
         */
        String KEY_STATEFULSET_UID = "k8s.statefulset.uid";

        /**
         * Tag: K8S StatefulSet 的 UID。
         */
        TagKey.StringKey TAG_STATEFULSET_UID = TagKeys.ofString(KEY_STATEFULSET_UID);

        /**
         * Key: K8S StatefulSet 的名称。
         */
        String KEY_STATEFULSET_NAME = "k8s.statefulset.name";

        /**
         * Tag: K8S StatefulSet 的名称。
         */
        TagKey.StringKey TAG_STATEFULSET_NAME = TagKeys.ofString(KEY_STATEFULSET_NAME);

        /**
         * Key: K8S DaemonSet 的 UID。
         */
        String KEY_DAEMONSET_UID = "k8s.daemonset.uid";

        /**
         * Tag: K8S DaemonSet 的 UID。
         */
        TagKey.StringKey TAG_DAEMONSET_UID = TagKeys.ofString(KEY_DAEMONSET_UID);

        /**
         * Key: K8S DaemonSet 的名称。
         */
        String KEY_DAEMONSET_NAME = "k8s.daemonset.name";

        /**
         * Tag: K8S DaemonSet 的名称。
         */
        TagKey.StringKey TAG_DAEMONSET_NAME = TagKeys.ofString(KEY_DAEMONSET_NAME);

        /**
         * Key: K8S Job 的 UID。
         */
        String KEY_JOB_UID = "k8s.job.uid";

        /**
         * Tag: K8S Job 的 UID。
         */
        TagKey.StringKey TAG_JOB_UID = TagKeys.ofString(KEY_JOB_UID);

        /**
         * Key: K8S Job 的名称。
         */
        String KEY_JOB_NAME = "k8s.job.name";

        /**
         * Tag: K8S Job 的名称。
         */
        TagKey.StringKey TAG_JOB_NAME = TagKeys.ofString(KEY_JOB_NAME);

        /**
         * Key: K8S CronJob 的 UID。
         */
        String KEY_CRONJOB_UID = "k8s.cronjob.uid";

        /**
         * Tag: K8S CronJob 的 UID。
         */
        TagKey.StringKey TAG_CRONJOB_UID = TagKeys.ofString(KEY_CRONJOB_UID);

        /**
         * Key: K8S CronJob 的名称。
         */
        String KEY_CRONJOB_NAME = "k8s.cronjob.name";

        /**
         * Tag: K8S CronJob 的名称。
         */
        TagKey.StringKey TAG_CRONJOB_NAME = TagKeys.ofString(KEY_CRONJOB_NAME);
    }

    /**
     * 操作系统相关信息。
     */
    interface OS {
        /**
         * Key: 操作系统的类型。
         */
        String KEY_TYPE = "os.type";

        /**
         * Tag: 操作系统的类型。
         */
        TagKey.StringKey TAG_TYPE = TagKeys.ofString(KEY_TYPE);

        /**
         * 操作系统类型的定义。
         */
        interface Types {
            /**
             * Microsoft Windows。
             */
            String WINDOWS = "windows";

            /**
             * Linux。
             */
            String LINUX = "linux";

            /**
             * Apple Darwin。
             */
            String DARWIN = "darwin";

            /**
             * FreeBSD。
             */
            String FREEBSD = "freebsd";

            /**
             * NetBSD。
             */
            String NETBSD = "netbsd";

            /**
             * OpenBSD。
             */
            String OPENBSD = "openbsd";

            /**
             * DragonFly BSD。
             */
            String DRAGONFLYBSD = "dragonflybsd";

            /**
             * HP-UX (Hewlett Packard Unix)。
             */
            String HPUX = "hpux";

            /**
             * AIX (Advanced Interactive eXecutive)。
             */
            String AIX = "aix";

            /**
             * SunOS, Oracle Solaris。
             */
            String SOLARIS = "solaris";

            /**
             * IBM z/OS。
             */
            String Z_OS = "z_os";

            /**
             * 其他操作系统。
             */
            String OTHERS = "others";
        }

        /**
         * Key: 操作系统的描述。
         */
        String KEY_DESCRIPTION = "os.description";

        /**
         * Tag: 操作系统的描述。
         */
        TagKey.StringKey TAG_DESCRIPTION = TagKeys.ofString(KEY_DESCRIPTION);

        /**
         * Key: 操作系统的名称。
         */
        String KEY_NAME = "os.name";

        /**
         * Tag: 操作系统的名称。
         */
        TagKey.StringKey TAG_NAME = TagKeys.ofString(KEY_NAME);

        /**
         * Key: 操作系统的版本。
         */
        String KEY_VERSION = "os.version";

        /**
         * Tag: 操作系统的版本。
         */
        TagKey.StringKey TAG_VERSION = TagKeys.ofString(KEY_VERSION);
    }

    /**
     * 进程相关信息。
     */
    interface Process {
        /**
         * Key: 进程的 PID。
         */
        String KEY_PID = "process.pid";

        /**
         * Tag: 进程的 PID。
         */
        TagKey.LongKey TAG_PID = TagKeys.ofLong(KEY_PID);

        /**
         * Key: 父进程的 PID。
         */
        String KEY_PARENT_PID = "process.parent_pid";

        /**
         * Tag: 父进程的 PID。
         */
        TagKey.LongKey TAG_PARENT_PID = TagKeys.ofLong(KEY_PARENT_PID);

        /**
         * Key: 可执行文件的名称。<br>
         * 在 Unix/Linux 系统中，可以从文件 /proc/[pid]/status 的 Name 节点获取。
         * 在 Windows 系统中，可以使用 GetProcessImageFileName() 函数获取。
         */
        String KEY_EXECUTABLE_NAME = "process.executable.name";

        /**
         * Tag: 可执行文件的名称。
         */
        TagKey.StringKey TAG_EXECUTABLE_NAME = TagKeys.ofString(KEY_EXECUTABLE_NAME);

        /**
         * Key: 可执行文件的路径。<br>
         * 在 Unix/Linux 系统中，可以从文件 /proc/[pid]/exe 中获取。
         * 在 Windows 系统中，可以使用 GetProcessImageFileName() 函数获取。
         */
        String KEY_EXECUTABLE_PATH = "process.executable.path";

        /**
         * Tag: 可执行文件的路径。
         */
        TagKey.StringKey TAG_EXECUTABLE_PATH = TagKeys.ofString(KEY_EXECUTABLE_PATH);

        /**
         * Key: 进程的启动命令。<br>
         * 在 Unix/Linux 系统中，可以从文件 /proc/[pid]/cmdline 中获取。
         * 在 Windows 系统中，可以使用 GetCommandLine() 函数获取（返回值的第一个参数）。
         */
        String KEY_COMMAND = "process.command";

        /**
         * Tag: 进程的启动命令。
         */
        TagKey.StringKey TAG_COMMAND = TagKeys.ofString(KEY_COMMAND);

        /**
         * Key: 进程的启动命令行。
         */
        String KEY_COMMAND_LINE = "process.command_line";

        /**
         * Tag: 进程的启动命令行。
         */
        TagKey.StringKey TAG_COMMAND_LINE = TagKeys.ofString(KEY_COMMAND_LINE);

        /**
         * Key: 进程的启动命令行参数的数组。
         */
        String KEY_COMMAND_ARGS = "process.command_args";

        /**
         * Tag: 进程的启动命令行参数的数组。
         */
        TagKey.StringArrayKey TAG_COMMAND_ARGS = TagKeys.ofStringArray(KEY_COMMAND_ARGS);

        /**
         * Key: 进程的所有者的名称。
         */
        String KEY_OWNER = "process.owner";

        /**
         * Tag: 进程的所有者的名称。
         */
        TagKey.StringKey TAG_OWNER = TagKeys.ofString(KEY_OWNER);

        /**
         * Key: 进程的运行时的名称。
         */
        String KEY_RUNTIME_NAME = "process.runtime.name";

        /**
         * Tag: 进程的运行时的名称。
         */
        TagKey.StringKey TAG_RUNTIME_NAME = TagKeys.ofString(KEY_RUNTIME_NAME);

        /**
         * Key: 进程的运行时的版本。
         */
        String KEY_RUNTIME_VERSION = "process.runtime.version";

        /**
         * Tag: 进程的运行时的版本。
         */
        TagKey.StringKey TAG_RUNTIME_VERSION = TagKeys.ofString(KEY_RUNTIME_VERSION);

        /**
         * Key: 进程的运行时的描述。
         */
        String KEY_RUNTIME_DESCRIPTION = "process.runtime.description";

        /**
         * Tag: 进程的运行时的描述。
         */
        TagKey.StringKey TAG_RUNTIME_DESCRIPTION = TagKeys.ofString(KEY_RUNTIME_DESCRIPTION);
    }

    /**
     * 服务相关信息。
     */
    interface Service {
        /**
         * Key: 服务的（逻辑）名称。
         */
        String KEY_NAME = "service.name";

        /**
         * Tag: 服务的（逻辑）名称。
         */
        TagKey.StringKey TAG_NAME = TagKeys.ofString(KEY_NAME);

        /**
         * Key: 服务的命名空间。
         */
        String KEY_NAMESPACE = "service.namespace";

        /**
         * Tag: 服务的命名空间。
         */
        TagKey.StringKey TAG_NAMESPACE = TagKeys.ofString(KEY_NAMESPACE);

        /**
         * Key: 服务的所属群组。
         */
        String KEY_GROUP = "service.group";

        /**
         * Tag: 服务的所属群组。
         */
        TagKey.StringKey TAG_GROUP = TagKeys.ofString(KEY_GROUP);

        /**
         * Key: 服务的实例 ID。
         */
        String KEY_INSTANCE_ID = "service.instance.id";

        /**
         * Tag: 服务的实例 ID。
         */
        TagKey.StringKey TAG_INSTANCE_ID = TagKeys.ofString(KEY_INSTANCE_ID);

        /**
         * Key: 服务的版本。
         */
        String KEY_VERSION = "service.version";

        /**
         * Tag: 服务的版本。
         */
        TagKey.StringKey TAG_VERSION = TagKeys.ofString(KEY_VERSION);

        /**
         * Key: 服务的权重。（自定义）
         */
        String KEY_WEIGHT = "service.weight";

        /**
         * Tag: 服务的权重。
         */
        TagKey.LongKey TAG_WEIGHT = TagKeys.ofLong(KEY_WEIGHT);

        /**
         * Key: 服务的蓝绿颜色。（自定义）
         */
        String KEY_COLOR = "service.color";

        /**
         * Tag: 服务的蓝绿颜色。
         */
        TagKey.StringKey TAG_COLOR = TagKeys.ofString(KEY_COLOR);

        /**
         * Key: 服务发现的时间戳。（自定义）
         */
        String KEY_DISCOVERY_TIMESTAMP = "service.discovery.timestamp";

        /**
         * Tag: 服务发现的时间戳。
         */
        TagKey.DateKey TAG_DISCOVERY_TIMESTAMP = TagKeys.ofDate(KEY_DISCOVERY_TIMESTAMP);

        /**
         * Key: 服务发现的签名哈希。（自定义）<br>
         * 用于校验服务数据是否变更。
         */
        String KEY_DISCOVERY_SIGNATURE = "service.discovery.signature";

        /**
         * Tag: 服务发现的签名哈希。
         */
        TagKey.StringKey TAG_DISCOVERY_SIGNATURE = TagKeys.ofString(KEY_DISCOVERY_SIGNATURE);
    }
}
