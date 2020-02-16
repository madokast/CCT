package zrx.DemoAndStudy.test;

@SuppressWarnings("all")
public class A {
    int a;

    //成员函数
    //自身加5。用于对象的自我修改
    void add5ToSelf() { this.a += 5; }

    //自身加5并返回，用于跑火车
    A add5AndReturnSelf() {
        this.a += 5;
        return this;
    }

    //静态函数
    //复制对象后加5。
    //用于不能修改原对象的场合，例如调用此函数后我还要使用原对象
    static A add5(A aA) {
        A t = new A(aA);//复制对象
        t.a += 5;
        return t;
    }

    //构造函数
    public A(A a) {
        this.a = a.a;
    }
}
