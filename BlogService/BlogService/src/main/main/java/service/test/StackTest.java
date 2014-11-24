package service.test;


import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;

import service.utils.MongoUtils;

import com.mongodb.DBCollection;

@Controller
public class StackTest {
	
		private MongoTemplate mongoTemplate;
		
		private MongoUtils mongoUtils;
		
		
		
		@Autowired
		public void setMongoTemplate(MongoTemplate mongoTemplate) {
			this.mongoTemplate = mongoTemplate;
		}
		
		public void setMongoUtils(MongoUtils mongoUtils) {
			this.mongoUtils = mongoUtils;
		}
	  
		public DBCollection getCollection(String collection) {
			System.out.println("Inside get collection "+collection);
			try
			{
				return mongoTemplate.getCollection(collection);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
		
		public void viewMongotemplate()
		{
			System.out.println("Mongo template is: "+mongoTemplate);
		}
		public static void main(String[] args) {
			StackTest test= new StackTest();
			
			test.viewMongotemplate();
		}

}
