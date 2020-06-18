package bupt.dropmistake.tool;

import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Split {

    static ArrayList<String> stop_list;

    public static String delete_space(String str_with_space) {
        String str_with_out_space = "";
        for (int i = 0; i < str_with_space.length(); i++) {
            char temp = str_with_space.charAt(i);
            if (temp != ' ') {
                str_with_out_space = str_with_out_space + temp;
                // 因为这个情景下的文本不是很长，所以直接用字符串拼接即可
            }
        }
        return str_with_out_space;
    }

    public static void read_stop_list(InputStream txt_path){
        stop_list = new ArrayList<String>();
        // 创建arraylist来存储读出的停用词
        // 现在已知停用词表(txt格式)是一个词一行存储，词的末尾有空格
        try {
            InputStreamReader file_Reader = new InputStreamReader(txt_path);
            BufferedReader buffer_BufferedReader = new BufferedReader(file_Reader);
            String read_line;

            while ((read_line = buffer_BufferedReader.readLine()) != null) {
                String out_str = delete_space(read_line);// 去掉了空格之后的字符串，将其添加进输出的arraylist里
                stop_list.add(out_str);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean word_not_in(String check_str) {
        boolean not_in = true;
        for (String str : stop_list) {
            if (check_str.equals(str)) {
                // 这里判断字符串是否相等需要使用equals方法
                not_in = false;
                break;// 若检测到有任何一个词与检测词相同，则结束循环，并且返回假
            }
        }
        return not_in;// 返回值为真，则说明这个词语不在停用词表当中，即可以添加进入
    }
    public static void readFromTxt(InputStream addStream) {// 从txt文件当中导出数学词汇的方法
        BufferedReader readTxt;
        try {
            readTxt = new BufferedReader(new InputStreamReader(addStream));
            String lineString = null;
            while ((lineString = readTxt.readLine()) != null) {
                // System.out.println(lineString);
                CustomDictionary.add(lineString);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static String[] striped(String text) {

        ArrayList<String> resultList = new ArrayList<>();
        java.util.List<Term> temp_List = StandardTokenizer.segment(text);
        for (int j = 0; j < temp_List.size(); j++) {
            String str_out = temp_List.get(j).toString();
            String[] str_splited = str_out.split("/");
            if ((str_splited[1].equals("n") || str_splited[1].equals("v") ||str_splited[1].equals("nz"))&& (word_not_in(str_splited[0]) == true)){
                // 若索引表word_list当中还没有这个词，并且这个词是名词或者动词，并且这个词不在之前得到的停用词表当中，就将其加入到索引表当中
                resultList.add(str_splited[0]);
            }
        }
        String[] result = resultList.toArray(new String[0]);
        return result;
    }

}
