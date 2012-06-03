/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package citationhistogram;

import java.io.IOException;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

/**
 *
 * @author dfserrano
 */
public class Reduce extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
    
    @Override
    public void reduce(IntWritable key, Iterable<IntWritable> values,
                Context context) throws IOException, InterruptedException {

        int count = 0;
        
        for (IntWritable val : values) {
            count += val.get();
        }

        context.write(key, new IntWritable(count));

    }
}
