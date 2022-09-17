package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(final String filePath) throws IOException {
        final LineCallback<Integer> sumCallback = (line, value) -> value + Integer.valueOf(line);
        
        return this.lineReadTemplate(filePath, sumCallback, 0);
    }

    public Integer calcMultiply(final String filePath) throws IOException {
        final LineCallback<Integer> multiplyCallback = (line, value) -> value * Integer.valueOf(line);
    
        return this.lineReadTemplate(filePath, multiplyCallback, 1);
    }
    
    public String concatenate(final String filePath) throws IOException {
        final LineCallback<String> concatenateCallback = (line, value) -> value + line;
        
        return this.lineReadTemplate(filePath, concatenateCallback, "");
    }

    public <T> T lineReadTemplate(final String filePath, final LineCallback<T> callback, final T initVal) throws IOException {
        BufferedReader bufferedReader = null;
    
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
        
            T res = initVal;
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
