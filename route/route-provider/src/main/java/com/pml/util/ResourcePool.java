package com.pml.util;

public interface ResourcePool {
    Integer requestResource(ResourceType tunnel);

    public enum ResourceType {
        // 资源类型，可扩展
        TUNNEL, USER
    }
}
