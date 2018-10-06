package LTSM.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import weka.clusterers.ModifiedKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.timeseries.TimeDistance;

public class DFT_Filter {

	public static void main(String args[]) throws Exception {

		FileReader fr = new FileReader(
				"C:\\maktas\\workspace\\LTSM\\provenance_10GB.txt");
		BufferedReader br = new BufferedReader(fr);

		FileWriter fw = new FileWriter(
				"C:\\maktas\\workspace\\LTSM\\index_arff_provenance_10GB_DFT.txt",
				true);
		BufferedWriter bw = new BufferedWriter(fw);

		FastVector atts = new FastVector();
		atts.addElement(new Attribute("workflow_type"));
		atts.addElement(new Attribute("re_0"));
		atts.addElement(new Attribute("im_0"));
		atts.addElement(new Attribute("re_1"));
		atts.addElement(new Attribute("im_1"));
		atts.addElement(new Attribute("re_2"));
		atts.addElement(new Attribute("im_2"));

		Instances data_new = new Instances("DFT", atts, 0);

		int index = 0;

		while (br.ready()) {
			String myreadline = br.readLine();
			if (myreadline.startsWith("#"))
				continue;

			String[] sa = myreadline.split(",");

			if (sa[0].contains("nam"))
				sa[0] = "0";
			if (sa[0].contains("ncfs"))
				sa[0] = "1";
			if (sa[0].contains("gene2life"))
				sa[0] = "2";
			if (sa[0].contains("scoop"))
				sa[0] = "3";
			if (sa[0].contains("animation"))
				sa[0] = "4";
			if (sa[0].contains("motif"))
				sa[0] = "5";

			for (int i = 0; i < sa.length; i++) {
				if (sa[i].equals("PROCESS"))
					sa[i] = "0";
				if (sa[i].equals("ARTIFACT"))
					sa[i] = "1";
				if (sa[i].equals("AGENT"))
					sa[i] = "2";
			}

			double[] attr = new double[sa.length - 1];
			for (int j = 1; j < sa.length; j++) {
				attr[j - 1] = Double.parseDouble(sa[j]);
			}

			double[] dft =TimeDistance.DFT(attr);
			double[] res = new double[7];
			res[0] = Double.parseDouble(sa[0]);
			for (int i = 1; i < res.length; i++) {
				res[i] = dft[i-1];
			}
			
			Instance inst = new Instance(1.0, res);
			data_new.add(inst);

			// bw.append(index + ":\t" + sa[0]);
			// bw.newLine();
			// index++;
		}

		br.close();
		fr.close();
		bw.flush();
		bw.close();
		fw.close();

		ArffSaver arfsaver = new ArffSaver();
		String[] options_arfsaver = new String[2];
		options_arfsaver[0] = "-o"; // The output file
		options_arfsaver[1] = "C:\\maktas\\workspace\\LTSM\\arff_provenance_10GB_DFT_tagged.arff";

		// arfsaver.setDestination(new File("test"));
		arfsaver.setOptions(options_arfsaver);
//MODIFIED_BY_AKTAS		
//		arfsaver.setInstances(data_new);
		arfsaver.writeBatch();
	}
}
