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
package edu.carleton.tim.jdsm;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javolution.xml.stream.XMLStreamException;

import org.jscience.mathematics.structure.Field;

/**
 * Theis interface is a representation of a Design Structure Matrix (DSM).<br>
 * <br>
 * A DSM is a square matrix with cells along the diagonal representing the
 * system elements, and the off-diagonal cells representing the dependencies
 * between the system elements. A value in the cell at row i and column j means
 * that the element at position i depends on the element at position j. A binary
 * DSM only provides indications of existence of dependencies, but the cells can
 * also be populated with numeric measure to indicate the degree of the
 * dependencies.
 * 
 * @author Roberto Milev
 */
public interface DesignStructureMatrix<F extends Field<F>>{

	/**
	 * Gets the cluster end position mappings.
	 * 
	 * @return the cluster end position mappings
	 */
	public abstract Map<String, Integer> getClusterEndPositionMappings();

	/**
	 * Gets the cluster start position mappings.
	 * 
	 * @return the cluster start position mappings
	 */
	public abstract Map<String, Integer> getClusterStartPositionMappings();

	/**
	 * Gets the name position mappings.
	 * 
	 * @return the name position mappings
	 */
	public abstract Map<String, Integer> getNamePositionMappings();

	/**
	 * Gets the position name mappings.
	 * 
	 * @return the position name mappings
	 */
	public abstract Map<Integer, String> getPositionNameMappings();

	/**
	 * Gets the map.
	 * 
	 * @return the map
	 */
	public abstract F[][] getMap();

	/**
	 * Pretty print.
	 */
	public abstract void prettyPrint();

	/**
	 * Reset clusters.
	 */
	public abstract void resetClusters();

	/**
	 * Shift.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 */
	public abstract void shift(int from, int to);

	/**
	 * Swap.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 */
	public abstract void swap(int from, int to);

	/**
	 * Clone.
	 * 
	 * @return the design structure matrix< f>
	 */
	public DesignStructureMatrix<F> clone();
	
	/**
	 * Serializes the object to XML format.
	 * 
	 * @param outputStream
	 *            stream to write XML to
	 * 
	 * @throws XMLStreamException
	 *             the XML stream exception
	 */
	public void saveToXml(OutputStream outputStream) throws XMLStreamException;

	/**
	 * Deserializes the object from XML format.
	 * 
	 * @param outputStream
	 *            stream to read XML from
	 * 
	 * @throws XMLStreamException
	 *             the XML stream exception
	 */
	public void loadFromXml(InputStream outputStream) throws XMLStreamException;


}