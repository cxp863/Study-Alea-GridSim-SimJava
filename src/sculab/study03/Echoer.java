package sculab.study03;

import eduni.simjava.Sim_event;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.Gridlet;

/**
 * Echoer接收一个网格任务，改变状态之后再传送回去
 */
public class Echoer extends GridSim {
    // 构造函数
    public Echoer(String name, double baudRate) throws Exception{
        super(name, baudRate);
    }

    @Override
    public void body() {
        // Sim_event在其后通过copy函数复制内部状态变相实现了左值引用，整个GridSim都有十分浓厚的C++风格，这是其中的一个例证。
        Sim_event simEvent = new Sim_event();

        while(true) {
            // 执行循环，每次获取下一个event实体
            sim_get_next(simEvent);
            if(simEvent.get_tag() == GridSimTags.END_OF_SIMULATION) {
                break;
            } else {
                Gridlet gridlet = (Gridlet) simEvent.get_data();  // 这个地方强制转换，还真不是因为泛型造成的。
                try {
                    // 接收gridlet网格任务，尝试设置状态为成功，然后打印信息，然后获取谁发送的信息，然后echo给他
                    gridlet.setGridletStatus(Gridlet.SUCCESS);
                    System.out.println("Echoer received gridlet which id is " + gridlet.getGridletID());
                    int senderId = simEvent.get_src();
                    super.send(senderId, GridSimTags.SCHEDULE_NOW, GridSimTags.GRIDLET_RETURN, gridlet);
                } catch (Exception e) {
                    System.err.println("errors in Echoer.body() function.");
                }
            }
        }
        // 仿真结束的时候，结束输入输出实体
        super.terminateIOEntities();
    }
}
