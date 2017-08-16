package edu.carleton.tim.jdsm.dependency.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the generated package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * The Constant _Name_QNAME.
     */
    private final static QName _Name_QNAME = new QName("", "name");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: generated.
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Class }.
     *
     * @return the class
     */
    public Class createClass() {
        return new Class();
    }

    /**
     * Create an instance of {@link Dependencies }.
     *
     * @return the dependencies
     */
    public Dependencies createDependencies() {
        return new Dependencies();
    }

    /**
     * Create an instance of {@link Feature }.
     *
     * @return the feature
     */
    public Feature createFeature() {
        return new Feature();
    }

    /**
     * Create an instance of {@link Inbound }.
     *
     * @return the inbound
     */
    public Inbound createInbound() {
        return new Inbound();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}.
     *
     * @param value the value
     * @return the JAXB element< string>
     */
    @XmlElementDecl(namespace = "", name = "name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link Outbound }.
     *
     * @return the outbound
     */
    public Outbound createOutbound() {
        return new Outbound();
    }

    /**
     * Create an instance of {@link Package }.
     *
     * @return the package
     */
    public Package createPackage() {
        return new Package();
    }

}
