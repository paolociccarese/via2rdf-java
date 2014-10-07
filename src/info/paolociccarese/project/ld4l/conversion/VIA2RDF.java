/*
* Copyright 2014 Paolo Ciccarese
*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package info.paolociccarese.project.ld4l.conversion;

import info.paolociccarese.project.jsondp.java.core.JsonDpObject;
import info.paolociccarese.project.ld4l.conversion.via.IResultsHandler;
import info.paolociccarese.project.ld4l.conversion.via.OaiPmhViaSaxHandler;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author Dr. Paolo Ciccarese
 */
public class VIA2RDF implements IResultsHandler {

	public static void main(String[] args) throws SecurityException,
			IOException {

		BasicConfigurator.configure();
		Logger  logger = Logger.getLogger("info.paolociccarese.project.ld4l.conversion.via");
		logger.setLevel(Level.INFO);
		
		VIA2RDF via2rdf = new VIA2RDF();
		via2rdf.convert();
	}
	
	private void convert() {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		try {
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(new OaiPmhViaSaxHandler(this));
			xmlReader.parse(convertToFileURL("data/VIA_recordIdentifier_HUAM211406.via.xml"));
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
	public void notifyResult(JsonDpObject result) {
		System.out.println(result.toString());
		System.out.println(result.toStringWithProvenance());
	}
}
