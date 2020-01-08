/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.streaming.examples.windowing;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SessionWindowTimeGapExtractor;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.examples.wordcount.WordCount;
import org.apache.flink.streaming.examples.wordcount.util.WordCountData;

/**
 * Implements a windowed version of the streaming "WordCount" program.
 *
 * <p>The input is a plain text file with lines separated by newline characters.
 *
 * <p>Usage: <code>WordCount --input &lt;path&gt; --output &lt;path&gt; --window &lt;n&gt; --slide &lt;n&gt;</code><br>
 * If no parameters are provided, the program is run with default data from
 * {@link WordCountData}.
 *
 * <p>This example shows how to:
 * <ul>
 * <li>write a simple Flink Streaming program,
 * <li>use tuple data types,
 * <li>use basic windowing abstractions.
 * </ul>
 */
public class WindowWordCount {


	public static void main(String[] args) throws Exception {
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		System.out.println(StreamExecutionEnvironment.getDefaultLocalParallelism());
		// get input data
		DataStream<String> text = env.fromElements(WordCountData.WORDS);

		final int windowSize = 10;
		final int slideSize = 10;

		DataStream<Tuple2<String, Integer>> counts =
		// split up the lines in pairs (2-tuples) containing: (word,1)
		text.flatMap(new WordCount.Tokenizer())
				// create windows of windowSize records slided every slideSize records
				.keyBy(0)
				.countWindow(windowSize, slideSize)
				// group by the tuple field "0" and sum up tuple field "1"
				.sum(1);

		System.out.println("Printing result to stdout.");
		counts.print().setParallelism(1);

		// execute program
		env.execute("WindowWordCount");
	}
}
