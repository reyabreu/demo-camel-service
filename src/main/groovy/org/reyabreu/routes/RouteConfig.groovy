package org.reyabreu.routes;

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.rest.RestBindingMode
import org.reyabreu.domain.Thing
import org.reyabreu.domain.ThingSearchResults
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
public class RouteConfig extends RouteBuilder {

	@Value('${rest.host}')
	String host

	@Value('${rest.port}')
	String port

	@Override
	public void configure() throws Exception {

		restConfiguration()
				.component('jetty')
				.host(host)
				.port(port)
				.bindingMode(RestBindingMode.json)

		rest('/things')
				.post().type(Thing).to('direct:createThing')
				.get().outType(ThingSearchResults).to('direct:getThings')
				.get('/{id}').outType(Thing).to('direct:getThing')
				//				.put('/{id}').type(Thing).to('direct:changeThing')
				.delete('/{id}').outType(Thing).to('direct:removeThing')

		from('direct:createThing')
				.to('jpa:org.reyabreu.domain.Thing')
				.end()

		from('direct:getThing')
				.to('sql:select * from THING where id=:#${header.id}?dataSource=dataSource&outputType=SelectOne')
				.bean('transformer', 'mapThing')
				.end()

		from('direct:getThings')
				.setProperty('query')
				.method('transformer', 'constructQuery(${headers})')
				.toD('sql:${property.query}?dataSource=dataSource')
				.bean('transformer', 'mapThingSearchResults')
				.end()

		//		from('direct:changeThing')
		//				.setProperty('query')
		//				.method('transformer', 'constructUpdate(${headers})')
		//				.toD('sql:${property.query}?dataSource=dataSource')
		//				.bean('transformer', 'updateResult')

		from('direct:removeThing')
				.to('direct:getThing')
				.setProperty('thing', body())
				.to('sql:delete from THING where id = :#${body.id}?dataSource=dataSource')
				.setBody(exchangeProperty('thing'))
				.end()
	}
}
