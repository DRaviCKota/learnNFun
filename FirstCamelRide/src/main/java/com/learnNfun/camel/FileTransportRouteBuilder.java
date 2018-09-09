package com.learnNfun.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;

public class FileTransportRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {

		from("file:src/data?noop=true")
			.choice()
				.when(xpath("/person/city = 'London'"))
					.log("UK Message")
					.to("file:target/messages/uk")
				.otherwise()
					.log("Other message")
					.to("file:target/messages/others");
				
		
	}
}
