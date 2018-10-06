package weka.timeseries;

import java.io.File;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class TimeDistance {
	public static double[] DFT(double[] value) throws Exception {

		FastVector atts = new FastVector();
		atts.addElement(new Attribute("workflow_1_1"));
		Instances data = new Instances("provenance", atts, 0);

		for (int i = 0; i < value.length; i++) {
			double[] values = new double[data.numAttributes()];
			values[0] = value[i];
			Instance inst = new Instance(1.0, values);
			data.add(inst);
		}

		SimilarityAnalysis sma = new SimilarityAnalysis();
		String[] options = new String[8];
		options[0] = "-T"; // template range
		options[1] = "first-1";

		options[2] = "-e"; // epsilon
		options[3] = "10";

		options[4] = "-r"; // number of coefficients
		options[5] = "3";

		options[6] = "-f"; // use FFT
		options[7] = "false";

		sma.setOptions(options);
		// sma.analyze(data);

		// System.out.println(sma.toString());
		Instances inst = sma.getFTcoef(data);
		double[] FT = new double[2 * sma.getNumCoeffs()];
		for (int i = 0; i < sma.getNumCoeffs(); i++) {
			FT[i * 2] = inst.instance(i).value(0);
			FT[i * 2 + 1] = inst.instance(i).value(1);
		}

		return FT;
	}

	public static double DFT_distance(double[] value1, double[] value2)
			throws Exception {
		if (value1.length != value2.length) {
			throw new Exception("Two DFT values should be equal length!");
		}

		FastVector atts = new FastVector();
		atts.addElement(new Attribute("workflow_1_1"));
		atts.addElement(new Attribute("workflow_2_1"));
		Instances data = new Instances("provenance", atts, 0);

		for (int i = 0; i < value1.length; i++) {
			double[] values = new double[data.numAttributes()];
			values[0] = value1[i];
			values[1] = value2[i];
			Instance inst = new Instance(1.0, values);
			data.add(inst);
		}

		SimilarityAnalysis sma = new SimilarityAnalysis();
		String[] options = new String[8];
		options[0] = "-T"; // template range
		options[1] = "first-1";

		options[2] = "-e"; // epsilon
		options[3] = "10";

		options[4] = "-r"; // number of coefficients
		options[5] = "3";

		options[6] = "-f"; // use FFT
		options[7] = "false";

		sma.setOptions(options);
		// sma.analyze(data);

		// System.out.println(sma.toString());
		return sma.calculateDistance(data);
	}

	public static double EuDist(double[] value1, double[] value2) {
		double dist = 0;
		for (int i = 0; i < value1.length; i++) {
			dist += (value1[i] - value2[i]) * (value1[i] - value2[i]);
		}

		return Math.sqrt(dist);
	}

	public static void main(String args[]) {
		try {
			double[] value1 = { 2, 1, 5, 1, 1, 8, 2, 2, 1, 1, 2, 2, 2, 1, 1, 1,
					1, 11, 2, 1, 5 };
			double[] value2 = { 2, 1, 7, 1, 1, 8, 2, 3, 1, 1, 3, 2, 2, 3, 1, 1,
					1, 11, 2, 1, 7 };
			double[] value3 = { 2, 1, 7, 1, 1, 8, 2, 3, 1, 1, 1, 10, 2, 1, 2,
					1, 1, 3, 2, 1, 2 };
			// TimeDistance.DFT_distance(value2, value3);
			double[] value4 = { 2, 1, 7, 1, 1, 8, 2, 3, 1, 1, 1, 10, 2, 1, 2,
					1, 1, 3, 2, 1, 2 };
			double[] value5 = { 2, 1, 7, 1, 1, 8, 2, 3, 1, 1, 3, 2, 2, 3, 1, 1,
					1, 11, 2, 1, 5 };
			
			double[] value_d = { 1, 5, 13, 2, 2, 9, 1, 2 , 19.5, 2, 3, 9, 1, 2, 27.5, 2,2,9, 1,1,21 };
			double[] value_d_tm = { 1,5,1,0, 2,2,1,0,1,2,1.5,1,2,3,1,1,1,2,1,3,2,2,1,1,1,1,0,4};
			
			double[] value_a_st = { 2, 1, 1, 0, 1, 1, 2, 1, 2, 2, 1, 1, 1, 2,
					1, 1, 2, 2, 1, 1, 1, 1, 1, 2, 2, 1, 0, 1 };
			double[] value_b_st = { 2, 1, 1, 0, 1, 1, 3, 1, 2, 3, 1, 1, 1, 3,
					1, 1, 2, 3, 1, 1, 1, 1, 1, 3, 2, 1, 0, 1 };
			double[] value_c_st = { 2, 1, 1, 0, 1, 1, 3, 1, 2, 3, 1, 1, 1, 1,
					1, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 1, 0, 1 };
			double[] value_d_st = { 1,7,0.86,0, 2,3,1.67,0,1,3,3,1,2,3,1.67,1,1,2,2,4,2,2,1,3,1,1,0,7};

			System.out.println(TimeDistance
		.DFT_distance(value_a_st, value_b_st));
			// double[] test = TimeDistance.DFT(value2);

			// for(double db : test)
			// System.out.println(db);

			// calculate euclidean dist
//			System.out.println(EuDist(value_b_st, value_d_st));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
