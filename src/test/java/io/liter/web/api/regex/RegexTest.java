package io.liter.web.api.regex;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexTest {

    private static Logger log = LoggerFactory.getLogger(RegexTest.class);


    @Test
    public void validationPasswd() {
        String password = "test1234!T";
        String pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$"; //8~20자리 숫자 영문 특수문자 혼용
        pattern ="^(?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,20}$"; //8~20자리 숫자 영문 대문자 소문자 특수문자

        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(password);

        if (!matcher.matches()) {
            log.debug("]-----] false [-----[");
        } else {
            log.debug("]-----] true [-----[");
        }

    }
}