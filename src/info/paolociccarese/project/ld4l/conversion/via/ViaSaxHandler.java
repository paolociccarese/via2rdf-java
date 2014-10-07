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
package info.paolociccarese.project.ld4l.conversion.via;

import info.paolociccarese.project.jsondp.java.core.JsonDpArray;
import info.paolociccarese.project.jsondp.java.core.JsonDpObject;
import info.paolociccarese.project.ld4l.conversion.utils.JsonDpUtils;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *  @author Dr. Paolo Ciccarese
 */
public class ViaSaxHandler extends DefaultHandler {

	private static Logger logger = Logger.getLogger(ViaSaxHandler.class.getName());
	
	StringBuffer valueBuffer = new StringBuffer();
	
	//ProJsonUtils proJsonUtils;
	
	JsonDpObject viaRecord;
	JsonDpObject viaRecordWork;
	JsonDpObject creator;
	JsonDpArray notes;
	JsonDpArray workTypes;
	
	boolean viaDocument = false;
	boolean viaRecordId = false;
	boolean viaWork = false;
	
	boolean viaWorkImage = false;
	boolean viaWorkImageThumbnail = false;
	
	boolean viaWorkTitle = false;
	boolean viaWorkTitleTextElement = false;
	
	boolean viaWorkType = false;
	boolean viaWorkMaterial = false;
	boolean viaWorkNotes = false;
	boolean viaWorkCopyright = false;
	boolean viaWorkDimensions = false;
	boolean viaWorkFreeDate = false;
	
	boolean viaWorkItemIdentifier = false;
	boolean viaWorkItemIdentifierType = false;
	boolean viaWorkItemIdentifierNumber = false;
	
	boolean viaWorkCreator = false;
	boolean viaWorkCreatorNameElement = false;
	boolean viaWorkCreatorDates = false;
	boolean viaWorkCreatorNationality = false;
	
	boolean viaWorkTopic = false;
	boolean viaWorkTopicTerm = false;
	
	boolean viaWorkStyle = false;
	boolean viaWorkStyleTerm = false;
	
	boolean viaWorkCulture = false;
	boolean viaWorkCultureTerm = false;
	
	boolean viaWorkRepository = false;
	boolean viaWorkRepositoryName = false;
	
	boolean viaWorkProduction = false;
	boolean viaWorkPlaceOfProduction = false;
	boolean viaWorkPlace = false;
	
	protected IResultsHandler resultsHandler;
	
	public ViaSaxHandler(IResultsHandler resultsHandler) {
		this.resultsHandler = resultsHandler;
	}
	
	public void startDocument() throws SAXException {
		logger.info("VIA|Start document");
	}

