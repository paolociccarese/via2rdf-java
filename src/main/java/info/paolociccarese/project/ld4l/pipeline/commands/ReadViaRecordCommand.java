package info.paolociccarese.project.ld4l.pipeline.commands;

import info.paolociccarese.project.dpf.java.core.IStage;
import info.paolociccarese.project.dpf.java.core.IStageCommand;
import info.paolociccarese.project.dpf.java.core.IStageListener;
import info.paolociccarese.project.jsondp.java.core.JsonDpObject;
import info.paolociccarese.project.ld4l.conversion.via.IResultsHandler;
import info.paolociccarese.project.ld4l.conversion.via.OaiPmhViaSaxHandler;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author Dr. Paolo Ciccarese
 */
public class ReadViaRecordCommand implements IStageCommand, IResultsHandler {

	IStageListener _listener;
	
	IStage _parentStage;
	Map<String, Object> _parameters;
	
	public ReadViaRecordCommand(IStageListener listener) {
		_listener = listener;
	}
	
	@Override
	public void run(IStage parentStage, Map<String, Object> parameters, Object data) {
		_parentStage = parentStage;
		_parameters = parameters;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		try {
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(new OaiPmhViaSaxHandler(this));
			xmlReader.parse(convertToFileURL(parameters.get("file").toString()));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	@Override
	public void notifyResult(Object result) {
		System.out.println(result.toString());
		if(result instanceof JsonDpObject)
		System.out.println(((JsonDpObject)result).plainJsonWithProvenanceToString());
		
		_listener.notifyStageCompletion(_parentStage, _parameters, result);
	}
}
