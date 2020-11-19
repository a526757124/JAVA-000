package com.billy.springbean;

import com.billy.springbean.scan.ScanConfig;
import com.billy.springbean.scan.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ScanConfig.class)
public class ScanTest {
    @Autowired
    private Student student;

    @Test
    public void test() {
        student.show();
    }
}
