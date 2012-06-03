/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package citationcount;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

/**
 *
 * @author dfserrano
 */
public class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, IntWritable> {
    
    @Override
    public void reduce(Text key, Iterator<Text> values,
            OutputCollector<Text, IntWritable> output,
            Reporter reporter) throws IOException {

        int count = 0;

        while (values.hasNext()) {
            values.next();
            count++;
        }

        output.collect(key, new IntWritable(count));
    }
}
