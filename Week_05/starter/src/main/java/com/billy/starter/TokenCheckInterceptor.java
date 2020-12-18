package com.billy.starter;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author lijun2
 * @since 1.8
 */
@Component
@SuppressWarnings("all")
public class TokenCheckInterceptor {

    public TokenCheckInterceptor() {
        System.out.println("===>check start");
    }

    @Override

    public String toString() {
        this.anyMethod(1,2);
        return super.toString();
    }

    @Deprecated()
    /**
     *
     * @param a
     * @param b
     * @return
     */
    private int anyMethod(int a, int b) {
        return a + b;
    }

}



