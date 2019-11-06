package sculab.study02;

import gridsim.*;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;

/**
 * 创建任务和任务列表
 * 示例不创建任何仿真，只创建任务，所以不需要初始化GridSim。
 */
public class Main {
    public static void main(String[] args) {
        try {
            GridletList gridlets = createGridLet();
            ResourceUserList resourceUsers =  createGridUsers(gridlets);
            printGridletsInfo(gridlets);
        } catch (Exception e) {
            System.err.println("出错了");
        }
    }

    private static GridletList createGridLet() {
        // 网格计算任务的容器
        GridletList gridlets = new GridletList();
        // 任务长度，任务执行之前节点文件已经存在的文件大小，任务执行之后节点文件大小，
        double[] mipsLength = {3500, 5000, 9000};
        long[] beforeSimulationFileSizes = {300, 500, 900};
        long[] afterSimulationFileSizes = {300, 500, 900};
        int i = 0;
        // 使用手动设置的方式添加网格任务
        for (; i < mipsLength.length; i++) {
            // 向网格任务链表容器中添加网格任务（ID、任务计算长度，任务执行之前文件的大小，执行之后文件的大小）
            gridlets.add(new Gridlet(i, mipsLength[i], beforeSimulationFileSizes[i], afterSimulationFileSizes[i]));
        }

        // 使用随机的方式添加网格任务
        // 这个类基本上就是静态的，设置的这个MIPS值也是静态的
        GridSimStandardPE.setRating(1000);
        Random random = new Random();
        // 使用GridSimRandom和GridSimStandardPE创建5个任务
        for (; i < 5 + mipsLength.length; i++) {
            // 长度是按照秒计算的，一个标准CPU核心能够计算的值的百万指令数，乘以要执行的秒数得来的。
            double length = GridSimStandardPE.toMIs(random.nextDouble() * 1000);
            // GridSimRandom.real定义自己查看，在注释里面有
            long originFileSize = (long) GridSimRandom.real(100, 0.1, 0.5, random.nextDouble());
            long finalFileSize = (long) GridSimRandom.real(25, 0.1, 0.5, random.nextDouble());

            gridlets.add(new Gridlet(i, length, originFileSize, finalFileSize));
        }

        return gridlets;
    }

    /**
     * 创建网格用户，并且分配给他们网格任务。
     *
     * @param gridlets 网格任务
     * @return 网格用户列表
     */
    private static ResourceUserList createGridUsers(GridletList gridlets) {
        // 创建用户列表（其实就是用户ID列表，用户并不存在一个对象）
        ResourceUserList resourceUsers = new ResourceUserList();
        resourceUsers.add(0);
        resourceUsers.add(1);
        resourceUsers.add(2);

        int id = -1;
        // 分配任务给用户，012给用户0， 345给用户1，67给用户2。这个分配的代码看看就知道咋回事了。
        for (int i = 0; i < gridlets.size(); i++) {
            if(i % resourceUsers.size() == 0) {
                id++;
            }
            gridlets.get(i).setUserID(id);
        }

        return resourceUsers;
    }

    private static void printGridletsInfo(GridletList gridlets) {
        for(int i=0; i<gridlets.size(); i++) {
            Gridlet g = gridlets.get(i);
            System.out.println(g.getGridletID() + " " + g.getUserID() + " " +
                    g.getGridletLength() + " " + g.getGridletFileSize() + " " +
                    g.getGridletOutputSize());
        }
    }
}
