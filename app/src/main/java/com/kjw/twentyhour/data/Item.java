
package com.kjw.twentyhour.data;

import java.util.HashMap;
import java.util.Map;

public class Item {

    public String address;
    private Addrdetail addrdetail;
    private Boolean isAdmAddress;
    private Boolean isRoadAddress;
    private Point point;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Addrdetail getAddrdetail() {
        return addrdetail;
    }

    public void setAddrdetail(Addrdetail addrdetail) {
        this.addrdetail = addrdetail;
    }

    public Boolean getIsAdmAddress() {
        return isAdmAddress;
    }

    public void setIsAdmAddress(Boolean isAdmAddress) {
        this.isAdmAddress = isAdmAddress;
    }

    public Boolean getIsRoadAddress() {
        return isRoadAddress;
    }

    public void setIsRoadAddress(Boolean isRoadAddress) {
        this.isRoadAddress = isRoadAddress;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
