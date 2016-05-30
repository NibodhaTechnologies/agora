package com.nibodha.agora.services.objectmapper.config;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destination" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "source",
        "destination",
        "constantValue"
})
public class Field {

    @XmlElement(required = true)
    protected String source;
    @XmlElement(required = true)
    protected String destination;
    @XmlElement(name = "constant-value")
    protected String constantValue;
    @XmlAttribute(name = "mapping-id-ref")
    protected String mappingIdRef;
    @XmlAttribute(name = "custom-converter-ref")
    protected String customConverterRef;

    /**
     * Gets the value of the source property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * Gets the value of the destination property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets the value of the destination property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDestination(String value) {
        this.destination = value;
    }

    /**
     * Gets the value of the constantValue property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getConstantValue() {
        return constantValue;
    }

    /**
     * Sets the value of the constantValue property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setConstantValue(String value) {
        this.constantValue = value;
    }

    /**
     * Gets the value of the mappingIdRef property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMappingIdRef() {
        return mappingIdRef;
    }

    /**
     * Sets the value of the mappingIdRef property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMappingIdRef(String value) {
        this.mappingIdRef = value;
    }

    /**
     * Gets the value of the customConverterRef property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCustomConverterRef() {
        return customConverterRef;
    }

    /**
     * Sets the value of the customConverterRef property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCustomConverterRef(String value) {
        this.customConverterRef = value;
    }
}