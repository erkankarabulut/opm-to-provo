package LTSM.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class ARFF_Filter {
	public static void main(String[] args) {
		try {
			FileReader fr = new FileReader("C:\\maktas\\workspace\\LTSM\\test_arff\\provenanceDB\\provenance_10GB.txt");
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter("C:\\maktas\\workspace\\LTSM\\test_arff\\provenanceDB\\provenance_10GB_motif.txt",
					true);
			BufferedWriter bw = new BufferedWriter(fw);

			String myreadline;
			while (br.ready()) {
				myreadline = br.readLine();
				if (myreadline.startsWith("#"))
					continue;

				if(myreadline.contains("motif")){
					bw.append(myreadline);
					bw.newLine();
				}

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
}