	public void endDocument() throws SAXException {
		logger.info("VIA|End document");
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if(viaRecord==null) viaRecord = new JsonDpObject();
		
		valueBuffer = new StringBuffer();
		
		if(viaDocument) {
			logger.debug("VIA|Start element: " + qName);
			
			if (qName.equalsIgnoreCase("RecordId")) {
				viaRecordId = true;
			} else if (qName.equalsIgnoreCase("work")) {
				viaRecordWork = new JsonDpObject();
				workTypes = new JsonDpArray();
				notes = new JsonDpArray();
				viaWork = true;
			}
			
			if(viaWork) {
				if (qName.equalsIgnoreCase("Image")) {
					JsonDpObject image = new JsonDpObject(); 
							
					for(int i=0; i<attributes.getLength(); i++) {
						logger.info("VIA|Work|Image|" + attributes.getQName(i) + "=" + attributes.getValue(i));
						if(attributes.getQName(i).equals("xlink:href")) 
							image.put("via:image", attributes.getValue(i), JsonDpUtils.getProvenance());
						else image.put("via:" + attributes.getQName(i) , attributes.getValue(i), JsonDpUtils.getProvenance());
					}	
					viaRecordWork.put("via:image", image, JsonDpUtils.getProvenance());
					viaWorkImage = true;
				} else if (qName.equalsIgnoreCase("Title")) {
					viaWorkTitle = true;
				} else if (qName.equalsIgnoreCase("itemIdentifier")) {
					viaWorkItemIdentifier = true;
				} else if (qName.equalsIgnoreCase("creator")) {
					creator = new JsonDpObject();
					viaWorkCreator = true;
				} else if (qName.equalsIgnoreCase("topic")) {
					viaWorkTopic = true;
				} else if (qName.equalsIgnoreCase("style")) {
					viaWorkStyle = true;
				} else if (qName.equalsIgnoreCase("culture")) {
					viaWorkCulture = true;
				} else if (qName.equalsIgnoreCase("repository")) {
					viaWorkRepository = true;
				} else if (qName.equalsIgnoreCase("production")) {
					viaWorkProduction = true;
				} else if (qName.equalsIgnoreCase("dimensions")) {
					viaWorkDimensions = true;
				} else if (qName.equalsIgnoreCase("freedate")) {
					viaWorkFreeDate = true;
				} else if (qName.equalsIgnoreCase("workType")) {
					viaWorkType = true;
				} else if (qName.equalsIgnoreCase("materials")) {
					viaWorkMaterial = true;
				} else if (qName.equalsIgnoreCase("notes")) {
					viaWorkNotes = true;
				} else if (qName.equalsIgnoreCase("copyright")) {
					viaWorkCopyright = true;
				}
				
				if(viaWorkImage) {
					if (qName.equalsIgnoreCase("thumbnail")) {				
						
						JsonDpObject image = new JsonDpObject(); 
						
						for(int i=0; i<attributes.getLength(); i++) {
							logger.info("VIA|Work|Thumbnail|" + attributes.getQName(i) + "=" + attributes.getValue(i));
							if(attributes.getQName(i).equals("xlink:href")) 
								image.put("via:url", attributes.getValue(i), JsonDpUtils.getProvenance());
							else image.put("via:" + attributes.getQName(i) , attributes.getValue(i), JsonDpUtils.getProvenance());
						}	
						viaRecordWork.put("via:thumbnail", image, JsonDpUtils.getProvenance());
					
						viaWorkImageThumbnail = true;
					}
				}
				
				if(viaWorkTitle) {
					if (qName.equalsIgnoreCase("TextElement")) {
						viaWorkTitleTextElement = true;
					}
				}
				
				if(viaWorkItemIdentifier) {
					if (qName.equalsIgnoreCase("type")) {
						viaWorkItemIdentifierType = true;
					} else if (qName.equalsIgnoreCase("number")) {
						viaWorkItemIdentifierNumber = true;
					}
				}
				
				if(viaWorkTopic) {
					if (qName.equalsIgnoreCase("term")) {
						viaWorkTopicTerm = true;
					}
				}
				
				if(viaWorkStyle) {
					if (qName.equalsIgnoreCase("term")) {
						viaWorkStyleTerm = true;
					}
				}
				
				if(viaWorkCulture) {
					if (qName.equalsIgnoreCase("term")) {
						viaWorkCultureTerm = true;
					}
				}
				
				if(viaWorkRepository) {
					if (qName.equalsIgnoreCase("repositoryName")) {
						viaWorkRepositoryName = true;
					}
				}
				
				if(viaWorkProduction) {
					if(viaWorkPlaceOfProduction) {
						if (qName.equalsIgnoreCase("place")) {
							viaWorkPlace = true;
						}	
					}
					
					if (qName.equalsIgnoreCase("placeOfProduction")) {
						viaWorkPlaceOfProduction = true;
					}				
				}
			}
		}
		
		if (qName.equalsIgnoreCase("viaRecord")) {
			logger.debug("VIA|Start Via document: " + qName);
			viaDocument = true;
		} 
	}

