package cn.jk.kaoyandanci;

import org.junit.Test;

import cn.jk.kaoyandanci.util.DayUtil;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {


        System.out.print(DayUtil.getStartOfYesterday());
    }
}