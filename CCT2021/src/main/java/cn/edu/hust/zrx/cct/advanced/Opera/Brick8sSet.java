package cn.edu.hust.zrx.cct.advanced.Opera;

import java.util.HashSet;
import java.util.Set;

/**
 * Description
 * 多个 Brick8s
 * <p>
 * Data
 * 16:42
 *
 * @author zrx
 * @version 1.0
 */

public class Brick8sSet {

    Set<Brick8s> set = new HashSet<>();

    public static Brick8sSet empty(){
        return new Brick8sSet();
    }

    public Brick8sSet add(Brick8s brick8s){
        this.set.add(brick8s);
        return this;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CONDUCTOR\n");
        for (Brick8s brick8s : set) {
            sb.append(brick8s.toStringWithoutHeadTail());
        }

        sb.append("QUIT\nEOF\n");

        return sb.toString();
    }
}
