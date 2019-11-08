package sculab.study04;

import gridsim.*;

import java.util.LinkedList;

/**
 * 实际上，这个实体做的事情，一定程度上类似于一个调度器
 * 它一直尝试获取计算资源列表，当发现列表不为空的时候，拿到第一个计算资源，也就是一个网格（有若干计算节点的计算机）
 * 然后向这个网格发送计算任务，并等待计算任务结束，拿到返回的计算任务对象（gridlet）
 * 执行完毕之后，它关闭了调度仿真。
 *
 * 计算资源GridResource，计算任务Gridlet，我都拆分到了Main类里面，用静态内容表示了。
 */

public class SimEntity extends GridSim {
    private Integer id;
    private String name;
    // 任务列表
    private GridletList gridlets;
    // 任务执行完之后，回收的任务列表
    private GridletList receivedGridlets = new GridletList();

    public SimEntity(String name, double baudRate, GridletList gridlets) throws Exception{
        super(name, baudRate);
        this.name = name;
        this.id = getEntityId(name);
        this.gridlets = gridlets;

        // 设置gridlets的ID
        for(int i=0; i<gridlets.size(); i++) {
            gridlets.get(i).setUserID(this.id);
        }
    }

    public GridletList getReceivedGridlets() {
        return receivedGridlets;
    }

    @Override
    public void body() {
        int resourceId;
        // 这个地方只创建了一个网格资源，所以while这么一下没问题，拿到一个资源，记住id，后边直接就可以向这个资源提交任务了
        while(true) {
            super.gridSimHold(1.0);
            // 资源列表，其实是资源id列表，没泛型
            LinkedList<Integer> resourceIdList = super.getGridResourceList();
            if (!resourceIdList.isEmpty()) {
                resourceId = resourceIdList.get(0);
                // 向资源实体请求发送资源属性
                super.send(resourceId, GridSimTags.SCHEDULE_NOW, GridSimTags.RESOURCE_CHARACTERISTICS, this.id);
                // 获取网格资源属性
                ResourceCharacteristics resourceCharacteristics = (ResourceCharacteristics) super.receiveEventObject();
                String resourceName = resourceCharacteristics.getResourceName();
                // 记录事件
                super.recordStatistics("Received ResourceCharacteristics from " + resourceName, "");
                break;
            } else {
                System.out.println("等待获取资源...");
            }
        }

        for(int i=0; i<this.gridlets.size(); i++) {
            Gridlet gridlet = this.gridlets.get(i);
            // 把网格任务发送到指定id的网格资源上
            super.gridletSubmit(gridlet, resourceId);
            // 等待网格资源返回网格任务，添加到列表里
            Gridlet receivedGridlet = super.gridletReceive();
            this.receivedGridlets.add(receivedGridlet);
        }

        // 记录了事件，就得关了它
        super.shutdownGridStatisticsEntity();
        super.shutdownUserEntity();
        super.terminateIOEntities();
    }
}
