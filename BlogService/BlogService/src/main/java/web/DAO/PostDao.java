package web.DAO;

import java.net.UnknownHostException;
import java.util.ArrayList;

import web.MODEL.Comment;
import web.MODEL.Post;

import com.mongodb.DBObject;

public interface PostDao {
	
	public String addPost(Post postRequest) throws UnknownHostException;
	
	public Post getPost(String permalink);
	
	public String addComment(Comment commentRequest);
	
	public ArrayList<Post> showPost();
	
	public ArrayList<Post> getPostByTag(String tag);
	
	public void likePost(String permalink);

}
