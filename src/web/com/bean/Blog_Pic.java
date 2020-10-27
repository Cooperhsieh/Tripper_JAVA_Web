package web.com.bean;

import java.io.Serializable;

public class Blog_Pic  {
    private String blogId ;
    private String locId ;
    private String pic1 ;
    private String pic2 ;
    private String pic3 ;
    private String pic4 ;
    private byte[] pic11;
    private byte[] pic12;
    private byte[] pic13;
    private byte[] pic14;
    
    

    public byte[] getPic11() {
		return pic11;
	}

	public void setPic11(byte[] pic11) {
		this.pic11 = pic11;
	}

	public byte[] getPic12() {
		return pic12;
	}

	public void setPic12(byte[] pic12) {
		this.pic12 = pic12;
	}

	public byte[] getPic13() {
		return pic13;
	}

	public void setPic13(byte[] pic13) {
		this.pic13 = pic13;
	}

	public byte[] getPic14() {
		return pic14;
	}

	public void setPic14(byte[] pic14) {
		this.pic14 = pic14;
	}

	public Blog_Pic() {
        super();
    }
    
    public Blog_Pic(String pic1, String pic2, String pic3, String pic4) {
    
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
        this.pic4 = pic4;
    }

    public Blog_Pic(String blogId, String locId, String pic1, String pic2, String pic3, String pic4) {
        this.blogId = blogId;
        this.locId = locId;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
        this.pic4 = pic4;
    }
    public Blog_Pic(String blogId, String locId, byte[] pic11,byte[] pic12,byte[] pic13,byte[] pic14) {
    	this.blogId = blogId;
    	this.locId = locId;
    	this.pic11 = pic11;
    	this.pic12 = pic12;
    	this.pic13 = pic13;
    	this.pic14 = pic14;
    	
    }



    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public String getPic4() {
        return pic4;
    }

    public void setPic4(String pic4) {
        this.pic4 = pic4;
    }
}