	public void characters(char ch[], int start, int length)
			throws SAXException {	
		valueBuffer.append(new String(ch, start, length));
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		if(viaDocument) {
			logger.debug("VIA|End element: " + qName);
			
			if (qName.equalsIgnoreCase("viaRecord")) {
				logger.debug("VIA|End Via document: " + qName);
				
				resultsHandler.notifyResult(viaRecord);
				
				viaDocument = false;
			} else if (qName.equalsIgnoreCase("RecordId")) {	
				logger.info("VIA|RecordId=" + valueBuffer.toString().trim());
				
				viaRecord.put("via:recordId", valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
				
				viaRecordId = false;
			} else if (qName.equalsIgnoreCase("work")) {
				
				viaRecord.put("via:work", viaRecordWork, JsonDpUtils.getProvenance());
				
				if(notes.size()>0) viaRecordWork.put("via:notes", notes, JsonDpUtils.getProvenance());
				if(workTypes.size()>0) viaRecordWork.put("via:workTypes", workTypes, JsonDpUtils.getProvenance());
				
				viaWork = false;
			}
			
			if(viaWork) {
				if (qName.equalsIgnoreCase("Image")) {
					viaWorkImage = false;
				} else if (qName.equalsIgnoreCase("Title")) {
					viaWorkTitle = false;
				} else if (qName.equalsIgnoreCase("itemIdentifier")) {
					viaWorkItemIdentifier = false;
				} else if (qName.equalsIgnoreCase("creator")) {
					viaRecordWork.put("via:creator", creator, JsonDpUtils.getProvenance());
					viaWorkCreator = false;
				} else if (qName.equalsIgnoreCase("topic")) {
					viaWorkTopic = false;
				} else if (qName.equalsIgnoreCase("style")) {
					viaWorkStyle = false;
				} else if (qName.equalsIgnoreCase("culture")) {
					viaWorkCulture = false;
				} else if (qName.equalsIgnoreCase("repository")) {
					viaWorkRepository = false;
				} else if (qName.equalsIgnoreCase("production")) {
					viaWorkProduction = false;
				} else if (qName.equalsIgnoreCase("dimensions")) {
					logger.info("VIA|Work|Dimensions=" + valueBuffer.toString().trim());
					
					viaRecordWork.put("via:dimension", valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
					
					viaWorkDimensions = false;
				} else if (qName.equalsIgnoreCase("freedate")) {
					logger.info("VIA|Work|FreeDate=" + valueBuffer.toString().trim());
					
					viaRecordWork.put("via:freedate", valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
					
					viaWorkFreeDate = false;
				} else if (qName.equalsIgnoreCase("workType")) {
					logger.info("VIA|Work|Type=" + valueBuffer.toString().trim());
					
					workTypes.add(valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
					
					viaWorkType = false;
				} else if (qName.equalsIgnoreCase("materials")) {
					logger.info("VIA|Work|Materials=" + valueBuffer.toString().trim());
					
					viaRecordWork.put("via:materials", valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
					
					viaWorkMaterial = false;
				} else if (qName.equalsIgnoreCase("notes")) {
					logger.info("VIA|Work|Notes=" + valueBuffer.toString().trim());
					
					notes.add(valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
					
					viaWorkNotes = false;
				} else if (qName.equalsIgnoreCase("copyright")) {
					logger.info("VIA|Work|Copyright=" + valueBuffer.toString().trim());
					
					viaRecordWork.put("via:copyright", valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
					
					viaWorkCopyright = false;
				}
				
				if(viaWorkImage) {
					if (qName.equalsIgnoreCase("thumbnail")) {
						//logger.info("VIA|WorkThumbnail=" + valueBuffer.toString().trim());
						viaWorkImageThumbnail = false;
					}
				}
				
				if(viaWorkTitle) {
					if (qName.equalsIgnoreCase("TextElement")) {
						logger.info("VIA|Work|Title=" + valueBuffer.toString().trim());
						viaWorkTitleTextElement = false;
					}
				}
				
				if(viaWorkItemIdentifier) {
					if (qName.equalsIgnoreCase("type")) {
						logger.info("VIA|Work|ItemIdentifier|Type=" + valueBuffer.toString().trim());
						viaWorkItemIdentifierType = false;
					} else if (qName.equalsIgnoreCase("number")) {
						logger.info("VIA|Work|ItemIdentifier|Number=" + valueBuffer.toString().trim());
						viaWorkItemIdentifierNumber = false;
					}
				}
				
				if(viaWorkCreator) {
					if (qName.equalsIgnoreCase("nameElement")) {
						logger.info("VIA|Work|Creator|NameElement=" + valueBuffer.toString().trim());
						
						creator.put("via:nameElement", valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
						//viaRecordWork.put("via:creator", creator);
						
						viaWorkCreatorNameElement = false;
					} else if (qName.equalsIgnoreCase("dates")) {
						logger.info("VIA|Work|Creator|Dates=" + valueBuffer.toString().trim());
						
						creator.put("via:dates", valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
						//viaRecordWork.put("via:creator", creator);
						
						viaWorkCreatorDates = false;
					} else if (qName.equalsIgnoreCase("nationality")) {
						logger.info("VIA|Work|Creator|Nationality=" + valueBuffer.toString().trim());
						
						creator.put("via:nationality", valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
						//viaRecordWork.put("via:creator", creator);
						
						viaWorkCreatorNationality = false;
					}
				}
				
				if(viaWorkTopic) {
					if (qName.equalsIgnoreCase("term")) {
						logger.info("VIA|Work|Topic|Term=" + valueBuffer.toString().trim());
						
						JsonDpObject topic = new JsonDpObject(); 
						topic.put("via:term", valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
						viaRecordWork.put("via:topic", topic, JsonDpUtils.getProvenance());
						
						viaWorkTopicTerm = false;
					}
				}
				
				if(viaWorkStyle) {
					if (qName.equalsIgnoreCase("term")) {
						logger.info("VIA|Work|Style|Term=" + valueBuffer.toString().trim());
						
						JsonDpObject style = new JsonDpObject(); 
						style.put("via:term", valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
						viaRecordWork.put("via:style", style, JsonDpUtils.getProvenance());
						
						viaWorkStyleTerm = false;
					}
				}
				
				if(viaWorkCulture) {
					if (qName.equalsIgnoreCase("term")) {
						logger.info("VIA|Work|Culture|Term=" + valueBuffer.toString().trim());
						
						JsonDpObject culture = new JsonDpObject(); 
						culture.put("via:term", valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
						viaRecordWork.put("via:culture", culture, JsonDpUtils.getProvenance());
						
						viaWorkCultureTerm = false;
					}
				}
				
				if(viaWorkRepository) {
					if (qName.equalsIgnoreCase("repositoryName")) {
						logger.info("VIA|Work|Repository|Name=" + valueBuffer.toString().trim());
						
						JsonDpObject repository = new JsonDpObject(); 
						repository.put("via:name", valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
						viaRecordWork.put("via:repository", repository, JsonDpUtils.getProvenance());
						
						viaWorkRepositoryName = false;
					}
				}
				
				if(viaWorkProduction) {					
					if(viaWorkPlaceOfProduction) {
						if (qName.equalsIgnoreCase("place")) {
							logger.info("VIA|Work|Place=" + valueBuffer.toString().trim());
							
							JsonDpObject culture = new JsonDpObject(); 
							culture.put("via:term", valueBuffer.toString().trim(), JsonDpUtils.getProvenance());
							viaRecordWork.put("via:place", culture, JsonDpUtils.getProvenance());
							
							viaWorkPlace = false;
						}	
					}
					
					if (qName.equalsIgnoreCase("placeOfProduction")) {
						viaWorkPlaceOfProduction = false;
					}
				}
			}
		}
	}
}
