//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.04.06 at 08:24:41 AM CDT 
//


package com.techolution.bi.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MoneyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MoneyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Container" type="{}ContainerType"/>
 *         &lt;element name="TotalValue" type="{}TotalValueType"/>
 *         &lt;element name="Wad" type="{}WadType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MoneyType", propOrder = {
    "container",
    "totalValue",
    "wad"
})
public class MoneyType {

    @XmlElement(name = "Container", required = true)
    protected ContainerType container;
    @XmlElement(name = "TotalValue", required = true)
    protected TotalValueType totalValue;
    @XmlElement(name = "Wad", required = true)
    protected WadType wad;

    /**
     * Gets the value of the container property.
     * 
     * @return
     *     possible object is
     *     {@link ContainerType }
     *     
     */
    public ContainerType getContainer() {
        return container;
    }

    /**
     * Sets the value of the container property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainerType }
     *     
     */
    public void setContainer(ContainerType value) {
        this.container = value;
    }

    /**
     * Gets the value of the totalValue property.
     * 
     * @return
     *     possible object is
     *     {@link TotalValueType }
     *     
     */
    public TotalValueType getTotalValue() {
        return totalValue;
    }

    /**
     * Sets the value of the totalValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link TotalValueType }
     *     
     */
    public void setTotalValue(TotalValueType value) {
        this.totalValue = value;
    }

    /**
     * Gets the value of the wad property.
     * 
     * @return
     *     possible object is
     *     {@link WadType }
     *     
     */
    public WadType getWad() {
        return wad;
    }

    /**
     * Sets the value of the wad property.
     * 
     * @param value
     *     allowed object is
     *     {@link WadType }
     *     
     */
    public void setWad(WadType value) {
        this.wad = value;
    }

}