package bupt.dropmistake.tool;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Problem implements Serializable {
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

    public Problem() {
    }
    public Problem(int difficulty, ArrayList<String> knowledgePoint, String problemURL, String answerURL, String date) {
        this.difficulty = difficulty;
        this.knowledgePoint = knowledgePoint;
        this.problemURL = problemURL;
        this.answerURL = answerURL;
        this.date = date;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setKlgStr(String klgStr) {
        this.klgStr = klgStr;
    }
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public ArrayList<String> getKnowledgePoint() {
        return knowledgePoint;
    }

    public void setKnowledgePoint(ArrayList<String> knowledgePoint) {
        this.knowledgePoint = knowledgePoint;
    }

    public int getExposure() {
        return exposure;
    }

    public void setExposure(int exposure) {
        this.exposure = exposure;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProblemURL() {
        return problemURL;
    }

    public void setProblemURL(String problemURL) {
        this.problemURL = problemURL;
    }

    public String getAnswerURL() {
        return answerURL;
    }

    public void setAnswerURL(String answerURL) {
        this.answerURL = answerURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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