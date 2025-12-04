import java.util.Comparator;

/**
 * 游乐设施接口：定义核心功能
 */
public interface RideInterface {
    // Part3：排队管理
    void addVisitorToQueue(Visitor visitor);
    void removeVisitorFromQueue();
    void printQueue();

    // Part4A：游玩历史管理
    void addVisitorToHistory(Visitor visitor);
    boolean checkVisitorFromHistory(Visitor visitor);
    int numberOfVisitors();
    void printRideHistory();

    // Part4B：排序
    void sortRideHistory(Comparator<Visitor> comparator);

    // Part5：运行周期
    void runOneCycle();

    // Part6-7：导入导出
    void exportRideHistory(String filePath);
    void importRideHistory(String filePath);
}