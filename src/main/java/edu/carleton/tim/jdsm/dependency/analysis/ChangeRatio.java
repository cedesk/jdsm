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
package edu.carleton.tim.jdsm.dependency.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import edu.carleton.tim.jdsm.DesignStructureMatrix;
import edu.carleton.tim.jdsm.dependency.Dependency;

/**
 * The Class ChangeRatio. The metric indicates level of change between two DSMs
 * representing a system in different points in time. <br>
 * The value us calculated as: <br>
 * <code>(newElementsCount + removedElementsCount) / (totalBeforeCount)</code>
 * 
 * @author Roberto Milev
 */
public class ChangeRatio {

	/** The logger. */
	private static Logger logger = Logger.getLogger(ChangeRatio.class);

	/**
	 * Computes the change ratio.
	 * 
	 * @param dsm1
	 *            the initial version of the DSM
	 * @param dsm2
	 *            the second version of the DSM
	 * 
	 * @return the change ratio
	 */
	public static double computeChangeRatio(DesignStructureMatrix<Dependency> dsm1,
			DesignStructureMatrix<Dependency> dsm2) {

		logger.info("Started computing change ratio.");
		List<String> removedElements = new ArrayList<String>(dsm1
				.getNamePositionMappings().keySet());
		List<String> addedElements = new ArrayList<String>(dsm2
				.getNamePositionMappings().keySet());
		float totalBeforeCount = addedElements.size();
		{
			Collections.sort(removedElements);
			Collections.sort(addedElements);

			for (int i = 0; i < addedElements.size(); i++) {
				String className = addedElements.get(i);
				if (removedElements.contains(className)) {
					removedElements.remove(className);
					addedElements.remove(className);
				}
			}

			float newElementsCount = addedElements.size();
			float removedElementsCount = removedElements.size();

			double changeRatio =(newElementsCount + removedElementsCount) / (totalBeforeCount); 
			
			logger.info("Computed change ratio: "+changeRatio);
			logger.info("Started computing change ratio.");
			return changeRatio;
		}
	}
}
