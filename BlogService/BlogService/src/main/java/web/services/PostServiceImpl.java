package web.services;

import java.util.ArrayList;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import web.Business.PostBusiness;
import web.MODEL.Comment;
import web.MODEL.Post;

public class PostServiceImpl implements PostManager {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	private PostBusiness postBusiness;
	
	public void setPostBusiness(PostBusiness postBusiness)
	{
		this.postBusiness= postBusiness;
	}

	public String addPost(Post postRequest) {
		String response= postBusiness.addPost(postRequest);
		return response;
	}

	public Post getPost(String permalink) {
		//String response= postBusiness.getPost(permalink);
		Post post=postBusiness.getPost(permalink);
		return post;
	}

	public String addComment(Comment commentRequest) {
		String response= postBusiness.addComment(commentRequest);
		System.out.println("Inside addPost service");
		return response;
	}

	public ArrayList<Post> showPost() {
		//System.out.println("Entering into show post method");
		//Post post= postBusiness.showPost();
		System.out.println("Entering into show post method");
		ArrayList<Post> post= postBusiness.showPost();
		return post;
	}

	public ArrayList<Post> getPostByTag(String tag) {
		System.out.println("Entering into get post by tag method");
		System.out.println("Post object is :"+tag);
		//ArrayList<Post> post=postBusiness.getPostByTag(tags);
		ArrayList<Post> post=postBusiness.getPostByTag(tag);
		
		return post;
	}

	public void likePost(String id) {
		System.out.println("Entering like post method in services");
		postBusiness.likePost(id);
		
	}

	public void likeComment(String id) {
		// TODO Auto-generated method stub
		
	}
	

}
