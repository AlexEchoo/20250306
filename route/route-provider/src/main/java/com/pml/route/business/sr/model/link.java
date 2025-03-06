package com.pml.route.business.sr.model;

import com.pml.route.business.sr.model.Curve.Curve;
import com.pml.route.business.sr.model.Curve.CurveInterval;
import com.pml.route.business.sr.model.Curve.Curves;
import io.swagger.models.auth.In;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class link {
    private Curves ingressCurve = new Curves();
    private Curve egressCurve;
    private Integer satNodeNum = input.satOfOneOrbit * input.numOfOrbit;
    private Integer classNum = 2;
    private Integer id = null;
    private satNode sourceSatellite = null;
    private satNode destSatellite = null;
    private Double rate = null;
    private Double allocatedRate = null;
    private Double usedRate = null;
    private Double buffer = null;
    private Double allocatedBuffer = null;
    private Double usedBuffer = null;
    private Double propagationDelay = null;
    private Double boundedQueuingDelay = null;
    private String linkClass = null;
    private Double cbsServerRate = null;
    private Double cbsServerWaitingTime = null;
    private Double RijA = null;
    private Double RijB = null;
    private Double TijA = null;
    private Double TijB = null;

    private Double maxPacketLength = null;
    private Double[][] ingressRate = new Double[satNodeNum + 1][classNum + 1];
    private Double[][] ingressBurst = new Double[satNodeNum + 1][classNum + 1];

    public link(Integer id, satNode sourceSatellite, satNode destSatellite, Double rate, Double buffer
            , Double propagationDelay, Double boundedQueuingDelay, String linkClass, Double cbsServerRate,
                Double cbsServerWaitingTime, Double maxPacketLength, Double RijA, Double TijA, Double RijB, Double TijB) {
        this.id = id;
        this.sourceSatellite = sourceSatellite;
        this.destSatellite = destSatellite;
        this.rate = rate;
        this.allocatedRate = rate;
        this.usedRate = 0.0;
        this.buffer = buffer;
        this.allocatedBuffer = buffer;
        this.usedBuffer = 0.0;
        this.propagationDelay = propagationDelay;
        this.boundedQueuingDelay = boundedQueuingDelay;
        this.linkClass = linkClass;
        this.cbsServerRate = cbsServerRate;
        this.cbsServerWaitingTime = cbsServerWaitingTime;
        this.maxPacketLength = maxPacketLength;
        this.RijA = RijA;
        this.RijB = RijB;
        this.TijA = TijA;
        this.TijB = TijB;
        for (int i = 0; i < this.satNodeNum + 1; i++) {
            for (int j = 0; j < this.classNum + 1; j++) {
                ingressRate[i][j] = 0.0;
                ingressBurst[i][j] = 0.0;
            }
        }
    }

    public void addFlow(Integer priNode, Integer priClassIndex, Double rate, Double burst, satNode priSatNode, String priClass) {
        this.setIngressRateByIndex(priNode, priClassIndex,
                this.getIngressRateByIndex(priNode, priClassIndex) + rate);
        this.setIngressBurstByIndex(priNode, priClassIndex,
                this.getIngressBurstByIndex(priNode, priClassIndex) + burst);
        this.ingressCurve.insertOneCurve(priSatNode, sourceSatellite, destSatellite, priClass, linkClass, rate, burst);
        this.ingressCurve.output();
        Curve tempCurve = this.ingressCurve.head;
        this.egressCurve = new Curve();
        while (tempCurve != null) {
            if (tempCurve.getPriClass().equals("A")) {
                this.egressCurve.addAnotherCurve(tempCurve.physicalLimitedCurve(RijA, maxPacketLength, 1.0));
            } else if (tempCurve.getPriClass().equals("B")) {
                this.egressCurve.addAnotherCurve(tempCurve.physicalLimitedCurve(RijB, maxPacketLength, 1.0));
            } else {
                this.egressCurve.addAnotherCurve(tempCurve);
            }
            tempCurve = tempCurve.getNextCurve();
        }
        if (this.ingressCurve.size >= 3) {
            int i = 0;
        }
//        Curve curve = this.egressCurve.physicalLimitedCurve(cbsServerRate, maxPacketLength, 1.0);
//        this.usedBuffer = curve.computeMaxBuffer(cbsServerRate, cbsServerWaitingTime);
        this.usedBuffer = this.egressCurve.computeMaxBuffer(cbsServerRate, cbsServerWaitingTime);

        if (this.usedBuffer > this.buffer) {
            int k = 0;
            return;
        }
        this.allocatedBuffer = buffer - usedBuffer;
        this.usedRate += rate;
        this.allocatedRate -= rate;
    }

    public Boolean addFlowByBuffer(Integer priNode, Integer priClassIndex, Double rate, Double burst, satNode priSatNode, String priClass) {
        this.setIngressRateByIndex(priNode, priClassIndex,
                this.getIngressRateByIndex(priNode, priClassIndex) + rate);
        this.setIngressBurstByIndex(priNode, priClassIndex,
                this.getIngressBurstByIndex(priNode, priClassIndex) + burst);
        this.ingressCurve.insertOneCurve(priSatNode, sourceSatellite, destSatellite, priClass, linkClass, rate, burst);
        this.ingressCurve.output();
        Curve tempCurve = this.ingressCurve.head;
        this.egressCurve = new Curve();
        while (tempCurve != null) {
            if (tempCurve.getPriClass().equals("A")) {
                this.egressCurve.addAnotherCurve(tempCurve.physicalLimitedCurve(RijA, maxPacketLength, 1.0));
            } else if (tempCurve.getPriClass().equals("B")) {
                this.egressCurve.addAnotherCurve(tempCurve.physicalLimitedCurve(RijB, maxPacketLength, 1.0));
            } else {
                this.egressCurve.addAnotherCurve(tempCurve);
            }
            tempCurve = tempCurve.getNextCurve();
        }
        if (this.ingressCurve.size >= 3) {
            int i = 0;
        }
//        Curve curve = this.egressCurve.physicalLimitedCurve(cbsServerRate, maxPacketLength, 1.0);
//        this.usedBuffer = curve.computeMaxBuffer(cbsServerRate, cbsServerWaitingTime);
        this.usedBuffer = this.egressCurve.computeMaxBuffer(cbsServerRate, cbsServerWaitingTime);
        this.allocatedBuffer = buffer - usedBuffer;
        this.usedRate += rate;
        this.allocatedRate -= rate;
        if (this.usedBuffer > this.buffer) {
            int k = 0;
            return false;
        }

        return true;
    }

    public void remFlow(Integer priNode, Integer priClassIndex, Double rate, Double burst, satNode priSatNode, String priClass) {
        this.setIngressRateByIndex(priNode, priClassIndex,
                this.getIngressRateByIndex(priNode, priClassIndex) - rate);
        this.setIngressBurstByIndex(priNode, priClassIndex,
                this.getIngressBurstByIndex(priNode, priClassIndex) - burst);
        ingressCurve.removeOneCurve(priSatNode, sourceSatellite, destSatellite, priClass, linkClass, rate, burst);
        Curve tempCurve = this.ingressCurve.head;
        this.egressCurve = new Curve();
        while (tempCurve != null) {
            if (tempCurve.getPriClass().equals("A")) {
                this.egressCurve.addAnotherCurve(tempCurve.physicalLimitedCurve(RijA, maxPacketLength, 1.0));
            } else if (tempCurve.getPriClass().equals("B")) {
                this.egressCurve.addAnotherCurve(tempCurve.physicalLimitedCurve(RijB, maxPacketLength, 1.0));
            } else {
                this.egressCurve.addAnotherCurve(tempCurve);
            }
            tempCurve = tempCurve.getNextCurve();
        }
//        this.usedBuffer = this.egressCurve.physicalLimitedCurve(cbsServerRate, maxPacketLength, 1.0).computeMaxBuffer(cbsServerRate, cbsServerWaitingTime);
        this.usedBuffer = this.egressCurve.computeMaxBuffer(cbsServerRate, cbsServerWaitingTime);
        this.allocatedBuffer = buffer - usedBuffer;
        this.usedRate -= rate;
        if(this.allocatedBuffer<0){
            ingressCurve.removeOneCurve(priSatNode, sourceSatellite, destSatellite, priClass, linkClass, rate, burst);
        }
        this.allocatedRate += rate;
    }

    public void setIngressRateByIndex(Integer priNode, Integer priClassIndex, Double rate) {
        this.ingressRate[priNode][priClassIndex] = rate;
    }

    public Double getIngressRateByIndex(Integer priNode, Integer priClassIndex) {
        return this.ingressRate[priNode][priClassIndex];
    }

    public void setIngressBurstByIndex(Integer priNode, Integer priClassIndex, Double Burst) {
        this.ingressBurst[priNode][priClassIndex] = Burst;
    }

    public Double getIngressBurstByIndex(Integer priNode, Integer priClassIndex) {
        return this.ingressBurst[priNode][priClassIndex];
    }

}
