package sculab.study03;

import gridsim.*;

import java.util.Calendar;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        try {
            initGridSim();
            GridletList gridlets = createGridLet();
            Sender sender = new Sender("Sender", 560.0, gridlets);
            GridSim.startGridSimulation();
            GridletList receivedGridlets = sender.getReceivedGridlets();
            printGridlets(receivedGridlets);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * study01里面复制来的
     * @throws Exception
     */
    public static void initGridSim() throws Exception {
        // 内部计时器，用来记录仿真的开始与结束时间
        Calendar calendar = Calendar.getInstance();
        // 统计过程中，不包含在内的文件名称和处理过程名称。
        String[] excludeFiles = {""};
        String[] excludeProcessing = {""};

        // 初始化GridSim包（用户数0，仿真计时器，调试记录true，排除文件，排除的处理过程，报告名称
        GridSim.init(0, calendar, true, excludeFiles, excludeProcessing, null);
    }

    /**
     * study02里面复制来的
     * @return
     */
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

    private static void printGridlets(GridletList gridlets) {
        for (int i = 0; i < gridlets.size(); i++) {
            System.out.print(gridlets.get(i).getGridletID() + ": " );
            if(gridlets.get(i).getGridletStatus() == Gridlet.SUCCESS) {
                System.out.println("Success!");
            } else {
                System.out.println("False!");
            }
        }
    }
}
