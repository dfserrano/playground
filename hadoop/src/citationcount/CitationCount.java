/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package citationcount;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

/**
 *
 * @author dfserrano
 */
public class CitationCount extends Configured implements Tool {

    public int run(String[] args) throws Exception {

        Configuration conf = getConf();

        JobConf job = new JobConf(conf, CitationCount.class);

        Path in = new Path(args[0]);
        Path out = new Path(args[1]);

        FileInputFormat.setInputPaths(job, in);
        FileOutputFormat.setOutputPath(job, out);

        job.setJobName("CitationCount");
        job.setMapperClass(MapClass.class);
        job.setReducerClass(Reduce.class);
        job.setInputFormat(KeyValueTextInputFormat.class);
        job.setOutputFormat(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.set("key.value.separator.in.input.line", ",");

        JobClient.runJob(job);

        return 0;

    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new CitationCount(), args);

        System.exit(res);

    }
}
