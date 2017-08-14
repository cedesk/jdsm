package edu.carleton.tim.jdsm.dependency.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 * &lt;complexContent&gt;
 * &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 * &lt;attGroup ref=&quot;{}attlist.outbound&quot;/&gt;
 * &lt;/restriction&gt;
 * &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "content" })
@XmlRootElement(name = "outbound")
public class Outbound {

	/** The confirmed. */
	@XmlAttribute
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String confirmed;
	
	/** The content. */
	@XmlValue
	protected String content;
	
	/** The type. */
	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String type;

	/**
	 * Gets the value of the confirmed property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getConfirmed() {
		return confirmed;
	}

	/**
	 * Gets the value of the content property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the confirmed property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setConfirmed(String value) {
		confirmed = value;
	}

	/**
	 * Sets the value of the content property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setContent(String value) {
		content = value;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setType(String value) {
		type = value;
	}

}
