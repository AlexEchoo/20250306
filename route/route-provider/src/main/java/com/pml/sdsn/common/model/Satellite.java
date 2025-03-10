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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 虚拟卫星节点
 */
@ApiModel(description = "虚拟卫星节点")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2022-08-25T14:33:22.964+08:00")
public class Satellite {
  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("controller_ip")
  private String controllerIp = null;

  @JsonProperty("cpu_quota")
  private String cpuQuota = null;

  @JsonProperty("cpuset_cpus")
  private String cpusetCpus = null;


  @JsonProperty("ovs_br")
  private String ovsBr = null;

  @JsonProperty("ovs_ip")
  private String ovsIp = null;

  @JsonProperty("status")
  private State status = null;

  @JsonProperty("track")
  private Integer track = null;

  @JsonProperty("numberInTrack")
  private Integer numberInTrack = null;

  @JsonProperty("trackType")
  private String trackType = null;

  @JsonProperty("groundStation")
  private Boolean groundStation = null;

  @JsonProperty("node_type")
  private String nodeType = null;

  @JsonProperty("constellation")
  private String constellation = null;

  @JsonProperty("number")
  private Integer number = null;

  @JsonProperty("ncfSideIp")
  private String ncfSideIp = null;

  @JsonProperty("address")
  private Integer address = null;

  @JsonProperty("index")
  private Integer index = null;

  @JsonProperty("label")
  private Integer label = null;

  public Satellite uuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

  /**
   * Get uuid
   * @return uuid
   **/
  @JsonProperty("uuid")
  @ApiModelProperty(value = "")
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Satellite name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 卫星名称
   * @return name
   **/
  @JsonProperty("name")
  @ApiModelProperty(value = "卫星名称")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Satellite controllerIp(String controllerIp) {
    this.controllerIp = controllerIp;
    return this;
  }

  /**
   * 控制器ip
   * @return controllerIp
   **/
  @JsonProperty("controller_ip")
  @ApiModelProperty(value = "控制器ip")
  public String getControllerIp() {
    return controllerIp;
  }

  public void setControllerIp(String controllerIp) {
    this.controllerIp = controllerIp;
  }

  public Satellite cpuQuota(String cpuQuota) {
    this.cpuQuota = cpuQuota;
    return this;
  }

  /**
   * cpu配额
   * @return cpuQuota
   **/
  @JsonProperty("cpu_quota")
  @ApiModelProperty(value = "cpu配额")
  public String getCpuQuota() {
    return cpuQuota;
  }

  public void setCpuQuota(String cpuQuota) {
    this.cpuQuota = cpuQuota;
  }

  public Satellite cpusetCpus(String cpusetCpus) {
    this.cpusetCpus = cpusetCpus;
    return this;
  }

  /**
   * cpu
   * @return cpusetCpus
   **/
  @JsonProperty("cpuset_cpus")
  @ApiModelProperty(value = "cpu")
  public String getCpusetCpus() {
    return cpusetCpus;
  }

  public void setCpusetCpus(String cpusetCpus) {
    this.cpusetCpus = cpusetCpus;
  }


  /**
   * Get interfaces
   * @return interfaces
   **/
  @JsonProperty("interfaces")
  @ApiModelProperty(value = "")

  public Satellite ovsBr(String ovsBr) {
    this.ovsBr = ovsBr;
    return this;
  }

  /**
   * ovs网桥
   * @return ovsBr
   **/
  @JsonProperty("ovs_br")
  @ApiModelProperty(value = "ovs网桥")
  public String getOvsBr() {
    return ovsBr;
  }

  public void setOvsBr(String ovsBr) {
    this.ovsBr = ovsBr;
  }

  public Satellite ovsIp(String ovsIp) {
    this.ovsIp = ovsIp;
    return this;
  }

  /**
   * ovsip
   * @return ovsIp
   **/
  @JsonProperty("ovs_ip")
  @ApiModelProperty(value = "ovsip")
  public String getOvsIp() {
    return ovsIp;
  }

  public void setOvsIp(String ovsIp) {
    this.ovsIp = ovsIp;
  }

  public Satellite status(State status) {
    this.status = status;
    return this;
  }

  /**
   * 状态
   * @return status
   **/
  @JsonProperty("status")
  @ApiModelProperty(value = "状态")
  public State getStatus() {
    return status;
  }

  public void setStatus(State status) {
    this.status = status;
  }

  public Satellite track(Integer track) {
    this.track = track;
    return this;
  }

  /**
   * Get track
   * @return track
   **/
  @JsonProperty("track")
  @ApiModelProperty(value = "")
  public Integer getTrack() {
    return track;
  }

  public void setTrack(Integer track) {
    this.track = track;
  }

  public Satellite numberInTrack(Integer numberInTrack) {
    this.numberInTrack = numberInTrack;
    return this;
  }

  /**
   * Get numberInTrack
   * @return numberInTrack
   **/
  @JsonProperty("numberInTrack")
  @ApiModelProperty(value = "")
  public Integer getNumberInTrack() {
    return numberInTrack;
  }

  public void setNumberInTrack(Integer numberInTrack) {
    this.numberInTrack = numberInTrack;
  }

  public Satellite trackType(String trackType) {
    this.trackType = trackType;
    return this;
  }

  /**
   * Get trackType
   * @return trackType
   **/
  @JsonProperty("trackType")
  @ApiModelProperty(value = "")
  public String getTrackType() {
    return trackType;
  }

