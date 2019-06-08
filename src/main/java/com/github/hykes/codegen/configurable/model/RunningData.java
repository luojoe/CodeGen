package com.github.hykes.codegen.configurable.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于存上一次运行的数据 model
 *
 * @author sh.luo
 * @date 2019/06/08
 */
public class RunningData implements Serializable {
    private static final long serialVersionUID = 8791352048133834684L;

    private Map<String, String> runningData = new HashMap<>();

    public String getRunningData(String key) {
        return this.runningData.get(key);
    }

    public void setRunningData(String key, String value) {
        this.runningData.put(key, value);
    }

    public void setRunningData(Map<String, String> runningData) {
        this.runningData = runningData;
    }

    public Map<String, String> getRunningData() {
        return this.runningData;
    }
}
