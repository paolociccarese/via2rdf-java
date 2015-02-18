package info.paolociccarese.project.ld4l.pipeline.dbpedia;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * @author Dr. Paolo Ciccarese
 */
class QueryingService {
	
	private final Logger log = LoggerFactory.getLogger(PlacesTokenizer.class);

	public Set<String> queryForPlace(int index, String place) {
		String sparqlQuery =
			"select distinct ?s where {" +
				"?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Place>. " +
				"?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/PopulatedPlace>. " +
				"?s <http://www.w3.org/2000/01/rdf-schema#label> ?name. " +
				"FILTER (str(?name) = \"" + place + "\")" +
			"} LIMIT 10";
		//log.info("L" + index + " " + sparqlQuery);

		Query query = QueryFactory.create(sparqlQuery);
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

		Set<String> findings = new HashSet<String>();
		ResultSet results = qexec.execSelect();

		while(results.hasNext()) {
			QuerySolution result = results.nextSolution();
			RDFNode graph_name = result.get("s");
			findings.add(graph_name.toString());
		}
		
		log.info("L" + index + " [" + place + "] results: " + findings.size());
		qexec.close();
		return findings;
	}
}
