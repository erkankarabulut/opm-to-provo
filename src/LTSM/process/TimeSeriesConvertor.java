package LTSM.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class TimeSeriesConvertor {
	List<String> sequences_name = new ArrayList<String>();
	List<String[]> sequences = new ArrayList<String[]>();

	public void parse(String inputFileUrl, String outputFileUrl) {
		try {
			FileReader fr = new FileReader(inputFileUrl);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(outputFileUrl, true);
			BufferedWriter bw = new BufferedWriter(fw);

			/*
			 * first pass of input file
			 */
			String myreadline;
			int numberOfSubsets = -1;
			while (br.ready()) {
				myreadline = br.readLine();
				if (myreadline.startsWith("#"))
					continue;

				String[] sa = myreadline.split(",");
				if (numberOfSubsets == -1)
					numberOfSubsets = sa.length-1;
				
				if(sa.length != numberOfSubsets+1){
					System.out.println("Invalid:"+sa[0]);
					continue;
				}
				
				sequences_name.add(sa[0]);
				String[] sequence = new String[sa.length - 1];
				
				System.out.println(numberOfSubsets);

				for (int i = 0; i < sequence.length; i++) {
					sequence[i] = sa[i + 1];
				}
				sequences.add(sequence);
			}

			/*
			 * generate output file
			 */
			bw.append("@RELATION provenance_sequence");
			bw.newLine();
			bw.newLine();

			for (String s : sequences_name) {
				bw.append("@ATTRIBUTE " + s + "\t" + "NUMERIC");
				bw.newLine();
			}

			bw.append("@DATA");
			bw.newLine();
			for (int i = 0; i < numberOfSubsets; i++) {
				for (int j = 0; j < sequences.size(); j++) {
					if(j != 0)
						bw.append(",");
					
					if (sequences.get(j)[i].equalsIgnoreCase("PROCESS")) {
						bw.append("1");
					}else if (sequences.get(j)[i].equalsIgnoreCase("ARTIFACT")) {
						bw.append("2");
					}else if (sequences.get(j)[i].equalsIgnoreCase("AGENT")) {
						bw.append("3");
					}else{
						bw.append(sequences.get(j)[i]);
					}
				}
				
				bw.newLine();
			}

			bw.flush();
			bw.close();
			br.close();
			fw.close();
			br.close();
			fr.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		TimeSeriesConvertor tc = new TimeSeriesConvertor();
		tc.parse("provenance_2.txt", "timeseries_provenance_experiment_3.arff");
	}
}
