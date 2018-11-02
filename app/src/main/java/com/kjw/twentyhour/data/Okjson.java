
package com.kjw.twentyhour.data;

import java.util.HashMap;
import java.util.Map;

public class Okjson {

    private Result result;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
