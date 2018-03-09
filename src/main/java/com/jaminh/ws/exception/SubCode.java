package com.jaminh.ws.exception;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.xml.namespace.QName;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SubCode {
	
	/**
	 * The sub code, to be used.
	 *
	 * <p>The format used is that of {@link QName#toString()}, i.e. "{" + Namespace URI + "}" + local part, where the
	 * namespace is optional.
	 *
	 * <p>Note that subcodes are only supported on SOAP 1.2.
	 */
	String code();

}
