package LTSM.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import weka.clusterers.Cobweb;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.clusterers.ModifiedKMeans;

//import weka.core.converters.ConverterUtils.DataSource;

public class GraphCluster {
	private static int numberOfCluster = 6;//29;

	public static void main(String args[]) throws Exception {
		// load data
		ArffLoader loader = new ArffLoader();
		// loader.setFile(new File(
		// "C:\\Program Files\\Weka-3-6\\data\\arff_provenance.arff"));
		// Instances structure = loader.getStructure();
		loader.setSource(new File("C:\\maktas\\workspace\\LTSM\\arff_provenance_10GB_motif.arff"));
		
//		loader.setSource(new File("C:\\maktas\\workspace\\LTSM\\bank.arff"));

		Instances data = loader.getDataSet();
		// train Cobweb
		// Cobweb cw = new Cobweb();
		// cw.buildClusterer(data);
		// ModifiedKMeans skm = new ModifiedKMeans();
		SimpleKMeans skm = new SimpleKMeans();
		String[] options = new String[2];
		options[0] = "-N"; // number of clusters
		options[1] = String.valueOf(numberOfCluster);
		skm.setOptions(options);
//		skm.setInitializeUsingKMeansPlusPlusMethod(true);
		// skm.setPreserveInstancesOrder(true);
		skm.buildClusterer(data);
		//
		System.out.println(skm.toString());
		// /*
		Instances[] data_new = new Instances[skm.numberOfClusters()];
		for (int i = 0; i < data_new.length; i++) {
			data_new[i] = new Instances(data, 10000);
			//System.out.println(data_new[i] + "  " + data);
		}

		// System.out.println(skm.getAssignments().length);
		// for (int ass : skm.getAssignments()) {
		// // System.out.println(ass);
		// data_new[ass].add(data.instance(ass));
		//
		// }

		FileWriter fw = new FileWriter(
				"C:\\maktas\\workspace\\LTSM\\result_bank.txt",
				true);
		BufferedWriter bw = new BufferedWriter(fw);

		for (int i = 0; i < data.numInstances(); i++) {
			// int result = skm.clusterInstance(data.instance(i));
			int result = skm.clusterInstance(data.instance(i));
			data_new[result].add(data.instance(i));
			//System.out.println(data.instance(i));
			bw.append(i + "\t" + result + "\n");
		}

		bw.flush();
		bw.close();
		fw.close();

		// */
		//
		// for (int i = 0; i < data_new.length; i++) {
		// System.out.println(i + " has " + data_new[i].numInstances()
		// + " instances");
		// ArffSaver arfsaver = new ArffSaver();
		// String[] options_arfsaver = new String[2];
		// options_arfsaver[0] = "-o"; // The output file
		// options_arfsaver[1] =
		// "C:\\Users\\peng\\workspace\\LTSM\\arff_provenance_10GB_33_"
		// + i + ".arff";
		//
		// // arfsaver.setDestination(new File("test"));
		// arfsaver.setOptions(options_arfsaver);
		// arfsaver.setInstances(data_new[i]);
		// arfsaver.writeBatch();
		// }

		// Instance current;
		// while ((current = loader.getNextInstance(structure)) != null)
		// cw.updateClusterer(current);
		// cw.updateFinished();

		// loader.setSource(new File(
		// "C:\\Program Files\\Weka-3-6\\data\\arff_provenance_2.arff"));
		// Instances data1 = loader.getDataSet();
		//
		// System.out.println("# - cluster - distribution");
		// for (int i = 0; i < data1.numInstances(); i++) {
		// int cluster = skm.clusterInstance(data1.instance(i));
		// double[] dist = skm.distributionForInstance(data1
		// .instance(i));
		// System.out.print((i + 1));
		// System.out.print(" - ");
		// System.out.print(cluster);
		// System.out.print(" - ");
		// System.out.print(Utils.arrayToString(dist));
		// System.out.println();
		// }
	}
}
