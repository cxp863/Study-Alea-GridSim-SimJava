package sculab.study05;

import gridsim.*;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        try {
            initGridSim();
            GridResource gridResource1 = createGridResource("Resource_1");
            GridResource gridResource2 = createGridResource("Resource_2");
            GridResource gridResource3 = createGridResource("Resource_3");

            GridletList gridlets = createGridlets();
            SimEntity simEntity = new SimEntity("simEntity", 560.0, gridlets, 3);
            GridSim.startGridSimulation();
            printGridlets(simEntity.getReceivedGridlets());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    /**
     * Study01 里面复制来的，用来初始化GridSim包
     * 注意更改了用户数量为1。因为这次需要用户向网格提交资源
     */
    private static void initGridSim() {
        // 内部计时器，用来记录仿真的开始与结束时间
        Calendar calendar = Calendar.getInstance();
        // 统计过程中，不包含在内的文件名称和处理过程名称。
        String[] excludeFiles = {""};
        String[] excludeProcessing = {""};

        // 初始化GridSim包（用户数1，仿真计时器，调试记录true，排除文件，排除的处理过程，报告名称
        GridSim.init(1, calendar, true, excludeFiles, excludeProcessing, null);
    }

    /**
     * Study01 里面复制来的，用来创建网格资源
     * @return
     * @throws Exception
     */
    public static GridResource createGridResource(String resourceName) throws Exception{
        // 创建机器列表，并添加机器（提供机器的id，CPU数量，MIPS值）。PS：这个机器似乎没地方设置内存数量？
        MachineList machines = new MachineList();
        machines.add(new Machine(0, 4, 538));
        machines.add(new Machine(1, 4, 538));
        machines.add(new Machine(2, 2, 538));

        // 创建资源属性对象（机器架构，操作系统，机器列表，分配策略，时区，每秒的花费）
        // todo 这里的分配策略是什么鬼
        ResourceCharacteristics resourceCharacteristics =
                new ResourceCharacteristics("mipsel 24k", "Padavan", machines,
                        ResourceCharacteristics.TIME_SHARED, 8.0, 1.0);

        // 一周干几天，哪些节假日不干活。这些主要是用来设置工作背景负载的。
        LinkedList<Integer> weekends = new LinkedList<>();
        weekends.add(Calendar.SUNDAY);
        weekends.add(Calendar.SATURDAY);

        LinkedList<Integer> holidays = new LinkedList<>();

        // 创建网格资源（资源名称，带宽，随机数种子，资源属性，背景工作负载与衰减因子（峰值与谷值，一天24个小时的实际仿真负载是根据
        // 一个负载矩阵加上随机扰动生成的。另外，相对于工作日，假日的负载会相对于工作日乘以衰减因子），周末假日与法定假日链表。
        GridResource gridResource = new GridResource(resourceName, 100.0, 378519, resourceCharacteristics,
                0.0, 0.0, 0.0, weekends, holidays);

        return gridResource;
    }

    /**
     * 从Study03 复制过来的，打印网格任务列表
     * @param gridlets
     */
    private static void printGridlets(GridletList gridlets) {
        for (int i = 0; i < gridlets.size(); i++) {
            System.out.print(gridlets.get(i).getGridletID() + ": " );
            if(gridlets.get(i).getGridletStatus() == Gridlet.SUCCESS) {
                System.out.print("Success ");
            } else {
                System.out.print("False ");
            }

            System.out.println(gridlets.get(i).getResourceName(gridlets.get(i).getResourceID()) + " " + gridlets.get(i).getProcessingCost());

        }
    }

    /**
     * 从Study02里面复制来的
     * @return
     */
    private static GridletList createGridlets() {
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
}
