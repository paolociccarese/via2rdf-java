package info.paolociccarese.project.ld4l.pipeline.commands;

import info.paolociccarese.project.dpf.java.core.IStage;
import info.paolociccarese.project.dpf.java.core.IStageCommand;
import info.paolociccarese.project.dpf.java.core.IStageListener;
import info.paolociccarese.project.ld4l.conversion.via.IResultsHandler;
import info.paolociccarese.project.ld4l.pipeline.ViaRecordConversionPipeline;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dr. Paolo Ciccarese
 */
public class ListViaRecordsCommand implements IStageCommand, IResultsHandler {

	private final Logger log = LoggerFactory.getLogger(ListViaRecordsCommand.class);
	
	IStageListener _listener;
	
	IStage _parentStage;
	Map<String, Object> _parameters;
	
	public ListViaRecordsCommand(IStageListener listener) {
		_listener = listener;
	}
	
	@Override
	public void run(IStage parentStage, Map<String, Object> parameters, Object data) {
		_parentStage = parentStage;
		_parameters = parameters;
		
		String folders = parameters.get("folders").toString();
		log.info("Reading folders: " + folders);
		
		List<String> files = new ArrayList<String>();
		listFilesForFolder(new File(folders), files);
		
		for(String file: files) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("file", file);
			
			log.info("Reading file: " + file);
			
			ViaRecordConversionPipeline pipeline = new ViaRecordConversionPipeline();
			pipeline.start(params, null);
			
			break;
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

	@Override
	public void notifyResult(Object result) {
		// TODO Auto-generated method stub
	}
}
