package zrx.beam;


import zrx.CCT.ConcreteCCT.AllCCTs;

import java.util.*;

/**
 * 又一层封装!!
 *
 * 用于并行运行大量的粒子
 * 使用方法很简单，new出这个类的实例，然后add set start
 * 其中start会自动运行所有的粒子，并行!，但是会使用join方法，等到所有的粒子运动完之后才会继续执行main方法
 *
 * 2019年8月27日
 */

public class BeamRun {
    private Set<ParticleDescribe> particleDescribeSet = new HashSet<>();
    private List<Thread> threadList = new ArrayList<>();
    private AllCCTs allCCTs = null;
    private double length;
    private double step;
    private int plotInterval;
    private boolean isSetMotionParameters = false;

    /**
     * 空构造器，减少复杂
     */
    public BeamRun() {
    }

    /**
     * 添加粒子，可反复添加哦
     * @param particle 粒子
     * @param describe 粒子画图时的描述符 见Plot2d
     * @return this
     */
    public BeamRun addParticle(RunningParticle particle, String describe) {
        this.particleDescribeSet.add(new ParticleDescribe(particle, describe));

        return this;
    }

    /**
     * 添加多个粒子
     * @param particles 多个粒子
     * @param describe 描述符
     * @return this
     */
    public BeamRun addParticles(RunningParticle[] particles, String describe) {
        for (int i = 0; i < particles.length; i++) {
            this.addParticle(particles[i], describe);
        }

        return this;
    }

    /**
     * 设定运动参数等信息
     * @param allCCTs 所运动的cct
     * @param length 运动长度
     * @param step 步长
     * @param plotInterval 画图间隔
     * @return this
     */
    public BeamRun setMotionParameters(AllCCTs allCCTs, double length, double step, int plotInterval) {
        if(isSetMotionParameters){
            System.err.println("[警告] 你已经设定过这个BeamRun的运动参数了");
        }

        this.allCCTs = allCCTs;
        this.length = length;
        this.step = step;
        this.plotInterval = plotInterval;

        this.isSetMotionParameters = true;

        return this;
    }

    /**
     * 并行运动所有粒子，但main.joinAll()
     */
    public void startThread() {
        if(particleDescribeSet.size()==0){
            System.err.println("BeamRun中未添加任何粒子");
            System.exit(-1);
        }

        if(!isSetMotionParameters){
            System.err.println("BeamRun中没有设置运动参数");
            System.exit(-1);
        }

        this.particleDescribeSet.forEach(pd->
                threadList.add(pd.getParticle().runInAllCCTsThread(
                        this.allCCTs, this.length, this.step, this.plotInterval, pd.getDescribe()))
                );


        this.threadList.forEach(t->{
            try {
                t.join();
            }catch (Exception e){e.printStackTrace();}
        });
    }


    /**
     * 内部类。包装了RunningParticle particle和 String describe
     */
    private class ParticleDescribe {
        private RunningParticle particle;
        private String describe;

        public ParticleDescribe(RunningParticle particle, String describe) {
            this.particle = particle;
            this.describe = describe;
        }

        public RunningParticle getParticle() {
            return particle;
        }

        public String getDescribe() {
            return describe;
        }
    }
}
