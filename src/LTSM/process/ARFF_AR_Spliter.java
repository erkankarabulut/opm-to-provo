package LTSM.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.timeseries.TimeDistance;

public class ARFF_AR_Spliter {
	public static void main(String args[]) throws Exception {

		FileReader fr = new FileReader(
				"C:\\maktas\\workspace\\LTSM\\test_arff\\provenanceDB\\provenance_10GB.txt");
		BufferedReader br = new BufferedReader(fr);

		FileWriter fw = new FileWriter(
				"C:\\maktas\\workspace\\LTSM\\test_arff\\index\\index_arff_provenance_10GB_" + "motif" + ".txt",
				true);
		BufferedWriter bw = new BufferedWriter(fw);

		FastVector atts = new FastVector();

		for (int i = 0; i < 10; i++) {
			atts.addElement(new Attribute("nodeType_" + i));
			atts.addElement(new Attribute("numOfNodes_" + i));
			atts.addElement(new Attribute("AvgInlinks_" + i));
			atts.addElement(new Attribute("AvgOutlinks_" + i));
		}

		Instances data_new = new Instances("DFT", atts, 0);

		int index = 0;

		while (br.ready()) {
			String myreadline = br.readLine();
			if (myreadline.startsWith("#"))
				continue;

			String[] sa = myreadline.split(",");

			if (!sa[0].contains("motif"))
				continue;

			for (int i = 0; i < sa.length; i++) {
				if (sa[i].equals("PROCESS"))
					sa[i] = "0";
				if (sa[i].equals("ARTIFACT"))
					sa[i] = "1";
				if (sa[i].equals("AGENT"))
					sa[i] = "2";
			}

			double[] attr = new double[40];
			for (int j = 0; j < 40; j++) {
				if (j < sa.length - 1)
					attr[j] = Double.parseDouble(sa[j + 1]);
				else
					attr[j] = -1;
			}

			Instance inst = new Instance(1.0, attr);
			data_new.add(inst);

			 bw.append(index + ":\t" + sa[0]);
			 bw.newLine();
			 index++;
		}

		br.close();
		fr.close();
		bw.flush();
		bw.close();
		fw.close();
/*
		ArffSaver arfsaver = new ArffSaver();
		String[] options_arfsaver = new String[2];
		options_arfsaver[0] = "-o"; // The output file
		options_arfsaver[1] = "C:\\maktas\\workspace\\LTSM\\test_arff\\index\\arff_provenance_10GB_animation.arff";

//		arfsaver.setDestination(new File("test"));
		arfsaver.setOptions(options_arfsaver);
//MODIFIED_BY_AKTAS
//		arfsaver.setInstances(data_new);
		arfsaver.writeBatch();
*/		
	}
}
