package com.example.pancharm.common.contract;

public interface ImageEntity<P> {
    void setPath(String path);

    void setParent(P parent);
}
