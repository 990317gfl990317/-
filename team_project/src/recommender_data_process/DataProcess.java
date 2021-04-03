package haha;

import java.io.*;
import java.util.*;

public class DataProcess {
    public static void main(String[] args) {
        try {
            Map<String, ArrayList<MovieRating>> item_to_user = new HashMap<>();
            Map<String, ArrayList<MovieRating>> user_to_item = new HashMap<>();
            TreeSet<Integer> mvids = new TreeSet<>();
            ArrayList<MovieRating> list = new ArrayList<>();
            ArrayList<MovieRating> ulist = new ArrayList<>();
            FileReader freader = new FileReader("D:\\Git\\ratings_train.dat");
            BufferedReader reader = new BufferedReader(freader);
            String str = "";
            TreeSet<Integer> movieset = new TreeSet<>();
            TreeMap<Integer, String> moviemap = new TreeMap<>();
            String userid = null;
            while ((str = reader.readLine()) != null && !(str = reader.readLine()).equals("")) {
                String[] temp = str.split("::");
                moviemap.put(Integer.parseInt(temp[1]), str);
                mvids.add(Integer.parseInt(temp[0]));
                if (item_to_user.containsKey(temp[1])) {
                    list = item_to_user.get(temp[1]);
                    list.add(new MovieRating(temp[0], temp[1], temp[2], temp[3]));
                    item_to_user.put(temp[1], list);
                } else {
                    list = new ArrayList<>();
                    list.add(new MovieRating(temp[0], temp[1], temp[2], temp[3]));
                    item_to_user.put(temp[1], list);
                }
                if (user_to_item.containsKey(temp[0])) {
                    ulist = user_to_item.get(temp[0]);
                    ulist.add(new MovieRating(temp[0], temp[1], temp[2], temp[3]));
                    user_to_item.put(temp[0], ulist);
                } else {
                    ulist = new ArrayList<>();
                    ulist.add(new MovieRating(temp[0], temp[1], temp[2], temp[3]));
                    user_to_item.put(temp[0], ulist);
                }
            }
            list = null;
            ulist = null;
            reader.close();
            freader.close();
            System.out.println("------------------------------------");
            Iterator<Integer> it = mvids.iterator();
            while (it.hasNext()) {
                Integer mvid = it.next();
                MovieRating a = new MovieRating();
                a.setMovie(mvid + "");
                try {
                    similarity(item_to_user, a, user_to_item);
                } catch (Exception e) {
                    System.out.println("null" + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算相似度
     *
     * @param va
     * @param vb
     * @param //flag true:item-to-user  false:user-to-item
     * @return
     */
    public static float sim(ArrayList<MovieRating> va, ArrayList<MovieRating> vb) {
        // 如果向量维度不相等，则不能计算，函数退出
        if (va.size() != vb.size()) {
            return 0;
        }
        int size = va.size();
        float simVal = 0;
        //sim(va,vb) = (va * vb) / (|va| * |vb|)
        // 分子 = va.get(0)*vb.get(0) + va.get(1)*vb.get(1) +...+ va.get(size - 1)*vb.get(size - 1)
        // 分母 = va的模 * vb的模 = sqrt((va.get(0))的平方 + (va.get(1))的平方 + ... + va.get(size - 1)的平方) * sqrt((vb.get(0))的平方 + (vb.get(1))的平方 + ... + vb.get(size - 1)的平方)
        float num = 0;// numerator分子
        float den = 1;// denominator分母

        float mu1 = 0, mu2 = 0;
        for (int i = 0; i < va.size(); i++) {
            MovieRating ura = (MovieRating) va.get(i);
            MovieRating urb = (MovieRating) vb.get(i);
            float n1 = Float.parseFloat(ura.getRating());
            float n2 = Float.parseFloat(urb.getRating());
            num = num + n1 * n2;
            mu1 = (float) (mu1 + Math.pow(n1, 2));
            mu2 = (float) (mu2 + Math.pow(n2, 2));
        }
        den = (float) (Math.sqrt(mu1) * Math.sqrt(mu2));
        simVal = num / den;
        return simVal;
    }

    /**
     * 存储对象
     *
     * @param item_to_user
     * @param user_to_item
     */
    public static void storeObject(Map<String, ArrayList<MovieRating>> item_to_user, Map<String, ArrayList<MovieRating>> user_to_item) {
        try {
            FileOutputStream foo = new FileOutputStream("D:/recommender_system_data/item_user.dat");
            ObjectOutputStream oo = new ObjectOutputStream(foo);
            oo.writeObject(item_to_user);
            oo.writeObject(user_to_item);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * item_to_user 邻居查找
     *
     * @param item_to_user
     * @param //movieid
     */
    public static void similarity(Map<String, ArrayList<MovieRating>> item_to_user, MovieRating amovieid, Map<String, ArrayList<MovieRating>> user_to_item) {

        //vi邻居
        List alist = getNeighbor(item_to_user, amovieid, user_to_item);
        ArrayList<MovieRating> va = item_to_user.get(amovieid.getMovie());
        if (va != null)
            Collections.sort(va, new UserComparator());//对va按userid进行升序排序
        else
            return;
        try {
            FileWriter fw = new FileWriter("D:\\Git\\test.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            String str = amovieid.getMovie() + ":";
            bw.write(str);
            Iterator<MovieRating> it = alist.iterator();
            System.out.println("-------------bi--------------------------");
            int count = 0;
            while (it.hasNext()) {
                MovieRating item_id = it.next();
                ArrayList<MovieRating> vb = item_to_user.get(item_id.getMovie());
                Collections.sort(vb, new UserComparator());//对va按userid进行升序排序
                //TreeSet setb=new TreeSet<>(vb);
                sortUser(va, vb);
                //对va,vb按用户进行排序
                double s = sim(va, vb);//计算相似度
                str = item_id.getMovie() + "," + item_id.getRating() + "\t";
                bw.write(str);
                if (count >= 15)
                    break;
                count++;
            }
            bw.newLine();
            bw.flush();
            bw.close();
            fw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    /**
     * 获得邻居
     *
     * @param item_to_user
     * @param amovieid
     * @param user_to_item
     * @return
     */
    public static ArrayList<MovieRating> getNeighbor(Map<String, ArrayList<MovieRating>> item_to_user, MovieRating amovieid, Map<String, ArrayList<MovieRating>> user_to_item) {
        ArrayList<MovieRating> alist = item_to_user.get(amovieid.getMovie());
        ArrayList<MovieRating> res = new ArrayList<>();
        if (alist != null) {
            Iterator<MovieRating> it = alist.iterator();
            while (it.hasNext()) {
                MovieRating aUserRa = it.next();
                ArrayList<MovieRating> ulist = user_to_item.get(aUserRa.getUser());
                res.addAll(ulist);
            }
        }
        return res;
    }

    /**
     * 调整va,vb 是两个向量维度相同
     *
     * @param va
     * @param vb
     * @return
     */
    public static void sortUser(ArrayList<MovieRating> va, ArrayList<MovieRating> vb) {
        int i = 0, j = 0;
        for (; i < va.size() && j < vb.size(); ) {
            int n1 = Integer.parseInt(va.get(i).getUser());
            int n2 = Integer.parseInt(vb.get(j).getUser());
            if (n1 == n2) {

            } else if (n1 < n2) {
                MovieRating te = va.get(i);
                MovieRating ne = new MovieRating(te.getUser(), te.getMovie(), "0", "0");
                vb.add(j, ne);
            } else {
                MovieRating te = vb.get(i);
                MovieRating ne = new MovieRating(te.getUser(), te.getMovie(), "0", "0");
                va.add(i, ne);
            }
            i++;
            j++;
        }

        List<MovieRating> sub = new ArrayList();
        if (i < va.size()) {
            sub = (List<MovieRating>) va.subList(i, va.size());
            for (int d = 0; d < sub.size(); d++) {
                MovieRating te = sub.get(d);
                MovieRating ne = new MovieRating(te.getUser(), te.getMovie(), "0", "0");
                vb.add(ne);
            }
        } else if (j < vb.size()) {
            sub = (List<MovieRating>) vb.subList(j, vb.size());
            for (int d = 0; d < sub.size(); d++) {
                MovieRating te = sub.get(d);
                MovieRating ne = new MovieRating(te.getUser(), te.getMovie(), "0", "0");
                va.add(ne);
            }
        }
    }

    // 自定义比较器
    static class UserComparator implements Comparator {
        public int compare(Object o1, Object o2) {// 实现接口中的方法
            MovieRating p1 = (MovieRating) o1; // 强制转换
            MovieRating p2 = (MovieRating) o2;
            int a = Integer.parseInt(p1.getUser());
            int b = Integer.parseInt(p2.getUser());
            if (a > b)
                return 1;
            else if (a < b)
                return -1;
            else
                return 0;
        }
    }
}