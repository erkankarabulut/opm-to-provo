package LTSM.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Calculator {
	public static void main(String[] args) {
		int index = 0;
		int num_process = 0, num_artifact = 0, num_agent = 0;
		int num_in_edge_process = 0, num_in_edge_artifact = 0, num_in_edge_agent = 0, num_out_edge_process = 0, num_out_edge_artifact = 0, num_out_edge_agent = 0;

		try {
			FileReader fr = new FileReader("C:\\maktas\\workspace\\LTSM\\test_arff\\arffDB\\arff_provenance_10GB_14.txt");
			BufferedReader br = new BufferedReader(fr);

			String myreadline;
			while (br.ready()) {
				myreadline = br.readLine();
				if (myreadline.startsWith("#"))
					continue;

				if (myreadline.contains("nam")) {
					String[] elements = myreadline.split(",");

					for (int i = 1; i < elements.length; i += 4) {
						if (elements[i].equalsIgnoreCase("process")) {
							num_process += Integer.parseInt(elements[i + 1]);
							num_in_edge_process += Integer
									.parseInt(elements[i + 1])
									* Double.parseDouble(elements[i + 2]);
							num_out_edge_process += Integer
									.parseInt(elements[i + 1])
									* Double.parseDouble(elements[i + 3]);
						} else if (elements[i].equalsIgnoreCase("artifact")) {
							num_artifact += Integer.parseInt(elements[i + 1]);
							num_in_edge_artifact += Integer
									.parseInt(elements[i + 1])
									* Double.parseDouble(elements[i + 2]);
							num_out_edge_artifact += Integer
									.parseInt(elements[i + 1])
									* Double.parseDouble(elements[i + 3]);
						} else if (elements[i].equalsIgnoreCase("agent")) {
							num_agent += Integer.parseInt(elements[i + 1]);
							num_in_edge_agent += Integer
									.parseInt(elements[i + 1])
									* Double.parseDouble(elements[i + 2]);
							num_out_edge_agent += Integer
									.parseInt(elements[i + 1])
									* Double.parseDouble(elements[i + 3]);
						}
					}
					
					index++;
				}

			}

			System.out.println("num_process: "+num_process/index);
			System.out.println("num_artifact: "+num_artifact/index);
			System.out.println("num_agent: "+num_agent/index);
			
			System.out.println("average_in_edge_process: "+1.0*num_in_edge_process/num_process);
			System.out.println("average_in_edge_artifact: "+1.0*num_in_edge_artifact/num_process);
			System.out.println("average_in_edge_agent: "+1.0*num_in_edge_agent/num_agent);
			
			System.out.println("average_out_edge_process: "+1.0*num_out_edge_process/num_process);
			System.out.println("average_out_edge_artifact: "+1.0*num_out_edge_artifact/num_artifact);
			System.out.println("average_out_edge_agent: "+1.0*num_out_edge_agent/num_agent);
			
			br.close();
			br.close();
			fr.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
