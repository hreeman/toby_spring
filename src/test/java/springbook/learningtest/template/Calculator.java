package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(final String filePath) throws IOException {
        final BufferedReaderCallback sumCallback = bufferedReader -> {
            Integer sum = 0;
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                sum += Integer.valueOf(line);
            }

            return sum;
        };
        
        return this.fileReadTemplate(filePath, sumCallback);
    }
    
    public Integer fileReadTemplate(final String filePath, final BufferedReaderCallback callback) throws IOException {
        BufferedReader bufferedReader = null;
    
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            
            int ret = callback.doSomethingWithReader(bufferedReader);
            
            return ret;
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
    
    public Integer calcMultiply(final String filePath) throws IOException {
        final BufferedReaderCallback multiplyCallback = bufferedReader -> {
            Integer multiply = 1;
            String line = null;
        
            while ((line = bufferedReader.readLine()) != null) {
                multiply *= Integer.valueOf(line);
            }
        
            return multiply;
        };
    
        return this.fileReadTemplate(filePath, multiplyCallback);
    }
}
