package LTSM.process;

import java.io.File;

import weka.clusterers.ModifiedKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.timeseries.SimilarityAnalysis;

public class TimeSeriesAnalysis {
	public static void main(String args[]) throws Exception {
		// load data
		ArffLoader loader = new ArffLoader();
		loader.setSource(new File(
				"C:\\maktas\\workspace\\LTSM\\timeseries_provenance_experiment_3.arff"));
		Instances data = loader.getDataSet();
		
//		FastVector atts = new FastVector();
//		atts.addElement(new Attribute("workflow_1_1"));
//		atts.addElement(new Attribute("workflow_2_1"));
//		atts.addElement(new Attribute("workflow_3_1"));
//		Instances data = new Instances("provenance", atts, 0);
//		
//		double[] values = new double[data.numAttributes()];
//		values[0] = 2;
//		values[1] = 2;
//		values[2] = 2;
//		Instance inst = new Instance(1.0, values);
//		data.add(inst);
//		
//		values = new double[data.numAttributes()];
//		values[0] = 1;
//		values[1] = 1;
//		values[2] = 1;
//		inst = new Instance(1.0, values);
//		data.add(inst);
//		
//		values = new double[data.numAttributes()];
//		values[0] = 5;
//		values[1] = 7;
//		values[2] = 7;
//		inst = new Instance(1.0, values);
//		data.add(inst);
//		
//		values = new double[data.numAttributes()];
//		values[0] = 0;
//		values[1] = 0;
//		values[2] = 0;
//		inst = new Instance(1.0, values);
//		data.add(inst);

		SimilarityAnalysis sma = new SimilarityAnalysis();
		String[] options = new String[6];
		options[0] = "-T"; // template range
		options[1] = "first-1";
		
		options[2] = "-e"; // epsilon
		options[3] = "10";
		
		options[4] = "-r"; // number of coefficients
		options[5] = "3";
		
		options[4] = "-f"; // use FFT
		options[5] = "false";
		
		sma.setOptions(options);
		sma.analyze(data);
		System.out.println(sma.toString());
	}
}
