/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package citationcount;

import java.io.IOException;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

/**
 *
 * @author dfserrano
 */

public class MapClass extends MapReduceBase
        implements Mapper<Text, Text, Text, Text> {

    public void map(Text key, Text value,
            OutputCollector<Text, Text> output,
            Reporter reporter) throws IOException {

        output.collect(value, key);

    }
}
