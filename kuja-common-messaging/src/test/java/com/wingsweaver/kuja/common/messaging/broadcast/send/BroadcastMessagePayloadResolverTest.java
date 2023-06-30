package com.wingsweaver.kuja.common.messaging.broadcast.send;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.appinfo.matcher.AppInfoValueEqualsMatcherSpec;
import com.wingsweaver.kuja.common.boot.appinfo.matcher.CompositeAppInfoMatcherSpec;
import com.wingsweaver.kuja.common.boot.appinfo.matcher.CompositeMatchMode;
import com.wingsweaver.kuja.common.boot.appinfo.matcher.HasAppInfoValueMatcherSpec;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.send.SenderInfoResolverProperties;
import com.wingsweaver.kuja.common.messaging.broadcast.common.BroadcastPayload;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.BroadcastNotification;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.BroadcastNotificationConverter;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.DefaultBroadcastNotificationDispatcher;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.DefaultBroadcastNotificationPredicate;
import com.wingsweaver.kuja.common.messaging.common.DefaultSenderInfoResolver;
import com.wingsweaver.kuja.common.messaging.core.send.MessageSendContext;
import com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes;
import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.model.tags.Tags;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.idgen.StringIdGenerator;
import com.wingsweaver.kuja.common.utils.support.idgen.UuidStringIdGenerator;
import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BroadcastMessagePayloadResolverTest {
    @Test
    void test() throws Exception {
        Map<String, Object> map = MapUtil.from(
                ResourceAttributes.Host.KEY_NAME, "test-server",
                ResourceAttributes.Process.KEY_PID, 123456,
                ResourceAttributes.Service.KEY_NAME, "test-service"
        );
        AppInfo appInfo = AppInfo.of(map);

        DefaultSenderInfoResolver senderInfoResolver = new DefaultSenderInfoResolver();
        senderInfoResolver.setAppInfo(appInfo);
        senderInfoResolver.setExportItems(SenderInfoResolverProperties.DEFAULT_EXPORT_ITEMS);

        StringIdGenerator idGenerator = new UuidStringIdGenerator();
        BroadcastMessagePayloadResolver payloadResolver = new BroadcastMessagePayloadResolver();
        payloadResolver.setIdGenerator(idGenerator);
        payloadResolver.setSenderInfoResolver(senderInfoResolver);
        payloadResolver.afterPropertiesSet();

        DefaultBroadcastNotificationPredicate notificationPredicate = new DefaultBroadcastNotificationPredicate();
        notificationPredicate.setAppInfo(appInfo);

        DefaultBroadcastNotificationDispatcher notificationDispatcher = new DefaultBroadcastNotificationDispatcher();
        notificationDispatcher.setNotificationPredicate(notificationPredicate);
        notificationDispatcher.setHandlers(CollectionUtil.listOf(
                notification -> System.out.println("received notification = " + notification)
        ));

        {
            BroadcastMessage message = new BroadcastMessage();
            message.setId(idGenerator.nextId());
            message.setMessageType("test-message");
            Map<TagKey<?>, Object> tagsMap = MapUtil.from(ResourceAttributes.OS.TAG_NAME, "some-os");
            message.setTags(Tags.of(tagsMap));

            MessageSendContext context = new MessageSendContext();
            context.setOriginalMessage(message);
            Tuple2<Boolean, Object> tuple2 = payloadResolver.resolvePayload(context);
            assertTrue(tuple2.getT1());
            assertNotNull(tuple2.getT2());

            BroadcastPayload payload = (BroadcastPayload) tuple2.getT2();
            System.out.println("payload = " + payload);

            BroadcastNotification notification = BroadcastNotificationConverter.from(payload);
            System.out.println("resolved notification = " + notification);
            assertNotNull(notification.getContent());

            notificationDispatcher.dispatch(notification);
        }
    }

    @Test
    void test2() throws Exception {
        Map<String, Object> map = MapUtil.from(
                ResourceAttributes.Host.KEY_NAME, "test-server",
                ResourceAttributes.Process.KEY_PID, 123456,
                ResourceAttributes.Service.KEY_NAME, "test-service"
        );
        AppInfo appInfo = AppInfo.of(map);

        DefaultSenderInfoResolver senderInfoResolver = new DefaultSenderInfoResolver();
        senderInfoResolver.setAppInfo(appInfo);
        senderInfoResolver.setExportItems(SenderInfoResolverProperties.DEFAULT_EXPORT_ITEMS);

        StringIdGenerator idGenerator = new UuidStringIdGenerator();
        BroadcastMessagePayloadResolver payloadResolver = new BroadcastMessagePayloadResolver();
        payloadResolver.setIdGenerator(idGenerator);
        payloadResolver.setSenderInfoResolver(senderInfoResolver);
        payloadResolver.afterPropertiesSet();

        DefaultBroadcastNotificationPredicate notificationPredicate = new DefaultBroadcastNotificationPredicate();
        notificationPredicate.setAppInfo(appInfo);

        DefaultBroadcastNotificationDispatcher notificationDispatcher = new DefaultBroadcastNotificationDispatcher();
        notificationDispatcher.setNotificationPredicate(notificationPredicate);
        notificationDispatcher.setHandlers(CollectionUtil.listOf(
                notification -> System.out.println("received notification = " + notification)
        ));

        {
            String content = "test-message-2:" + UUID.randomUUID();

            BroadcastMessageT<String> message = new BroadcastMessageT<>();
            message.setId(idGenerator.nextId());
            message.setMessageType("test-message-2");
            message.setContent(content);

            AppInfoValueEqualsMatcherSpec subSpec1 = new AppInfoValueEqualsMatcherSpec();
            subSpec1.setKey(ResourceAttributes.Service.KEY_NAME);
            subSpec1.setTarget("some-service");

            HasAppInfoValueMatcherSpec subSpec2 = new HasAppInfoValueMatcherSpec();
            subSpec2.setKey(ResourceAttributes.Host.KEY_NAME);

            CompositeAppInfoMatcherSpec target = new CompositeAppInfoMatcherSpec();
            target.setMode(CompositeMatchMode.ANY);
            target.setMatchers(CollectionUtil.listOf(subSpec1, subSpec2));
            message.setTarget(target);

            MessageSendContext context = new MessageSendContext();
            context.setOriginalMessage(message);
            Tuple2<Boolean, Object> tuple2 = payloadResolver.resolvePayload(context);
            assertTrue(tuple2.getT1());
            assertNotNull(tuple2.getT2());

            BroadcastPayload payload = (BroadcastPayload) tuple2.getT2();
            System.out.println("payload = " + payload);

            BroadcastNotification notification = BroadcastNotificationConverter.from(payload);
            System.out.println("resolved notification = " + notification);
            assertEquals(content, notification.getContent().original());

            notificationDispatcher.dispatch(notification);
        }
    }
}