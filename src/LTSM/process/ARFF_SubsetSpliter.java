package LTSM.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class ARFF_SubsetSpliter {

	public static void main(String[] args) {
		Map<Integer, StringBuffer> map = new HashMap<Integer, StringBuffer>();

		try {
			FileReader fr = new FileReader("C:\\maktas\\workspace\\LTSM\\test_arff\\provenanceDB\\provenance_10GB.txt");
			BufferedReader br = new BufferedReader(fr);

			int numberOfSubset = -1, index = 0;
			String myreadline;
			while (br.ready()) {
				myreadline = br.readLine();
				if (myreadline.startsWith("#"))
					continue;

				//System.out.println(myreadline);

				String[] sa = myreadline.split(",");

				if (!map.containsKey(sa.length)) {
					map.put(sa.length, new StringBuffer());
				}

				map.get(sa.length).append(myreadline + "\n");
			}

			//System.out.println(map.keySet().size());  
			br.close();
			fr.close();

			for (Integer num : map.keySet()) {
				//System.out.println("arff_provenance_10GB_" 	+ num + ".txt");
				FileWriter fw = new FileWriter("C:\\maktas\\workspace\\LTSM\\test_arff\\arffSubletDB\\arff_provenance_10GB_"
						+ num + ".txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.append(map.get(num).toString());

				bw.flush();
				bw.close();
				fw.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
