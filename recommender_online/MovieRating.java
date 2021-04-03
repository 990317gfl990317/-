package haha.hehe;

import java.io.Serializable;
import java.util.Comparator;

public class MovieRating implements Serializable ,Comparator<MovieRating>{
	private String movie;
	private String rating;
	
	
	public MovieRating(String movie, String rating) {
		super();
		this.movie = movie;
		this.rating = rating;
	}



	public MovieRating() {
		super();
	}
	
	

	public String getMovie() {
		return movie;
	}



	public void setMovie(String movie) {
		this.movie = movie;
	}



	public String getRating() {
		return rating;
	}



	public void setRating(String rating) {
		this.rating = rating;
	}



	@Override
	public String toString() {
		return "MovieRating [movie=" + movie + ", rating=" + rating + "]";
	}



	@Override
	public int compare(MovieRating o1, MovieRating o2) {
		String a=o1.getRating();
		String b=o2.getRating();
		Double aa=Double.parseDouble(a);
		Double bb=Double.parseDouble(b);
		if(aa>bb)
			return 1;
		else if(aa<bb)
			return -1;
		else
			return 0;
	}
	
	

}
