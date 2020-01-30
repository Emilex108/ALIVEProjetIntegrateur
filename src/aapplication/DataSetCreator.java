package aapplication;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class DataSetCreator {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		int NB_DATA = 69420;
		double d,a,g = 0;
		int detection = 8;
		String set = "";
		PrintWriter writer = new PrintWriter("RESULTS.txt", "UTF-8");
		for(int i = 0; i < NB_DATA; i++) {
			d = (Math.random()*300);
			a = (Math.random()*300);
			g = (Math.random()*300);
			set += d/300 + ";" + a/300 + ";" + g/300 + ";";
			if(d < detection && a < detection && g < detection){
			    	set += "0";
			    }else if(g < detection && a < detection && !(d < detection)){
			    	set += "1";
			    }else if(!(g < detection) && a < detection && d < detection){
			    	set += "2";
			    }else if(g < detection && !(a < detection) && d < detection){
			    	set += "3";
			    }else if(!(g < detection) && !(a < detection) && d < detection){
			    	set += "2";
			    }else if (g < detection && !(a < detection) && !(d < detection)){
			    	set += "1";
			    }else if(!(g < detection) && !(a < detection) && !(d < detection)){
			    	set += "3";
			    }else if(!(g < detection) && a < detection && !(d < detection)){
			      if(d < g){
			    	  set += "2";
			      }else if(g < d){
			    	  set += "1";
			      }else{
			    	  set += "0";
			      }
			    }
			writer.println(set);
			set = "";
		}
		System.out.println("Results created");
		writer.close();
	}

}
