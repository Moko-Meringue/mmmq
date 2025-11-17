package org.mmmq.subscriber;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ThreadPoolExecutor;

class MessageListener implements ApplicationListener<MMMQEvent> {

    final String topic;
    final Object bean;
    final Method method;
    final JavaType javaType;
    final ObjectMapper objectMapper;
    final ThreadPoolExecutor executor;

    public MessageListener(
            String topic,
            Object bean,
            Method method,
            JavaType javaType,
            ObjectMapper objectMapper,
            ThreadPoolExecutor executor
    ) {
        this.topic = topic;
        this.bean = bean;
        this.method = method;
        this.javaType = javaType;
        this.objectMapper = objectMapper;
        this.executor = executor;
    }

    @Override
    public void onApplicationEvent(MMMQEvent event) {
        if (!topic.equals(event.message.topic())) {
            return;
        }
        executor.execute(() -> invokeListenerMethod(bean, method, javaType, event));
    }

    private void invokeListenerMethod(Object bean, Method method, JavaType javaType, MMMQEvent mmmqEvent) {
        try {
            Object dto = objectMapper.convertValue(mmmqEvent.message.content(), javaType);
            method.invoke(bean, dto);
        } catch (IllegalArgumentException e) {
            throw new MessageConversionException(
                    String.format(
                            "Failed to convert Message topic: %s, expected type: %s",
                            mmmqEvent.message.topic(),
                            javaType.getRawClass().getName()
                    ), e
            );
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            throw new ListenerExecutionException(
                    "Failed to invoke listener method" + ": " + cause.getMessage(), cause);
        } catch (Exception e) {
            throw new ListenerExecutionException(
                    "Unexpected error during listener method invocation: " + e.getMessage(), e
            );
        }
    }
}
