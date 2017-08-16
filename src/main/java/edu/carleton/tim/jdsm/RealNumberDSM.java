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

import javolution.xml.XMLFormat;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;
import org.apache.log4j.Logger;
import org.jscience.mathematics.number.Real;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link DesignStructureMatrix} for values of the algebraic
 * field {@link Real}. <br>
 * <br>
 * This class represents a DSM with real number values indicating a measure of
 * dependencies between the elements . A value in a cell at row i and column j
 * means that the element at position i depends on the element at position j and
 * the cell value indicates a measure of dependency.
 *
 * @author Roberto Milev
 */
public class RealNumberDSM implements DesignStructureMatrix<Real> {

    /**
     * The logger.
     */
    private static Logger logger = Logger.getLogger(RealNumberDSM.class);

    /**
     * The cluster end position mappings.
     */
    protected Map<String, Integer> clusterEndPositionMappings = new HashMap<String, Integer>();

    /**
     * The cluster start position mappings.
     */
    protected Map<String, Integer> clusterStartPositionMappings = new HashMap<String, Integer>();

    /**
     * The name position mappings.
     */
    protected Map<String, Integer> namePositionMappings = new HashMap<String, Integer>();

    /**
     * The position name mappings.
     */
    protected Map<Integer, String> positionNameMappings = new HashMap<Integer, String>();

    /**
     * The map.
     */
    protected Real[][] map;

    /**
     * Default constructor.
     */
    public RealNumberDSM() {
    }

    /**
     * Instantiates a new design structure matrix impl.
     *
     * @param _cluster_end_position_mappings   the _cluster_end_position_mappings
     * @param _cluster_start_position_mappings the _cluster_start_position_mappings
     * @param _name_position_mappings          the _name_position_mappings
     * @param _position_name_mappings          the _position_name_mappings
     * @param map                              the map
     */
    public RealNumberDSM(
            Map<String, Integer> _cluster_end_position_mappings,
            Map<String, Integer> _cluster_start_position_mappings,
            Map<String, Integer> _name_position_mappings,
            Map<Integer, String> _position_name_mappings,
            Real[][] map) {
        this.clusterEndPositionMappings = _cluster_end_position_mappings;
        this.clusterStartPositionMappings = _cluster_start_position_mappings;
        this.namePositionMappings = _name_position_mappings;
        this.positionNameMappings = _position_name_mappings;
        this.map = map;
    }

    /* (non-Javadoc)
     * @see edu.carleton.tim.jdsm.DesignStructureMatrix#getClusterEndPositionMappings()
     */
    public Map<String, Integer> getClusterEndPositionMappings() {
        return clusterEndPositionMappings;
    }

    /* (non-Javadoc)
     * @see edu.carleton.tim.jdsm.DesignStructureMatrix#getClusterStartPositionMappings()
     */
    public Map<String, Integer> getClusterStartPositionMappings() {
        return clusterStartPositionMappings;
    }

    /* (non-Javadoc)
     * @see edu.carleton.tim.jdsm.DesignStructureMatrix#getNamePositionMappings()
     */
    public Map<String, Integer> getNamePositionMappings() {
        return namePositionMappings;
    }

    /* (non-Javadoc)
     * @see edu.carleton.tim.jdsm.DesignStructureMatrix#getPositionNameMappings()
     */
    public Map<Integer, String> getPositionNameMappings() {
        return positionNameMappings;
    }

    /* (non-Javadoc)
     * @see edu.carleton.tim.dsm.DesignStructureMatrix#getMap()
     */
    public Real[][] getMap() {
        return map;
    }


