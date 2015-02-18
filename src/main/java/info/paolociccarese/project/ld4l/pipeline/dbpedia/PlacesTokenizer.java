package info.paolociccarese.project.ld4l.pipeline.dbpedia;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.sparql.sse.Item;

/**
 * This class splits the strings representing places into convenient
 * tokens that can be used for more efficiently searching DBpedia.
 * 
 * Cases:
 * 1) Paris 		  
 *      -> trimmed only
 * 2) Etruria, Europe 
 *      -> first considered all together and then considered 
 *         only in the first part before the comma
 * 3) Malone, New York, United States 
 *      -> first considering the part before the second comma 
 *         and then just the part before the first comma.
 * 4) Athens (Attica), Europe 
 *      -> always considering the part before the open parenteshis 
 * 5) Thourioi (Lucania), Lucania, Europe 
 *      -> always considerting the part before the open parenteshis
 * 
 * @author Dr. Paolo Ciccarese
 */
class PlacesTokenizer {

	private final Logger log = LoggerFactory.getLogger(PlacesTokenizer.class);
	
	public List<String> getPlaceTokens(String place) {
		List<String> matches = new ArrayList<String>();

		log.info("Tokenizing: " + place);
		if(place.indexOf(',')==-1 && place.indexOf('(')==-1) {
			log.debug("A--> " + place);
			matches.add(place.trim());
		} else if(place.indexOf(',')>0 && place.indexOf('(')==-1) {
			if(StringUtils.countMatches(place, ",")==1) {
				log.debug("B--> " + place);
				matches.add(place.trim());
				log.debug("B--> " + place.substring(0, place.indexOf(',')));
				matches.add(place.substring(0, place.indexOf(',')).trim());
			} else if(StringUtils.countMatches(place, ",")==2) {
				log.debug("D--> " + place.substring(0, place.lastIndexOf(',')));
				matches.add(place.substring(0, place.lastIndexOf(',')).trim());
				log.debug("D--> " + place.substring(0, place.indexOf(',')));
				matches.add(place.substring(0, place.indexOf(',')).trim());
			}
		} else if(place.indexOf(',')>0 && place.indexOf('(')>0) {
			if(StringUtils.countMatches(place, ",")==1) {
				log.debug("C--> " + place.substring(0, place.indexOf('(')));
				matches.add(place.substring(0, place.indexOf('(')).trim());
			} else if(StringUtils.countMatches(place, ",")==2) {
				log.debug("E--> " + place.substring(0, place.indexOf('(')));
				matches.add(place.substring(0, place.indexOf('(')).trim());
			}
		}
		
		String delim = "";
		StringBuffer sb = new StringBuffer();
		for (String i : matches) {
		    sb.append(delim).append(i);
		    delim = ",";
		}
		
		log.info("Tokens (;): " + sb.toString());
		return matches;
	}
}
