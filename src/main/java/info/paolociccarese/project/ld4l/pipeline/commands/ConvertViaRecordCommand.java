package info.paolociccarese.project.ld4l.pipeline.commands;

import info.paolociccarese.project.dpf.java.core.IStage;
import info.paolociccarese.project.dpf.java.core.IStageCommand;
import info.paolociccarese.project.dpf.java.core.IStageListener;
import info.paolociccarese.project.jsondp.java.core.JsonDpAware;
import info.paolociccarese.project.jsondp.java.core.JsonDpObject;
import info.paolociccarese.project.ld4l.conversion.via.IResultsHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

import org.json.simple.JSONObject;

/**
 * @author Dr. Paolo Ciccarese
 */
public class ConvertViaRecordCommand implements IStageCommand, IResultsHandler {

	IStageListener _listener;
	
	IStage _parentStage;
	Map<String, Object> _parameters;
	
	public ConvertViaRecordCommand(IStageListener listener) {
		_listener = listener;
	}
	
	@Override
	public void run(IStage parentStage, Map<String, Object> parameters, Object data) {
		_parentStage = parentStage;
		_parameters = parameters;
		
		if(data instanceof JsonDpObject) {
			// Detect record identifier
			String recordId = ((JsonDpObject)data).get("via:recordId").toString();	
			
			String BIBFRAME_PREFIX = "http://bibframe.org/";
			
			JSONObject jo = new JSONObject();
			jo.put("@id", "http://ld4l.harvard.edu/resources/via/" + recordId);
			jo.put("@type", BIBFRAME_PREFIX + "Work");
			
			// TODO write
			notifyResult(jo);
			return;
		}
		
		// TODO write
		notifyResult(data);
	}

	@Override
	public void notifyResult(Object result) {
		System.out.println(result.toString());
		if(result instanceof JsonDpObject)
		System.out.println(((JsonDpObject)result).plainJsonWithProvenanceToString());
		
		_listener.notifyStageCompletion(_parentStage, _parameters, result);
	}
}
