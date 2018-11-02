
package com.kjw.twentyhour.data;

import java.util.HashMap;
import java.util.Map;

public class Addrdetail {

    private String country;
    private String sido;
    private String sigugun;
    private String dongmyun;
    private String ri;
    private String rest;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSido() {
        return sido;
    }

    public void setSido(String sido) {
        this.sido = sido;
    }

    public String getSigugun() {
        return sigugun;
    }

    public void setSigugun(String sigugun) {
        this.sigugun = sigugun;
    }

    public String getDongmyun() {
        return dongmyun;
    }

    public void setDongmyun(String dongmyun) {
        this.dongmyun = dongmyun;
    }

    public String getRi() {
        return ri;
    }

    public void setRi(String ri) {
        this.ri = ri;
    }

    public String getRest() {
        return rest;
    }

    public void setRest(String rest) {
        this.rest = rest;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
