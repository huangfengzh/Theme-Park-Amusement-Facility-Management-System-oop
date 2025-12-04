/**
 * 主演示类：测试所有功能模块
 */
public class AssignmentTwo {
    public static void main(String[] args) {
        // 初始化操作员（真实姓名+合理排班）
        Employee operator1 = new Employee("E001", "张明", 30, "过山车操作员", "09:00", "18:00");
        Employee operator2 = new Employee("E002", "李娟", 28, "激流勇进操作员", "09:30", "17:30");
        Employee operator3 = new Employee("E003", "王浩", 35, "摩天轮操作员", "08:30", "19:00");
        Employee operator4 = new Employee("E004", "赵静", 26, "旋转木马操作员", "10:00", "18:30");

        System.out.println("=== 主题公园游乐设施访客管理系统演示 ===\n");

        // ------------------------------ Part3：排队队列管理演示 ------------------------------
        System.out.println("=== Part3：排队队列管理演示（含老人/儿童优先） ===");
        Ride rollerRide = new Ride("R001", "过山车", 40, operator1);
        // 添加访客（真实姓名+年龄匹配门票+日期区分周末）
        rollerRide.addVisitorToQueue(new Visitor("V001", "陈宇", 28, "成人票", "2025-06-15", 120.0, "2分30秒"));
        rollerRide.addVisitorToQueue(new Visitor("V002", "林晓婷", 22, "学生票", "2025-06-15", 100.0, "2分30秒"));
        rollerRide.addVisitorToQueue(new Visitor("V003", "王建国", 65, "老人票", "2025-06-14", 80.0, "2分30秒")); // 老人（优先）
        rollerRide.addVisitorToQueue(new Visitor("V004", "张思远", 19, "学生票", "2025-06-16", 100.0, "2分30秒"));
        rollerRide.addVisitorToQueue(new Visitor("V005", "刘乐乐", 8, "儿童票", "2025-06-14", 60.0, "2分30秒"));  // 儿童（优先）
        // 打印队列
        rollerRide.printQueue();
        // 移除2名访客（优先队列先出）
        rollerRide.removeVisitorFromQueue();
        rollerRide.removeVisitorFromQueue();
        System.out.println("\n移除2名访客后：");
        rollerRide.printQueue();
        System.out.println();

        // ------------------------------ Part4A：游玩历史管理演示 ------------------------------
        System.out.println("=== Part4A：游玩历史管理演示 ===");
        Ride rapidsRide = new Ride("R002", "激流勇进", 20, operator2);
        // 添加游玩历史（真实姓名+合理消费）
        rapidsRide.addVisitorToHistory(new Visitor("V006", "吴浩然", 26, "成人票", "2025-06-15", 90.0, "3分钟"));
        rapidsRide.addVisitorToHistory(new Visitor("V007", "郑佳怡", 30, "年卡", "2025-06-15", 0.0, "3分钟"));
        rapidsRide.addVisitorToHistory(new Visitor("V008", "马天宇", 21, "学生票", "2025-06-14", 70.0, "3分钟"));
        rapidsRide.addVisitorToHistory(new Visitor("V009", "徐静", 24, "成人票", "2025-06-16", 90.0, "3分钟"));
        rapidsRide.addVisitorToHistory(new Visitor("V010", "郭子轩", 29, "年卡", "2025-06-15", 0.0, "3分钟"));
        // 校验访客是否存在
        Visitor checkVisitor = new Visitor("V008", "马天宇", 21, "学生票", "2025-06-14");
        rapidsRide.checkVisitorFromHistory(checkVisitor);
        rapidsRide.checkVisitorFromHistory(new Visitor("V999", "不存在", 30, "成人票", "2025-06-20"));
        // 统计人数并打印历史
        rapidsRide.numberOfVisitors();
        rapidsRide.printRideHistory();
        System.out.println();

        // ------------------------------ Part4B：游玩历史排序演示 ------------------------------
        System.out.println("=== Part4B：游玩历史排序演示 ===");
        Ride ferrisRide = new Ride("R003", "摩天轮", 36, operator3);
        // 添加未排序的历史（真实姓名+日期差异）
        ferrisRide.addVisitorToHistory(new Visitor("V011", "高雨欣", 23, "成人票", "2025-06-19", 80.0, "10分钟"));
        ferrisRide.addVisitorToHistory(new Visitor("V012", "梁家辉", 35, "年卡", "2025-06-20", 0.0, "10分钟"));
        ferrisRide.addVisitorToHistory(new Visitor("V013", "宋佳琪", 28, "学生票", "2025-06-19", 60.0, "10分钟"));
        ferrisRide.addVisitorToHistory(new Visitor("V014", "邓超", 40, "成人票", "2025-06-20", 80.0, "10分钟"));
        ferrisRide.addVisitorToHistory(new Visitor("V015", "赵丽颖", 25, "年卡", "2025-06-18", 0.0, "10分钟"));
        // 排序前
        System.out.println("排序前的游玩历史：");
        ferrisRide.printRideHistory();
        // 排序（使用自定义比较器）
        ferrisRide.sortRideHistory(new VisitorComparator());
        // 排序后
        System.out.println("\n排序后的游玩历史：");
        ferrisRide.printRideHistory();
        System.out.println();

        // ------------------------------ Part5：运行游乐周期演示 ------------------------------
        System.out.println("=== Part5：运行游乐周期演示 ===");
        Ride cycleRide = new Ride("R001", "过山车", 40, operator1);
        // 添加10名访客（真实姓名+周末客流）
        String[] weekendDates = {"2025-06-14", "2025-06-15", "2025-06-14", "2025-06-15", "2025-06-14"};
        String[] names = {"张艺兴", "杨幂", "鹿晗", "迪丽热巴", "黄子韬", "杨颖", "王俊凯", "王源", "易烊千玺", "关晓彤"};
        for (int i = 0; i < 10; i++) {
            int age = 18 + i;
            String date = weekendDates[i % 5];
            String ticketType = age <= 22 ? "学生票" : "成人票";
            double cost = age <= 22 ? 100.0 : 120.0;
            cycleRide.addVisitorToQueue(new Visitor("V0" + (20 + i), names[i], age, ticketType, date, cost, "2分30秒"));
        }
        // 运行前队列
        System.out.println("运行前的排队队列：");
        cycleRide.printQueue();
        // 运行1个周期
        cycleRide.runOneCycle();
        // 运行后队列和历史
        System.out.println("\n运行后的排队队列：");
        cycleRide.printQueue();
        System.out.println("\n运行后的游玩历史：");
        cycleRide.printRideHistory();
        System.out.println();

        // ------------------------------ Part6：导出游玩历史演示 ------------------------------
        System.out.println("=== Part6：导出游玩历史演示 ===");
        Ride exportRide = new Ride("R002", "激流勇进", 20, operator2);
        exportRide.addVisitorToHistory(new Visitor("V016", "陈伟霆", 32, "成人票", "2025-06-20", 90.0, "3分钟"));
        exportRide.addVisitorToHistory(new Visitor("V017", "刘诗诗", 34, "年卡", "2025-06-20", 0.0, "3分钟"));
        exportRide.addVisitorToHistory(new Visitor("V018", "王嘉尔", 27, "成人票", "2025-06-20", 90.0, "3分钟"));
        exportRide.addVisitorToHistory(new Visitor("V019", "杨紫", 26, "成人票", "2025-06-20", 90.0, "3分钟"));
        exportRide.addVisitorToHistory(new Visitor("V020", "李现", 31, "年卡", "2025-06-20", 0.0, "3分钟"));
        exportRide.exportRideHistory("ride_history_export.csv");
        System.out.println();

        // ------------------------------ Part7：导入游玩历史演示 ------------------------------
        System.out.println("=== Part7：导入游玩历史演示 ===");
        Ride importRide = new Ride("R004", "旋转木马", 24, operator4);
        importRide.importRideHistory("ride_history_export.csv");
        importRide.numberOfVisitors();
        importRide.printRideHistory();

        System.out.println("\n=== 所有功能演示完成 ===");
    }
}