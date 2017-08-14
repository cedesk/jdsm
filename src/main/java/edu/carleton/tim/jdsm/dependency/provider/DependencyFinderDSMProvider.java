/*
Copyright (c) 2008, Roberto Milev
All rights reserved.

Redistribution and use in source and binary forms, with or 
without modification, are permitted provided that the following 
conditions are met:

    * Redistributions of source code must retain the above 
      copyright notice, this list of conditions and the following 
      disclaimer.
    * Redistributions in binary form must reproduce the above 
      copyright notice, this list of conditions and the following 
      disclaimer in the documentation and/or other materials 
      provided with the distribution.
    * Neither the name of the Carleton University nor the names 
      of its contributors may be used to endorse or promote 
      products derived from this software without specific prior 
      written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
*/
package edu.carleton.tim.jdsm.dependency.provider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.carleton.tim.jdsm.DesignStructureMatrix;
import edu.carleton.tim.jdsm.dependency.Dependency;
import edu.carleton.tim.jdsm.dependency.DependencyDSM;
import edu.carleton.tim.jdsm.dependency.jaxb.Class;
import edu.carleton.tim.jdsm.dependency.jaxb.Dependencies;
import edu.carleton.tim.jdsm.dependency.jaxb.Feature;
import edu.carleton.tim.jdsm.dependency.jaxb.ObjectFactory;
import edu.carleton.tim.jdsm.dependency.jaxb.Outbound;
import edu.carleton.tim.jdsm.dependency.jaxb.Package;


/**
 * Utility class that creates instances of {@link DependencyDSM} using the
 * output of the Dependency Finder tool (http://depfind.sourceforge.net/). <br>
 * The DTD defining the XML format is:
 * http://depfind.sourceforge.net/dtd/dependencies.dtd
 */
public class DependencyFinderDSMProvider {

	/** The logger. */
	private static Logger logger = Logger.getLogger(DependencyFinderDSMProvider.class);

	/** The cluster end position mappings. */
	protected Map<String, Integer> clusterEndPositionMappings = new HashMap<String, Integer>();
	
	/** The cluster start position mappings. */
	protected Map<String, Integer> clusterStartPositionMappings = new HashMap<String, Integer>();
	
	/** The map. */
	protected Dependency[][] map;
	
	/** The name position mappings. */
	protected Map<String, Integer> namePositionMappings = new HashMap<String, Integer>();
	
	/** The position name mappings. */
	protected Map<Integer, String> positionNameMappings = new HashMap<Integer, String>();
	
	/** The filter. */
	private Pattern[] filter;

