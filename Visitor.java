/**
 * 访客类（继承Person）：主题公园游客
 */
public class Visitor extends Person {
    private String ticketType;   // 门票类型
    private String visitDate;    // 访问日期（yyyy-MM-dd）
    private double rideCost;     // 游玩消费（元）
    private String rideDuration; // 游玩时长（如：2分30秒）

    // 带参构造器（自动匹配门票类型）
    public Visitor(String id, String name, int age, String ticketType, String visitDate, double rideCost, String rideDuration) {
        super(id, name, age);
        this.ticketType = matchTicketType(age, ticketType); // 按年龄校验门票类型
        this.visitDate = visitDate;
        this.rideCost = rideCost;
        this.rideDuration = rideDuration;
    }

    // 兼容原构造器（避免影响其他调用）
    public Visitor(String id, String name, int age, String ticketType, String visitDate) {
        this(id, name, age, ticketType, visitDate, 0.0, "0分钟");
    }

    // 按年龄匹配合理门票类型
    private String matchTicketType(int age, String inputType) {
        if (age <= 12 && !"儿童票".equals(inputType)) {
            System.out.println("访客" + getName() + "年龄" + age + "岁，自动调整门票类型为儿童票");
            return "儿童票";
        } else if (age <= 22 && !"学生票".equals(inputType) && !"儿童票".equals(inputType)) {
            System.out.println("访客" + getName() + "年龄" + age + "岁，自动调整门票类型为学生票");
            return "学生票";
        } else if (age >= 60 && !"老人票".equals(inputType)) {
            System.out.println("访客" + getName() + "年龄" + age + "岁，自动调整门票类型为老人票");
            return "老人票";
        }
        return inputType;
    }

    // 判断是否为特殊人群（老人/儿童，优先排队）
    public boolean isPriority() {
        int age = getAge();
        return age <= 12 || age >= 60;
    }

    // Getter和Setter
    public String getTicketType() {
        return ticketType;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public double getRideCost() {
        return rideCost;
    }

    public String getRideDuration() {
        return rideDuration;
    }

    // 实现父类抽象方法（移除@Override注解，因父类为抽象方法，无需注解）
    public String getPersonType() {
        return "访客";
    }

    // 重写toString（包含消费和时长）
    @Override
    public String toString() {
        return "ID: " + getId() + ", 姓名: " + getName() + ", 年龄: " + getAge() +
                ", 门票类型: " + ticketType + ", 访问日期: " + visitDate +
                ", 消费: " + rideCost + "元, 时长: " + rideDuration;
    }

    // 重写equals和hashCode（用于contains判断）
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visitor visitor = (Visitor) o;
        return getId().equals(visitor.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}