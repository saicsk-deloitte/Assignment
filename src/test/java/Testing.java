import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class Testing {

        @Test
        public void test_JUnit() {
            System.out.println("This is the testcase in this class");
            String str1="This is the testcase in this class";
            assertEquals("This is the testcase in this class", str1);
        }
}
