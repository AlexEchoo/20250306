package com.pml.route.business.sr.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class input {
//    static public Double bandwidthRate = 1.0;
    static public Double bandwidthRate = 5.0;
    static public Double feederRate = 10.0;
    static public Double Mbit = 1000000.0;
    //这几类包的最大包长
    static public Double LijE = 0.002 * Mbit;
    static public Double LijA = 0.001 * Mbit;
    static public Double LijB = 0.001 * Mbit;
    static public Double _LijA = Math.max(LijE, LijB);
    static public Double maxPacketLength = Math.max(Math.max(LijE, LijA), LijB);
    static public Double linerate = 10000.0;
    //链路实际带宽
    static public Double cij = bandwidthRate * linerate * Mbit;
    static public Double fij = feederRate * linerate * Mbit;
    //Idole slope
    static public Double IijA = cij * 15 / 100;
    static public Double IijB = cij * 7.5 / 100;
    static public Double SijA = IijA - cij;
    static public Double SijB = IijB - cij;
    //RDT的参数
    static public Double rij = cij * 1 / 10;
    static public Double bij = 0.004 * Mbit;
    //服务速率
    static public Double RijA = IijA * (cij - rij) / (IijA - SijA);
//    40500000
//    {
//        System.out.println(RijA);
//    }
    //服务等待时间
    static public Double TijA = 1 / (cij - rij) * (_LijA + bij + rij * _LijA / cij);
//    {
//        System.out.println(TijA);
//    }
    //B类流量的服务速率
    static public Double RijB = IijB * (cij - rij) / (IijB - SijB);
    static public Double TijB = 1 / (cij - rij) * (LijE + LijA - IijA * _LijA / SijA + bij +
            rij * _LijA / cij);

//    static public String filePath = "route/data/224.txt";
//    static public String filePath = "/home/robot/224.txt";
//    static public String filePath = "/home/ssr/java_project/500.txt";
//    static public String filePath = "E:\\code\\java_code\\controller_sunkk_jitter_v1.1_更改main函数_添加JitterControl类_debug完成\\route\\data\\108fan.txt";
    static public String filePath = "/home/mysat/DetNet/xw/sat/108fan.txt";
    static public Integer simulationTime = 1200;
    static public Double snapshotInterval = 60.0;
    static public Integer snapshotNum = 20;
    static public Integer defaultSnapshotIndex = 0;
    static public Double topoInterval = 1.0;
    static public Double rEarth = 6371393.0;
//    static public Double hSat = 600000.0;
    static public Double hSat = 1150000.0;
//    static public Integer satOfOneOrbit = 25;
    static public Integer satOfOneOrbit = 9;
//    static public Integer numOfOrbit = 20;
    static public Integer numOfOrbit = 12;
    static public Double opticalSpeed = 300000000.0;
//    static public Double satBuffer = 1 * Mbit;
    static public Double satBuffer = 5 * Mbit;




    public Double satBandwidth = bandwidthRate * linerate * Mbit;
    public Double feederBandwith = feederRate * linerate * Mbit;

//    static public Integer numOfFlow = 20;
    static public Integer numOfFlow = 200;
    static public Double rFlow = 10 * Mbit;
    static public Double bFlow = 0.005 * Mbit;
    static public Double maxDelayFlowRatio = 1.5;








    // 地面站全球分布，随机。星地，也有CBS  Haerbin (128°4′, 45°3′) Shang- hai (121°3′, 31°0′)  Xi’an (108°4′, 33°8′)
    //  Yunnan (103°4′, 25°3′) Qinghai (95°0′, 36°2′)  Xinjiang (80°6′, 39°4′),
    static public Integer groundStationNum = 6;
    static public Double[] gSLongitude = {45.0, 31.0, 33.0, 25.0, 36.0, 39.0};
    static public Double[] gSLatitude = {128.0, 121.0, 108.0, 103.0, 95.0, 80.0};
    static public Double gS2SatMaxPropagationDelay = 0.015;

    static public Integer flowDuration = 1;

    public Integer numOfFindKPath = 1;

    public void updateInputByNewbandwidthRate(Double bandwidthRate)
    {
        this.satBandwidth = bandwidthRate * this.linerate * this.Mbit;
        this.cij = bandwidthRate * this.linerate * this.Mbit;
        this.IijA = this.cij * 15 / 100;
        this.IijB = this.cij * 7.5 / 100;
        this.SijA = this.IijA - this.cij;
        this.SijB = this.IijB - this.cij;
        this.rij = this.cij * 1 / 10;
        this.feederBandwith = this.feederBandwith;
        this.bij = 0.004 * this.Mbit;
        this.RijA = this.IijA * (this.cij - this.rij) / (this.IijA - this.SijA);
        this.TijA = 1 / (this.cij - this.rij) * (this._LijA + this.bij + this.rij * this._LijA / this.cij);
        this.RijB = this.IijB * (this.cij - this.rij) / (this.IijB - this.SijB);
        this.TijB = 1 / (this.cij - this.rij) * (this.LijE + this.LijA - this.IijA * this._LijA / this.SijA + this.bij +
                this.rij * this._LijA / this.cij);
    }
}
