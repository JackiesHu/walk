package com.buxingzhe.pedestrian.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/23.
 */
public class AddreSuggData implements Serializable {
    public String name;
    public AddressSuggLoca location;
    public String uid;
    public String city;
    public String district;
    public String business;
    public String cityid;
    public String nameAddress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddreSuggData data = (AddreSuggData) o;

        if (name != null ? !name.equals(data.name) : data.name != null) return false;
        if (uid != null ? !uid.equals(data.uid) : data.uid != null) return false;
        if (city != null ? !city.equals(data.city) : data.city != null) return false;
        if (district != null ? !district.equals(data.district) : data.district != null)
            return false;
        if (business != null ? !business.equals(data.business) : data.business != null)
            return false;
        if (cityid != null ? !cityid.equals(data.cityid) : data.cityid != null) return false;
        return !(nameAddress != null ? !nameAddress.equals(data.nameAddress) : data.nameAddress != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (district != null ? district.hashCode() : 0);
        result = 31 * result + (business != null ? business.hashCode() : 0);
        result = 31 * result + (cityid != null ? cityid.hashCode() : 0);
        result = 31 * result + (nameAddress != null ? nameAddress.hashCode() : 0);
        return result;
    }
}
