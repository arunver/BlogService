package web.DAO;

/**
 * PostDaoImpl.java
 * Class which implements all the operations related to a post (CRUD);
 * @author Arun.Verma
 * 
 */

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import service.utils.MongoUtils;
import web.MODEL.Comment;
import web.MODEL.Post;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

public class PostDaoImpl implements PostDao {

	private MongoTemplate mongoTemplate;
	private MongoUtils mongoUtils;

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public void setMongoUtils(MongoUtils mongoUtils) {
		this.mongoUtils = mongoUtils;
	}

	public String addPost(Post postRequest) throws UnknownHostException {
		/**
		 * wait for an acknowledgement from the primary server If no exception
		 * is raised, then response is "OK"
		 */
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.writeConcern(WriteConcern.JOURNAL_SAFE);

		System.out.println("Inside post addPost dao ");

		MongoClient client = new MongoClient("localhost", 27017);
		DB db = client.getDB("blog");
		DBCollection collection = db.getCollection("posts");
		String response = "";
		String title = postRequest.getTitle();

		System.out.println("Title is: " + title);

		String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
		permalink = permalink.replaceAll("\\W", ""); // get rid of non
														// alphanumeric
		permalink = permalink.toLowerCase();

		BasicDBObject userPost = new BasicDBObject();
		userPost.append("title", title);
		userPost.append("body", postRequest.getBody());
		userPost.append("author", postRequest.getAuthor());
		userPost.append("permalink", permalink);
		userPost.append("tags", postRequest.getTags());
		userPost.append("comments", postRequest.getComments());
		userPost.append("date", new java.util.Date());

		try {
			collection.insert(userPost);
			response = "Post with permalink " + permalink + " inserted";
		} catch (Exception e) {
			response = "Error in inserting post with permalink " + permalink;
		}
		return response;
	}

