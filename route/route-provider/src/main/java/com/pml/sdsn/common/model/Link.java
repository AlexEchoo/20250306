package com.pml.sdsn.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pml.sdsn.common.model.State;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Link
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2022-08-25T14:33:24.122+08:00")
public class Link {
  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("ingress_bandwidth")
  private Double ingressBandwidth = null;

  @JsonProperty("egress_bandwidth")
  private Double egressBandwidth = null;

  @JsonProperty("source")
  private String source = null;

  @JsonProperty("destination")
  private String destination = null;

  @JsonProperty("sourceAddress")
  private String sourceAddress = null;

  @JsonProperty("destinationAddress")
  private String destinationAddress = null;

  @JsonProperty("sourceIntf")
  private String sourceIntf = null;

  @JsonProperty("destinationIntf")
  private String destinationIntf = null;

  @JsonProperty("admin_state")
  private State adminState = null;

  @JsonProperty("state")
  private State state = null;

  @JsonProperty("delay_size")
  private Double delaySize = null;

  @JsonProperty("packet_loss")
  private Double packetLoss = null;



  @JsonProperty("band_width")
  private Double bandWidth = null;

  @JsonProperty("label")
  private Integer label = null;

  @JsonProperty("used_bandwidth")
  private Double usedBandwidth = null;

  @JsonProperty("unused_bandwidth")
  private Double unusedBandwidth = null;

  @JsonProperty("metric")
  private Integer metric = null;


  @JsonProperty("traffic")
  private Integer traffic = null;


