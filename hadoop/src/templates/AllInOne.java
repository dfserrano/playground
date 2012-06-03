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
package templates;

import java.io.IOException;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author dfserrano
 */
public class AllInOne extends Configured implements Tool {
    
    /**
     * MAP <K1, V1, K2, V2>
     */
    public static class MapClass extends Mapper<LongWritable, Text, Text, IntWritable> {

        @Override
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            
        }
    }
    
    
    /**
     * REDUCE <K2, V2, K3, V3>
     */
    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) 
                throws IOException, InterruptedException {
            for (IntWritable val : values) {
            
            }
        }
    }

    public int run(String[] args) throws Exception {

        Job job = new Job(getConf());
        job.setJarByClass(AllInOne.class);
        job.setJobName("JobName");

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(MapClass.class);
        //job.setCombinerClass(Reduce.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(KeyValueTextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        
        Path in = new Path(args[0]);
        Path out = new Path(args[1]);
        
        FileInputFormat.setInputPaths(job, in);
        FileOutputFormat.setOutputPath(job, out);

        boolean success = job.waitForCompletion(true);
        
        return success ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int ret = ToolRunner.run(new AllInOne(), args);
        System.exit(ret);
    }
}
