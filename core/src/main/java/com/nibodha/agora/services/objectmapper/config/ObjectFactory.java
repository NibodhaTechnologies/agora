//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.24 at 12:49:00 PM IST 
//


package com.nibodha.agora.services.objectmapper.config;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.nibodha.agora.schema.transformation package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.nibodha.agora.schema.transformation
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MappingConfiguration }
     * 
     */
    public MappingConfiguration createMappingConfiguration() {
        return new MappingConfiguration();
    }

    /**
     * Create an instance of {@link Mapping }
     * 
     */
    public Mapping createMappingConfigurationMapping() {
        return new Mapping();
    }

    /**
     * Create an instance of {@link Field }
     * 
     */
    public Field createMappingConfigurationMappingField() {
        return new Field();
    }

}
