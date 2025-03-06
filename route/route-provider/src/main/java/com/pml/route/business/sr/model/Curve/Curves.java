package com.pml.route.business.sr.model.Curve;

import com.pml.route.business.sr.model.satNode;

public class Curves {
    public Integer size = 0;
    public Curve head = null;
    public Curve last = null;

    public Curves() {
        this.size = 0;
        this.head = null;
        this.last = null;
    }

    private Curve get(Integer index) {
        Curve p = this.head;
        for (int i = 0; i < index; i++) {
            p = p.getNextCurve();
        }
        return p;
    }

    private Curves copy() {
        Curve cur = this.head;
        Curves dup = new Curves();
        Integer index = 0;
        while (cur != null) {
            dup.insert(cur, index);
            cur = cur.getNextCurve();
            index += 1;
        }
        return dup;
    }

    private void insert(Curve data, Integer index) {
        Curve node = data.copy();
        if (this.size == 0) {
            this.head = node;
            this.last = node;
        } else if (index == 0) {
            node.setNextCurve(this.head);
            this.head = node;
        } else if (index != this.size) {
            this.last.setNextCurve(node);
            this.last = node;
        } else {
            Curve pre_node = get(index - 1);
            node.setNextCurve(pre_node.getNextCurve());
            pre_node.setNextCurve(node);
        }
        this.size += 1;
    }

    private Curve remove(Integer index) {
        Curve remove_node = new Curve();
        if (index == 0) {
            if (this.head.equals(this.last)) {
                remove_node = this.head;
                this.head = null;
                this.last = null;
            } else {
                remove_node = this.head;
                this.head = this.head.getNextCurve();
            }
        } else if (index == this.size - 1) {
            Curve pre_node = get(index - 1);
            remove_node = pre_node.getNextCurve();
            pre_node.setNextCurve(null);
            this.last = pre_node;
        } else {
            Curve pre_node = get(index - 1);
            Curve next_node = pre_node.getNextCurve().getNextCurve();
            remove_node = pre_node.getNextCurve();
            pre_node.setNextCurve(next_node);
        }
        this.size -= 1;
        return remove_node;
    }

    public void output() {
        Curve p = this.head;
        while (p != null) {
            p.output();
            p = p.getNextCurve();
        }
    }

    public Curve getOneCurveFromLink(satNode priNode, String priClass, satNode thisNode, String thisClass, satNode nextNode) {
        Curve tempCurve = this.head;
        if (this.head != null) {
            for (int i = 0; i < this.size; i++) {
                if (tempCurve.getPriNode().equals(priNode)
                        && tempCurve.getPriClass().equals(priClass)
                        && tempCurve.getThisNode().equals(thisNode)
                        && tempCurve.getThisClass().equals(thisClass)
                        && tempCurve.getNextNode().equals(nextNode)) {
                    return tempCurve;
                }
                tempCurve = tempCurve.getNextCurve();
            }
        }
        return null;
    }

    public void insertOneCurve(satNode priNode, satNode thisNode, satNode nextNode, String priClass, String thisClass, Double rate, Double burst) {
        Curve newCurve = new Curve(priNode, thisNode, nextNode, priClass, thisClass, rate, burst);
        if (getOneCurveFromLink(priNode, priClass, thisNode, thisClass, nextNode) != null) {
            getOneCurveFromLink(priNode, priClass, thisNode, thisClass, nextNode).addAnotherCurve(newCurve);
        } else {
            this.insert(newCurve, this.size);
        }
    }

    public void removeOneCurve(satNode priNode, satNode thisNode, satNode nextNode, String priClass, String thisClass, Double rate, Double burst) {
        Curve newCurve = new Curve(priNode, thisNode, nextNode, priClass, thisClass, rate, burst);
        Curve tempCurve = this.head;
        try {
            for (int i = 0; i < this.size; i++) {
                if (tempCurve.getPriNode().equals(priNode)
                        && tempCurve.getPriClass().equals(priClass)
                        && tempCurve.getThisNode().equals(thisNode)
                        && tempCurve.getThisClass().equals(thisClass)
                        && tempCurve.getNextNode().equals(nextNode)) {
                    getOneCurveFromLink(priNode, priClass, thisNode, thisClass, nextNode).remAnotherCurve(newCurve);
                    if (getOneCurveFromLink(priNode, priClass, thisNode, thisClass, nextNode).getHead().getR() == 0) {
//                        getOneCurveFromLink(priNode, priClass, thisNode, thisClass, nextNode).remove(i);
//                        System.out.println("kk");
                    }
                    break;
                }
                tempCurve = tempCurve.getNextCurve();
            }
        } catch (Exception e) {
//            throw new RuntimeException(e);
            return;
        }
    }
}
