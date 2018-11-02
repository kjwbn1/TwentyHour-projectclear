
package com.kjw.twentyhour.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {

    private Integer total;
    private String userquery;
    private List<Item> items = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getUserquery() {
        return userquery;
    }

    public void setUserquery(String userquery) {
        this.userquery = userquery;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
