/*
 * sr模型定义　
 * sr模型
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.pml.sdsn.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * TunnelParam
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2022-08-11T15:53:02.916+08:00")
public class TunnelParam {
  @JsonProperty("sourceUuid")
  private String sourceUuid = null;

  @JsonProperty("sourceUserAddr")
  private String sourceUserAddr = null;

  @JsonProperty("destUserAddr")
  private String destUserAddr = null;

  @JsonProperty("sourceCli")
  private String sourceCli = null;

  @JsonProperty("destCli")
  private String destCli = null;

  @JsonProperty("destUuid")
  private String destUuid = null;

  /**
   * Gets or Sets accessType
   */
  public enum AccessTypeEnum {
    USER("user"),
    
    STOSTATION("stoStation"),
    
    CTOSTATION("ctoStation"),
    
    COMMON("common");

    private String value;

    AccessTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AccessTypeEnum fromValue(String text) {
      for (AccessTypeEnum b : AccessTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("accessType")
  private AccessTypeEnum accessType = null;

  @JsonProperty("bandwidth")
  private Long bandwidth = null;

  @JsonProperty("delay")
  private Double delay = null;

  @JsonProperty("time")
  private Long time = null;

  @JsonProperty("stationMappingTime")
  private Long stationMappingTime = null;

  @JsonProperty("vpnType")
  private String vpnType = null;

  @JsonProperty("vpnName")
  private String vpnName = null;

  @JsonProperty("priority")
  private Integer priority = null;

  @JsonProperty("calcPolicy")
  private String calcPolicy = null;

  @JsonProperty("supportHsb")
  private Boolean supportHsb = null;

  @JsonProperty("constraint")
  private Constraint constraint = null;

  public TunnelParam sourceUuid(String sourceUuid) {
    this.sourceUuid = sourceUuid;
    return this;
  }

  /**
   * Get sourceUuid
   * @return sourceUuid
   **/
  @JsonProperty("sourceUuid")
  @ApiModelProperty(value = "")
  public String getSourceUuid() {
    return sourceUuid;
  }

  public void setSourceUuid(String sourceUuid) {
    this.sourceUuid = sourceUuid;
  }

  public TunnelParam sourceUserAddr(String sourceUserAddr) {
    this.sourceUserAddr = sourceUserAddr;
    return this;
  }

  /**
   * Get sourceUserAddr
   * @return sourceUserAddr
   **/
  @JsonProperty("sourceUserAddr")
  @ApiModelProperty(value = "")
  public String getSourceUserAddr() {
    return sourceUserAddr;
  }

  public void setSourceUserAddr(String sourceUserAddr) {
    this.sourceUserAddr = sourceUserAddr;
  }

  public TunnelParam destUserAddr(String destUserAddr) {
    this.destUserAddr = destUserAddr;
    return this;
  }

  /**
   * Get destUserAddr
   * @return destUserAddr
   **/
  @JsonProperty("destUserAddr")
  @ApiModelProperty(value = "")
  public String getDestUserAddr() {
    return destUserAddr;
  }

  public void setDestUserAddr(String destUserAddr) {
    this.destUserAddr = destUserAddr;
  }

  public TunnelParam sourceCli(String sourceCli) {
    this.sourceCli = sourceCli;
    return this;
  }

  /**
   * Get sourceCli
   * @return sourceCli
   **/
  @JsonProperty("sourceCli")
  @ApiModelProperty(value = "")
  public String getSourceCli() {
    return sourceCli;
  }

  public void setSourceCli(String sourceCli) {
    this.sourceCli = sourceCli;
  }

  public TunnelParam destCli(String destCli) {
    this.destCli = destCli;
    return this;
  }

  /**
   * Get destCli
   * @return destCli
   **/
  @JsonProperty("destCli")
  @ApiModelProperty(value = "")
  public String getDestCli() {
    return destCli;
  }

  public void setDestCli(String destCli) {
    this.destCli = destCli;
  }

  public TunnelParam destUuid(String destUuid) {
    this.destUuid = destUuid;
    return this;
  }

  /**
   * Get destUuid
   * @return destUuid
   **/
  @JsonProperty("destUuid")
  @ApiModelProperty(value = "")
  public String getDestUuid() {
    return destUuid;
  }

  public void setDestUuid(String destUuid) {
    this.destUuid = destUuid;
  }

  public TunnelParam accessType(AccessTypeEnum accessType) {
    this.accessType = accessType;
    return this;
  }

  /**
   * Get accessType
   * @return accessType
   **/
  @JsonProperty("accessType")
  @ApiModelProperty(value = "")
  public AccessTypeEnum getAccessType() {
    return accessType;
  }

  public void setAccessType(AccessTypeEnum accessType) {
    this.accessType = accessType;
  }

  public TunnelParam bandwidth(Long bandwidth) {
    this.bandwidth = bandwidth;
    return this;
  }

  /**
   * Get bandwidth
   * @return bandwidth
   **/
  @JsonProperty("bandwidth")
  @ApiModelProperty(value = "")
  public Long getBandwidth() {
    return bandwidth;
  }

  public void setBandwidth(Long bandwidth) {
    this.bandwidth = bandwidth;
  }

  public TunnelParam delay(Double delay) {
    this.delay = delay;
    return this;
  }

  /**
   * Get delay
   * @return delay
   **/
  @JsonProperty("delay")
  @ApiModelProperty(value = "")
  public Double getDelay() {
    return delay;
  }

  public void setDelay(Double delay) {
    this.delay = delay;
  }

  public TunnelParam time(Long time) {
    this.time = time;
    return this;
  }

  /**
   * Get time
   * @return time
   **/
  @JsonProperty("time")
  @ApiModelProperty(value = "")
  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public TunnelParam stationMappingTime(Long stationMappingTime) {
    this.stationMappingTime = stationMappingTime;
    return this;
  }

  /**
   * Get stationMappingTime
   * @return stationMappingTime
   **/
  @JsonProperty("stationMappingTime")
  @ApiModelProperty(value = "")
  public Long getStationMappingTime() {
    return stationMappingTime;
  }

  public void setStationMappingTime(Long stationMappingTime) {
    this.stationMappingTime = stationMappingTime;
  }

  public TunnelParam vpnType(String vpnType) {
    this.vpnType = vpnType;
    return this;
  }

  /**
   * Get vpnType
   * @return vpnType
   **/
  @JsonProperty("vpnType")
  @ApiModelProperty(value = "")
  public String getVpnType() {
    return vpnType;
  }

  public void setVpnType(String vpnType) {
    this.vpnType = vpnType;
  }

  public TunnelParam vpnName(String vpnName) {
    this.vpnName = vpnName;
    return this;
  }

  /**
   * Get vpnName
   * @return vpnName
   **/
  @JsonProperty("vpnName")
  @ApiModelProperty(value = "")
  public String getVpnName() {
    return vpnName;
  }

  public void setVpnName(String vpnName) {
    this.vpnName = vpnName;
  }

  public TunnelParam priority(Integer priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   **/
  @JsonProperty("priority")
  @ApiModelProperty(value = "")
  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public TunnelParam calcPolicy(String calcPolicy) {
    this.calcPolicy = calcPolicy;
    return this;
  }

  /**
   * Get calcPolicy
   * @return calcPolicy
   **/
  @JsonProperty("calcPolicy")
  @ApiModelProperty(value = "")
  public String getCalcPolicy() {
    return calcPolicy;
  }

  public void setCalcPolicy(String calcPolicy) {
    this.calcPolicy = calcPolicy;
  }

  public TunnelParam supportHsb(Boolean supportHsb) {
    this.supportHsb = supportHsb;
    return this;
  }

  /**
   * Get supportHsb
   * @return supportHsb
   **/
  @JsonProperty("supportHsb")
  @ApiModelProperty(value = "")
  public Boolean isSupportHsb() {
    return supportHsb;
  }

  public void setSupportHsb(Boolean supportHsb) {
    this.supportHsb = supportHsb;
  }

  public TunnelParam constraint(Constraint constraint) {
    this.constraint = constraint;
    return this;
  }

  /**
   * Get constraint
   * @return constraint
   **/
  @JsonProperty("constraint")
  @ApiModelProperty(value = "")
  public Constraint getConstraint() {
    return constraint;
  }

  public void setConstraint(Constraint constraint) {
    this.constraint = constraint;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TunnelParam tunnelParam = (TunnelParam) o;
    return Objects.equals(this.sourceUuid, tunnelParam.sourceUuid) &&
        Objects.equals(this.sourceUserAddr, tunnelParam.sourceUserAddr) &&
        Objects.equals(this.destUserAddr, tunnelParam.destUserAddr) &&
        Objects.equals(this.sourceCli, tunnelParam.sourceCli) &&
        Objects.equals(this.destCli, tunnelParam.destCli) &&
        Objects.equals(this.destUuid, tunnelParam.destUuid) &&
        Objects.equals(this.accessType, tunnelParam.accessType) &&
        Objects.equals(this.bandwidth, tunnelParam.bandwidth) &&
        Objects.equals(this.delay, tunnelParam.delay) &&
        Objects.equals(this.time, tunnelParam.time) &&
        Objects.equals(this.stationMappingTime, tunnelParam.stationMappingTime) &&
        Objects.equals(this.vpnType, tunnelParam.vpnType) &&
        Objects.equals(this.vpnName, tunnelParam.vpnName) &&
        Objects.equals(this.priority, tunnelParam.priority) &&
        Objects.equals(this.calcPolicy, tunnelParam.calcPolicy) &&
        Objects.equals(this.supportHsb, tunnelParam.supportHsb) &&
        Objects.equals(this.constraint, tunnelParam.constraint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceUuid, sourceUserAddr, destUserAddr, sourceCli, destCli, destUuid, accessType, bandwidth, delay, time, stationMappingTime, vpnType, vpnName, priority, calcPolicy, supportHsb, constraint);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TunnelParam {\n");

    sb.append("    sourceUuid: ").append(toIndentedString(sourceUuid)).append("\n");
    sb.append("    sourceUserAddr: ").append(toIndentedString(sourceUserAddr)).append("\n");
    sb.append("    destUserAddr: ").append(toIndentedString(destUserAddr)).append("\n");
    sb.append("    sourceCli: ").append(toIndentedString(sourceCli)).append("\n");
    sb.append("    destCli: ").append(toIndentedString(destCli)).append("\n");
    sb.append("    destUuid: ").append(toIndentedString(destUuid)).append("\n");
    sb.append("    accessType: ").append(toIndentedString(accessType)).append("\n");
    sb.append("    bandwidth: ").append(toIndentedString(bandwidth)).append("\n");
    sb.append("    delay: ").append(toIndentedString(delay)).append("\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    stationMappingTime: ").append(toIndentedString(stationMappingTime)).append("\n");
    sb.append("    vpnType: ").append(toIndentedString(vpnType)).append("\n");
    sb.append("    vpnName: ").append(toIndentedString(vpnName)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    calcPolicy: ").append(toIndentedString(calcPolicy)).append("\n");
    sb.append("    supportHsb: ").append(toIndentedString(supportHsb)).append("\n");
    sb.append("    constraint: ").append(toIndentedString(constraint)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

