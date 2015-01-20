package info.paolociccarese.project.ld4l.pipeline.commands;

import info.paolociccarese.project.dpf.java.core.IStage;
import info.paolociccarese.project.dpf.java.core.IStageCommand;
import info.paolociccarese.project.dpf.java.core.IStageListener;
import info.paolociccarese.project.jsondp.java.core.JsonDpArray;
import info.paolociccarese.project.jsondp.java.core.JsonDpObject;
import info.paolociccarese.project.ld4l.conversion.via.IResultsHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkTypesExtractionCommand implements IStageCommand, IResultsHandler {

	private final Logger log = LoggerFactory.getLogger(WorkTypesExtractionCommand.class);
	
	private IStage _parentStage;
	private IStageListener _listener;
	
	private Map<String, String> _parameters;
	private Map<String, String> _cachedTypes;
	
	public WorkTypesExtractionCommand(IStageListener listener) {
		_listener = listener;
	}
	
	@Override
	public void notifyResult(Object result) {
		_listener.notifyStageCompletion(_parentStage, _parameters, result);
	}

	@Override
	public void run(IStage parentStage, Map<String, String> parameters, Object data) {
		_parentStage = parentStage;
		_parameters = parameters;			
		
		setup();
		
		log.debug("Analyzing data for types extraction...");
		
		if(data instanceof JsonDpObject) {
			Object types = ((JsonDpObject)((JsonDpObject)data).get("via:work")).get("via:workTypes");	
			if(types instanceof JsonDpArray) {
				for(int i=0; i<((JsonDpArray)types).size(); i++) {
					Object typeAsArray = (((JsonDpArray)types).getWithProvenanceAsPlainJson(i));
					Object typeAsString = (((JsonDpArray)types).get(i));
					
					JSONObject provenance1 = new JSONObject();
					provenance1.put("pav:createdBy", "WorkTypesExtractionCommand");
					
					JSONObject provenance2 = new JSONObject();
					provenance2.put("pav:derivedFrom", typeAsString);

					if(_cachedTypes.containsKey(((JsonDpObject)typeAsString).get("via:text").toString())) {
						log.info("Type found " + ((JsonDpObject)typeAsString).get("via:text").toString());
					} else {
						log.warn("Type not found " + ((JsonDpObject)typeAsString).get("via:text").toString());
					}
					
					/*
					if(((JsonDpObject)typeAsString).get("via:text").toString().equals("calligraphy")) {	
						
						JsonDpObject typeJsonObject = new JsonDpObject();
						typeJsonObject.put("@id", "http://vocab.getty.edu/aat/300266660", provenance2);
						typeJsonObject.put("label", "calligraphy (visual works)", provenance2);
						
						JsonDpObject replacement = ((JsonDpObject)typeAsString);
						replacement.put("skos:exactMatch", typeJsonObject, provenance1);
						
						((JsonDpArray)types).replace(i, replacement);
					} else if(((JsonDpObject)typeAsString).get("via:text").toString().equals("handscroll")) {	
						JsonDpObject typeJsonObject = new JsonDpObject();
						typeJsonObject.put("@id", "http://vocab.getty.edu/aat/300178463", provenance1);
						typeJsonObject.put("label", "handscrolls", provenance1);
						
						JsonDpObject replacement = ((JsonDpObject)typeAsString);
						replacement.put("skos:exactMatch", typeJsonObject, provenance2);
						
						((JsonDpArray)types).replace(i, replacement);
					}
					*/
				}
			}
		}
		System.out.println(data);
		System.out.println(((JsonDpObject)data).plainJsonWithProvenanceToString());
		notifyResult(data);
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
	
	private void setup() {
		_cachedTypes = new HashMap<String, String>();
		
		log.info("Loading worktypes.csv...");
		String csvFile = "data/harvard-types/worktypes.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	 
				if(line.trim().length()==0) break;
				
				// use comma as separator
				String[] country = line.split(cvsSplitBy);
				_cachedTypes.put(country[0].trim(), country[2].trim());
	 
			}
	 
			//loop map
			for (Map.Entry<String, String> entry : _cachedTypes.entrySet()) {
	 
				//System.out.println("Type [type= " + entry.getKey() + " , getty-aat="
				//	+ entry.getValue() + "]");
	 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
