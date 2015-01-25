package info.paolociccarese.project.ld4l.pipeline.commands;

import info.paolociccarese.project.dpf.java.core.IStage;
import info.paolociccarese.project.dpf.java.core.IStageCommand;
import info.paolociccarese.project.dpf.java.core.IStageListener;
import info.paolociccarese.project.jsondp.java.core.JsonDpObject;
import info.paolociccarese.project.ld4l.conversion.via.IResultsHandler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * @author Dr. Paolo Ciccarese
 */
public class WriteViaRecordCommand implements IStageCommand, IResultsHandler {

	IStageListener _listener;
	
	IStage _parentStage;
	Map<String, Object> _parameters;
	
	public WriteViaRecordCommand(IStageListener listener) {
		_listener = listener;
	}
	
	@Override
	public void run(IStage parentStage, Map<String, Object> parameters, Object data) {
		_parentStage = parentStage;
		_parameters = parameters;

		System.out.println(parameters.get("file").toString().replace("data", "output").replace(".xml", ".json"));
		

			Model model = ModelFactory.createDefaultModel();
			InputStream inputStream = new ByteArrayInputStream(data.toString().getBytes(Charset.forName("UTF-8")));
			RDFDataMgr.read(model, inputStream, "http://bibframe.org/", Lang.JSONLD);
			
			System.out.println(model);
			
			OutputStream output;
			try {
				output = new FileOutputStream(parameters.get("file").toString().replace("data", "output").replace(".xml", ".rdf"));
				RDFDataMgr.write(output, model, Lang.RDFXML);
			} catch (FileNotFoundException e) {
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
