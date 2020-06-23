package bupt.dropmistake.tool.dna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.neo4j.driver.v1.Record;

import bupt.dropmistake.tool.Problem;

public class Generation {

	private Test[] tests = new Test[100];
	/* 所需种群一共100个（待定），一个个体有23题，包括12道选择 5分，6道填空 5分，5道解答 12分 */
	private Problem[] allPros;// 题库中所有题目
	private int proNum; // 题库中所有题目个数
	// 确定用户是想要标准试卷（题型的数量确定），或根据用户搜索题型、难度、知识点、综合度来确定的贴合用户画像的试卷，
	// 还是一份自定义试卷（题型数量用户自己定,或者说限定只有选择、填空题，或者只有解答题）

	public Generation(int expectD, ArrayList<String> expectKnowledgePoints,int expectE , List<Record> pros) { 
		Test.setSize1(12);
		Test.setSize2(6);
		Test.setSize3(5);
		Test.setSumScore(150);
		proNum = pros.size();
		allPros = new Problem[proNum];
		for (int i = 0; i < proNum; i++) { // 把数据库所有题目放在程序中，生成题库
			Record temp = pros.get(i);
			List<String> klgList = Arrays.asList(temp.get(1).toString().replace("\"", "").split("\\^\\.\\^"));
			allPros[i] = new Problem(Integer.parseInt(temp.get(0).toString()), new ArrayList<String>(klgList),
					Integer.parseInt(temp.get(2).toString()), Integer.parseInt(temp.get(3).toString()), i);
		}
		for (int i = 0; i < 100; i++) {
			tests[i] = new Test();
			int j=0;
			while (j<23) {
				Problem random = getRandomPro(); // roll一道题目出来
				if (tests[i].occurTimes.get(random.getId()) == null) {//保证不重复
					tests[i].getProblems().add(getRandomPro());
					tests[i].occurTimes.put(random.getId(), 1);
					j++;
				}
			}
			tests[i].setAdaptability(countAdaptability(tests[i], expectD, expectKnowledgePoints, expectE));
		}
	}

//	public Generation(int size1, int size2, int size3, int expectD, int expectE, int expectC) {
//		Test.setSize1(size1);
//		Test.setSize2(size2);
//		Test.setSize3(size3);
//		Test.setSumScore(100);
//		for (int i = 0; i < 100; i++) {
//			tests[i] = new Test();
//			/* 添加代码，通过随机选取题目，初始化每个test */
//
//			tests[i].setAdaptability(countAdaptability(tests[i], expectD, expectE));
//		}
//	}

	public Test[] getTests() {
		return tests;
	}

	public double countAdaptability(Test test, int expectD, ArrayList<String> expectKnowledgePoints,int expectE)// 计算适应度
	{//expectD输入值必须在1-5之间 expectKnowledgePoints为期待的知识点集合 expectE在0-1之间
		double adaptabilityD = 0, adaptabilityK = 0, adaptabilityE = 0, adaptabilityC = 0;
		ArrayList<String> knowledgePoints=new ArrayList<String>();
		double adaptability = 0;
		for (int i = 0; i < test.getProblems().size(); i++) {
			adaptabilityD += test.getProblems().get(i).getDifficulty() * test.getProblems().get(i).getScore()
					/ Test.getSumScore();
		}
		for(int i=0;i<test.getProblems().size();i++) {
			for(String temp:test.getProblems().get(i).getKnowledgePoint())
				if(knowledgePoints.indexOf(temp)!=-1&&expectKnowledgePoints.indexOf(temp)!=-1)
					knowledgePoints.add(temp);
		}
		adaptabilityK=knowledgePoints.size();
		/* 知识点的匹配待定 */
		for (int i = 0; i < test.getProblems().size(); i++) {
			adaptabilityE += test.getProblems().get(i).getExposure()
					/ (test.getSize1() + test.getSize2() + test.getSize3());
		}
		/* 综合度的计算待定 */
		adaptability = 1-Math.abs(expectD-adaptabilityD)/5-(1-adaptabilityK/expectKnowledgePoints.size())-Math.abs(expectE-adaptabilityE);
		return adaptability;
	}

	public void delDupl(Test test)// 去重，更新test重复的试题为新题目
	{
		ArrayList<Problem> problems=test.getProblems();
		for (int i = 0;i<problems.size();i++) {
			int key = problems.get(i).getId();  //这道题的id
			if (test.occurTimes.get(key).equals(2)) { //发现重复
				problems.remove(i); //删掉这道题
				Problem random = getRandomPro(); //再roll一道不重复的题
				while(test.occurTimes.containsKey(random.getId())) {
					random=getRandomPro();
				}
				problems.add(random);  //添加这道新题
				test.occurTimes.replace(key, 1);
				test.occurTimes.put(random.getId(), 1);
			}
		}
	}