    /* (non-Javadoc)
     * @see edu.carleton.tim.dsm.DesignStructureMatrix#prettyPrint()
     */
    public void prettyPrint() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                System.out.print("\t" + map[i][j]);
            }
            System.out.println("");
        }
    }

    /* (non-Javadoc)
     * @see edu.carleton.tim.dsm.DesignStructureMatrix#resetClusters()
     */
    public void resetClusters() {
        this.clusterStartPositionMappings = new HashMap<String, Integer>();
        this.clusterEndPositionMappings = new HashMap<String, Integer>();
    }


    /* (non-Javadoc)
     * @see edu.carleton.tim.dsm.DesignStructureMatrix#shift(int, int)
     */
    public void shift(int from, int to) {
        if (from < to) {
            for (int i = 0; i < to - from; i++) {
                swap(from + i, from + i + 1);
            }
        } else {
            for (int i = 0; i < from - to; i++) {
                swap(from - i, from - i - 1);
            }
        }
    }

    /* (non-Javadoc)
     * @see edu.carleton.tim.dsm.DesignStructureMatrix#swap(int, int)
     */
    public void swap(int from, int to) {
        Real temp;
        for (int i = 0; i < map.length; i++) {
            temp = map[from][i];
            map[from][i] = map[to][i];
            map[to][i] = temp;
        }
        for (int i = 0; i < map.length; i++) {
            temp = map[i][from];
            map[i][from] = map[i][to];
            map[i][to] = temp;
        }

        String fromName = positionNameMappings.get(from);
        String toName = positionNameMappings.get(to);

        positionNameMappings.put(from, toName);
        positionNameMappings.put(to, fromName);
        namePositionMappings.put(fromName, to);
        namePositionMappings.put(toName, from);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public DesignStructureMatrix<Real> clone() {
        Map<String, Integer> _cluster_end_position_mappings = new HashMap<String, Integer>();
        Map<String, Integer> _cluster_start_position_mappings = new HashMap<String, Integer>();
        Map<String, Integer> _name_position_mappings = new HashMap<String, Integer>();
        Map<Integer, String> _position_name_mappings = new HashMap<Integer, String>();
        Real[][] map = new Real[this.map.length][this.map.length];

        for (String key : this.clusterEndPositionMappings.keySet()) {
            _cluster_end_position_mappings.put(new String(key), new Integer(this.clusterEndPositionMappings.get(key)));
        }
        for (String key : this.clusterStartPositionMappings.keySet()) {
            _cluster_start_position_mappings.put(new String(key), new Integer(this.clusterStartPositionMappings.get(key)));
        }
        for (String key : this.namePositionMappings.keySet()) {
            _name_position_mappings.put(new String(key), new Integer(this.namePositionMappings.get(key)));
        }
        for (Integer key : this.positionNameMappings.keySet()) {
            _position_name_mappings.put(new Integer(key), new String(this.positionNameMappings.get(key)));
        }
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                map[i][j] = this.map[i][j];
            }
        }
        return new RealNumberDSM(_cluster_end_position_mappings, _cluster_start_position_mappings, _name_position_mappings, _position_name_mappings, map);
    }

    /* (non-Javadoc)
     * @see edu.carleton.tim.jdsm.DesignStructureMatrix#saveToXml(java.io.OutputStream)
     */
    public void saveToXml(OutputStream outputStream) throws XMLStreamException {
        XMLObjectWriter writer = XMLObjectWriter.newInstance(outputStream);
        writer.setIndentation("\t");
        writer.write(this, this.getClass().getCanonicalName(), RealNumberDSM.class);
        writer.close();
        logger.info("Saved DSM to XML");
    }

    /* (non-Javadoc)
     * @see edu.carleton.tim.jdsm.DesignStructureMatrix#loadFromXml(java.io.InputStream)
     */
    public void loadFromXml(InputStream outputStream) throws XMLStreamException {
        XMLObjectReader reader = XMLObjectReader.newInstance(outputStream);
        RealNumberDSM dsm = reader.read(this.getClass().getCanonicalName(), this.getClass());
        reader.close();
        clusterEndPositionMappings = dsm.clusterEndPositionMappings;
        clusterStartPositionMappings = dsm.clusterStartPositionMappings;
        namePositionMappings = dsm.namePositionMappings;
        positionNameMappings = dsm.positionNameMappings;
        map = dsm.map;
        logger.info("Loaded DSM from XML");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = new String("\n");
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                result += map[i][j] + "\t";
            }
            result += "\n";
        }
        return result;
    }

    /**
     * The Constant XML.
     */
    protected static final XMLFormat<RealNumberDSM> XML = new XMLFormat<RealNumberDSM>(
            RealNumberDSM.class) {

        @Override
        public void read(InputElement xml, RealNumberDSM dsm)
                throws XMLStreamException {
            dsm.clusterEndPositionMappings = xml.get("clusterEndPositionMappings");
            dsm.clusterStartPositionMappings = xml.get("clusterStartPositionMappings");
            dsm.namePositionMappings = xml.get("namePositionMappings");
            dsm.positionNameMappings = xml.get("positionNameMappings");
            dsm.map = xml.get("map");
        }

        @Override
        public void write(RealNumberDSM dsm, OutputElement xml)
                throws XMLStreamException {
            xml.add(dsm.clusterEndPositionMappings, "clusterEndPositionMappings");
            xml.add(dsm.clusterStartPositionMappings, "clusterStartPositionMappings");
            xml.add(dsm.namePositionMappings, "namePositionMappings");
            xml.add(dsm.positionNameMappings, "positionNameMappings");
            xml.add(dsm.map, "map");
        }
    };
}