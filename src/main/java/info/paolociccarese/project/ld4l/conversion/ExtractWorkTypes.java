package info.paolociccarese.project.ld4l.conversion;

import info.paolociccarese.project.jsondp.java.core.JsonDpObject;
import info.paolociccarese.project.ld4l.conversion.via.IResultsHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

public class ExtractWorkTypes implements IResultsHandler {

	private Set<String> types = new HashSet<String>();
	
	public static void main(String[] args) throws IOException, ParseException {
		BasicConfigurator.configure();
		Logger  logger = Logger.getLogger("info.paolociccarese.project.ld4l.conversion");
		logger.setLevel(Level.ERROR);
		
		ExtractWorkTypes extractWorkTypes = new ExtractWorkTypes();
		extractWorkTypes.extract();
	}
	
	private void extract() throws IOException, ParseException {
//		List<String> fileNames = new ArrayList<String>();
//		File folder = new File("data/via-records/");
//		listFilesForFolder(folder, fileNames);
//		
//		for(String fileName: fileNames) {
//			SAXParserFactory spf = SAXParserFactory.newInstance();
//			spf.setNamespaceAware(true);
//			try {
//				SAXParser saxParser = spf.newSAXParser();
//				XMLReader xmlReader = saxParser.getXMLReader();
//				xmlReader.setContentHandler(new WorkTypesSaxHandler(this));
//				System.out.println(fileName);
//				xmlReader.parse(convertToFileURL(fileName));
//			} catch (ParserConfigurationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SAXException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		FileWriter fw = new FileWriter("output/types.txt");
//		Iterator<String> i = types.iterator();
//		while(i.hasNext()) {
//			fw.write(i.next() +"\n");
//		}
//		fw.close();
//		
//		Set<String> types = new HashSet<String>();
//		BufferedReader br = new BufferedReader(new FileReader("output/types.txt"));
//		JSONParser parser = new JSONParser();
//		String line;
//		while ((line = br.readLine()) != null) {
//			Object array = parser.parse(line);
//			if(array instanceof JSONArray) {
//				for(int x=0; x<((JSONArray)array).size(); x++) {
//					types.add(((JSONObject)((JSONArray) array).get(x)).get("via:text").toString());
//				}
//			}
//			
//		}
//		br.close();
//		
//		FileWriter fw2 = new FileWriter("output/types2.txt");
//		Iterator<String> i2 = types.iterator();
//		while(i2.hasNext()) {
//			fw2.write(i2.next() +"\n");
//		}
//		fw2.close();
		
		String csvFile = "data/cornell-types/worktypes-reduced.csv";
		BufferedReader br2 = null;
		String line2 = "";
		String cvsSplitBy = ",";
		try {
			 
			br2 = new BufferedReader(new FileReader(csvFile));
			while ((line2 = br2.readLine()) != null) {
	 
			        // use comma as separator
				String[] country = line2.split(cvsSplitBy);
	 
				if(!country[0].equals("\"") && !country[0].trim().equals("")) 
					if(country[0].trim().equals("WorkType") || country.length<6) continue; 
					//System.out.println(country[0].trim());
			
					if(country[5].startsWith("300"))
						System.out.println(country[0].trim() + " - http://vocab.getty.edu/aat/" + country[5].trim());
					else
						System.out.println(country[0].trim() + " -**************************** " + country[5].toUpperCase().trim());
	 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br2 != null) {
				try {
					br2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		System.out.println("Done");
	}
	
	int counter = 0;
	
	@Override
	public void notifyResult(Object result) {
		System.out.println("*********** " + counter++);
		System.out.println(result.toString());
		if(result instanceof JsonDpObject) {
			if(((JsonDpObject)((JsonDpObject)result).get("via:work")!=null) && ((JsonDpObject)((JsonDpObject)result).get("via:work")).get("via:workTypes")!=null) { 
				System.out.println(((JsonDpObject)((JsonDpObject)result).get("via:work")).get("via:workTypes"));
				types.add(((JsonDpObject)((JsonDpObject)result).get("via:work")).get("via:workTypes").toString());
			}
		}
	}
	
	private void listFilesForFolder(final File folder, List<String> files) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry, files);
	        } else {
	        	files.add(folder + "/" + fileEntry.getName());
	        }
	    }
	}
	
	private String convertToFileURL(String filename) {
		String path = new File(filename).getAbsolutePath();
		if (File.separatorChar != '/') {
			path = path.replace(File.separatorChar, '/');
		}

		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return "file:" + path;
	}
}
