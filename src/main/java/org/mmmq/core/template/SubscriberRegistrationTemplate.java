package org.mmmq.core.template;

public record SubscriberRegistrationTemplate(
        String name,
        String host
) {

    public SubscriberRegistrationTemplate{
        if(name == null){
            throw new IllegalArgumentException("name is null");
        }
        if(host == null){
            throw new IllegalArgumentException("host is null");
        }
    }
}