	public Post getPost(String permalink) {
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.writeConcern(WriteConcern.JOURNAL_SAFE);

		MongoClient client = null;
		
		String response="";
		Post postObj = new Post();
		try {
			client = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		DB db = client.getDB("blog");
		DBCollection collection = db.getCollection("posts");

		BasicDBObject post = new BasicDBObject();
		post.append("permalink", permalink);

		DBObject resultPost = collection.findOne(post);

		postObj.setAuthor((String) resultPost.get("author"));
		postObj.setBody((String) resultPost.get("body"));
		postObj.setPermalink((String) resultPost.get("permalink"));
		postObj.setTitle((String) resultPost.get("title"));
		
		BasicDBList tag = (BasicDBList) resultPost.get("tags");
		int size= tag.size();
		String[] tags = new String[size];

		Iterator it = tag.iterator();
		int count = 0;
		while (it.hasNext()) {

			tags[count] = (String) it.next();
			count++;
		}
		postObj.setTags(tags);

		BasicDBList comments = (BasicDBList) resultPost.get("comments");
		List<Comment> comment = new ArrayList<Comment>();

		Iterator commIt = comments.iterator();
		while (commIt.hasNext()) {
			Comment commentObj = new Comment();
			BasicDBObject comm = (BasicDBObject) commIt.next();
			
			commentObj.setBody(comm.getString("body"));
			commentObj.setAuthor(comm.getString("author"));
			commentObj.setEmail(comm.getString("email"));
			commentObj.setDate(new java.util.Date());
			
			comment.add(commentObj);
		}

		postObj.setComments(comment);
		System.out.println("The post author is: "+postObj.getAuthor());
		
		return postObj;
	}

	public String addComment(Comment commentRequest) {
		String response = "";
		try {
			MongoClient client = new MongoClient("localhost", 27017);
			DB db = client.getDB("blog");
			DBCollection collection = db.getCollection("posts");

			BasicDBObject comment = new BasicDBObject();
			comment.put("body", commentRequest.getBody());
			comment.put("author", commentRequest.getAuthor());
			comment.put("email", commentRequest.getEmail());
			//comment.put("date", commentRequest.getDate());
			comment.put("date", new java.util.Date());
			
			BasicDBObject permalink = new BasicDBObject();
			permalink.put("permalink", "us_constitution");

			BasicDBObject updateComment = new BasicDBObject();
			updateComment.put("$push", new BasicDBObject("comments", comment));
			WriteResult result = collection.update(permalink, updateComment,
					true, true);

			if (result.getN() == 1) {
				response = "Your comment has been posted";
			} else {
				response = "Failed to post your comment. Please check again";
			}
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return response;
	}

	public ArrayList<Post> showPost() {

		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();

		System.out.println("Inside show post method dao");
		MongoClient client = null;
		try {
			client = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		DB db = client.getDB("blog");
		DBCollection collection = db.getCollection("posts");

		/*BasicDBObject post = new BasicDBObject();
		post.append("permalink", "us_constitution");*/

		DBCursor cursor = collection.find();
		
		ArrayList<Post> postList= new ArrayList<Post>();
		int count=0;
		while (cursor.hasNext() && count<10) {
			DBObject tobj = cursor.next();
			Post postObj = new Post();

			postObj.setAuthor((String) tobj.get("author"));
			postObj.setTitle((String) tobj.get("title"));
			postObj.setBody((String) tobj.get("body"));
			postObj.setPermalink((String) tobj.get("permalink"));
			postObj.setDate((Date) tobj.get("date"));

			BasicDBList tag = (BasicDBList) tobj.get("tags");
			int size = tag.size();
			List<Comment> comment = new ArrayList<Comment>();

			BasicDBList comments = (BasicDBList) tobj.get("comments");

			String[] tags = new String[size];

			Iterator commIt = comments.iterator();
			Iterator it = tag.iterator();
			int countTag = 0;
			while (it.hasNext()) {

				tags[countTag] = (String) it.next();
				countTag++;
			}

			while (commIt.hasNext()) {
				Comment commentObj = new Comment();
				BasicDBObject comm = (BasicDBObject) commIt.next();
				commentObj.setBody(comm.getString("body"));
				commentObj.setAuthor(comm.getString("author"));
				commentObj.setEmail(comm.getString("email"));

				comment.add(commentObj);
			}

			System.out.println();
			postObj.setTags(tags);
			
			postObj.setComments(comment);

			System.out.println("Inside show post service");
			postList.add(postObj);
			count++;

		}

		return postList;

	}

	public ArrayList<Post> getPostByTag(String[] tags) {
		System.out.println("post with user defined tags are inside here");
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();

		System.out.println("Inside show post method dao");
		MongoClient client = null;
		try {
			client = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		DB db = client.getDB("blog");
		DBCollection collection = db.getCollection("posts");
		
		
		BasicDBList tagsToSearch = new BasicDBList();
       for(int i=0; i<tags.length; i++)
       {
    	   tagsToSearch.add(tags[i]);
       }
       
       BasicDBObject tagSearch= new  BasicDBObject("$in", tagsToSearch);
       BasicDBObject query= new BasicDBObject("tags", tagSearch);
       
		
		DBCursor cursor = collection.find(query);
		
		ArrayList<Post> postList= new ArrayList<Post>();
		int count=0;
		while (cursor.hasNext()) {
			DBObject tobj = cursor.next();
			Post postObj = new Post();

			postObj.setAuthor((String) tobj.get("author"));
			postObj.setTitle((String) tobj.get("title"));
			postObj.setBody((String) tobj.get("body"));
			postObj.setPermalink((String) tobj.get("permalink"));
			postObj.setDate((Date) tobj.get("date"));

			BasicDBList tag = (BasicDBList) tobj.get("tags");
			int size = tag.size();
			List<Comment> comment = new ArrayList<Comment>();

			BasicDBList comments = (BasicDBList) tobj.get("comments");

			String[] tagss= new String[size];

			Iterator commIt = comments.iterator();
			Iterator it = tag.iterator();
			int countTag = 0;
			while (it.hasNext()) {

				tagss[countTag] = (String) it.next();
				countTag++;
			}

			while (commIt.hasNext()) {
				Comment commentObj = new Comment();
				BasicDBObject comm = (BasicDBObject) commIt.next();
				commentObj.setBody(comm.getString("body"));
				commentObj.setAuthor(comm.getString("author"));
				commentObj.setEmail(comm.getString("email"));

				comment.add(commentObj);
			}

			System.out.println();
			postObj.setTags(tagss);
			
			postObj.setComments(comment);

			System.out.println("Inside show post service");
			postList.add(postObj);
			count++;

		}

		return postList;
	}

}
