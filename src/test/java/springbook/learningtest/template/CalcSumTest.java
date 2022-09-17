package springbook.learningtest.template;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;


public class CalcSumTest {
    @DisplayName("파일의 숫자 합을 계산하는 테스트코드")
    @Test
    public void sumOfNumbers() throws IOException {
        final Calculator calculator = new Calculator();
        
        final int sum = calculator.calcSum(
                this.getClass()
                        .getResource("/numbers.txt")
                        .getPath()
        );
        
        assertThat(sum, is(10));
    }
}
