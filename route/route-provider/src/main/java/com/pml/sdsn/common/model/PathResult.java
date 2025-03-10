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
import com.pml.sdsn.common.model.PathKey;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * PathResult
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2022-08-25T14:33:24.122+08:00")
public class PathResult {
  @JsonProperty("pathKey")
  private PathKey pathKey = null;

  @JsonProperty("primaryPath")
  private Path primaryPath = null;

  @JsonProperty("slavePath")
  private Path slavePath = null;

  /**
   * Gets or Sets policy
   */
  public enum PolicyEnum {
    METRIC("metric"),
    
    DELAY("delay"),
    
    HOP("hop");

    private String value;

    PolicyEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PolicyEnum fromValue(String text) {
      for (PolicyEnum b : PolicyEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("policy")
  private PolicyEnum policy = null;

  @JsonProperty("bandwidth")
  private Long bandwidth = null;

  @JsonProperty("constraint")
  private Constraint constraint = null;

  @JsonProperty("isMaster")
  private Boolean isMaster = null;

  @JsonProperty("defaultRoute")
  private Boolean defaultRoute = null;

  @JsonProperty("seizeTunnels")
  private List<String> seizeTunnels = null;

  @JsonProperty("priority")
  private Integer priority = null;

  public PathResult pathKey(PathKey pathKey) {
    this.pathKey = pathKey;
    return this;
  }

  /**
   * Get pathKey
   * @return pathKey
   **/
  @JsonProperty("pathKey")
  @ApiModelProperty(value = "")
  public PathKey getPathKey() {
    return pathKey;
  }

  public void setPathKey(PathKey pathKey) {
    this.pathKey = pathKey;
  }

  public PathResult primaryPath(Path primaryPath) {
    this.primaryPath = primaryPath;
    return this;
  }

  /**
   * Get primaryPath
   * @return primaryPath
   **/
  @JsonProperty("primaryPath")
  @ApiModelProperty(value = "")
  public Path getPrimaryPath() {
    return primaryPath;
  }

  public void setPrimaryPath(Path primaryPath) {
    this.primaryPath = primaryPath;
  }

  public PathResult slavePath(Path slavePath) {
    this.slavePath = slavePath;
    return this;
  }

  /**
   * Get slavePath
   * @return slavePath
   **/
  @JsonProperty("slavePath")
  @ApiModelProperty(value = "")
  public Path getSlavePath() {
    return slavePath;
  }

  public void setSlavePath(Path slavePath) {
    this.slavePath = slavePath;
  }

  public PathResult policy(PolicyEnum policy) {
    this.policy = policy;
    return this;
  }

  /**
   * Get policy
   * @return policy
   **/
  @JsonProperty("policy")
  @ApiModelProperty(value = "")
  public PolicyEnum getPolicy() {
    return policy;
  }

  public void setPolicy(PolicyEnum policy) {
    this.policy = policy;
  }

  public PathResult bandwidth(Long bandwidth) {
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

  public PathResult constraint(Constraint constraint) {
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

  public PathResult isMaster(Boolean isMaster) {
    this.isMaster = isMaster;
    return this;
  }

  /**
   * Get isMaster
   * @return isMaster
   **/
  @JsonProperty("isMaster")
  @ApiModelProperty(value = "")
  public Boolean isIsMaster() {
    return isMaster;
  }

  public void setIsMaster(Boolean isMaster) {
    this.isMaster = isMaster;
  }

  public PathResult defaultRoute(Boolean defaultRoute) {
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

  public PathResult seizeTunnels(List<String> seizeTunnels) {
    this.seizeTunnels = seizeTunnels;
    return this;
  }

  public PathResult addSeizeTunnelsItem(String seizeTunnelsItem) {
    if (this.seizeTunnels == null) {
      this.seizeTunnels = new ArrayList<>();
    }
    this.seizeTunnels.add(seizeTunnelsItem);
    return this;
  }

  /**
   * Get seizeTunnels
   * @return seizeTunnels
   **/
  @JsonProperty("seizeTunnels")
  @ApiModelProperty(value = "")
  public List<String> getSeizeTunnels() {
    return seizeTunnels;
  }

  public void setSeizeTunnels(List<String> seizeTunnels) {
    this.seizeTunnels = seizeTunnels;
  }

  public PathResult priority(Integer priority) {
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
    PathResult pathResult = (PathResult) o;
    return Objects.equals(this.pathKey, pathResult.pathKey) &&
        Objects.equals(this.primaryPath, pathResult.primaryPath) &&
        Objects.equals(this.slavePath, pathResult.slavePath) &&
        Objects.equals(this.policy, pathResult.policy) &&
        Objects.equals(this.bandwidth, pathResult.bandwidth) &&
        Objects.equals(this.constraint, pathResult.constraint) &&
        Objects.equals(this.isMaster, pathResult.isMaster) &&
        Objects.equals(this.defaultRoute, pathResult.defaultRoute) &&
        Objects.equals(this.seizeTunnels, pathResult.seizeTunnels) &&
        Objects.equals(this.priority, pathResult.priority);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pathKey, primaryPath, slavePath, policy, bandwidth, constraint, isMaster, defaultRoute, seizeTunnels, priority);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PathResult {\n");

    sb.append("    pathKey: ").append(toIndentedString(pathKey)).append("\n");
    sb.append("    primaryPath: ").append(toIndentedString(primaryPath)).append("\n");
    sb.append("    slavePath: ").append(toIndentedString(slavePath)).append("\n");
    sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
    sb.append("    bandwidth: ").append(toIndentedString(bandwidth)).append("\n");
    sb.append("    constraint: ").append(toIndentedString(constraint)).append("\n");
    sb.append("    isMaster: ").append(toIndentedString(isMaster)).append("\n");
    sb.append("    defaultRoute: ").append(toIndentedString(defaultRoute)).append("\n");
    sb.append("    seizeTunnels: ").append(toIndentedString(seizeTunnels)).append("\n");
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

