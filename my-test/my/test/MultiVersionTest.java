package my.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;

public class MultiVersionTest {

	public static void main(String[] args) {
		System.setProperty("h2.baseDir", "D:\\h2database\\baseDir");
		MVStore s = MVStore.open("D:\\h2database\\baseDir\\multiVersion");
		// create/get the map named "data"
		MVMap<String, String> map = s.openMap("data");
		List<String> list = new ArrayList<String>();
		for(int i = 0 ; i < 320 ; i ++){
        	Random r = new Random();
        	int n1 = r.nextInt();
        	int n2 = r.nextInt();
        	String key = "" +i*n1 + "zhh" + i + "";
        	list.add(key);
			map.put(key, "" +i*n2 + "zhh_lastname" + i + "");
        }
		System.out.println("第一次插入所有完毕!!!\r\n");
		long oldVersion = s.getCurrentVersion();
		System.out.println("oldVersion=" + oldVersion);
		s.commit();
		System.out.println("第一次提交所有完毕!!!\r\n");
		int i = 0;
		for(String li : list){
			if(i < 220){
				Random r = new Random();
				int n1 = r.nextInt();
				map.put(li, "" +i*n1 + "zhh_new_lastname" + i + "");
			}else{
				map.remove(li);
			}
			i++;
		}
		System.out.println("第二次插入所有完毕!!!\r\n");
		MVMap<String, String> oldMap =
		        map.openVersion(oldVersion);
		s.commit();
		System.out.println("第二次提交所有完毕!!!\r\n");
		System.out.println("老的记录如下:");
		for(String li : list){
			System.out.println(oldMap.get(li));
		}
		System.out.println("新的记录如下:");
		for(String li : list){
			System.out.println(map.get(li));
		}
	}

}
