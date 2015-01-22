package info.paolociccarese.project.ld4l.conversion.utils;

import org.json.simple.JSONObject;

public class JsonDpUtils {

	public static JSONObject getProvenance() {
		JSONObject provenance = new JSONObject();
		provenance.put("pav:importedFrom", "Via Records");
		return provenance;
	}
}
