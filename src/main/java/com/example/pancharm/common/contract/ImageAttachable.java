package com.example.pancharm.common.contract;

import java.util.Set;

public interface ImageAttachable<T> {
    int getId();

    void setImages(Set<T> images);
}
