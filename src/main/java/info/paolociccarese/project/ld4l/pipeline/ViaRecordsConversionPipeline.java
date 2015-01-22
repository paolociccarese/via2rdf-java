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
import org.slf4j.LoggerFactory;

/**
 * Main pipeline for the retrieval and conversion of VIA records.
 * The pipeline loads one record at a time and, for each record, 
 * it triggers a subpipeline for enrichemnt and conversions.
 * 
 * @author Dr. Paolo Ciccarese
 */
public class ViaRecordsConversionPipeline extends APipeline {

	private final org.slf4j.Logger log = LoggerFactory.getLogger(ViaRecordsConversionPipeline.class);
	
	private static final String NAME = "ViaRecordsConversionPipeline";
	
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
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("folders", "data/via-records/via_xml_records_HUAM1");
		
		ViaRecordsConversionPipeline pipeline = new ViaRecordsConversionPipeline();
		pipeline.start(params, null);
	}
	
	public ViaRecordsConversionPipeline() {
		log.debug("Pipeline definition started");
		
		IStage stage1 = new Stage("1", new ListViaRecordsCommand(this));
		stage1.setExecutable(true);
		_stages.add(stage1);
		
		log.info("Pipeline definition completed with " + _stages.size() + " stage(s)");
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
