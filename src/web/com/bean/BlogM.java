package web.com.bean;

import java.io.Serializable;
import java.util.List;

public class BlogM implements Serializable {

    private String blogTittle;
    private String blogDesc;
    private String BlogId;
    
    public BlogM(String blogTittle, String blogDesc, String blogId, List<BlogD> blogDs) {
		super();
		this.blogTittle = blogTittle;
		this.blogDesc = blogDesc;
		BlogId = blogId;
		this.blogDs = blogDs;
	}
    
    
    public BlogM(String blogTittle, String blogDesc) {
		super();
		this.blogTittle = blogTittle;
		this.blogDesc = blogDesc;
		;
	}
    
    
	public BlogM(String blogTittle, String blogDesc, String blogId) {
		super();
		this.blogTittle = blogTittle;
		this.blogDesc = blogDesc;
		BlogId = blogId;
	}


	private List<BlogD> blogDs;
	public String getBlogTittle() {
		return blogTittle;
	}
	public void setBlogTittle(String blogTittle) {
		this.blogTittle = blogTittle;
	}
	public String getBlogDesc() {
		return blogDesc;
	}
	public void setBlogDesc(String blogDesc) {
		this.blogDesc = blogDesc;
	}
	public String getBlogId() {
		return BlogId;
	}
	public void setBlogId(String blogId) {
		BlogId = blogId;
	}
	public List<BlogD> getBlogDs() {
		return blogDs;
	}
	public void setBlogDs(List<BlogD> blogDs) {
		this.blogDs = blogDs;
	}
	

  
}

