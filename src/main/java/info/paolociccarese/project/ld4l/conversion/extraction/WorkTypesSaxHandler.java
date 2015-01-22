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
package info.paolociccarese.project.ld4l.conversion.extraction;

import info.paolociccarese.project.ld4l.conversion.via.IResultsHandler;
import info.paolociccarese.project.ld4l.conversion.via.ViaSaxHandler;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *  @author Dr. Paolo Ciccarese
 */
public class WorkTypesSaxHandler extends ViaSaxHandler {

	private static Logger logger = Logger
			.getLogger(WorkTypesSaxHandler.class.getName());

	boolean oaiDocument = false;

	public WorkTypesSaxHandler(IResultsHandler resultsHandler) {
		super(resultsHandler);
	}
	
	public void startDocument() throws SAXException {
		logger.info("OaiPmh|Start document");
	}

	public void endDocument() throws SAXException {
		logger.info("OaiPmh|End document");
		
		resultsHandler.notifyResult(viaRecord);
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		if(oaiDocument && !viaDocument) {
			logger.debug("OaiPmh|Start element: " + qName);
		}

		if (qName.equalsIgnoreCase("OAI-PMH")) {
			logger.debug("OaiPmh|Start OAI-PMH document: " + qName);
			oaiDocument = true;
		}
	}

	public void characters(char ch[], int start, int length)
			throws SAXException {
		super.characters(ch, start, length);

		if (new String(ch, start, length).trim().length() > 0) {
			logger.debug("OaiPmh|Text: " + new String(ch, start, length));
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		
		if(oaiDocument && !viaDocument) {
			logger.debug("OaiPmh|End element: " + qName);
		}

		if (qName.equalsIgnoreCase("OAI-PMH")) {
			logger.debug("OaiPmh|End OAI-PMH document: " + qName);
			oaiDocument = false;
		}
	}
}
