package edu.carleton.tim.jdsm.dependency.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
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
 * &lt;sequence&gt;
 * &lt;element ref=&quot;{}name&quot;/&gt;
 * &lt;element ref=&quot;{}outbound&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 * &lt;element ref=&quot;{}inbound&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 * &lt;element ref=&quot;{}class&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 * &lt;/sequence&gt;
 * &lt;attGroup ref=&quot;{}attlist.package&quot;/&gt;
 * &lt;/restriction&gt;
 * &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "name", "outbound", "inbound", "clazz" })
@XmlRootElement(name = "package")
public class Package {

	/** The clazz. */
	@XmlElement(name = "class")
	protected List<Class> clazz;
	
	/** The confirmed. */
	@XmlAttribute
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String confirmed;
	
	/** The inbound. */
	protected List<Inbound> inbound;
	
	/** The name. */
	@XmlElement(required = true)
	protected String name;
	
	/** The outbound. */
	protected List<Outbound> outbound;

	/**
	 * Gets the value of the clazz property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the clazz property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getClazz().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Class }
	 * 
	 * @return the clazz
	 */
	public List<Class> getClazz() {
		if (clazz == null) {
			clazz = new ArrayList<Class>();
		}
		return clazz;
	}

	/**
	 * Gets the value of the confirmed property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getConfirmed() {
		return confirmed;
	}

	/**
	 * Gets the value of the inbound property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the inbound property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getInbound().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Inbound }
	 * 
	 * @return the inbound
	 */
	public List<Inbound> getInbound() {
		if (inbound == null) {
			inbound = new ArrayList<Inbound>();
		}
		return inbound;
	}

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the value of the outbound property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the outbound property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getOutbound().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link Outbound }
	 * 
	 * @return the outbound
	 */
	public List<Outbound> getOutbound() {
		if (outbound == null) {
			outbound = new ArrayList<Outbound>();
		}
		return outbound;
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
	 * Sets the value of the name property.
	 * 
	 * @param value allowed object is {@link String }
	 */
	public void setName(String value) {
		name = value;
	}

}
