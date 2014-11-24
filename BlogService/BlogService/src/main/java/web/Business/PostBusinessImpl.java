package web.Business;

import java.net.UnknownHostException;
import java.util.ArrayList;

import web.DAO.PostDao;
import web.MODEL.Comment;
import web.MODEL.Post;

import com.mongodb.DBObject;

public class PostBusinessImpl implements PostBusiness{

	private PostDao postDao;
	
	public void setPostDao(PostDao postDao)
	{
		this.postDao=postDao;
	}
	
	public String addPost(Post postRequest) {
		String response="";
		try {
			response= postDao.addPost(postRequest);
			System.out.println("Inside addPost business impl");
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public Post getPost(String permalink) {
		//String response= postDao.getPost(permalink);
		Post post=postDao.getPost(permalink);
		return post;
	}

	public String addComment(Comment commentRequest) {
		String response= postDao.addComment(commentRequest);
		return response;
	}
	
	public ArrayList<Post> showPost()
	{
		System.out.println("Inside show post method business");
		ArrayList<Post> ob=  postDao.showPost();
		
		//String response=postDao.showPost();
		//return response;
		return ob;
	}

	

	public ArrayList<Post> getPostByTag(String tag) {
		// TODO Auto-generated method stub
		System.out.println("Inside get post by tag");
		System.out.println(postDao.getPostByTag(tag).size());
		return postDao.getPostByTag(tag);
	}

	public void likePost(String permalink) {
		System.out.println("Entering into like post in business");
		postDao.likePost(permalink);
		
	}

}
