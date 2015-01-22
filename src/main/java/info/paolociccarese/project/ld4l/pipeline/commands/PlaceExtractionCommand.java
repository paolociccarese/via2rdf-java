package info.paolociccarese.project.ld4l.pipeline.commands;

import info.paolociccarese.project.dpf.java.core.IStage;
import info.paolociccarese.project.dpf.java.core.IStageCommand;
import info.paolociccarese.project.dpf.java.core.IStageListener;
import info.paolociccarese.project.jsondp.java.core.JsonDpObject;
import info.paolociccarese.project.ld4l.conversion.via.IResultsHandler;

import java.util.Map;

public class PlaceExtractionCommand implements IStageCommand, IResultsHandler {

	private IStage _parentStage;
	private IStageListener _listener;
	
	private Map<String, Object> _parameters;
	
	public PlaceExtractionCommand(IStageListener listener) {
		_listener = listener;
	}
	
	@Override
	public void notifyResult(Object result) {
		_listener.notifyStageCompletion(_parentStage, _parameters, result);
	}

	@Override
	public void run(IStage parentStage, Map<String, Object> parameters, Object data) {
		_parentStage = parentStage;
		_parameters = parameters;
		
		if(data instanceof JsonDpObject) {
			Object types = ((JsonDpObject)((JsonDpObject)data).get("via:work")).get("via:place");
			
			//System.out.println(types.getClass().getName());
			//System.out.println(((JsonDpObject)types).toJsonWithProvenanceString());
		}
	}
}
