/*
 * swagger说明文档　
 * 学习Swagger
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
import com.pml.sdsn.common.model.Link;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * PathParam
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2022-08-25T14:33:24.122+08:00")
public class PathParam {
  @JsonProperty("sourceUuid")
  private String sourceUuid = null;

  @JsonProperty("destUuid")
  private String destUuid = null;

  @JsonProperty("supportHsb")
  private Boolean supportHsb = null;

  @JsonProperty("bandwidth")
  private Long bandwidth = null;

  /**
   * Gets or Sets calcPolicy
   */
  public enum CalcPolicyEnum {
    METRIC("metric"),
    
    DELAY("delay"),
    
    HOP("hop"),
    
    LOADBALANCE("loadbalance"),
    
    ENVELOPE("envelope");

    private String value;

    CalcPolicyEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CalcPolicyEnum fromValue(String text) {
      for (CalcPolicyEnum b : CalcPolicyEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("calcPolicy")
  private CalcPolicyEnum calcPolicy = null;

  @JsonProperty("delay")
  private Double delay = null;

  @JsonProperty("snapshotTime")
  private Long snapshotTime = null;

  @JsonProperty("increaseLinks")
  private List<Link> increaseLinks = null;

  @JsonProperty("constraint")
  private Constraint constraint = null;


  @JsonProperty("tunnelUuid")
  private String tunnelUuid = null;

  @JsonProperty("beforehand")
  private Boolean beforehand = null;


  @JsonProperty("specialLine")
  private Boolean specialLine = null;

  @JsonProperty("defaultRoute")
  private Boolean defaultRoute = null;

  @JsonProperty("priority")
  private Integer priority = null;

  public PathParam sourceUuid(String sourceUuid) {
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

  public PathParam destUuid(String destUuid) {
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

  public PathParam supportHsb(Boolean supportHsb) {
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

  public PathParam bandwidth(Long bandwidth) {
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

  public PathParam calcPolicy(CalcPolicyEnum calcPolicy) {
    this.calcPolicy = calcPolicy;
    return this;
  }

  /**
   * Get calcPolicy
   * @return calcPolicy
   **/
  @JsonProperty("calcPolicy")
  @ApiModelProperty(value = "")
  public CalcPolicyEnum getCalcPolicy() {
    return calcPolicy;
  }

  public void setCalcPolicy(CalcPolicyEnum calcPolicy) {
    this.calcPolicy = calcPolicy;
  }

  public PathParam delay(Double delay) {
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

  public PathParam snapshotTime(Long snapshotTime) {
    this.snapshotTime = snapshotTime;
    return this;
  }

  /**
   * Get snapshotTime
   * @return snapshotTime
   **/
  @JsonProperty("snapshotTime")
  @ApiModelProperty(value = "")
  public Long getSnapshotTime() {
    return snapshotTime;
  }

  public void setSnapshotTime(Long snapshotTime) {
    this.snapshotTime = snapshotTime;
  }

  public PathParam increaseLinks(List<Link> increaseLinks) {
    this.increaseLinks = increaseLinks;
    return this;
  }

  public PathParam addIncreaseLinksItem(Link increaseLinksItem) {
    if (this.increaseLinks == null) {
      this.increaseLinks = new ArrayList<>();
    }
    this.increaseLinks.add(increaseLinksItem);
    return this;
  }

  /**
   * Get increaseLinks
   * @return increaseLinks
   **/
  @JsonProperty("increaseLinks")
  @ApiModelProperty(value = "")
  public List<Link> getIncreaseLinks() {
    return increaseLinks;
  }

  public void setIncreaseLinks(List<Link> increaseLinks) {
    this.increaseLinks = increaseLinks;
  }

  public PathParam constraint(Constraint constraint) {
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



  public PathParam tunnelUuid(String tunnelUuid) {
    this.tunnelUuid = tunnelUuid;
    return this;
  }

  /**
   * Get tunnelUuid
   * @return tunnelUuid
   **/
  @JsonProperty("tunnelUuid")
  @ApiModelProperty(value = "")
  public String getTunnelUuid() {
    return tunnelUuid;
  }

  public void setTunnelUuid(String tunnelUuid) {
    this.tunnelUuid = tunnelUuid;
  }

  public PathParam beforehand(Boolean beforehand) {
    this.beforehand = beforehand;
    return this;
  }

  /**
   * Get beforehand
   * @return beforehand
   **/
  @JsonProperty("beforehand")
  @ApiModelProperty(value = "")
  public Boolean isBeforehand() {
    return beforehand;
  }

  public void setBeforehand(Boolean beforehand) {
    this.beforehand = beforehand;
  }

  public PathParam specialLine(Boolean specialLine) {
    this.specialLine = specialLine;
    return this;
  }

  /**
   * Get specialLine
   * @return specialLine
   **/
  @JsonProperty("specialLine")
  @ApiModelProperty(value = "")
  public Boolean isSpecialLine() {
    return specialLine;
  }

  public void setSpecialLine(Boolean specialLine) {
    this.specialLine = specialLine;
  }

  public PathParam defaultRoute(Boolean defaultRoute) {
    this.defaultRoute = defaultRoute;
    return this;
  }

  /**
   * Get defaultRoute
   * @return defaultRoute
   **/
  @JsonProperty("defaultRoute")
  @ApiModelProperty(value = "")
  public Boolean isDefaultRoute() {
    return defaultRoute;
  }

  public void setDefaultRoute(Boolean defaultRoute) {
    this.defaultRoute = defaultRoute;
  }

  public PathParam priority(Integer priority) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PathParam pathParam = (PathParam) o;
    return Objects.equals(this.sourceUuid, pathParam.sourceUuid) &&
        Objects.equals(this.destUuid, pathParam.destUuid) &&
        Objects.equals(this.supportHsb, pathParam.supportHsb) &&
        Objects.equals(this.bandwidth, pathParam.bandwidth) &&
        Objects.equals(this.calcPolicy, pathParam.calcPolicy) &&
        Objects.equals(this.delay, pathParam.delay) &&
        Objects.equals(this.snapshotTime, pathParam.snapshotTime) &&
        Objects.equals(this.increaseLinks, pathParam.increaseLinks) &&
        Objects.equals(this.tunnelUuid, pathParam.tunnelUuid) &&
        Objects.equals(this.beforehand, pathParam.beforehand) &&
        Objects.equals(this.specialLine, pathParam.specialLine) &&
        Objects.equals(this.defaultRoute, pathParam.defaultRoute) &&
        Objects.equals(this.priority, pathParam.priority);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceUuid, destUuid, supportHsb, bandwidth, calcPolicy, delay, snapshotTime, increaseLinks, constraint, tunnelUuid, beforehand, specialLine, defaultRoute, priority);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PathParam {\n");

    sb.append("    sourceUuid: ").append(toIndentedString(sourceUuid)).append("\n");
    sb.append("    destUuid: ").append(toIndentedString(destUuid)).append("\n");
    sb.append("    supportHsb: ").append(toIndentedString(supportHsb)).append("\n");
    sb.append("    bandwidth: ").append(toIndentedString(bandwidth)).append("\n");
    sb.append("    calcPolicy: ").append(toIndentedString(calcPolicy)).append("\n");
    sb.append("    delay: ").append(toIndentedString(delay)).append("\n");
    sb.append("    snapshotTime: ").append(toIndentedString(snapshotTime)).append("\n");
    sb.append("    increaseLinks: ").append(toIndentedString(increaseLinks)).append("\n");
    sb.append("    constraint: ").append(toIndentedString(constraint)).append("\n");
    sb.append("    tunnelUuid: ").append(toIndentedString(tunnelUuid)).append("\n");
    sb.append("    beforehand: ").append(toIndentedString(beforehand)).append("\n");
    sb.append("    specialLine: ").append(toIndentedString(specialLine)).append("\n");
    sb.append("    defaultRoute: ").append(toIndentedString(defaultRoute)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
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

