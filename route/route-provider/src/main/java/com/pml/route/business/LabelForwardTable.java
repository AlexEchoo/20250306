package com.pml.route.business;

import java.io.Serializable;

public class LabelForwardTable implements Serializable {
    private String uuid;

    private String satelliteId;

    private String satelliteName;

    private Integer label;

    private String outInterfaceId;

    private String outInterfaceName;

    private String nextHop;

    private String nextSatellite;

    private static final long serialVersionUID = 1L;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getSatelliteId() {
        return satelliteId;
    }

    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId == null ? null : satelliteId.trim();
    }

    public String getSatelliteName() {
        return satelliteName;
    }

    public void setSatelliteName(String satelliteName) {
        this.satelliteName = satelliteName == null ? null : satelliteName.trim();
    }

    public Integer getLabel() {
        return label;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }

    public String getOutInterfaceId() {
        return outInterfaceId;
    }

    public void setOutInterfaceId(String outInterfaceId) {
        this.outInterfaceId = outInterfaceId == null ? null : outInterfaceId.trim();
    }

    public String getOutInterfaceName() {
        return outInterfaceName;
    }

    public void setOutInterfaceName(String outInterfaceName) {
        this.outInterfaceName = outInterfaceName == null ? null : outInterfaceName.trim();
    }

    public String getNextHop() {
        return nextHop;
    }

    public void setNextHop(String nextHop) {
        this.nextHop = nextHop == null ? null : nextHop.trim();
    }

    public String getNextSatellite() {
        return nextSatellite;
    }

    public void setNextSatellite(String nextSatellite) {
        this.nextSatellite = nextSatellite == null ? null : nextSatellite.trim();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LabelForwardTable other = (LabelForwardTable) that;
        return (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
            && (this.getSatelliteId() == null ? other.getSatelliteId() == null : this.getSatelliteId().equals(other.getSatelliteId()))
            && (this.getSatelliteName() == null ? other.getSatelliteName() == null : this.getSatelliteName().equals(other.getSatelliteName()))
            && (this.getLabel() == null ? other.getLabel() == null : this.getLabel().equals(other.getLabel()))
            && (this.getOutInterfaceId() == null ? other.getOutInterfaceId() == null : this.getOutInterfaceId().equals(other.getOutInterfaceId()))
            && (this.getOutInterfaceName() == null ? other.getOutInterfaceName() == null : this.getOutInterfaceName().equals(other.getOutInterfaceName()))
            && (this.getNextHop() == null ? other.getNextHop() == null : this.getNextHop().equals(other.getNextHop()))
            && (this.getNextSatellite() == null ? other.getNextSatellite() == null : this.getNextSatellite().equals(other.getNextSatellite()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getSatelliteId() == null) ? 0 : getSatelliteId().hashCode());
        result = prime * result + ((getSatelliteName() == null) ? 0 : getSatelliteName().hashCode());
        result = prime * result + ((getLabel() == null) ? 0 : getLabel().hashCode());
        result = prime * result + ((getOutInterfaceId() == null) ? 0 : getOutInterfaceId().hashCode());
        result = prime * result + ((getOutInterfaceName() == null) ? 0 : getOutInterfaceName().hashCode());
        result = prime * result + ((getNextHop() == null) ? 0 : getNextHop().hashCode());
        result = prime * result + ((getNextSatellite() == null) ? 0 : getNextSatellite().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", uuid=").append(uuid);
        sb.append(", satelliteId=").append(satelliteId);
        sb.append(", satelliteName=").append(satelliteName);
        sb.append(", label=").append(label);
        sb.append(", outInterfaceId=").append(outInterfaceId);
        sb.append(", outInterfaceName=").append(outInterfaceName);
        sb.append(", nextHop=").append(nextHop);
        sb.append(", nextSatellite=").append(nextSatellite);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}