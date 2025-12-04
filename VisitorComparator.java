import java.util.Comparator;

/**
 * 访客比较器：按访问日期升序、年龄降序排序
 */
public class VisitorComparator implements Comparator<Visitor> {
    @Override
    public int compare(Visitor v1, Visitor v2) {
        // 1. 按访问日期升序（YYYY-MM-DD格式可直接字符串比较）
        int dateCompare = v1.getVisitDate().compareTo(v2.getVisitDate());
        if (dateCompare != 0) {
            return dateCompare;
        }
        // 2. 日期相同则按年龄降序
        return Integer.compare(v2.getAge(), v1.getAge());
    }
}