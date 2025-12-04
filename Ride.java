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
    }

    // ------------------------------ Part4A：游玩历史管理 ------------------------------
    @Override
    public void addVisitorToHistory(Visitor visitor) {
        if (visitor != null) {
            rideHistory.add(visitor);
            System.out.println("访客 " + visitor.getName() + " 已添加到 " + rideName + " 游玩历史！" +
                    " 消费：" + visitor.getRideCost() + "元，时长：" + visitor.getRideDuration());
        } else {
            System.out.println("添加失败：访客信息为空！");
        }
    }

    @Override
    public boolean checkVisitorFromHistory(Visitor visitor) {
        if (visitor == null) {
            System.out.println("查询失败：访客信息为空！");
            return false;
        }
        boolean exists = rideHistory.contains(visitor);
        if (exists) {
            System.out.println("访客 " + visitor.getName() + " 曾游玩过 " + rideName + "！");
        } else {
            System.out.println("访客 " + visitor.getName() + " 未游玩过 " + rideName + "！");
        }
        return exists;
    }

    @Override
    public int numberOfVisitors() {
        int count = rideHistory.size();
        System.out.println(rideName + " 游玩历史总人数：" + count);
        return count;
    }

    @Override
    public void printRideHistory() {
        if (rideHistory.isEmpty()) {
            System.out.println(rideName + " 暂无游玩历史！");
            return;
        }
        System.out.println("\n" + rideName + " 游玩历史（共" + rideHistory.size() + "人）：");
        // 必须使用Iterator遍历（否则无分）
        Iterator<Visitor> iterator = rideHistory.iterator();
        int index = 1;
        while (iterator.hasNext()) {
            Visitor visitor = iterator.next();
            System.out.println(index + ". " + visitor);
            index++;
        }
    }

    // ------------------------------ Part4B：游玩历史排序 ------------------------------
    /**
     * 使用Comparator对游玩历史进行排序
     * @param comparator 自定义比较器
     */
    public void sortRideHistory(Comparator<Visitor> comparator) {
        if (rideHistory.isEmpty()) {
            System.out.println("游玩历史为空，无需排序！");
            return;
        }
        Collections.sort(rideHistory, comparator);
        System.out.println(rideName + " 游玩历史排序完成！");
    }

    // ------------------------------ Part5：运行游乐周期（含状态/排班校验） ------------------------------
    @Override
    public void runOneCycle() {
        // 1. 校验设施状态
        if (!"运行中".equals(status)) {
            System.out.println("运行失败：" + rideName + " 当前状态为【" + status + "】，无法运行！");
            return;
        }
        // 2. 校验操作员是否在岗
        if (operator == null || !operator.isOnDuty()) {
            String msg = operator == null ? "未分配操作员" : "操作员" + operator.getName() + "未在排班时间内（" +
                    operator.getWorkStartTime() + "-" + operator.getWorkEndTime() + "）";
            System.out.println("运行失败：" + msg + "！");
            return;
        }
        // 3. 校验队列是否为空
        int totalQueueSize = priorityQueue.size() + normalQueue.size();
        if (totalQueueSize == 0) {
            System.out.println("运行失败：" + rideName + " 无排队访客！");
            return;
        }

        System.out.println("\n" + rideName + " 开始运行第 " + (numOfCycles + 1) + " 周期（单周期最大乘客数：" + maxRider + "，时长：" + cycleDuration + "）！");
        int ridersThisCycle = 0;

        // 先从优先队列取访客
        while (!priorityQueue.isEmpty() && ridersThisCycle < maxRider) {
            addVisitorToHistory(priorityQueue.poll());
            ridersThisCycle++;
        }
        // 优先队列取完后，从普通队列取
        while (!normalQueue.isEmpty() && ridersThisCycle < maxRider) {
            addVisitorToHistory(normalQueue.poll());
            ridersThisCycle++;
        }

        numOfCycles++; // 周期数+1
        System.out.println(rideName + " 第 " + numOfCycles + " 周期运行完成！本次搭载 " + ridersThisCycle + " 人。");
    }

    // ------------------------------ Part6：导出游玩历史到文件（优化CSV格式） ------------------------------
    public void exportRideHistory(String filePath) {
        if (rideHistory.isEmpty()) {
            System.out.println("导出失败：" + rideName + " 暂无游玩历史！");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 写入CSV表头
            writer.write("访客ID,姓名,年龄,门票类型,访问日期,游玩消费(元),游玩时长");
            writer.newLine();
            // 遍历游玩历史，按CSV格式写入文件
            for (Visitor visitor : rideHistory) {
                String line = String.join(",",
                        visitor.getId(),
                        visitor.getName(),
                        String.valueOf(visitor.getAge()),
                        visitor.getTicketType(),
                        visitor.getVisitDate(),
                        String.valueOf(visitor.getRideCost()),
                        visitor.getRideDuration()
                );
                writer.write(line);
                writer.newLine();
            }
            System.out.println("游玩历史已成功导出到：" + filePath);
        } catch (IOException e) {
            System.out.println("导出失败：" + e.getMessage());
        }
    }

    // ------------------------------ Part7：从文件导入游玩历史（增强数据校验） ------------------------------
    public void importRideHistory(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("导入失败：文件 " + filePath + " 不存在！");
            return;
        }

        int importedCount = 0;
        int invalidCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // 跳过表头
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 按逗号分割CSV数据
                String[] parts = line.split(",");
                if (parts.length != 7) {
                    invalidCount++;
                    System.out.println("跳过无效数据行（字段数错误）：" + line);
                    continue;
                }

                try {
                    // 解析数据并校验
                    String id = parts[0];
                    String name = parts[1];
                    int age = Integer.parseInt(parts[2]);
                    String ticketType = parts[3];
                    String visitDate = parts[4];
                    double rideCost = Double.parseDouble(parts[5]);
                    String rideDuration = parts[6];

                    // 校验日期格式
                    LocalDate.parse(visitDate);
                    // 校验门票类型
                    List<String> validTickets = Arrays.asList("儿童票", "学生票", "成人票", "老人票", "年卡", "通票");
                    if (!validTickets.contains(ticketType)) {
                        throw new IllegalArgumentException("门票类型无效");
                    }

                    // 创建Visitor对象并添加到历史
                    Visitor visitor = new Visitor(id, name, age, ticketType, visitDate, rideCost, rideDuration);
                    rideHistory.add(visitor);
                    importedCount++;
                } catch (NumberFormatException e) {
                    invalidCount++;
                    System.out.println("跳过无效数据行（数字格式错误）：" + line);
                } catch (DateTimeParseException e) {
                    invalidCount++;
                    System.out.println("跳过无效数据行（日期格式错误）：" + line);
                } catch (IllegalArgumentException e) {
                    invalidCount++;
                    System.out.println("跳过无效数据行（" + e.getMessage() + "）：" + line);
                }
            }
            System.out.println("导入完成！共导入 " + importedCount + " 条有效记录，跳过 " + invalidCount + " 条无效记录。");
        } catch (IOException e) {
            System.out.println("导入失败：" + e.getMessage());
        }
    }

    // 重写toString()方法
    @Override
    public String toString() {
        String operatorInfo = (operator != null) ? operator.getName() : "未分配";
        return "设施ID: " + rideId + ", 设施名称: " + rideName + ", 最大承载量: " + maxCapacity +
                ", 操作员: " + operatorInfo + ", 已运行周期: " + numOfCycles + ", 状态: " + status;
    }
}