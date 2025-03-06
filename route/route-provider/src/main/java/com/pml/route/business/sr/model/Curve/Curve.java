package com.pml.route.business.sr.model.Curve;

import com.pml.route.business.sr.model.satNode;
import io.swagger.models.auth.In;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class Curve {
    private Curve nextCurve = null;
    private Curve priCurve = null;
    public satNode priNode=null;
    public String priClass=null;
    public satNode thisNode=null;
    public String thisClass=null;
    public satNode nextNode=null;
    private Integer size = 0;
    private CurveInterval head = null;
    private CurveInterval last = null;
    public Curve()
    {
        this.size=0;
        this.head = null;
        this.last = null;
        CurveInterval node = CurveInterval.builder().r(0.0).b(0.0).t0(0.0).t1(1.0).build();
        insert(node, this.size);
    }
    public Curve(satNode priNode, satNode thisNode, satNode nextNode, String priClass, String thisClass)
    {
        this.size=0;
        this.head = null;
        this.last = null;
        this.priClass = priClass;
        this.priNode = priNode;
        this.thisClass = thisClass;
        this.thisNode = thisNode;
        this.nextNode = nextNode;
        CurveInterval node = CurveInterval.builder().r(0.0).b(0.0).t0(0.0).t1(1.0).build();
        insert(node, this.size);
    }
    public Curve(satNode priNode, satNode thisNode, satNode nextNode, String priClass, String thisClass, Double rate, Double burst)
    {
        this.size=0;
        this.head = null;
        this.last = null;
        this.priClass = priClass;
        this.priNode = priNode;
        this.thisClass = thisClass;
        this.thisNode = thisNode;
        this.nextNode = nextNode;
        CurveInterval node = CurveInterval.builder().r(rate).b(burst).t0(0.0).t1(1.0).build();
        insert(node, this.size);
    }
    private void __check_scale(){
        if (this.size>2){
            this.head.setT0(0.0);
            Integer temp_size = this.size;
            Integer k = 0;
            for (int i = 0; i<temp_size; i++){
                k = temp_size - 1 - i;
                k = Math.min(k, temp_size - 1);
                //i = Math.max(temp_size - k, i);
                if(get(k).getT0().equals(get(k).getT1()))
                {
                    remove(k);
                }
            }
        }
        if(this.size>2){
            Integer temp_size = this.size;
            for(int i = 0;i<temp_size-1;i++){
                Integer k = temp_size-1-i;
                if(get(k).getR().equals(get(k-1).getR()) && get(k).getB().equals(get(k-1).getB()) && get(k).getT0().equals(get(k-1).getT1()))
                {
                    get(k-1).setT1(get(k).getT1());
                    remove(k);
                }
            }
        }
    }
    public Curve copy(){
        CurveInterval cur = this.head;
        Curve dum_link = new Curve();
        while (cur!=null){
            dum_link.insertByTime(cur.copy());
            cur = cur.getNext();
        }
        dum_link.priNode= this.priNode;
        dum_link.priClass=this.priClass;
        dum_link.thisNode=this.thisNode;
        dum_link.thisClass=this.thisClass;
        dum_link.nextNode=this.nextNode;
        dum_link.size = this.size;
        return dum_link;
    }
    public Integer getIndexByTime(Double time){
        Integer index = 0;
        CurveInterval p = this.head;
        if (this.head != null){
            for(int i = 0;i<this.size;i++){
                if((p.getT0()<=time && p.getT1()>time) ||(p.getT0()<=time && p.getT1()>=time && index == this.size-1) ){
                    return index;
                }
                p = p.getNext();
                index += 1;
            }
            if (time == this.last.getT1()){
                index -=1;
            }
        }
        return index;
    }
    public CurveInterval get(Integer index){
        CurveInterval p = this.head;
        for(int i=0; i<index;i++){
            p = p.getNext();
        }
        return p;
    }
    public void insert(CurveInterval curveInterval, Integer index){
        if (this.size==0){
            this.head = curveInterval;
            this.last = curveInterval;
        } else if (index == 0) {
            CurveInterval next_node = this.head;
            curveInterval.setNext(next_node);
            this.head = curveInterval;
            next_node.setPri(curveInterval);
        } else if (index == this.size) {
            CurveInterval pri_node = this.last;
            pri_node.setNext(curveInterval);
            this.last = curveInterval;
            curveInterval.setPri(pri_node);
        }
        else{
            CurveInterval pri_node = this.get(index-1);
            CurveInterval next_node = pri_node.getNext();
            curveInterval.setNext(next_node);
            curveInterval.setPri(pri_node);
            pri_node.setNext(curveInterval);
            next_node.setPri(curveInterval);
        }
        this.size += 1;
    }
    public void  insertByTime(CurveInterval node){
        Double T0 = node.getT0();
        Double T1 = node.getT1();
        Double r = node.getR();
        Double b = node.getB();
        Integer interval_start_index = this.getIndexByTime(T0);
        Integer interval_end_index = this.getIndexByTime(T1);
        if (interval_end_index>interval_start_index){
            if(interval_end_index - interval_start_index>1){
                Integer delete_interval_num = interval_end_index-interval_start_index-1;
                while(delete_interval_num>0){
                    remove(interval_start_index+1);
                    delete_interval_num-=1;
                }
            }
            Integer pri_interval_index = getIndexByTime(T0);
            Integer next_interval_index = getIndexByTime(T1);
            get(pri_interval_index).setT1(T0);
            get(next_interval_index).setT0(T1);
            insert(node ,pri_interval_index + 1);
        } else if (interval_end_index == interval_start_index) {
            node = CurveInterval.builder().r(get(interval_start_index).getR()).b(get(interval_start_index).getB()).t0(T1).t1(get(interval_start_index).getT1()).build();
            get(interval_start_index).setT1(T0);
            insert(node, interval_start_index+1);
            node = CurveInterval.builder().r(r).b(b).t0(T0).t1(T1).build();
            insert(node,interval_start_index+1);
        }
        __check_scale();
    }
    public void  insertByMetrics(Double r, Double b, Double t0, Double t1){
        CurveInterval node = CurveInterval.builder().r(r).b(b).t0(t0).t1(t1).build();
        Double T0 = node.getT0();
        Double T1 = node.getT1();
        r = node.getR();
        b = node.getB();
        Integer interval_start_index = this.getIndexByTime(T0);
        Integer interval_end_index = this.getIndexByTime(T1);
        if (interval_end_index>interval_start_index){
            if(interval_end_index - interval_start_index>1){
                Integer delete_interval_num = interval_end_index-interval_start_index-1;
                while(delete_interval_num>0){
                    remove(interval_start_index+1);
                    delete_interval_num-=1;
                }
            }
            Integer pri_interval_index = getIndexByTime(T0);
            Integer next_interval_index = getIndexByTime(T1);
            get(pri_interval_index).setT1(T0);
            get(next_interval_index).setT0(T1);
            insert(node ,pri_interval_index + 1);
        } else if (interval_end_index == interval_start_index) {
            node = CurveInterval.builder().r(get(interval_start_index).getR()).b(get(interval_start_index).getB()).t0(T1).t1(get(interval_start_index).getT1()).build();
            get(interval_start_index).setT1(T0);
            insert(node, interval_start_index+1);
            node = CurveInterval.builder().r(r).b(b).t0(T0).t1(T1).build();
            insert(node,interval_start_index+1);
        }
        __check_scale();
    }
    public CurveInterval remove(Integer index){
        // 删除了在remove函数中调用__check_scale避免两个函数互相调用。
        CurveInterval remove_node = new CurveInterval();
        if(index==0){
            if(head.equals(last)){
                head = null;
                last = null;
                remove_node = head;
            }
            else {
                remove_node = head;
                head = head.getNext();
                head.setPri(null);
            }
        } else if (index == size - 1) {
            CurveInterval pri_node = get(index-1);
            remove_node = pri_node.getNext();
            pri_node.setNext(null);
            last = pri_node;
        }
        else{
            CurveInterval pri_node = get(index-1);
            CurveInterval next_node = pri_node.getNext().getNext();
            remove_node = pri_node.getNext();
            pri_node.setNext(next_node);
            next_node.setPri(pri_node);
        }
        size -= 1;
        //__check_scale();
        return remove_node;
    }
    public void output(){
        CurveInterval p =head;
        while (p!=null){
//            System.out.println("from link:[ " + priNode.getId() + priClass + thisNode.getId() +" ]"+ " to: [ " + thisNode.getId() + thisClass + nextNode.getId()+" ]");
//            System.out.println("B:"+p.getB() + "R:"+p.getR() + "T0:"+p.getT0() + "T1:"+p.getT1());
            p = p.getNext();
        }
    }
    public Double subs(Double time){
        Integer index = getIndexByTime(time);
        CurveInterval index_curve = get(index);
        Double value = index_curve.getR()*time+index_curve.getB();
        return value;
    }
    public void addBAll(Double new_b){
        for(int i =0; i<this.size;i++)
        {
            get(i).setB(get(i).getB()+new_b);
        }
    }
    public void addAnotherCurveOneInterval(CurveInterval curve)
    {
        Double T0 = curve.getT0();
        Double T1 = curve.getT1();
        Integer pre_interval_index = getIndexByTime(T0);
        Integer next_interval_index = getIndexByTime(T1);
        if (next_interval_index>pre_interval_index && this.head!=null){
            for(int i = pre_interval_index;i<next_interval_index+1;i++)
            {
                if(i==pre_interval_index)
                {
                    CurveInterval new_curve = CurveInterval.builder().r(curve.getR()+get(i).getR()).b(curve.getB()+get(i).getB()).t0(curve.getT0()).t1(get(i).getT1()).build();
                    insertByTime(new_curve);
                } else if (i == next_interval_index) {
                    CurveInterval new_curve = CurveInterval.builder().r(curve.getR()+get(i).getR()).b(curve.getB()+get(i).getB()).t0(get(i).getT0()).t1(curve.getT1()).build();
                    insertByTime(new_curve);
                }
                else {
                    CurveInterval new_curve = CurveInterval.builder().r(curve.getR()+get(i).getR()).b(curve.getB()+get(i).getB()).t0(get(i).getT0()).t1(get(i).getT1()).build();
                    insertByTime(new_curve);
                }
            }
        }
        else if (this.head!=null){
            CurveInterval new_curve = CurveInterval.builder().r(curve.getR()+get(next_interval_index).getR()).b(curve.getB()+get(next_interval_index).getB()).t0(curve.getT0()).t1(curve.getT1()).build();
            insertByTime(new_curve);
        }
        else {
            CurveInterval new_curve = CurveInterval.builder().r(curve.getR()).b(curve.getB()).t0(curve.getT0()).t1(curve.getT1()).build();
            insertByTime(new_curve);
        }
    }
    public void remAnotherCurveOneInterval(CurveInterval curve)
    {
        Double T0 = curve.getT0();
        Double T1 = curve.getT1();
        Integer pre_interval_index = getIndexByTime(T0);
        Integer next_interval_index = getIndexByTime(T1);
        if (next_interval_index>pre_interval_index){
            for(int i = pre_interval_index;i<next_interval_index+1;i++)
            {
                if(i==pre_interval_index)
                {
                    CurveInterval new_curve = CurveInterval.builder().r(get(i).getR()-curve.getB()).b(get(i).getB()-curve.getB()).t0(curve.getT0()).t1(get(i).getT1()).build();
                    insertByTime(new_curve);
                } else if (i == next_interval_index) {
                    CurveInterval new_curve = CurveInterval.builder().r(get(i).getR()-curve.getB()).b(get(i).getB()-curve.getB()).t0(get(i).getT0()).t1(curve.getT1()).build();
                    insertByTime(new_curve);
                }
                else {
                    CurveInterval new_curve = CurveInterval.builder().r(get(i).getR()-curve.getB()).b(get(i).getB()-curve.getB()).t0(get(i).getT0()).t1(get(i).getT1()).build();
                    insertByTime(new_curve);
                }
            }
        }
        else{
            CurveInterval new_curve = CurveInterval.builder().r(get(next_interval_index).getR()-curve.getR()).b(get(next_interval_index).getB()-curve.getB()).t0(curve.getT0()).t1(curve.getT1()).build();
            insertByTime(new_curve);
        }
    }
    public void addAnotherCurve(Curve another_curve){
        CurveInterval temp_curve = another_curve.head;
        while(temp_curve != null){
            addAnotherCurveOneInterval(temp_curve);
            temp_curve = temp_curve.getNext();
        }
    }
    public void remAnotherCurve(Curve another_curve){
        CurveInterval temp_curve = another_curve.head;
        while(temp_curve != null){
            remAnotherCurveOneInterval(temp_curve);
            temp_curve = temp_curve.getNext();
        }
    }
    public Double computeKneePointBetweenTwoCurves(CurveInterval curve_1, CurveInterval curve_2)
    {
        if (curve_1.getR().equals(curve_2.getR()) == true)
        {
            return -1.0;
        }
        Double knee_point = (curve_1.getB()-curve_2.getB())/(curve_2.getR()-curve_1.getR());
        if (knee_point<=Math.min(curve_1.getT1(),curve_2.getT1()) && knee_point>=Math.max(curve_1.getT0(),curve_2.getT0()))
        {
            return knee_point;
        }
        else{
            return -1.0;
        }
    }
    public Double computeKneePoint(CurveInterval other_curve)
    {
        CurveInterval temp_curve = head;
        Double knee_point = -1.0;
        while(knee_point==-1.0 && temp_curve!=null)
        {
            knee_point = computeKneePointBetweenTwoCurves(temp_curve, other_curve);
            temp_curve = temp_curve.getNext();
        }
        return knee_point;
    }
    public Double computeMaxBuffer(Double r, Double T0)
    {
        Integer index = getIndexByTime(T0);
        Double max_Buffer = 0.0;
        for(int i=0;i<this.size;i++)
        {
            CurveInterval curve = get(i);
            if(get(i).getR()>=r){
                Double temp_max_buffer = curve.getR()*curve.getT1()+curve.getB()-r*(curve.getT1()>T0?(curve.getT1()-T0):0.0);
                 max_Buffer = Math.max(max_Buffer,temp_max_buffer);
//                System.out.println("r:"+this.head.getR()+"b:"+this.head.getB());
            }
            else{
                Double t = Math.max(T0,get(i).getT0());
                Double temp_max_buffer = get(i).getR()*t+get(i).getB()-r*(t>T0?(t-T0):0.0);
                max_Buffer = Math.max(max_Buffer, temp_max_buffer);
            }
        }
        return max_Buffer;
    }

    public Double computeMaxBuffer(Double r, Double T0, Double s){
        System.out.println("d");
        return r;
    }
    public Curve physicalLimitedCurve(Double cbsServericeRate, Double maxPacketLength, Double maxTime)
    {
        CurveInterval f = CurveInterval.builder().r(cbsServericeRate).b(maxPacketLength).t0(0.0).t1(maxTime).build();
        Double tKnee = computeKneePoint(f);
        Curve limitedCurve = copy();
        if (tKnee>0)
        {
            f = CurveInterval.builder().r(cbsServericeRate).b(maxPacketLength).t0(0.0).t1(tKnee).build();
            limitedCurve.insertByTime(f);
        }
        return limitedCurve;
    }
}
