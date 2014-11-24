package web.Business;

import java.util.ArrayList;

import web.MODEL.Comment;
import web.MODEL.Post;

import com.mongodb.DBObject;

public interface PostBusiness {
	
	public String addPost(Post postRequest);
	
	public Post getPost(String permalink);
	
	public String addComment(Comment comment);
	
	public ArrayList<Post> showPost();
	
	//public ArrayList<Post> getPostByTag(String[] tags);

	public ArrayList<Post> getPostByTag(Post  postObj);
}