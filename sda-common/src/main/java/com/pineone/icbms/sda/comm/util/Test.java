package com.pineone.icbms.sda.comm.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;


/*import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
*/


import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;

public class Test {
	public void updateTest() {
		// Add a new book to the collection
//		String service = "http://219.248.137.7:13030/icbms/update";
//		UpdateRequest ur = UpdateFactory
//				.create(""
//						+ "delete  { <http://www.iotoasis.org/TemperatureObservationValue_LB0001> <http://data.nasa.gov/qudt/owl/qudt#hasNumericValue>  ?value . } "
//						+ "insert  { <http://www.iotoasis.org/TemperatureObservationValue_LB0001> <http://data.nasa.gov/qudt/owl/qudt#hasNumericValue>  \"19\" . } " 
//						+ "WHERE   { <http://www.iotoasis.org/TemperatureObservationValue_LB0001> <http://data.nasa.gov/qudt/owl/qudt#hasNumericValue> ?value . }");
//		UpdateProcessor up = UpdateExecutionFactory.createRemote(ur, service);
//		up.execute();

		// Query the collection, dump output
//		String service1 = "http://219.248.137.7:13030/icbms/";
//		QueryExecution qe = QueryExecutionFactory.sparqlService(service1,
//				"SELECT * WHERE {<http://www.iotoasis.org/Student_S00001> ?r ?y} limit 20 ");
//
//		ResultSet results = qe.execSelect();
//		ResultSetFormatter.out(System.out, results); 
//		qe.close();
		Model model = ModelFactory.createDefaultModel();
		Statement s = model.createStatement(model.createResource("ex:e1"),model.createProperty("ex:p1"), ResourceFactory.createTypedLiteral(new String("0.6")));
		model.add(s);
		String query = "select * {?s ?p ?o }";
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		ResultSetFormatter.out(System.out, results);
		qe.close();
	}
	
	public static class Item {
		private String name;
		private int qty;
		private BigDecimal price;
		public Item(String name, int qty, BigDecimal price) {
			super();
			this.name = name;
			this.qty = qty;
			this.price = price;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getQty() {
			return qty;
		}
		public void setQty(int qty) {
			this.qty = qty;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
	}
	
	
	public static void main(String[] args) {
		Test t = new Test();
//		t.insertTest();
		t.updateTest();
		
		List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8);
		List<Integer> towEvenSquares = numbers.parallelStream()
				.filter(n -> {System.out.println("filtering "+n);  return n % 2  == 0;})
				.map(n -> {System.out.println("mappng " + n); return n*n;})
				.limit(20)
				.collect(Collectors.toList());

		//------------------------------------------
		List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
		
		List<Item> items2 = Arrays.asList(
				new Item("apple", 10, new BigDecimal("9.99")),
				new Item("banana", 20, new BigDecimal("19.99")),
				new Item("orange", 10, new BigDecimal("29.99")),
				new Item("watermelon", 10, new BigDecimal("29.99")),
				new Item("papaya", 20, new BigDecimal("9.99")),
				new Item("apple", 10, new BigDecimal("9.99")),
				new Item("banana", 10, new BigDecimal("19.99")),
				new Item("apple", 20, new BigDecimal("9.99"))
				);

		
		Map<String, Long> result = items.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
		System.out.println(result);
			
		Map<String, Long> counting = items2.stream().filter(item -> item.getName().equals("apple")).collect(Collectors.groupingBy(Item::getName, Collectors.counting()));
		//Map<String, Long> counting = items2.stream().collect(Collectors.groupingBy( (Item i) ->i.getName(), Collectors.counting()));
		
		System.out.println(counting);
		
		Map<String, Integer> sum = items2.stream().collect(Collectors.groupingBy(Item::getName, Collectors.summingInt(Item::getQty)));
		
		
		
		System.out.println(sum);
		
		
		
		String[] myArray = {"this", "is", "a", "sentence"};
		Arrays.stream(myArray).forEach(  (x) -> System.out.println(x) );
		
		//String result2 = Arrays.stream(myArray).forEach( (i) -> System.out.println(i); ).reduce("=", (a,b) -> a + b);
		//System.out.println("result2 : "+result2);
		
		
		
//		for(int i = 0; i < 1000000; i ++) {
//			if((i % 5000) == 0) {
//				System.out.println("com.pineone.icbms.sda.triple.regist.bin["+i+"] =>"+Utils.getSdaProperty("com.pineone.icbms.sda.triple.regist.bin"));
//			}
//		}
		
		UUID uid = UUID.randomUUID();
		System.out.println(uid.toString());
		
	}
}