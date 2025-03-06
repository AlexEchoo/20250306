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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * PathKey
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2022-08-25T14:33:24.122+08:00")
public class PathKey {
  @JsonProperty("sourceUuid")
  private String sourceUuid = null;

  @JsonProperty("destUuid")
  private String destUuid = null;

  @JsonProperty("tunnelUuid")
  private String tunnelUuid = null;

  @JsonProperty("snapshotTime")
  private Long snapshotTime = null;

  public PathKey sourceUuid(String sourceUuid) {
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

  public PathKey destUuid(String destUuid) {
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

  public PathKey tunnelUuid(String tunnelUuid) {
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

  public PathKey snapshotTime(Long snapshotTime) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PathKey pathKey = (PathKey) o;
    return Objects.equals(this.sourceUuid, pathKey.sourceUuid) &&
        Objects.equals(this.destUuid, pathKey.destUuid) &&
        Objects.equals(this.tunnelUuid, pathKey.tunnelUuid) &&
        Objects.equals(this.snapshotTime, pathKey.snapshotTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceUuid, destUuid, tunnelUuid, snapshotTime);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PathKey {\n");

    sb.append("    sourceUuid: ").append(toIndentedString(sourceUuid)).append("\n");
    sb.append("    destUuid: ").append(toIndentedString(destUuid)).append("\n");
    sb.append("    tunnelUuid: ").append(toIndentedString(tunnelUuid)).append("\n");
    sb.append("    snapshotTime: ").append(toIndentedString(snapshotTime)).append("\n");
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

