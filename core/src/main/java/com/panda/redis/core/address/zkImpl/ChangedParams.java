package com.panda.redis.core.address.zkImpl;

import java.util.List;

public class ChangedParams {

    private String parentPath;

    private List<String> childPath;

    public ChangedParams() {
    }

    public ChangedParams(String parentPath, List<String> childPath) {
        this.parentPath = parentPath;
        this.childPath = childPath;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public List<String> getChildPath() {
        return childPath;
    }

    public void setChildPath(List<String> childPath) {
        this.childPath = childPath;
    }
}
