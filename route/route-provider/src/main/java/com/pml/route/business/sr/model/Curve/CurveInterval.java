package com.pml.route.business.sr.model.Curve;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurveInterval {
    private Double r = null;
    private Double b = null;
    private Double t0 = null;
    private Double t1 = null;
    private CurveInterval next = null;
    private CurveInterval pri = null;
    public Double subs(Double time){
        Double value = this.r * this.b;
        return value;
    }
    public CurveInterval copy()
    {
        CurveInterval dup = new CurveInterval();
        dup.r = this.r;
        dup.b = this.b;
        dup.t1 = this.t1;
        dup.t0 = this.t0;
        return dup;
    }
}
