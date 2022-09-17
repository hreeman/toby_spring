package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(final String filePath) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        
        Integer sum = 0;
        String line = null;
        
        while ((line = bufferedReader.readLine()) != null) {
            sum += Integer.valueOf(line);
        }
        
        bufferedReader.close();
        
        return sum;
    }
}
