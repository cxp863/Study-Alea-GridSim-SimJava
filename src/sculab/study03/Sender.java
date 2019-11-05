package sculab.study03;

import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.Gridlet;
import gridsim.GridletList;


public class Sender extends GridSim {
    private String echoerName;
    private GridletList gridlets;
    private GridletList receivedGridlets = new GridletList();

    public Sender(String name, double baudRate, GridletList gridlets) throws Exception {
        super(name);
        this.gridlets = gridlets;

        echoerName = "Echoer";
        // Echoer并不是只新建了一个没用的对象，考虑到Echoer也继承了GridSim，构造函数调用了super，构建了输入输出网络
        // 实际上这让Sender能够把消息发送到这个Echoer里。
        new Echoer(echoerName, baudRate);
    }

    /**
     * 从构造函数那个地方拿到的所有gridlet，挨个发送给echoer，然后发送终止信号
     */
    @Override
    public void body() {
        for(int i=0; i<gridlets.size(); i++) {
            Gridlet gridlet = gridlets.get(i);
            System.out.println("Sender.body() is sending task which id is " + gridlet.getGridletID());
            // GridSimTags.SCHEDULE_NOW 无延迟地向指定的GridSim实体发送网格任
            super.send(echoerName, GridSimTags.SCHEDULE_NOW, GridSimTags.GRIDLET_SUBMIT, gridlet);
            Gridlet receivedGridlet = super.gridletReceive();
            System.out.println("Sender.body() is receiving task which id is " + receivedGridlet.getGridletID());
            receivedGridlets.add(receivedGridlet);
        }
        // 发送终止模拟信号
        super.send(echoerName, GridSimTags.SCHEDULE_NOW, GridSimTags.END_OF_SIMULATION);
    }

    /**
     * 返回收到的gridlets列表，让main类打印
     * @return
     */
    public GridletList getReceivedGridlets() {
        return receivedGridlets;
    }
}