	/**
	 * Load from dependency finder xml.
	 * 
	 * @param inputFileName
	 *            the input file name
	 * @param filterExpression
	 *            the filter expression
	 * 
	 * @return the design structure matrix< dependency>
	 * 
	 * @throws JAXBException
	 *             the JAXB exception
	 * @throws SAXException
	 *             the SAX exception
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public static DesignStructureMatrix<Dependency> loadDesignStructureMatrix(
			String inputFileName, String filterExpression)
			throws JAXBException, SAXException, ParserConfigurationException,
			FileNotFoundException {
		DependencyFinderDSMProvider instance = new DependencyFinderDSMProvider();
		logger.info("Started parsing dependencies from file: " + inputFileName);
		logger.info("Using filter expression:" + filterExpression);
		instance.setFilterExpression(filterExpression);
		Dependencies dependencies = null;

		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = parserFactory.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setEntityResolver(new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId)
					throws SAXException, IOException {
				return new InputSource(getClass().getResourceAsStream("/dependencies.dtd"));
			}
		});
		SAXSource saxSource = new SAXSource(xmlReader, new InputSource(
				new FileInputStream(inputFileName)));
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class
				.getPackage().getName());
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		dependencies = (Dependencies) unmarshaller.unmarshal(saxSource);

		int counter = 0;
		for (edu.carleton.tim.jdsm.dependency.jaxb.Package package1 : dependencies.getPackage()) {
			String currentPackage = package1.getName();
			if (instance.matchesPattern(currentPackage)) {
				List<Class> classes = package1.getClazz();

				instance.clusterStartPositionMappings.put(currentPackage,counter);
				instance.clusterEndPositionMappings.put(currentPackage, counter	+ classes.size());

				instance.updateParentPackages(currentPackage, classes.size());

				for (Class class1 : classes) {
					instance.namePositionMappings.put(class1.getName(), counter);
					instance.positionNameMappings.put(counter, class1.getName());
					counter++;
				}
			}
		}

		int totalNrClasses = instance.namePositionMappings.keySet().size();
		logger.info("Loaded " + totalNrClasses + " classes.");
		instance.map = new Dependency[totalNrClasses][totalNrClasses];

		for (int i = 0; i < totalNrClasses; i++) {
			for (int j = 0; j < totalNrClasses; j++) {
				instance.map[i][j] = Dependency.NO;
			}
		}

		for (Package package1 : dependencies.getPackage()) {
			String currentPackage = package1.getName();
			if (instance.matchesPattern(currentPackage)) {
				List<Class> classes = package1.getClazz();
				for (Class class1 : classes) {
					String currentClassName = class1.getName();
					Integer currentClassId = instance.namePositionMappings.get(currentClassName);
					List<Outbound> outboundList = class1.getOutbound();
					instance.processOutboundDependencies(currentClassId, outboundList);

					for (Feature feature : class1.getFeature()) {
						instance.processOutboundDependencies(currentClassId, feature.getOutbound());
					}
				}
			}
		}

		logger.info("Finished parsing dependencies from file: " + inputFileName);
		return new DependencyDSM(instance.clusterEndPositionMappings,
				instance.clusterStartPositionMappings,
				instance.namePositionMappings,
				instance.positionNameMappings,
				instance.map);
	}

	/**
	 * Instantiates a new dependency finder xml loader factory.
	 */
	private DependencyFinderDSMProvider() {
	}

	/**
	 * Gets the class name from feature name.
	 * 
	 * @param featureName
	 *            the feature name
	 * 
	 * @return the class name from feature name
	 */
	private String getClassNameFromFeatureName(String featureName) {
		if (featureName.lastIndexOf('(') > 0) {
			return featureName.substring(0, featureName.lastIndexOf('.',
					featureName.lastIndexOf('(')));
		} else {
			return featureName.substring(0, featureName.lastIndexOf('.'));
		}
	}

	/**
	 * Matches pattern.
	 * 
	 * @param currentPackage
	 *            the current package
	 * 
	 * @return true, if successful
	 */
	private boolean matchesPattern(String currentPackage) {
		for (Pattern pattern : filter) {
			if (pattern.matcher(currentPackage).matches()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Process outbound dependencies.
	 * 
	 * @param currentClassId
	 *            the current class id
	 * @param outboundList
	 *            the outbound list
	 */
	private void processOutboundDependencies(Integer currentClassId,
			List<Outbound> outboundList) {
		for (Outbound outbound : outboundList) {
			String dependableClassName = outbound.getContent();
			if (outbound.getType().equals("feature")) {
				dependableClassName = getClassNameFromFeatureName(dependableClassName);
			}

			Integer dependableClassId = namePositionMappings.get(dependableClassName);
			if (dependableClassId != null
					&& !dependableClassId.equals(currentClassId)) {
				map[currentClassId][dependableClassId] = Dependency.YES;
			}
		}
	}

	/**
	 * Sets the filter expression.
	 * 
	 * @param filterExpression
	 *            the new filter expression
	 */
	private void setFilterExpression(String filterExpression) {
		String[] regExps = filterExpression.split(",");
		filter = new Pattern[regExps.length];
		for (int i = 0; i < regExps.length; i++) {
			filter[i] = Pattern.compile(regExps[i].substring(1, regExps[i].length() - 1));
		}
	}

	/**
	 * Update parent packages.
	 * 
	 * @param packageName
	 *            the package name
	 * @param count
	 *            the count
	 */
	private void updateParentPackages(String packageName, int count) {
		if (packageName.lastIndexOf('.') > 0) {
			String parentPackageName = packageName.substring(0,
					packageName.lastIndexOf('.'));
			Integer parentPackageEnd = clusterEndPositionMappings.get(parentPackageName);
			if (parentPackageEnd != null) {
				clusterEndPositionMappings.put(parentPackageName,
						parentPackageEnd + count);
			}
			updateParentPackages(parentPackageName, count);
		}
	}

}