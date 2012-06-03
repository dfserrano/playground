/*
 * But first you’ll need to format your HDFS by using the command
 * $ bin/hadoop namenode -format
 *
 * We can now launch the daemons by use of the start-all.sh script. The Java jps
 * command will list all daemons to verify the setup was successful.
 * $ bin/start-dfs.sh
 * $ bin/start-mapred.sh
 * $ jps
 * 
 * When you’ve finished with Hadoop you can shut down the Hadoop daemons by the command
 * $ bin/stop-dfs.sh
 * $ bin/stop-mapred.sh
 * 
 * Hadoop file commands take the form of
 * $ bin/hadoop fs -cmd <args>
 */
package templates.deprecated;

import templates.*;
import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

/**
 *
 * @author dfserrano
 */
public class AllInOne {
    
    /**
     * MAP <K1, V1, K2, V2>
     */
    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

        @Override
        public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            
        }
    }
    
    
    /**
     * REDUCE <K2, V2, K3, V3>
     */
    public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            while (values.hasNext()) {
            
            }
        }
    }

    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf(AllInOne.class);
        conf.setJobName("wordcount");

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);

        conf.setMapperClass(Map.class);
        conf.setCombinerClass(Reduce.class);
        conf.setReducerClass(Reduce.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
        
        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));

        JobClient.runJob(conf);
    }
}
