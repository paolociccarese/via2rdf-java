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

		System.out.println(parameters.get("file").toString().replace("data", "output").replace(".xml", ".json"));
		
		try {
			File file1 = new File(parameters.get("file").toString().replace("data", "output").replace(".xml", ".json"));
			file1.getParentFile().mkdirs();
			FileOutputStream is1 = new FileOutputStream(file1);
            OutputStreamWriter osw1 = new OutputStreamWriter(is1);    
            Writer writer1 = new BufferedWriter(osw1);
			writer1.write(data.toString());
			writer1.close();
			
			if(data instanceof JsonDpAware) {
				File file2 = new File(parameters.get("file").toString().replace("data", "output").replace(".xml", "-dp.json"));
				file2.getParentFile().mkdirs();
				FileOutputStream is2 = new FileOutputStream(file2);
	            OutputStreamWriter osw2 = new OutputStreamWriter(is2);    
	            Writer writer2 = new BufferedWriter(osw2);
				writer2.write(((JsonDpAware)data).plainJsonWithProvenanceToString());
				writer2.close();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO write
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

	@Override
	public void notifyResult(Object result) {
		System.out.println(result.toString());
		if(result instanceof JsonDpObject)
		System.out.println(((JsonDpObject)result).plainJsonWithProvenanceToString());
		
		_listener.notifyStageCompletion(_parentStage, _parameters, result);
	}
}
