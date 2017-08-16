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
package edu.carleton.tim.jdsm.dependency;

import edu.carleton.tim.jdsm.DesignStructureMatrix;
import edu.carleton.tim.jdsm.RealNumberDSM;
import org.jscience.mathematics.number.ModuloInteger;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.structure.Field;


/**
 * Definition of a algebraic field that is used to define algebraic operations
 * on DSMs that show dependencies between elements. This is a simple binary
 * quasi field, as the inverse operation is not defined. It allows for fast
 * matrix operations compared with the operations on the fields {@link Real} and
 * {@link ModuloInteger} <br>
 * <br>
 * Operations are defined as (notations use Java boolean operators): <br>
 * <code>
 * <br> opposite = !x
 * <br> plus = x || y
 * <br> times = x && y
 * <br> inverse is not defined
 * </code> <br>
 * <br>
 * For a implementation of {@link DesignStructureMatrix} with values of the
 * algebraic field {@link Real}
 *
 * @author Roberto Milev
 * @see RealNumberDSM
 */
public class Dependency implements Field<Dependency> {

    /**
     * The Constant NO.
     */
    public static final Dependency NO = new Dependency(false);

    /**
     * The Constant YES.
     */
    public static final Dependency YES = new Dependency(true);

    /**
     * The _value.
     */
    private boolean _value;

    /**
     * Instantiates a new dependency.
     */
    public Dependency() {
        super();
    }

    /**
     * Instantiates a new dependency.
     *
     * @param value the value
     */
    public Dependency(boolean value) {
        _value = value;
    }

    /**
     * Instantiates a new dependency.
     *
     * @param dependency the dependency
     */
    public Dependency(Dependency dependency) {
        _value = dependency._value;
    }

    /* (non-Javadoc)
     * @see javolution.lang.ValueType#copy()
     */
    public Dependency copy() {
        return _value ? YES : NO;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Dependency)) {
            return false;
        } else {
            return ((Dependency) obj)._value == this._value;
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return _value ? 1231 : 1237;
    }

    /**
     * Long value.
     *
     * @return the long
     */
    public long longValue() {
        return _value ? 1 : 0;
    }

    /**
     * Boolean value.
     *
     * @return true, if successful
     */
    public boolean booleanValue() {
        return _value;
    }

    /* (non-Javadoc)
     * @see org.jscience.mathematics.structure.GroupMultiplicative#inverse()
     */
    public Dependency inverse() {
        throw new RuntimeException();
        // return _value ? YES : NO;
    }

    /* (non-Javadoc)
     * @see org.jscience.mathematics.structure.GroupAdditive#opposite()
     */
    public Dependency opposite() {
        return !_value ? YES : NO;
    }

    /* (non-Javadoc)
     * @see org.jscience.mathematics.structure.GroupAdditive#plus(java.lang.Object)
     */
    public Dependency plus(Dependency arg0) {
        return _value || arg0._value ? YES : NO;
    }

    /* (non-Javadoc)
     * @see org.jscience.mathematics.structure.Ring#times(java.lang.Object)
     */
    public Dependency times(Dependency arg0) {
        return _value && arg0._value ? YES : NO;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return _value ? "1" : "0";
    }

//	/** The Constant XML. */
//	protected static final XMLFormat<Dependency> XML = new XMLFormat<Dependency>(
//			Dependency.class) {
//
//		@Override
//		public void read(InputElement xml, Dependency dependency)
//				throws XMLStreamException {
//			dependency._value = xml.getAttribute("value", false);
//		}
//
//		@Override
//		public void write(Dependency dependency, OutputElement xml)
//				throws XMLStreamException {
//			xml.setAttribute("value", dependency._value);
//		}
//	};
}
