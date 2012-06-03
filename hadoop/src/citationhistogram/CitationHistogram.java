/*
 * Input of this example is the output from CitationCount
 * bin/hadoop jar /home/dfserrano/NetBeansProjects/HadoopExamples/dist/CitationHistogram.jar /user/dfserrano/examples/citationcount/output/part-00000 /user/dfserrano/examples/citationhistogram/output
 */
package citationhistogram;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.*;

/**
 *
 * @author dfserrano
 */
public class CitationHistogram extends Configured implements Tool {

    public int run(String[] args) throws Exception {

        Job job = new Job(getConf());
        job.setJarByClass(CitationHistogram.class);
        job.setJobName("CitationHistogram");

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
        int ret = ToolRunner.run(new CitationHistogram(), args);
        System.exit(ret);
    }
}
