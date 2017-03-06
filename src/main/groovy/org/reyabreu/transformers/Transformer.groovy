package org.reyabreu.transformers

import org.reyabreu.domain.Thing
import org.reyabreu.domain.ThingSearchResults
import org.springframework.stereotype.Component

@Component('transformer')
class Transformer {

	Thing mapThing(Map map) {
		new Thing(
				id: map.id,
				name: map.name,
				owner: map.owner
				)
	}

	String constructQuery(Map headers) {
		def wheres = []
		if (headers.name) {
			wheres << 'name = :#${header.name}'
		}
		if (headers.owner) {
			wheres << 'owner = :#${header.owner}'
		}

		def query = 'select * from THING'
		if (wheres) {
			query += ' where ' + String.join(' and ', wheres)
		}
		return query
	}

	ThingSearchResults mapThingSearchResults(List<Map> body) {
		new ThingSearchResults(
				size: body.size,
				things: body.collect { mapThing it }
				)
	}
}