  public Link uuid(String uuid) {
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

  public Link name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   **/
  @JsonProperty("name")
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Link ingressBandwidth(Double ingressBandwidth) {
    this.ingressBandwidth = ingressBandwidth;
    return this;
  }

  /**
   * Get ingressBandwidth
   * @return ingressBandwidth
   **/
  @JsonProperty("ingress_bandwidth")
  @ApiModelProperty(value = "")
  public Double getIngressBandwidth() {
    return ingressBandwidth;
  }

  public void setIngressBandwidth(Double ingressBandwidth) {
    this.ingressBandwidth = ingressBandwidth;
  }

  public Link egressBandwidth(Double egressBandwidth) {
    this.egressBandwidth = egressBandwidth;
    return this;
  }

  /**
   * Get egressBandwidth
   * @return egressBandwidth
   **/
  @JsonProperty("egress_bandwidth")
  @ApiModelProperty(value = "")
  public Double getEgressBandwidth() {
    return egressBandwidth;
  }

  public void setEgressBandwidth(Double egressBandwidth) {
    this.egressBandwidth = egressBandwidth;
  }

  public Link source(String source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   **/
  @JsonProperty("source")
  @ApiModelProperty(value = "")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public Link destination(String destination) {
    this.destination = destination;
    return this;
  }

  /**
   * Get destination
   * @return destination
   **/
  @JsonProperty("destination")
  @ApiModelProperty(value = "")
  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public Link sourceAddress(String sourceAddress) {
    this.sourceAddress = sourceAddress;
    return this;
  }

  /**
   * Get sourceAddress
   * @return sourceAddress
   **/
  @JsonProperty("sourceAddress")
  @ApiModelProperty(value = "")
  public String getSourceAddress() {
    return sourceAddress;
  }

  public void setSourceAddress(String sourceAddress) {
    this.sourceAddress = sourceAddress;
  }

  public Link destinationAddress(String destinationAddress) {
    this.destinationAddress = destinationAddress;
    return this;
  }

  /**
   * Get destinationAddress
   * @return destinationAddress
   **/
  @JsonProperty("destinationAddress")
  @ApiModelProperty(value = "")
  public String getDestinationAddress() {
    return destinationAddress;
  }

  public void setDestinationAddress(String destinationAddress) {
    this.destinationAddress = destinationAddress;
  }

  public Link sourceIntf(String sourceIntf) {
    this.sourceIntf = sourceIntf;
    return this;
  }

  /**
   * Get sourceIntf
   * @return sourceIntf
   **/
  @JsonProperty("sourceIntf")
  @ApiModelProperty(value = "")
  public String getSourceIntf() {
    return sourceIntf;
  }

  public void setSourceIntf(String sourceIntf) {
    this.sourceIntf = sourceIntf;
  }

  public Link destinationIntf(String destinationIntf) {
    this.destinationIntf = destinationIntf;
    return this;
  }

  /**
   * Get destinationIntf
   * @return destinationIntf
   **/
  @JsonProperty("destinationIntf")
  @ApiModelProperty(value = "")
  public String getDestinationIntf() {
    return destinationIntf;
  }

  public void setDestinationIntf(String destinationIntf) {
    this.destinationIntf = destinationIntf;
  }

  public Link adminState(State adminState) {
    this.adminState = adminState;
    return this;
  }

  /**
   * Get adminState
   * @return adminState
   **/
  @JsonProperty("admin_state")
  @ApiModelProperty(value = "")
  public State getAdminState() {
    return adminState;
  }

  public void setAdminState(State adminState) {
    this.adminState = adminState;
  }

  public Link state(State state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   **/
  @JsonProperty("state")
  @ApiModelProperty(value = "")
  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public Link delaySize(Double delaySize) {
    this.delaySize = delaySize;
    return this;
  }

  /**
   * Get delaySize
   * @return delaySize
   **/
  @JsonProperty("delay_size")
  @ApiModelProperty(value = "")
  public Double getDelaySize() {
    return delaySize;
  }

  public void setDelaySize(Double delaySize) {
    this.delaySize = delaySize;
  }

  public Link packetLoss(Double packetLoss) {
    this.packetLoss = packetLoss;
    return this;
  }

  /**
   * Get packetLoss
   * @return packetLoss
   **/
  @JsonProperty("packet_loss")
  @ApiModelProperty(value = "")
  public Double getPacketLoss() {
    return packetLoss;
  }

  public void setPacketLoss(Double packetLoss) {
    this.packetLoss = packetLoss;
  }


  /**
   * Get bandWidth
   * @return bandWidth
   **/
  @JsonProperty("band_width")
  @ApiModelProperty(value = "")
  public Double getBandWidth() {
    return bandWidth;
  }

  public void setBandWidth(Double bandWidth) {
    this.bandWidth = bandWidth;
  }

  public Link label(Integer label) {
    this.label = label;
    return this;
  }

  /**
   * sr adj标签
   * @return label
   **/
  @JsonProperty("label")
  @ApiModelProperty(value = "sr adj标签")
  public Integer getLabel() {
    return label;
  }

  public void setLabel(Integer label) {
    this.label = label;
  }

  public Link usedBandwidth(Double usedBandwidth) {
    this.usedBandwidth = usedBandwidth;
    return this;
  }

  /**
   * Get usedBandwidth
   * @return usedBandwidth
   **/
  @JsonProperty("used_bandwidth")
  @ApiModelProperty(value = "")
  public Double getUsedBandwidth() {
    return usedBandwidth;
  }

  public void setUsedBandwidth(Double usedBandwidth) {
    this.usedBandwidth = usedBandwidth;
  }

  public Link unusedBandwidth(Double unusedBandwidth) {
    this.unusedBandwidth = unusedBandwidth;
    return this;
  }

  /**
   * Get unusedBandwidth
   * @return unusedBandwidth
   **/
  @JsonProperty("unused_bandwidth")
  @ApiModelProperty(value = "")
  public Double getUnusedBandwidth() {
    return unusedBandwidth;
  }

  public void setUnusedBandwidth(Double unusedBandwidth) {
    this.unusedBandwidth = unusedBandwidth;
  }

  public Link metric(Integer metric) {
    this.metric = metric;
    return this;
  }

  /**
   * Get metric
   * @return metric
   **/
  @JsonProperty("metric")
  @ApiModelProperty(value = "")
  public Integer getMetric() {
    return metric;
  }

  public void setMetric(Integer metric) {
    this.metric = metric;
  }



  public Link traffic(Integer traffic) {
    this.traffic = traffic;
    return this;
  }

  /**
   * Get traffic
   * @return traffic
   **/
  @JsonProperty("traffic")
  @ApiModelProperty(value = "")
  public Integer getTraffic() {
    return traffic;
  }

  public void setTraffic(Integer traffic) {
    this.traffic = traffic;
  }



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Link link = (Link) o;
    return Objects.equals(this.uuid, link.uuid) &&
        Objects.equals(this.name, link.name) &&
        Objects.equals(this.ingressBandwidth, link.ingressBandwidth) &&
        Objects.equals(this.egressBandwidth, link.egressBandwidth) &&
        Objects.equals(this.source, link.source) &&
        Objects.equals(this.destination, link.destination) &&
        Objects.equals(this.sourceAddress, link.sourceAddress) &&
        Objects.equals(this.destinationAddress, link.destinationAddress) &&
        Objects.equals(this.sourceIntf, link.sourceIntf) &&
        Objects.equals(this.destinationIntf, link.destinationIntf) &&
        Objects.equals(this.adminState, link.adminState) &&
        Objects.equals(this.state, link.state) &&
        Objects.equals(this.delaySize, link.delaySize) &&
        Objects.equals(this.packetLoss, link.packetLoss) &&
        Objects.equals(this.bandWidth, link.bandWidth) &&
        Objects.equals(this.label, link.label) &&
        Objects.equals(this.usedBandwidth, link.usedBandwidth) &&
        Objects.equals(this.unusedBandwidth, link.unusedBandwidth) &&
        Objects.equals(this.metric, link.metric) &&
        Objects.equals(this.traffic, link.traffic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, name, ingressBandwidth, egressBandwidth, source, destination, sourceAddress, destinationAddress, sourceIntf, destinationIntf, adminState, state, delaySize, packetLoss,  bandWidth, label, usedBandwidth, unusedBandwidth, metric, traffic);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Link {\n");

    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    ingressBandwidth: ").append(toIndentedString(ingressBandwidth)).append("\n");
    sb.append("    egressBandwidth: ").append(toIndentedString(egressBandwidth)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    destination: ").append(toIndentedString(destination)).append("\n");
    sb.append("    sourceAddress: ").append(toIndentedString(sourceAddress)).append("\n");
    sb.append("    destinationAddress: ").append(toIndentedString(destinationAddress)).append("\n");
    sb.append("    sourceIntf: ").append(toIndentedString(sourceIntf)).append("\n");
    sb.append("    destinationIntf: ").append(toIndentedString(destinationIntf)).append("\n");
    sb.append("    adminState: ").append(toIndentedString(adminState)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    delaySize: ").append(toIndentedString(delaySize)).append("\n");
    sb.append("    packetLoss: ").append(toIndentedString(packetLoss)).append("\n");
    sb.append("    bandWidth: ").append(toIndentedString(bandWidth)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    usedBandwidth: ").append(toIndentedString(usedBandwidth)).append("\n");
    sb.append("    unusedBandwidth: ").append(toIndentedString(unusedBandwidth)).append("\n");
    sb.append("    metric: ").append(toIndentedString(metric)).append("\n");
    sb.append("    traffic: ").append(toIndentedString(traffic)).append("\n");
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

