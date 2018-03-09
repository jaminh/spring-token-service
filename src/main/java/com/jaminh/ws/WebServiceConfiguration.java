package com.jaminh.ws;

import java.util.List;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.SoapEnvelopeLoggingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;

@EnableWs
@Configuration
public class WebServiceConfiguration extends WsConfigurerAdapter {
	
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/ws/*");
	}
	
	@Bean(name = "wsdl")
	public SimpleWsdl11Definition wsdl11Definition() {
		return new SimpleWsdl11Definition(new ClassPathResource("wsdl/ws-trust-1.3.wsdl"));
	}
	
	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		interceptors.add(soapEnvelopeLoggingInterceptor());
		interceptors.add(schemaValidationInterceptor());
	}
	
	@Bean
	public SoapEnvelopeLoggingInterceptor soapEnvelopeLoggingInterceptor()
	{
		SoapEnvelopeLoggingInterceptor interceptor = new SoapEnvelopeLoggingInterceptor();
		return interceptor;
	}

	@Bean
	public PayloadValidatingInterceptor schemaValidationInterceptor()
	{
		PayloadValidatingInterceptor interceptor = new PayloadValidatingInterceptor();
		interceptor.setSchema(new ClassPathResource("xsd/ws-trust-1.3.xsd"));
		return interceptor;
	}
}
