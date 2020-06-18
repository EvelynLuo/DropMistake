package bupt.dropmistake.tool;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Problem {
    public int difficulty;// 难度系数
    public ArrayList<String> knowledgePoint;/* 知识点*/
    public String klgStr; //知识点字符串
    public int exposure;/*曝光度*/
    public int score;// 题目的分数
    public int mode; //题目的题型
    public int id;// 题目的题号

    public String problemURL;
    public String answerURL;
    public String date;

    public Problem(int difficulty, ArrayList<String> knowledgePoint, int exposure, int score, int id) {
        this.difficulty = difficulty;
        this.knowledgePoint = knowledgePoint;
        this.exposure = exposure;
        this.score = score;
        this.id = id;
    }

    public Problem(ArrayList<String> knowledgePoint, String problemURL, String answerURL, String date, int modeI) {
        this.knowledgePoint = knowledgePoint;
        this.problemURL = problemURL;
        this.answerURL = answerURL;
        this.date = date;
    }

    public Problem(String problemURL, String answerURL,int difficulty,ArrayList<String> knowledgePoint, int modeI){
        this.difficulty = difficulty;
        this.knowledgePoint = knowledgePoint;
        this.problemURL = problemURL;
        this.answerURL = answerURL;
    }

    //得到题型
    public String getMode(){
        if(mode==1)
            return "选择题";
        if(mode==4)
            return "填空题";
        else
            return "解答题";
    }

    // 得到知识点String
   public String getKlgStr() {
        String result = "";
        for (String str : knowledgePoint) {
            result += str + ' ';
        }
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return problemURL + '\n' + answerURL;
    }
}