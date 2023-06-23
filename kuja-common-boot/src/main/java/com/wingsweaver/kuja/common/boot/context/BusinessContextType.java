package com.wingsweaver.kuja.common.boot.context;

/**
 * 业务上下文的内置类型。
 *
 * @author wingsweaver
 */
public interface BusinessContextType {
    /**
     * 阻塞式。
     */
    interface Blocked {
    }

    /**
     * 非阻塞式。
     */
    interface Reactive {
    }

    /**
     * J2EE 标准。
     */
    @SuppressWarnings("PMD.ClassNamingShouldBeCamelRule")
    interface J2EE {
    }

    /**
     * Jakarta EE 标准。
     */
    @SuppressWarnings("PMD.ClassNamingShouldBeCamelRule")
    interface JakartaEE {
    }

    /**
     * Web 应用中的业务上下文。
     */
    interface Web extends BusinessContextType {
        /**
         * 其他的 Web 应用。
         */
        Web WEB = new Web() {
            @Override
            public String toString() {
                return "Web";
            }
        };

        /**
         * 阻塞式 Web 应用中的业务上下文。
         */
        interface Blocked extends Web, BusinessContextType.Blocked {
            /**
             * Web Servlet。
             */
            Web.Blocked SERVLET = new Web.Blocked() {
                @Override
                public String toString() {
                    return "Web.Blocked.Servlet";
                }
            };

            /**
             * J2EE Servlet。
             */
            @SuppressWarnings("PMD.ClassNamingShouldBeCamelRule")
            interface J2EE extends Web.Blocked, BusinessContextType.J2EE {
                /**
                 * Web Servlet。
                 */
                Web.Blocked.J2EE SERVLET = new Web.Blocked.J2EE() {
                    @Override
                    public String toString() {
                        return "Web.Blocked.Servlet.J2EE";
                    }
                };
            }

            /**
             * JakartaEE Servlet。
             */
            @SuppressWarnings("PMD.ClassNamingShouldBeCamelRule")
            interface JakartaEE extends Web.Blocked, BusinessContextType.JakartaEE {
                /**
                 * Web Servlet。
                 */
                Web.Blocked.JakartaEE SERVLET = new Web.Blocked.JakartaEE() {
                    @Override
                    public String toString() {
                        return "Web.Blocked.Servlet.JakartaEE";
                    }
                };
            }
        }

        /**
         * 响应式 Web 应用中的业务上下文。
         */
        interface Reactive extends Web, BusinessContextType.Reactive {
            /**
             * WebFlux。
             */
            Web.Reactive WEB_FLUX = new Web.Reactive() {
                @Override
                public String toString() {
                    return "Web.Reactive.WebFlux";
                }
            };
        }
    }

    /**
     * 消息处理中的业务上下文。
     */
    interface MessagingContext extends BusinessContextType {
        /**
         * 消息处理。
         */
        MessagingContext MESSAGING = new MessagingContext() {
            @Override
            public String toString() {
                return "Messaging";
            }
        };

        /**
         * JMS 标准。
         */
        @SuppressWarnings({"AbbreviationAsWordInName", "PMD.ClassNamingShouldBeCamelRule"})
        interface JMS extends MessagingContext {
            /**
             * JMS。
             */
            JMS JMS = new JMS() {
                @Override
                public String toString() {
                    return "Messaging.JMS";
                }
            };

            /**
             * ACTIVEMQ。
             */
            JMS ACTIVEMQ = new JMS() {
                @Override
                public String toString() {
                    return "Messaging.JMS.ActiveMQ";
                }
            };
        }

        /**
         * AMQP 标准。
         */
        @SuppressWarnings({"AbbreviationAsWordInName", "PMD.ClassNamingShouldBeCamelRule"})
        interface AMQP extends MessagingContext {
            /**
             * AMQP。
             */
            AMQP AMQP = new AMQP() {
                @Override
                public String toString() {
                    return "Messaging.AMQP";
                }
            };

            /**
             * RabbitMQ。
             */
            AMQP RABBITMQ = new AMQP() {
                @Override
                public String toString() {
                    return "Messaging.AMQP.RabbitMQ";
                }
            };
        }

        /**
         * Kafka。
         */
        MessagingContext KAFKA = new MessagingContext() {
            @Override
            public String toString() {
                return "Messaging.Kafka";
            }
        };

        /**
         * RocketMQ。
         */
        MessagingContext ROCKETMQ = new MessagingContext() {
            @Override
            public String toString() {
                return "Messaging.RocketMQ";
            }
        };

        /**
         * Apache Pulsar。
         */
        MessagingContext PULSAR = new MessagingContext() {
            @Override
            public String toString() {
                return "Messaging.Pulsar";
            }
        };
    }

    /**
     * 其他类型的业务上下文。
     */
    BusinessContextType OTHER = new BusinessContextType() {
        @Override
        public String toString() {
            return "Other";
        }
    };
}
