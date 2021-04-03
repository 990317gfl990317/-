package haha;

import java.io.Serializable;
import java.util.Comparator;

public class MovieRating implements Serializable ,Comparator<MovieRating>{
	private String user;
	private String movie;
	private String rating;
	private String stamp;
	
	
	public MovieRating() {
		super();
	}
	
	public MovieRating(String user, String rating) {
		super();
		this.user=user;
		this.rating=rating;
	}
	
	
	public MovieRating(String user, String movie, String rating) {
		super();
		this.user = user;
		this.movie = movie;
		this.rating = rating;
	}

	public MovieRating(String user, String movie, String rating, String stamp) {
		super();
		this.user = user;
		this.movie = movie;
		this.rating=rating;
		this.stamp = stamp;
	}

	public String getStamp() {
		return stamp;
	}

	public void setStamp(String stamp) {
		this.stamp = stamp;
	}

	public String getMovie() {
		return movie;
	}

	public void setMovie(String movie) {
		this.movie = movie;
	}

	public String getUser() {
		return this.user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	@Override
	public String toString() {
		return "MovieRating [user=" + user + ", movie=" + movie + ", rating=" + rating + ", stamp=" + stamp + "]";
	}
	@Override
	public int compare(MovieRating o1, MovieRating o2) {
		String a=o1.getUser();
		String b=o2.getUser();
		Integer aa=Integer.parseInt(a);
		Integer bb=Integer.parseInt(b);
		if(aa>bb)
			return 1;
		else if(aa<bb)
			return -1;
		else
			return 0;
	}
}
