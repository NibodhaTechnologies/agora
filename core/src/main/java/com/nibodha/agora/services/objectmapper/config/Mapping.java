//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.18 at 12:14:44 PM IST 
//


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
 *         &lt;element name="sheet" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="header-row" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="data-start-row" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destination" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="field" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="destination" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="mapping-id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "sheet",
        "headerRow",
        "dataStartRow",
        "source",
        "destination",
        "fields"
})
public class Mapping {

    protected int sheet;
    @XmlElement(name = "header-row")
    protected int headerRow;
    @XmlElement(name = "data-start-row")
    protected int dataStartRow;
    @XmlElement(required = true)
    protected String source;
    @XmlElement(required = true)
    protected String destination;
    @XmlElement(required = true)
    protected Fields fields;
    @XmlAttribute(name = "mapping-id")
    protected String mappingId;

    /**
     * Gets the value of the sheet property.
     */
    public int getSheet() {
        return sheet;
    }

    /**
     * Sets the value of the sheet property.
     */
    public void setSheet(int value) {
        this.sheet = value;
    }

    /**
     * Gets the value of the headerRow property.
     */
    public int getHeaderRow() {
        return headerRow;
    }

    /**
     * Sets the value of the headerRow property.
     */
    public void setHeaderRow(int value) {
        this.headerRow = value;
    }

    /**
     * Gets the value of the dataStartRow property.
     */
    public int getDataStartRow() {
        return dataStartRow;
    }

    /**
     * Sets the value of the dataStartRow property.
     */
    public void setDataStartRow(int value) {
        this.dataStartRow = value;
    }

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
     * Gets the value of the field property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the field property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getField().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Field }
     */
    public Fields getFields() {
        return this.fields;
    }

    /**
     * Gets the value of the mappingId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMappingId() {
        return mappingId;
    }

    /**
     * Sets the value of the mappingId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMappingId(String value) {
        this.mappingId = value;
    }

}