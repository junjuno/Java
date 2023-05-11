 

package exam;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prob1 {
	public static void main(String[] args) {
		String[] array={"황남기85점","조성호89점","한인성10점","독고정진77점"};
		printMaxScore(array);
	}	
	private static void printMaxScore(String[] array){
		int maxScore=-1;
		String maxName="";
		for(String str:array) {
			String regExp = "([가-힣A-Za-z]+)([0-9]+)점";
			Pattern pat = Pattern.compile(regExp);
			Matcher mat = pat.matcher(str);
			if(mat.find()) {
				String name = mat.group(1);
				String score = mat.group(2);
				int iscore = Integer.parseInt(score);
				if(iscore>maxScore) {
					maxScore = iscore;
					maxName = name;
				}
			}
			
		}
		System.out.println("최고점수는 "+maxName+"님 "+maxScore+"입니다.");
	}
	/*
	private static void printMaxScore(String[] array){
		int maxIndex=0;
		int maxValue=0;
		String name;
		for(int i=0;i<array.length;i++) {
			String score = array[i].replaceAll("[^0-9]", "");
			if (maxValue<Integer.parseInt(score)) {
				maxIndex=i;
				maxValue=Integer.parseInt(score);
			}
		}
		
		name = (String) array[maxIndex].subSequence(0, array[maxIndex].length()-3);
		
		System.out.println("최고점수는 "+name+"님 "+maxValue+"입니다.");
		 
	}	*/
	
	/*
	private static void printMaxScore(String[] array){
		// 구현하세요.
		int maxScore=-1;
		String maxName="";
		for(String str:array) {
			 
			char[] charrArr = str.toCharArray();
			String name="";
			String score="";
			for(int i=0; i<charrArr.length-1; i++) {
				if(Character.isDigit(charrArr[i])) {
					score += charrArr[i];
				}else {
					name += charrArr[i];
				}
			}
			
			int s = Integer.parseInt(score);
			if(Integer.parseInt(score) > maxScore) {
				maxScore = s;
				maxName = name;
			}
			//System.out.println(name + "===" + score);
		}
		System.out.println("최고점수는 " + maxName +"님 " + maxScore+"점 입니다.");
	}	*/
}
