package util;

import model.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MyFile {
    private String path;
    ;
    public MyFile() {
        this.path="src/dataset/Kfolder.csv";
    }

    public MyFile(String path) {
        this.path = path;
    }

    /**
     * 从csv文件读取数据并将其放到ArrayList中
     * @return 数据集
     */
    public ArrayList<Data> myFileReader() {
        File myFile = new File(path);
        BufferedReader br;
        //原始数据集
        ArrayList<Data> tuples = new ArrayList<>();
        //从csv文件读取数据
        try {
            br = new BufferedReader(new FileReader(myFile));
            String line;
            while ((line = br.readLine()) != null) {
                Data dataItem = new Data();
                //定义并初始化定长数组
                String[] TempTuple = new String[18];
                //将从csv文件读取到的数据存入定长数组
                String[] temp = line.split(",");
                for (int i = 0; i < TempTuple.length; i++) {
                    try {
                        if (temp[i].equals(" ") || temp[i].equals("")) {
                            temp[i] = "null";
                        }
                        TempTuple[i] = temp[i];
                    } catch (IndexOutOfBoundsException e) {
                        TempTuple[i] = "null";
                    }
                }
                //将定长数组存入ArrayList
                dataItem.setCid(TempTuple[0]);
                dataItem.setUid(TempTuple[1]);
                dataItem.setIdentify(TempTuple[2],TempTuple[3],TempTuple[4]);
                dataItem.setCertified(TempTuple[5]);
                dataItem.setLocation(TempTuple[6]);
                dataItem.setLevel(TempTuple[7]);
                dataItem.setAge(TempTuple[8]);
                dataItem.setGender(TempTuple[9]);
                dataItem.setGrade(TempTuple[10]);
                dataItem.setStartTime(TempTuple[11]);
                dataItem.setLastEventTime(TempTuple[12]);
                dataItem.setNevents(TempTuple[13]);
                dataItem.setNdays(TempTuple[14]);
                dataItem.setNplayVideos(TempTuple[15]);
                dataItem.setNchapters(TempTuple[16]);
                dataItem.setNforumPosts(TempTuple[17]);

                //将每一条数据添加到原始数据集中
                tuples.add(dataItem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tuples;
    }
}
