package sculab.study01;

import gridsim.*;

import java.util.Calendar;
import java.util.LinkedList;

/**
 * 创建一个包含三个机器的网格资源
 */
public class Study {
    public static void main(String[] args) {
        try {
            initGridSim();
            GridResource gridResource = createGridResource();
        } catch (Exception e) {
            System.err.println("出错了");
        }
    }

    public static void initGridSim() throws Exception {
        // 内部计时器，用来记录仿真的开始与结束时间
        Calendar calendar = Calendar.getInstance();
        // 统计过程中，不包含在内的文件名称和处理过程名称。
        String[] excludeFiles = {""};
        String[] excludeProcessing = {""};

        // 初始化GridSim包（用户数0，仿真计时器，调试记录true，排除文件，排除的处理过程，报告名称
        GridSim.init(0, calendar, true, excludeFiles, excludeProcessing, null);
    }

    public static GridResource createGridResource() throws Exception{
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
        GridResource gridResource = new GridResource("Resource_0", 100.0, 378519, resourceCharacteristics,
                0.0, 0.0, 0.0, weekends, holidays);

        return gridResource;
    }
}
