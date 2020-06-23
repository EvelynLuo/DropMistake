package bupt.dropmistake.tool.dna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bupt.dropmistake.tool.Problem;

public class Test 
{
	private ArrayList<Problem> problems=new ArrayList<Problem>();//index从0开始 size()返回真正的数量（从1开始算）
	public Map occurTimes = new HashMap<Integer, Integer>(); //记录一个题目id出现的次数避免重复题目
	private static int size1,size2,size3;//题型1 2 3的数量
	private static double sumScore;//试卷的总分
	private double adaptability;//试卷的适应度
		
	public ArrayList<Problem> getProblems() {
		return problems;
	}
	
	public static int getSize1() {
		return size1;
	}
	
	public static int getSize2() {
		return size2;
	}
	
	public static int getSize3() {
		return size3;
	}
	
	public static double getSumScore() {
		return sumScore;
	}
	
	public double getAdaptability() {
		return adaptability;
	}
	
	public static void setSize1(int size1) {
		Test.size1 = size1;
	}
	
	public static void setSize2(int size2) {
		Test.size2 = size2;
	}
	
	public static void setSize3(int size3) {
		Test.size3 = size3;
	}
	
	public static void setSumScore(int sumScore) {
		Test.sumScore = sumScore;
	}
	
	public void setAdaptability(double adaptability) {
		this.adaptability = adaptability;
	}
	
	
}
