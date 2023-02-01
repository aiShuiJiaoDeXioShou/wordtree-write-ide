package lh.wordtree;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

public class Test5 {

    @Test
    public void test1() {
        //日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            var date = new Date();
            var year = LocalDateTime.now().getYear();
            //起始日期
            Date d1 = sdf.parse(year + "-1-1");
            //结束日期
            Date d2 = sdf.parse(year + "-12-31");
            Date tmp = d1;
            Calendar dd = Calendar.getInstance();
            dd.setTime(d1);
            //打印2001年10月1日到2001年11月4日的日期
            while (tmp.getTime() <= d2.getTime()) {
                tmp = dd.getTime();
                System.out.println(sdf.format(tmp));
                //天数加上1
                dd.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        var now = LocalDateTime.now();
        var i = now.get(ChronoField.DAY_OF_WEEK);
        System.out.println(i);
    }

}
