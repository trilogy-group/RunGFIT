
package com.waysysweb.gfit;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebService(name = "GfitAPIPortType", targetNamespace = "http://waysysweb.com/gfit")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface GfitAPIPortType {


    /**
     * 
     * @param dir
     * @param output
     * @return
     *     returns java.lang.String
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://waysysweb.com/gfit")
    @RequestWrapper(localName = "run", targetNamespace = "http://waysysweb.com/gfit", className = "com.waysysweb.gfit.Run")
    @ResponseWrapper(localName = "runResponse", targetNamespace = "http://waysysweb.com/gfit", className = "com.waysysweb.gfit.RunResponse")
    public String run(
        @WebParam(name = "dir", targetNamespace = "http://waysysweb.com/gfit")
        String dir,
        @WebParam(name = "output", targetNamespace = "http://waysysweb.com/gfit")
        String output)
        throws WsiAuthenticationException_Exception
    ;

}
