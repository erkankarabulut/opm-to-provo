package LTSM.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class ResultCompare {
	public static void main(String args[]) {
		try {
			FileReader fr1 = new FileReader("C:\\maktas\\workspace\\LTSM\\index_arff_provenance_10GB_DFT.txt");
			BufferedReader br1 = new BufferedReader(fr1);
			FileReader fr2 = new FileReader(
					"C:\\maktas\\workspace\\LTSM\\result_arff_provenance_10GB_DFT_28_new.txt");
			BufferedReader br2 = new BufferedReader(fr2);

			/*
			 * first pass of input file
			 */
			String myreadline1, myreadline2;
			int numOfFailures = 0, numberOfCorrect = 0, totalNumberOfRecord = 0;
			int animation = 0, gene2life = 0, ncfs = 0, scoop = 0, nam = 0, motif = 0;

			HashMap<String, Integer> map = new HashMap<String, Integer>();

			// String animation_symbol_1 = "2", animation_symbol_2 = "6",
			// animation_symbol_3 = "7", animation_symbol_4 = "8";
			// String gene2life_symbol = "n";
			// String ncfs_symbol = "4";
			// String scoop_symbol = "1";
			// String nam_symbol = "9";
			// String motif_symbol_1 = "5", motif_symbol_2 = "3";

			while (br1.ready()) {
				if (!br2.ready()) {
					System.out.println("exception");
					break;
				}

				totalNumberOfRecord++;
				
				myreadline1 = br1.readLine();
				// System.out.println(myreadline1);
				myreadline2 = br2.readLine();
				// System.out.println(myreadline2);

				String[] sa1 = myreadline1.split("\t");
				String[] sa2 = myreadline2.split("\t");

//				System.out.println(sa1[1].split("/|-")[3]);
				
				if (map.containsKey(sa2[1] + ";" + sa1[1].split("/|-")[3])) {
					int tmp = map.get(sa2[1] + ";" + sa1[1].split("/|-")[3]) + 1;
					map.put(sa2[1] + ";" + sa1[1].split("/|-")[3], tmp);
				}else{
					map.put(sa2[1] + ";" + sa1[1].split("/|-")[3], 1);
				}

				// if (sa1[1].contains("animation")
				// && ((sa2[1].equalsIgnoreCase(animation_symbol_1) || sa2[1]
				// .equalsIgnoreCase(animation_symbol_2))
				// || sa2[1].equalsIgnoreCase(animation_symbol_3) || sa2[1]
				// .equalsIgnoreCase(animation_symbol_4))) {
				// animation++;
				// continue;
				// }
				// if (sa1[1].contains("gene2life")
				// && sa2[1].equalsIgnoreCase(gene2life_symbol)) {
				// gene2life++;
				// continue;
				// }
				//
				// if (sa1[1].contains("ncfs")
				// && sa2[1].equalsIgnoreCase(ncfs_symbol)) {
				// ncfs++;
				// continue;
				// }
				// if (sa1[1].contains("scoop")
				// && sa2[1].equalsIgnoreCase(scoop_symbol)) {
				// scoop++;
				// continue;
				// }
				// if (sa1[1].contains("nam-wrf")
				// && sa2[1].equalsIgnoreCase(nam_symbol)) {
				// nam++;
				// continue;
				// }
				// if (sa1[1].contains("motif")
				// && (sa2[1].equalsIgnoreCase(motif_symbol_1) || sa2[1]
				// .equalsIgnoreCase(motif_symbol_2))) {
				// motif++;
				// continue;
				// }
				// numOfFailures++;
			}

			HashMap<String, Integer> map2 = new HashMap<String, Integer>();

			Iterator<String> itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String tmp = itr.next();
				System.out.println(tmp.split(";")[0]+"#"+tmp.split(";")[1]+"#"+map.get(tmp));
				
				if (map2.containsKey(tmp.split(";")[0])) {
					if (map.get(tmp) > map2.get(tmp.split(";")[0]))
						map2.put(tmp.split(";")[0], map.get(tmp));
				} else
					map2.put(tmp.split(";")[0], map.get(tmp));
			}
			System.out.println();
			
			Iterator<String> itr2 = map2.keySet().iterator();
			while (itr2.hasNext()) {
				String tmp = itr2.next();
//				if (map.get(tmp) < map2.get(tmp.split(";")[0]))
				numberOfCorrect+= map2.get(tmp);
				System.out.println(tmp+"#"+map2.get(tmp));
				
			}

			fr1.close();
			br1.close();
			fr2.close();
			br2.close();

//			System.out.println("Num of Failure: " + numOfFailures);
//			System.out.println("animation: " + animation);
//			System.out.println("gene2life: " + gene2life);
//			System.out.println("ncfs: " + ncfs);
//			System.out.println("scoop: " + scoop);
//			System.out.println("nam: " + nam);
//			System.out.println("motif: " + motif);
			
			System.out.println("numberOfCorrect: " + numberOfCorrect);
			System.out.println("numberOfIncorrect: " + (totalNumberOfRecord-numberOfCorrect));
			System.out.println("totalNumberOfRecord: " + totalNumberOfRecord);
			System.out.println("purity: \n"+numberOfCorrect*1.0/totalNumberOfRecord);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
