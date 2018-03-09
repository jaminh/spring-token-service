package com.jaminh.ws.xml;

import java.time.ZonedDateTime;

import org.springframework.xml.xpath.NodeMapper;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class DateTimeNodeMapper implements NodeMapper<ZonedDateTime> {

	@Override
	public ZonedDateTime mapNode(Node node, int nodeNum) throws DOMException {
		ZonedDateTime dateTime = ZonedDateTime.parse(node.getTextContent());
		return dateTime;
	}

}
