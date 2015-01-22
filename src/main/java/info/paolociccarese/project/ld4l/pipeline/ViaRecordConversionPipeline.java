package info.paolociccarese.project.ld4l.pipeline;

import info.paolociccarese.project.dpf.java.core.APipeline;
import info.paolociccarese.project.dpf.java.core.IStage;
import info.paolociccarese.project.dpf.java.core.Stage;
import info.paolociccarese.project.ld4l.pipeline.commands.PlaceExtractionCommand;
import info.paolociccarese.project.ld4l.pipeline.commands.ReadViaRecordCommand;
import info.paolociccarese.project.ld4l.pipeline.commands.WorkTypesExtractionCommand;

import java.util.HashMap;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.slf4j.LoggerFactory;

public class ViaRecordConversionPipeline extends APipeline {
	
	private final org.slf4j.Logger log = LoggerFactory.getLogger(ViaRecordConversionPipeline.class);
	
	private static final String NAME = "ViaRecordConversionPipeline";
	
	private static void initializeLog4j() {
		// Log4j Logger configuration
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.DEBUG);
		
		//Define log pattern layout
		PatternLayout layout = new PatternLayout("%d{ISO8601} [%t] %-5p %c %x - %m%n");
		 
		//Add console appender to root logger
		rootLogger.addAppender(new ConsoleAppender(layout));
	}
	
	public static void main(String[] args) {
	
		initializeLog4j();
		
		ViaRecordConversionPipeline pipeline = new ViaRecordConversionPipeline();
		pipeline.start(new HashMap<String, Object>(), null);
	}

	public ViaRecordConversionPipeline() {
		log.debug("Pipeline definition started");
		
		IStage stage1 = new Stage("1", new ReadViaRecordCommand(this));
		stage1.setExecutable(true);
		_stages.add(stage1);
		
		IStage stage2 = new Stage("2", new WorkTypesExtractionCommand(this));
		stage2.setExecutable(true);
		_stages.add(stage2);
		
		IStage stage3 = new Stage("3", new PlaceExtractionCommand(this));
		stage3.setExecutable(true);
		_stages.add(stage3);
		
		log.info("Pipeline definition completed with " + _stages.size() + " stage(s)");
	}
	

	
//	@Override
//	public String getPipelineName() {
//		return NAME;
//	}

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
