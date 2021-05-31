package com.demo.loan;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class LoanRestAPI extends SpringRouteBuilder {

	@Autowired
	private Environment env;
	
	@Value("${camel.component.servlet.mapping.context-path}")
	private String contextPath;

	@Override
	public void configure() throws Exception {
		restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
				.dataFormatProperty("prettyPrint", "true")
				.enableCORS(true)
				.port(env.getProperty("server.port", "8080"))
				.contextPath(contextPath.substring(0, contextPath.length() - 2))
				// turn on swagger api-doc
				.apiContextPath("/api-doc")
				.apiProperty("api.title", "User API")
				.apiProperty("api.version", "1.0.0");

		rest()
			.tag("Loan Application")
			.description("Apply for loan")
				.post("/v1-0/loan/").id("Loan-ID")
					.consumes(MediaType.APPLICATION_JSON_VALUE)
					.type(LoanApplicationRequest.class)
					.bindingMode(RestBindingMode.json)
					.responseMessage()
						.code(204)
						.message("Loan application submitted")
					.endResponseMessage()
				.route()
					.process(new Processor() {
						
						@Override
						public void process(Exchange exchange) throws Exception {
							LoanApplicationRequest request = exchange.getIn().getBody(LoanApplicationRequest.class);
							request.setApplicationId(UUID.randomUUID().toString());
							exchange.getIn().setBody(request);
							exchange.getIn().setHeader(KafkaConstants.KEY, request.getApplicationId());
							
						}
					})
					.marshal().json(JsonLibrary.Jackson)
					.log(LoggingLevel.DEBUG, "com.demo","Sending Application request to topic : ${body}")
				.to("kafka:ApplicationTopic")
				.endRest();

	}

}
