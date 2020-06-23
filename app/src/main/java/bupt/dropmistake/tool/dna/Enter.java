package bupt.dropmistake.tool.dna;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import bupt.dropmistake.tool.Problem;

public class Enter {

	private Session session; // neo4j事务
	private List<Record> allPros;//题库中所有题目

	public Enter(Session session) {
		this.session = session;
	}

	//组卷整体 D为难度，E为暴露度
	public ArrayList<Problem> paper(int expectD, int expectE, ArrayList<String> expectKlgs) {
		storePros();
		System.out.println("题目载入成功");
		int iterationTimes = 0;// 迭代次数
//		int expectC = scanner.nextInt();
		Generation generation = new Generation (expectD, expectKlgs,expectE ,allPros);

		while (iterationTimes <= 5
				&& generation.getTests()[0].getAdaptability() < 0.95)/* 两个条件，迭代次数在一个范围之内且最大的适应度未达到要求 */
		{
			/* 保留前百分之5（比例待定）的试卷 */
			for (int i = 5; i < 100; i++)// 在100条染色体中利用轮盘赌算法找到95对染色体，并生成后代
			{
				double[] adaptablities = new double[100];
				for (int j = 0; j < 100; j++) {
					adaptablities[j] = generation.getTests()[j].getAdaptability();
				}
				int shot1, shot2;
				shot1 = generation.roulette(adaptablities);
				shot2 = generation.roulette(adaptablities);
				generation.getTests()[i] = generation.crossover(generation.getTests()[shot1],
						generation.getTests()[shot2]);
				/* 搜索一道新的题目 */
				Problem problem=generation.getRandomPro();
				generation.mutation(generation.getTests()[i], problem);
			}
			for (int i = 0; i < 100; i++) {
				generation.getTests()[i].setAdaptability(
						generation.countAdaptability(generation.getTests()[i], expectD, expectKlgs, expectE));
			}
			generation.quickSort(generation.getTests(), 0, 99);
			iterationTimes++;
		}

		//最佳匹配度试卷输出
		Test test = generation.getTests()[0];
		ArrayList<Problem> pro_list = test.getProblems();
		for (int j = 0; j < pro_list.size(); ++j) {
			Problem temProblem = pro_list.get(j);
			int id = temProblem.getId();
			StatementResult proResult = session.run("match(n:Qust) where id(n)="+id+" return n.png, n.mode");
            Record temp = proResult.next();

            String[] pngUrl = temp.get(0).toString().split("null");
            pngUrl[0] = "https://image.fclassroom.com" + pngUrl[0].substring(1, pngUrl[0].length() - 2);
            pngUrl[1] = "https://image.fclassroom.com" + pngUrl[1].substring(2, pngUrl[1].length() - 1);
            
            temProblem.setProblemURL(pngUrl[0]);
            temProblem.setAnswerURL(pngUrl[1]);
            temProblem.mode = temp.get(1).asInt();
		}
		return pro_list;
	}
	
	public void storePros() {
		StatementResult result = session.run("match (n:Qust) return n.hard,n.klg3,n.expsr,n.score");
		allPros = result.list();
	}

}
