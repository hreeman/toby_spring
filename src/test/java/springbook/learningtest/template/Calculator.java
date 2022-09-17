package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(final String filePath) throws IOException {
        final LineCallback sumCallback = (line, value) -> value + Integer.valueOf(line);
        
        return this.lineReadTemplate(filePath, sumCallback, 0);
    }

    public Integer calcMultiply(final String filePath) throws IOException {
        final LineCallback multiplyCallback = (line, value) -> value * Integer.valueOf(line);
    
        return this.lineReadTemplate(filePath, multiplyCallback, 1);
    }

    public Integer lineReadTemplate(final String filePath, final LineCallback callback, final Integer initVal) throws IOException {
        BufferedReader bufferedReader = null;
    
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
        
            Integer res = initVal;
            String line = null;
    
            while ((line = bufferedReader.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }
        
            return res;
        } catch (final IOException e) {
            System.out.println(e.getMessage());
        
            throw e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
