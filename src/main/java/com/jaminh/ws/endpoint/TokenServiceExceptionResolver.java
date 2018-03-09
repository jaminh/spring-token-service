package com.jaminh.ws.endpoint;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.AbstractSoapFaultDefinitionExceptionResolver;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.soap12.Soap12Fault;

import com.jaminh.ws.exception.SubCode;

public class TokenServiceExceptionResolver extends AbstractSoapFaultDefinitionExceptionResolver {
	
	private Map<Class<? extends Exception>, QName> subcodeMapping = new HashMap<Class<? extends Exception>, QName>();
	
	private TokenServiceExceptionResolver() {
	}
	
	private TokenServiceExceptionResolver(Map<Class<? extends Exception>, QName> mapping) {
		subcodeMapping.putAll(mapping);
	}

	@Override
	protected SoapFaultDefinition getFaultDefinition(Object endpoint, Exception ex) {
		SoapFaultDefinition soapFault = new SoapFaultDefinition();
		soapFault.setFaultCode(SoapFaultDefinition.SENDER);
		soapFault.setFaultStringOrReason(ex.getMessage());
		
		return soapFault;
	}
	
	@Override
	protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
		if (fault instanceof Soap12Fault)
		{
			QName subcode = getSubcode(ex);
			if (subcode != null)
			{
				((Soap12Fault) fault).addFaultSubcode(subcode);
			}
		}
	}

	protected QName getSubcode(Exception ex) {
		SubCode faultAnnotation = ex.getClass().getAnnotation(SubCode.class);
		if (faultAnnotation != null)
		{
			return QName.valueOf(faultAnnotation.code());
		}
		else if (subcodeMapping.containsKey(ex.getClass()))
		{
			return subcodeMapping.get(ex.getClass());
		}
		
		return null;
	}
}
