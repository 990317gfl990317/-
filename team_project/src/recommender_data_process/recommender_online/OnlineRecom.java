package haha.hehe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OnlineRecom {

	public static void main(String[] args) {
		Map<String,ArrayList<MovieRating>> map=getSimTable();
		getRecommend(map,"260");
	}

	/**获取推荐电影
	 * @param mvid
	 */
	public static void getRecommend(Map<String,ArrayList<MovieRating>> map,String mvid) {
		List rec=map.get(mvid);
		Collections.sort( rec,new MyComparator());
		Map<String,String> mvInf=getMovieInfo();
		for(int  i=0;i<15;i++) {
			MovieRating mv=(MovieRating) rec.get(i);
			System.out.println(mv.getMovie()+" - "+mvInf.get(mv.getMovie()));
		}
	}
	/**读取 movie.dat
	 * @param map
	 * @return
	 */
	public static Map<String,String> getMovieInfo() {
		Map<String, String> map=new HashMap();
		try {
			FileReader freader=new FileReader("D:\\Git\\movies.dat");
			BufferedReader reader=new BufferedReader(freader);
			String str="";
			try {
				while((str=reader.readLine())!=null&&!(str=reader.readLine()).equals("")) {
					String[] ss=str.split("::");
					map.put(ss[0].trim(), ss[1]);
				}
			} catch (Exception e) {

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return map;
	}
	/**读取 simTable.txt
	 * @param map
	 * @return
	 */
	public static Map<String,ArrayList<MovieRating>> getSimTable() {
		Map<String,ArrayList<MovieRating>> map=new HashMap<>();
		String str="";
		try {
			FileReader freader=new FileReader("D:\\Git\\test.txt");
			BufferedReader reader=new BufferedReader(freader);
			try {
				while((str=reader.readLine())!=null&&!(str=reader.readLine()).equals("")) {
					String[] ss=str.split(":");
					String[] tem=ss[1].split("\\t+");
					ArrayList<MovieRating> list=new ArrayList<>();
					for(int i=0;i<tem.length;i++) {
						String[] mv=tem[i].split(",");
						MovieRating mo=new MovieRating(mv[0], mv[1]);
						list.add(mo);
					}
					map.put(ss[0].trim(), list);
				}
			} catch (Exception e) {

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}
	// 自定义比较器
	static class MyComparator implements Comparator {
		public int compare(Object o1, Object o2) {// 实现接口中的方法
			MovieRating p1 =  (MovieRating) o1; // 强制转换
			MovieRating p2 =  (MovieRating) o2;
			Double a=Double.parseDouble(p1.getRating());
			Double b=Double.parseDouble(p2.getRating());
			if(a>b)
				return -1;
			else if(a<b)
				return 1;
			else
				return 0;
		}
	}

}