	public int roulette(double[] numbers)// 轮盘赌算法
	{
		double shot, cardinalNumber;
		int i,target;
		double sum=0;
		for(i=0;i<numbers.length;i++)
		{
			sum+=numbers[i];
		}
		shot = sum * new Random().nextDouble();
		for (target = 0, cardinalNumber = 0; target < 100-1 && cardinalNumber + numbers[target] < shot; target++) {
			cardinalNumber += numbers[target];
		}
		return target;
	}

	public int partition(Test tests[], int low, int high) {
		Test pivotKey = tests[low];
		while (low < high) {
			while (low < high && tests[high].getAdaptability() >= pivotKey.getAdaptability())
				high--;
			tests[low] = tests[high];
			while (low < high && tests[low].getAdaptability() <= pivotKey.getAdaptability())
				low++;
			tests[high] = tests[low];
		}
		tests[low] = pivotKey;
		return low;
	}

	public void quickSort(Test tests[], int start, int end) {
		if (start < end) {
			int pivotLocate = partition(tests, start, end);
			quickSort(tests, start, pivotLocate - 1);
			quickSort(tests, pivotLocate + 1, end);
		}
	}

	public Test crossover(Test test1, Test test2)// 交叉产生子染色体
	{
		Test newTest = new Test();
		Random random = new Random();
		int crossoverPoint1 = random.nextInt(Test.getSize1());
		int crossoverPoint2 = random.nextInt(Test.getSize2()) + 12;
		int crossoverPoint3 = random.nextInt(Test.getSize3()) + 18;
		for (int i = 0; i < Test.getSize1(); i++) {
			int id;
			if (i <= crossoverPoint1) {
				newTest.getProblems().add(test1.getProblems().get(i));
				id=test1.getProblems().get(i).getId();//csx加，记录题目出现次数，优化重复判定
			} else {
				newTest.getProblems().add(test2.getProblems().get(i));
				id=test2.getProblems().get(i).getId();
			}
			if (!newTest.occurTimes.containsKey(id)) {
				newTest.occurTimes.put(id, 1);
//				System.out.println("1.加入新题目-"+id);
			}
				
			else {
				newTest.occurTimes.replace(id, 2);
//				System.out.println("1.加入旧题目-"+id+"题目出现为2次");
			}
				
		}
		for (int i = Test.getSize1(); i < Test.getSize1() + Test.getSize2(); i++) {
			int id;
			if (i <= crossoverPoint2) {
				newTest.getProblems().add(test1.getProblems().get(i));
				id=test1.getProblems().get(i).getId();
			} else {
				newTest.getProblems().add(test2.getProblems().get(i));
				id=test2.getProblems().get(i).getId();
			}
			if (!newTest.occurTimes.containsKey(id)) {
				newTest.occurTimes.put(id, 1);
//				System.out.println("2.加入新题目-"+id);
			}
			else {
				newTest.occurTimes.replace(id, 2);
//				System.out.println("2.加入旧题目-"+id+"题目出现为2次");
			}

		}
		for (int i = Test.getSize1() + Test.getSize2(); i < Test.getSize1() + Test.getSize2() + Test.getSize3(); i++) {
			int id;
			if (i <= crossoverPoint3) {
				newTest.getProblems().add(test1.getProblems().get(i));
				id=test1.getProblems().get(i).getId();
			} else {
				newTest.getProblems().add(test2.getProblems().get(i));
				id=test2.getProblems().get(i).getId();
			}
			if (!newTest.occurTimes.containsKey(id)) {
				newTest.occurTimes.put(id, 1);
//				System.out.println("3.加入旧题目-"+id);
			}

			else {
				newTest.occurTimes.replace(id, 2);
//				System.out.println("3.加入旧题目-"+id+"题目出现为2次");
			}

		}
		delDupl(newTest);
		return newTest;
	}

	public void mutation(Test test, Problem problem)// 变异
	{
		Random random = new Random();
		int mutationPoint = random.nextInt(Test.getSize1() + Test.getSize2() + Test.getSize3());
		test.getProblems().set(mutationPoint, problem);
		test.occurTimes.put(problem.id, 1);
		delDupl(test);
	}

	public Problem getRandomPro() { //随机roll一道题目
		return allPros[(int) (Math.random() * proNum)];
	}

}
