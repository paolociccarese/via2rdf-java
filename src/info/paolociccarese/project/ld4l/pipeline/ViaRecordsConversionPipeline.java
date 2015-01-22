package info.paolociccarese.project.ld4l.pipeline;

import info.paolociccarese.project.dpf.java.core.APipeline;
import info.paolociccarese.project.dpf.java.core.IStage;
import info.paolociccarese.project.dpf.java.core.Stage;
import info.paolociccarese.project.ld4l.pipeline.commands.ListViaRecordsCommand;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class ViaRecordsConversionPipeline extends APipeline {

	private static final String NAME = "ViaRecordsConversionPipeline";
	
	public static void main(String[] args) {
		// JDK Logger configuration
		// Logger log = Logger.getLogger("info.paolociccarese.project.dpf.java");
		// log.setLevel(Level.ALL);
		
		// Log4j Logger configuration
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.INFO);
		
		//Define log pattern layout
		PatternLayout layout = new PatternLayout("%d{ISO8601} [%t] %-5p %c %x - %m%n");
		 
		//Add console appender to root logger
		rootLogger.addAppender(new ConsoleAppender(layout));
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("folders", "data/via-records/via_xml_records_HUAM1");
		
		ViaRecordsConversionPipeline pipeline = new ViaRecordsConversionPipeline();
		pipeline.start(params, null);
	}
	
	public ViaRecordsConversionPipeline() {
		IStage stage1 = new Stage("1", new ListViaRecordsCommand(this));
		stage1.setExecutable(true);
		_stages.add(stage1);
	}
	
	@Override
	public void pipelineTerminated(long startTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pipelineSuspended(long startTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pipelineResumed(long startTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pipelineCompleted(long startTime) {
		// TODO Auto-generated method stub
		
	}

}
