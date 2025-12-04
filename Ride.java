import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * 游乐设施类，实现RideInterface接口，管理设施信息、排队队列和游玩历史
 */
public class Ride implements RideInterface {
    // 实例变量（至少3个，包含1个Employee类型）
    private String rideId;         // 设施ID
    private String rideName;       // 设施名称（如：过山车、激流勇进）
    private int maxCapacity;       // 设施最大承载量（单周期）
    private Employee operator;     // 操作人员（Employee类型，必填）
    private String status;         // 设施状态（运行中/维护中/故障中）

    // 集合：排队队列（普通队列+优先队列）
    private Queue<Visitor> normalQueue;   // 普通访客队列（FIFO）
    private Queue<Visitor> priorityQueue; // 特殊人群队列（老人/儿童）
    // 集合：游玩历史（使用LinkedList）
    private LinkedList<Visitor> rideHistory;

    // 新增实例变量（Part5要求）
    private int maxRider;          // 单周期最大乘客数
    private int numOfCycles;       // 已运行周期数（默认0）
    private String cycleDuration;  // 单周期运行时长（如：2分钟）

    // 默认构造函数
    public Ride() {
        this.normalQueue = new LinkedList<>();
        this.priorityQueue = new LinkedList<>();
        this.rideHistory = new LinkedList<>();
        this.status = "运行中"; // 默认运行中
        this.numOfCycles = 0;
    }

    // 新增4参数构造器（适配AssignmentTwo中的调用）
    public Ride(String rideId, String rideName, int maxCapacity, Employee operator) {
        this(); // 调用默认构造器初始化集合
        this.rideId = rideId;
        this.rideName = rideName;
        this.maxCapacity = maxCapacity;
        this.operator = operator;
        this.maxRider = setDefaultMaxRider(rideName); // 按设施类型设默认值
        this.cycleDuration = setDefaultDuration(rideName); // 按设施类型设运行时长
        this.numOfCycles = 0;
    }

    // 带参构造函数（初始化所有实例变量）
    public Ride(String rideId, String rideName, int maxCapacity, Employee operator, int maxRider) {
        this(); // 调用默认构造函数初始化集合
        this.rideId = rideId;
        this.rideName = rideName;
        this.maxCapacity = maxCapacity;
        this.operator = operator;
        this.maxRider = maxRider >= 1 ? maxRider : 4;
        this.cycleDuration = setDefaultDuration(rideName);
        this.numOfCycles = 0;
    }

    // 按设施类型设置默认单周期人数
    private int setDefaultMaxRider(String rideName) {
        if (rideName.contains("过山车")) return 8;
        else if (rideName.contains("激流勇进")) return 4;
        else if (rideName.contains("摩天轮")) return 6;
        else return 4;
    }

    // 按设施类型设置默认运行时长
    private String setDefaultDuration(String rideName) {
        if (rideName.contains("过山车")) return "2分30秒";
        else if (rideName.contains("激流勇进")) return "3分钟";
        else if (rideName.contains("摩天轮")) return "10分钟";
        else return "2分钟";
    }

    // Getter和Setter方法
    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getRideName() {
        return rideName;
    }

    public void setRideName(String rideName) {
        this.rideName = rideName;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        if (maxCapacity > 0) {
            this.maxCapacity = maxCapacity;
        } else {
            System.out.println("最大承载量必须为正数！");
        }
    }

    public Employee getOperator() {
        return operator;
    }

    public void setOperator(Employee operator) {
        this.operator = operator;
    }

    public int getMaxRider() {
        return maxRider;
    }

    public void setMaxRider(int maxRider) {
        if (maxRider >= 1) { // 单周期至少1人
            this.maxRider = maxRider;
        } else {
            System.out.println("单周期最大乘客数至少为1！");
        }
    }

    public int getNumOfCycles() {
        return numOfCycles;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ------------------------------ Part3：排队队列管理（含优先队列） ------------------------------
    @Override
    public void addVisitorToQueue(Visitor visitor) {
        if (visitor != null) {
            // 特殊人群加入优先队列，普通人群加入普通队列
            if (visitor.isPriority()) {
                priorityQueue.offer(visitor);
                System.out.println("【优先队列】访客 " + visitor.getName() + "（" + visitor.getTicketType() + "）已加入 " + rideName + " 排队队列！");
            } else {
                normalQueue.offer(visitor);
                System.out.println("访客 " + visitor.getName() + "（" + visitor.getTicketType() + "）已加入 " + rideName + " 排队队列！");
            }
        } else {
            System.out.println("添加失败：访客信息为空！");
        }
    }

    @Override
    public void removeVisitorFromQueue() {
        // 优先队列不为空时，先移除优先队列访客
        if (!priorityQueue.isEmpty()) {
            Visitor removed = priorityQueue.poll();
            System.out.println("【优先队列】访客 " + removed.getName() + " 已从 " + rideName + " 队列移除！");
        } else if (!normalQueue.isEmpty()) {
            Visitor removed = normalQueue.poll();
            System.out.println("访客 " + removed.getName() + " 已从 " + rideName + " 队列移除！");
        } else {
            System.out.println("排队队列为空，无法移除访客！");
        }
    }

    @Override
    public void printQueue() {
        int totalSize = priorityQueue.size() + normalQueue.size();
        if (totalSize == 0) {
            System.out.println(rideName + " 暂无排队访客！");
            return;
        }

        System.out.println("\n" + rideName + " 排队队列（共" + totalSize + "人）：");
        // 打印优先队列
        if (!priorityQueue.isEmpty()) {
            System.out.println("【优先队列】：");
            int index = 1;
            for (Visitor v : priorityQueue) {
                System.out.println("  " + index + ". " + v);
                index++;
            }
        }
        // 打印普通队列
        if (!normalQueue.isEmpty()) {
            System.out.println("【普通队列】：");
            int index = 1;
            for (Visitor v : normalQueue) {
                System.out.println("  " + index + ". " + v);
                index++;
            }
        }
