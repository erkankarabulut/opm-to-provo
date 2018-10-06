package LTSM.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.timeseries.TimeDistance;

public class RulesChecker {
	public static void main(String args[]) throws Exception {
		List<String> rules = new ArrayList<String>();

		FileReader fr = new FileReader("C:\\maktas\\workspace\\LTSM\\ar_animation.txt");
		BufferedReader br = new BufferedReader(fr);

		ArffLoader loader = new ArffLoader();
		loader.setSource(new File("C:\\maktas\\workspace\\LTSM\\scoop.arff"));

		Instances data = loader.getDataSet();

		while (br.ready()) {
			String myreadline = br.readLine();

			String[] sa = myreadline.split("==>");

			if (sa.length != 2) {
				System.out.println("ERROR format");
				continue;
			}

			String[] toks1 = sa[0].split("\\s");
			String[] toks2 = sa[1].split("\\s");

			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < toks1.length; i++) {
				if (toks1[i].contains("=")) {
					String[] rul = toks1[i].split("=");

//					int offset = 0;
//					if (rul[0].split("_")[0].equalsIgnoreCase("nodeType"))
//						offset = 0;
//					else if (rul[0].split("_")[0]
//							.equalsIgnoreCase("numberOfNodes"))
//						offset = 1;
//					else if (rul[0].split("_")[0]
//							.equalsIgnoreCase("avgInLinks"))
//						offset = 2;
//					else if (rul[0].split("_")[0]
//							.equalsIgnoreCase("avgOutLinks"))
//						offset = 3;
//
//					sb.append((Integer.parseInt(rul[0].split("_")[1]) - 1) * 4
//							+ offset);
					sb.append(Integer.parseInt(rul[0].split("_")[1]));
					sb.append(" ");
					sb.append(rul[1]);
					sb.append(";");
				}

			}

			sb.append(":");

			for (int i = 0; i < toks2.length; i++) {
				if (toks2[i].contains("=")) {
					String[] rul = toks2[i].split("=");

//					int offset = 0;
//					if (rul[0].split("_")[0].equalsIgnoreCase("nodeType"))
//						offset = 0;
//					else if (rul[0].split("_")[0]
//							.equalsIgnoreCase("numberOfNodes"))
//						offset = 1;
//					else if (rul[0].split("_")[0]
//							.equalsIgnoreCase("avgInLinks"))
//						offset = 2;
//					else if (rul[0].split("_")[0]
//							.equalsIgnoreCase("avgOutLinks"))
//						offset = 3;

//					sb.append((Integer.parseInt(rul[0].split("_")[1]) - 1) * 4
//							+ offset);
					sb.append(Integer.parseInt(rul[0].split("_")[1]));
					sb.append(" ");
					sb.append(rul[1]);
					sb.append(";");
				}

			}

			System.out.println(sb.toString());
			rules.add(sb.toString());
		}

		int passed = 0;

		for (int i = 0; i < data.numInstances(); i++) {
			Instance inst = data.instance(i);

			boolean isPassed = true;
			for (String rule : rules) {
				String[] toks = rule.split(":");
				String[] sub_toks_1 = toks[0].split(";");
				String[] sub_toks_2 = toks[1].split(";");

				boolean needToTest = true;
				for (int j = 0; j < sub_toks_1.length; j++) {
					if (inst.numAttributes() <= Integer.parseInt(sub_toks_1[j]
							.split("\\s")[0])) {
						isPassed = false;
						// System.out.println("fail 3");
						break;
					}

					if (!inst.stringValue(
							Integer.parseInt(sub_toks_1[j].split("\\s")[0]))
							.equalsIgnoreCase(sub_toks_1[j].split("\\s")[1])) {
						needToTest = false;
						break;
					}
				}

				if (!isPassed)
					break;

				if (!needToTest)
					continue;

				for (int j = 0; j < sub_toks_2.length; j++) {
					if (inst.numAttributes() <= Integer.parseInt(sub_toks_2[j]
							.split("\\s")[0])) {
						isPassed = false;
						// System.out.println("fail 1");
						break;
					}

					if (!inst.stringValue(
							Integer.parseInt(sub_toks_2[j].split("\\s")[0]))
							.equalsIgnoreCase(sub_toks_2[j].split("\\s")[1])) {
						isPassed = false;
						System.out.println("fail 2");
						System.out.println(inst);
						break;
					}
					
				}

				if (!isPassed)
					break;
			}

			if (isPassed)
				passed++;
		}

		System.out.println(passed + " Passed!");

		br.close();
		fr.close();
	}
}