  public void setTrackType(String trackType) {
    this.trackType = trackType;
  }

  public Satellite groundStation(Boolean groundStation) {
    this.groundStation = groundStation;
    return this;
  }

  /**
   * Get groundStation
   * @return groundStation
   **/
  @JsonProperty("groundStation")
  @ApiModelProperty(value = "")
  public Boolean isGroundStation() {
    return groundStation;
  }

  public void setGroundStation(Boolean groundStation) {
    this.groundStation = groundStation;
  }

  public Satellite nodeType(String nodeType) {
    this.nodeType = nodeType;
    return this;
  }

  /**
   * Get nodeType
   * @return nodeType
   **/
  @JsonProperty("node_type")
  @ApiModelProperty(value = "")
  public String getNodeType() {
    return nodeType;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  public Satellite constellation(String constellation) {
    this.constellation = constellation;
    return this;
  }

  /**
   * Get constellation
   * @return constellation
   **/
  @JsonProperty("constellation")
  @ApiModelProperty(value = "")
  public String getConstellation() {
    return constellation;
  }

  public void setConstellation(String constellation) {
    this.constellation = constellation;
  }

  public Satellite number(Integer number) {
    this.number = number;
    return this;
  }

  /**
   * Get number
   * @return number
   **/
  @JsonProperty("number")
  @ApiModelProperty(value = "")
  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public Satellite ncfSideIp(String ncfSideIp) {
    this.ncfSideIp = ncfSideIp;
    return this;
  }

  /**
   * Get ncfSideIp
   * @return ncfSideIp
   **/
  @JsonProperty("ncfSideIp")
  @ApiModelProperty(value = "")
  public String getNcfSideIp() {
    return ncfSideIp;
  }

  public void setNcfSideIp(String ncfSideIp) {
    this.ncfSideIp = ncfSideIp;
  }

  public Satellite address(Integer address) {
    this.address = address;
    return this;
  }

  /**
   * 卫星编址
   * @return address
   **/
  @JsonProperty("address")
  @ApiModelProperty(value = "卫星编址")
  public Integer getAddress() {
    return address;
  }

  public void setAddress(Integer address) {
    this.address = address;
  }

  public Satellite index(Integer index) {
    this.index = index;
    return this;
  }

  /**
   * scf卫星编号索引
   * @return index
   **/
  @JsonProperty("index")
  @ApiModelProperty(value = "scf卫星编号索引")
  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }

  public Satellite label(Integer label) {
    this.label = label;
    return this;
  }

  /**
   * 节点标签
   * @return label
   **/
  @JsonProperty("label")
  @ApiModelProperty(value = "节点标签")
  public Integer getLabel() {
    return label;
  }

  public void setLabel(Integer label) {
    this.label = label;
  }


  // Override重写
  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Satellite satellite = (Satellite) o;
    return Objects.equals(this.uuid, satellite.uuid) &&
        Objects.equals(this.name, satellite.name) &&
        Objects.equals(this.controllerIp, satellite.controllerIp) &&
        Objects.equals(this.cpuQuota, satellite.cpuQuota) &&
        Objects.equals(this.cpusetCpus, satellite.cpusetCpus) &&
        Objects.equals(this.ovsBr, satellite.ovsBr) &&
        Objects.equals(this.ovsIp, satellite.ovsIp) &&
        Objects.equals(this.status, satellite.status) &&
        Objects.equals(this.track, satellite.track) &&
        Objects.equals(this.numberInTrack, satellite.numberInTrack) &&
        Objects.equals(this.trackType, satellite.trackType) &&
        Objects.equals(this.groundStation, satellite.groundStation) &&
        Objects.equals(this.nodeType, satellite.nodeType) &&
        Objects.equals(this.constellation, satellite.constellation) &&
        Objects.equals(this.number, satellite.number) &&
        Objects.equals(this.ncfSideIp, satellite.ncfSideIp) &&
        Objects.equals(this.address, satellite.address) &&
        Objects.equals(this.index, satellite.index) &&
        Objects.equals(this.label, satellite.label);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, name, controllerIp, cpuQuota, cpusetCpus, ovsBr, ovsIp, status, track, numberInTrack, trackType, groundStation, nodeType, constellation, number, ncfSideIp, address, index, label);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Satellite {\n");

    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    controllerIp: ").append(toIndentedString(controllerIp)).append("\n");
    sb.append("    cpuQuota: ").append(toIndentedString(cpuQuota)).append("\n");
    sb.append("    cpusetCpus: ").append(toIndentedString(cpusetCpus)).append("\n");
    sb.append("    ovsBr: ").append(toIndentedString(ovsBr)).append("\n");
    sb.append("    ovsIp: ").append(toIndentedString(ovsIp)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    track: ").append(toIndentedString(track)).append("\n");
    sb.append("    numberInTrack: ").append(toIndentedString(numberInTrack)).append("\n");
    sb.append("    trackType: ").append(toIndentedString(trackType)).append("\n");
    sb.append("    groundStation: ").append(toIndentedString(groundStation)).append("\n");
    sb.append("    nodeType: ").append(toIndentedString(nodeType)).append("\n");
    sb.append("    constellation: ").append(toIndentedString(constellation)).append("\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    ncfSideIp: ").append(toIndentedString(ncfSideIp)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    index: ").append(toIndentedString(index)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
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

