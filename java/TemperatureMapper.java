package temperature;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TemperatureMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	/*
	 * Missing values in data are 9999.
	 */
	private static final double MISSING = 999.9;
	private static int nBins;
	private static double[] bins;

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String line = value.toString();
		double airTemperature = 9999.;

		if (line.charAt(87) == '+') { // parseInt doesn't like leading plus signs
			airTemperature = Double.parseDouble(line.substring(88, 92));
		} else {
			airTemperature = Double.parseDouble(line.substring(87, 92));
		}

		airTemperature /= 10.;

		String quality = line.substring(92, 93);
		int binId = -9;
		if (airTemperature < MISSING && quality.matches("[01459]")) {
			for (int i = 0; i<nBins; i++) {
				if (airTemperature >= bins[i] && airTemperature < bins[i+1]) {
					binId = i + 1; // following ROOT convention
					break;
				}
			}
			if (binId > 0) {
				context.write(new Text(Integer.toString(binId)), new IntWritable(1));
			}
		}
	}

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		double min = Double.parseDouble(context.getConfiguration().get("histLimit_low"));
		double max = Double.parseDouble(context.getConfiguration().get("histLimit_hi"));
		nBins = Integer.parseInt(context.getConfiguration().get("nBins"));

		double binWidth = (max - min) / nBins;
		bins = new double[nBins+1];
		for (int i = 0; i <= nBins; i++) {
			bins[i] = min + (i * binWidth);
		}
	}
}
