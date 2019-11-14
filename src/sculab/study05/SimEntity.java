package sculab.study05;

import gridsim.*;

import java.util.LinkedList;
import java.util.Random;

/**
 * 这次这个实体多了一个随机选择网格的部分，这意味着它更像一个调度器了
 */

public class SimEntity extends GridSim {
    private Integer id;
    private String name;
    // 任务列表
    private GridletList gridlets;
    // 网格资源总数量，用来判断是不是初始化成功了。
    private int totalResource;
    // 任务执行完之后，回收的任务列表
    private GridletList receivedGridlets = new GridletList();

    public SimEntity(String name, double baudRate, GridletList gridlets, int totalResource) throws Exception{
        super(name, baudRate);
        this.name = name;
        this.id = GridSim.getEntityId(name);
        this.gridlets = gridlets;
        this.totalResource = totalResource;

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
        // 记录所有资源的属性（ID，资源花费，资源名称）（再次提醒：资源就是计算节点的组合）
        int[] resourceIds = new int[this.totalResource];
        double[] resourceCosts = new double[this.totalResource];
        String[] resourceNames = new String[this.totalResource];

        // 循环等待所有资源到位
        while(true) {
            super.gridSimHold(1.0);
            // 资源列表，其实是资源id列表，没泛型
            LinkedList<Integer> resourceIdList = GridSim.getGridResourceList();
            if (resourceIdList.size() != this.totalResource) {
                System.out.println("等待获取全部资源...");
            } else {
                // 所有资源到位之后，把资源特性信息记录到开头的函数内数组里面
                for(int i=0; i<this.totalResource; i++) {
                    resourceIds[i] = resourceIdList.get(i);
                    // 向资源实体发送获取属性请求
                    super.send(resourceIds[i], GridSimTags.SCHEDULE_NOW, GridSimTags.RESOURCE_CHARACTERISTICS, this.id);
                    ResourceCharacteristics resourceCharacteristics = (ResourceCharacteristics) super.receiveEventObject();
                    resourceNames[i] = resourceCharacteristics.getResourceName();
                    resourceCosts[i] = resourceCharacteristics.getCostPerSec();
                    super.recordStatistics("Received ResourceCharacteristics from " + resourceNames[i], "");
                }
                break;
            }
        }


        Random random = new Random();
        for(int i=0; i<this.gridlets.size(); i++) {
            Gridlet gridlet = this.gridlets.get(i);
            // 把网格任务发送到指定id的网格资源上
            super.gridletSubmit(gridlet, resourceIds[random.nextInt(this.totalResource)]);
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
