/**
 * 抽象人员类，定义所有人员的共同属性和行为
 * 不可实例化，仅作为Employee和Visitor的父类
 */
public abstract class Person {
    // 实例变量（至少3个）
    private String id;         // 唯一标识（员工号/访客号）
    private String name;       // 姓名
    private int age;           // 年龄

    // 默认构造函数
    public Person() {}

    // 带参构造函数（初始化所有实例变量）
    public Person(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // Getter和Setter方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age > 0 && age <= 120) { // 数据验证
            this.age = age;
        } else {
            System.out.println("年龄输入无效！");
        }
    }

    // 重写toString()方法，用于打印人员信息
    @Override
    public String toString() {
        return "ID: " + id + ", 姓名: " + name + ", 年龄: " + age;
    }
} 