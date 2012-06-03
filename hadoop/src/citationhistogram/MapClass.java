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

public class MapClass extends Mapper<Text, Text, IntWritable, IntWritable> {
    
    private final static IntWritable uno = new IntWritable(1);
    private IntWritable citationCount = new IntWritable();
    
    protected void map(Text key, Text value, Context context)
                throws IOException, InterruptedException {

        citationCount.set(Integer.parseInt(value.toString()));
        context.write(citationCount, uno);

    }
    
    //Version 0.2
    //public void map(Text key, Text value, OutputCollector<IntWritable, IntWritable> output, Reporter reporter) throws IOException {
}
