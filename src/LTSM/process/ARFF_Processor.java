package LTSM.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cgl.webgraph.Vertex;

public class ARFF_Processor {

	
//	public void ARFF_creator(Vertex [] PR){
//			try {
//			
//				FileWriter fw = new FileWriter("C:\\maktas\\workspace\\LTSM\\fileDB\\pagerank_data\\animation\\0\\output\\arff.arff", true);
//				BufferedWriter bw = new BufferedWriter(fw);
//				
//				FileWriter fw_index = new FileWriter("C:\\maktas\\workspace\\LTSM\\fileDB\\pagerank_data\\animation\\0\\output\\index_arff.txt", true);
//				BufferedWriter bw_index = new BufferedWriter(fw_index);
//
//					bw.append("@RELATION provenance");
//					bw.newLine();
//					bw.newLine();
//					for (int i = 1; i <= (PR.length); i++) {
//						bw.append("@ATTRIBUTE PR_" + i + "\t"
//								+ "NUMERIC");
//						bw.newLine();
//					}
//					bw.newLine();
//					bw.append("@DATA");
//					bw.newLine();
//					
//	                for (int j=0; j < PR.length;j++) {					
//	                	bw.append((new Double(PR[j].getPageRank())).toString());
//	                	if (j < PR.length - 1)
//	                		bw.append(",");
//					
//					    bw_index.append(j + ":\t" + PR[j].getName());
//					    bw_index.newLine();
//	                }
//				//}
//                //}
//
//				bw.flush();
//				bw.close();
//				fw.close();
//				bw_index.flush();
//				bw_index.close();
//				fw_index.close();
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}	
//	}
	
	
	
	public static void main(String[] args) {
		try {
			FileReader fr = new FileReader("C:\\maktas\\workspace\\LTSM\\test_arff\\arffDB\\arff_provenance_10GB_14.txt");
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter("C:\\maktas\\workspace\\LTSM\\test_arff\\arffDB\\arff_provenance_10GB_14.arff",
					true);
			BufferedWriter bw = new BufferedWriter(fw);
			FileWriter fw_index = new FileWriter(
					"C:\\maktas\\workspace\\LTSM\\test_arff\\arffDB\\index_arff_provenance_10GB_14.txt", true);
			BufferedWriter bw_index = new BufferedWriter(fw_index);

			int numberOfSubset = -1, index = 0;
			String myreadline;
			while (br.ready()) {
				myreadline = br.readLine();
				if (myreadline.startsWith("#"))
					continue;

				System.out.println(myreadline);

				String[] sa = myreadline.split(",");

				if (sa.length != numberOfSubset + 1) {
					if (numberOfSubset != -1) {
						System.out.println("Invalid: " + sa[0]);
						continue;
					} else {
						numberOfSubset = sa.length - 1;
						bw.append("@RELATION provenance");
						bw.newLine();
						bw.newLine();

						for (int i = 1; i <= (numberOfSubset / 4); i++) {
							bw.append("@ATTRIBUTE nodeType_" + i + "\t"
									+ "{0,1,2}");
							bw.newLine();
							bw.append("@ATTRIBUTE numberOfNodes_" + i + "\t"
									+ "NUMERIC");
							bw.newLine();
							bw.append("@ATTRIBUTE avgInLinks_" + i + "\t"
									+ "NUMERIC");
							bw.newLine();
							bw.append("@ATTRIBUTE avgOutLinks_" + i + "\t"
									+ "NUMERIC");
							bw.newLine();

//							bw.append("@ATTRIBUTE nodeType_" + i + "\t"
//									+ "{0,1,2}");
//							bw.newLine();
//							bw.append("@ATTRIBUTE numberOfNodes_" + i + "\t"
//									+ "NUMERIC");
//							bw.newLine();
//							bw.append("@ATTRIBUTE avg_numberOfCharacters_" + i
//									+ "\t" + "NUMERIC");
//							bw.newLine();
//							bw.append("@ATTRIBUTE avgInLinks_" + i + "\t"
//									+ "NUMERIC");
//							bw.newLine();
//							bw.append("@ATTRIBUTE avgOutLinks_" + i + "\t"
//									+ "NUMERIC");
//							bw.newLine();
						}

						bw.newLine();
						bw.append("@DATA");
						bw.newLine();
					}
				}

				if (sa[1].equals("PROCESS"))
					sa[1] = "0";
				if (sa[1].equals("ARTIFACT"))
					sa[1] = "1";
				if (sa[1].equals("AGENT"))
					sa[1] = "2";

				bw.append(sa[1]);
				for (int i = 2; i < numberOfSubset + 1; i++) {
					if (sa[i].equals("PROCESS"))
						sa[i] = "0";
					if (sa[i].equals("ARTIFACT"))
						sa[i] = "1";
					if (sa[i].equals("AGENT"))
						sa[i] = "2";

					bw.append("," + sa[i]);
				}

				bw.newLine();

				bw_index.append(index + ":\t" + sa[0]);
				bw_index.newLine();
				index++;
			}
			
			//System.out.println(index);

			bw.flush();
			bw.close();
			br.close();
			fw.close();
			br.close();
			fr.close();
			bw_index.flush();
			bw_index.close();
			fw_index.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
