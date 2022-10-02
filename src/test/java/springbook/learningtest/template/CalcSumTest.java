package springbook.learningtest.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class CalcSumTest {
    Calculator calculator;
    String numFilePath;
    
    @BeforeEach
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilePath = this.getClass().getResource("/numbers.txt").getPath();
    }
    
    @DisplayName("파일의 숫자 합을 계산하는 테스트코드")
    @Test
    public void sumOfNumbers() throws IOException {
        assertThat(this.calculator.calcSum(this.numFilePath)).isEqualTo(10);
    }
    
    @DisplayName("파일의 숫자 곱을 계산하는 테스트코드")
    @Test
    public void multiplyOfNumbers() throws IOException {
        assertThat(this.calculator.calcMultiply(this.numFilePath)).isEqualTo(24);
    }
    
    @DisplayName("파일의 문자열을 합치는 테스트코드")
    @Test
    public void concatenateStrings() throws IOException {
        assertThat(this.calculator.concatenate(this.numFilePath)).isEqualTo("1234");
    }
}
