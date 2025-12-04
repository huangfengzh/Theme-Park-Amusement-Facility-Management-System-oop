import java.time.LocalTime;

/**
 * 员工类（继承Person）：游乐设施操作员
 */
public class Employee extends Person {
    private String position;        // 职位（如：操作员、维护员）
    private LocalTime workStartTime;// 上班时间（如：09:00）
    private LocalTime workEndTime;  // 下班时间（如：18:00）

    // 带参构造器
    public Employee(String id, String name, int age, String position, String workStart, String workEnd) {
        super(id, name, age);
        this.position = position;
        this.workStartTime = LocalTime.parse(workStart);
        this.workEndTime = LocalTime.parse(workEnd);
    }

    // 校验员工是否在排班时间内（当前时间是否在上班-下班之间）
    public boolean isOnDuty() {
        LocalTime now = LocalTime.now();
        return !now.isBefore(workStartTime) && !now.isAfter(workEndTime);
    }

    // Getter和Setter
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalTime getWorkStartTime() {
        return workStartTime;
    }

    public LocalTime getWorkEndTime() {
        return workEndTime;
    }

    // 实现父类抽象方法（移除@Override注解，因父类为抽象方法，无需注解）
    public String getPersonType() {
        return "员工";
    }

    // 重写toString
    @Override
    public String toString() {
        return "员工ID: " + getId() + ", 姓名: " + getName() + ", 年龄: " + getAge() +
                ", 职位: " + position + ", 排班: " + workStartTime + "-" + workEndTime;
    }
}