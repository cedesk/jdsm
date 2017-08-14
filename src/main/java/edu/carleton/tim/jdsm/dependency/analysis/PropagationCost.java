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

import org.apache.log4j.Logger;
import org.jscience.mathematics.number.Rational;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.Matrix;

import edu.carleton.tim.jdsm.DesignStructureMatrix;
import edu.carleton.tim.jdsm.dependency.Dependency;


/**
 * The Class PropagationCost. This is a metric that indicates the level of
 * impact that a change in one element in the system has on the overall system.
 * 
 * @author Roberto Milev
 */
public class PropagationCost {

	/** The logger. */
	private static Logger logger = Logger.getLogger(PropagationCost.class);

	/**
	 * Compute propagation cost.
	 * 
	 * @param dsm
	 *            the DSM to be analyzed
	 * 
	 * @return the propagation cost. A value between 0 and 1 indicating the
	 *         impact in percents
	 */
	public static double computePropagationCost(DesignStructureMatrix<Dependency> dsm) {
		DesignStructureMatrix<Dependency> dsmCopy = dsm.clone();
		logger.info("Started computing propagation cost.");
		final int mapSize = dsmCopy.getMap().length;
		Matrix<Dependency> matrix = DenseMatrix.valueOf(dsmCopy.getMap());

		Dependency[][] zeroDep = new Dependency[matrix.getNumberOfColumns()]
		                                        [matrix.getNumberOfRows()];
		Dependency[][] singleDep = new Dependency[matrix.getNumberOfColumns()]
		                                          [matrix.getNumberOfRows()];

		for (int i = 0; i < matrix.getNumberOfRows(); i++) {
			for (int j = 0; j < matrix.getNumberOfRows(); j++) {
				if (i == j) {
					singleDep[i][j] = Dependency.YES;
				} else {
					singleDep[i][j] = Dependency.NO;
				}
				zeroDep[i][j] = Dependency.NO;

			}
		}

		Matrix<Dependency> zeroMatrix = DenseMatrix.valueOf(zeroDep);
		Matrix<Dependency> sumMatrix = DenseMatrix.valueOf(singleDep);
		Matrix<Dependency> powerMatrix = DenseMatrix.valueOf(singleDep);

		int counter = 0;
		while (counter < mapSize && !powerMatrix.equals(zeroMatrix)) {
			counter++;
			if (counter % 50 == 0) {
				logger.info("Processed " + counter + " of " + mapSize);
			}
			powerMatrix = powerMatrix.times(matrix);
			sumMatrix = sumMatrix.plus(powerMatrix);
		}

		long totalDeps = 0;
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				if ((sumMatrix.get(i, j)).booleanValue()) {
					totalDeps++;
				}
			}
		}

		double propagationCost = Rational.valueOf(totalDeps, mapSize * mapSize).doubleValue();
		logger.info("Computed propagation cost: "+propagationCost);
		return propagationCost;
	}
}
