package com.jaminh.ws.endpoint;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.transform.JDOMSource;
import org.springframework.util.StringUtils;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.xml.xpath.XPathOperations;

import com.jaminh.ws.target.TargetConverter;
import com.jaminh.ws.token.TokenService;
import com.jaminh.ws.trust.RequestType;

@Endpoint
public class TokenServiceEndpoint {
	
	private List<TargetConverter> targetConverters;
	
	private Map<String, TokenService> tokenServices;
	
	private XPathOperations xpath;
	
	public static final Namespace WS_TRUST_NAMESPACE = Namespace.getNamespace("wst", "http://docs.oasis-open.org/ws-sx/ws-trust/200512/");
	
	public static final Namespace WS_POLICY_NAMESPACE = Namespace.getNamespace("wsp", "http://schemas.xmlsoap.org/ws/2004/09/policy");
	
	public static final String TOKEN_TYPE_XPATH_EXPRESSION = "//wst:TokenType/text()";
	
	public static final String REQUEST_TYPE_XPATH_EXPRESSION = "//wst:RequestType/text()";
	
	public static final String CREATED_XPATH_EXPRESSION = "//wst:Lifetime/wsu:Created/text()";
	
	public static final String EXPIRES_XPATH_EXPRESSION = "";
	
	@PayloadRoot(localPart = "RequestSecurityToken", namespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/")
	@ResponsePayload
	public Element securityToken(@RequestPayload Element request)
	{
		JDOMSource source = new JDOMSource(request);
		
		Element appliesTo = request.getChild("AppliesTo", WS_POLICY_NAMESPACE);
		String target = getTarget(appliesTo);
		String requestTokenType = xpath.evaluateAsString(TOKEN_TYPE_XPATH_EXPRESSION, source);
		if (StringUtils.isEmpty(target) && StringUtils.isEmpty(requestTokenType))
		{
			throw new IllegalArgumentException("No target or token type supplied in request");
		}
		
		TokenService service = null;
		if (StringUtils.hasText(target) && tokenServices.containsKey(target))
		{
			service = tokenServices.get(target);
		}
		else if (tokenServices.containsKey(requestTokenType))
		{
			service = tokenServices.get(requestTokenType);
		}
		else
		{
			throw new IllegalArgumentException("No service for token type " + requestTokenType);
		}
		
		String requestRequestType = xpath.evaluateAsString(REQUEST_TYPE_XPATH_EXPRESSION, source);
		RequestType requestType = RequestType.getRequestType(URI.create(requestRequestType));
		
		Element response = null;
		switch (requestType)
		{
		case ISSUE:
			response = service.issue(request);
			break;
		case RENEW:
			response = service.renew(request);
			break;
		case CANCEL:
			response = service.cancel(request);
			break;
		case STS_CANCEL:
			response = service.stsCancel(request);
			break;
		case VALIDATE:
			response = service.validate(request);
			break;
		default:
			throw new IllegalArgumentException("Request type not understood");
		}

		return response;
	}
	
	/**
	 * Returns the request target if an AppliesTo element was included in the message, otherwise returns null.
	 * 
	 * @param element AppliesTo element
	 * @return the request target
	 */
	protected String getTarget(Element element)
	{
		if (element == null)
		{
			return null;
		}
		
		for (TargetConverter converter : targetConverters)
		{
			if (converter.canConvert(element))
			{
				return converter.getTarget(element);
			}
		}
		
		return null;
	}
	
	
}
