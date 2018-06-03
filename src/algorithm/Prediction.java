package algorithm;
/**
 * 预测算法的实现
 */

import model.Strings;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Prediction {
    private Connection conn;                                    //数据库连接
    private ArrayList<String> attrNames;                        //原始标签集
    private ArrayList<ArrayList<String>> attrValues;                 //标签值集
    private ArrayList<ArrayList<String>> data;                       //数据集
    private String decisionName = "certified";                      //决策属性
    private BufferedOutputStream outputStream;
    private Node node;
    private Strings tableNames;                                     //数据库表表名
    private String cid;                                              //课程号

    public Prediction(Connection conn, String cid) {
        this.conn = conn;
        tableNames = new Strings();
        this.cid = cid;
    }

    public Prediction(Connection conn){
        this.conn = conn;
        tableNames = new Strings();
    }

    /**
     * 从数据库获取所有的标签（属性）名称
     * @return      标签集
     */
    private List<String> getAttrNames(String tableName) {
        attrNames = new ArrayList<>();
        //获取属性名称
        String sql = "SHOW columns FROM " + tableName;
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String key = rs.getString(1);
                attrNames.add(key);
            }
            attrNames.remove("uid");
            attrNames.remove("cid");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attrNames;
    }

    /**
     * 从数据库获取每个标签的值集
     * @return      每个标签的取值
     */
    private List<ArrayList<String>> getAttrValues(String tableName) {
        attrValues = new ArrayList<>();
        //获取属性值
        for (String attr : attrNames) {
            String sql1 = "SELECT " + attr + " FROM " + tableName + " GROUP BY " + attr;
            ArrayList<String> temp = new ArrayList<>();
            try {
                PreparedStatement pst = conn.prepareStatement(sql1);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    temp.add(rs.getString(1));
                }
                attrValues.add(temp);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return attrValues;
    }

    /**
     * 从数据训练集获取所有的数据
     * @return      数据集
     */
    private List<ArrayList<String>> getData(String tableName) {
        data = new ArrayList<>();
        String sql="";
        if (cid==null){
            sql = "SELECT * FROM " +tableName;
        } else {
            sql = "SELECT * FROM " +tableName + " where cid = '" + cid + "'";
        }
        //获取数据
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ArrayList<String> record = new ArrayList<>();
                for (int i = 3; i <= 14; i++) {
                    record.add(rs.getString(i));
                }
                data.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }


    //将决策树的内容写进文件
    public void C45Tree(){
        node = new Node();
        node.setValue("root");
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream("out.txt"));
            //构建决策树
            buildTree(getData(tableNames.classfierTableName),getAttrNames(tableNames.classfierTableName), getAttrValues(tableNames.classfierTableName),node);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        prediction(getAttrNames(tableNames.predictionTableName),getData(tableNames.predictionTableName));
    }

    //构建决策树
    private void buildTree(List<ArrayList<String>> datalist, List<String> attrNames, List<ArrayList<String>> attrValues, Node fatherRoot){
        //获取最大信息增益
        String attr = getMaxGainRatioAttr(datalist, attrNames, attrValues);
        //
        Map<String, List<ArrayList<String>>> cutData = getCutData(attr, datalist, attrNames, attrValues);
        //遍历cutData的每一个值
        for (Map.Entry<String, List<ArrayList<String>>> entry : cutData.entrySet()) {
            String space = "";
            Node childNode = new Node();
            fatherRoot.addChildNode(childNode);
            try {
                for (int i = 0; i < ((data.get(0).size()) - entry.getValue().get(0).size() - 1); i++) {
                    space += "\t";
                }

                childNode.setValue(attr);
                childNode.setDisvisionValue(entry.getKey());

                outputStream.write((space + attr +":" + entry.getKey()+"\n").getBytes());

                int index = attrNames.indexOf(attr);
                ArrayList<String> names = new ArrayList<>();
                ArrayList<ArrayList<String>> values = new ArrayList<>();
                getCutNames(attrNames, names, attrValues, values, index);
                if ((!isDecisionValuesSettled(entry.getValue(), names)) && (names.size() > 1)) {
                    buildTree(entry.getValue(),names,values,childNode);
                } else {
                    childNode.setDecision(entry.getValue().get(0).get(names.indexOf(decisionName)));
                    outputStream.write(("\t" +space + "leafNode: " + entry.getValue().get(0).get(names.indexOf(decisionName)) + "\n").getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //判断决策树是否收敛
    private boolean isDecisionValuesSettled(List<ArrayList<String>> data, ArrayList<String> attrNames) {
        int index = attrNames.indexOf(decisionName);
        Set<String> result = data.stream().map(dataItem -> dataItem.get(index)).collect(Collectors.toSet());
        return result.size() == 1;
    }

    //获取删除最大增益属性后的属性值
    private void getCutNames(List<String> attrNames, List<String> names, List<ArrayList<String>> attrValues, List<ArrayList<String>> values, int index) {
        names.addAll(attrNames);
        names.remove(index);
        values.addAll(attrValues);
        values.remove(index);
    }

    /**
     * 获取具有最大信息增益率的标签后，删除数据中的该属性
     * @param attr       最大信息增益率标签
     * @param data       数据集
     * @param attrNames  数据标签集
     * @param attrValues 数据标签值集
     * @return cutData(最大增益率标签值,List:行数(ArrayList(删除最大增益率标签值后每一行的剩余标签值)))
     */
    private Map<String, List<ArrayList<String>>> getCutData(String attr, List<ArrayList<String>> data, List<String> attrNames, List<ArrayList<String>> attrValues) {
        //获取最大增益率标签的索引
        int index = attrNames.indexOf(attr);
        //待返回值
        Map<String, List<ArrayList<String>>> cutData = new HashMap<>();
        //循环最大增益率标签下的值
        for (String value : attrValues.get(index)) {
            List<ArrayList<String>> newData = new ArrayList<>();
            //处理每一行数据，将最大属性=value数据过滤出来，删除其中最大增益率的属性，将新数据添加到新数据集中；
            data.stream().filter(dataItem -> dataItem.get(index).equals(value)).forEach(dataItem -> {
                ArrayList<String> temp = new ArrayList<>();
                temp.addAll(dataItem);
                temp.remove(value);
                newData.add(temp);
            });
            if (newData.size() > 0) {
                cutData.put(value, newData);
            }
        }
        return cutData;
    }


    /**
     * 获取最大增益率的属性标签
     * @param data       数据集
     * @param attrNames  筛选后的标签集
     * @param attrValues 筛选后的标签值
     * @return
     */
    private String getMaxGainRatioAttr(List<ArrayList<String>> data, List<String> attrNames, List<ArrayList<String>> attrValues) {
        List<String> tempAttrNames = new ArrayList<>();
        tempAttrNames.addAll(attrNames);
        tempAttrNames.remove(decisionName);
        List<List<Object>> attrGainRatio = new ArrayList<>();
        for (String attrName : tempAttrNames) {
            List<Object> temp = Arrays.asList(attrName, getGainRation(attrName, data, attrNames, attrValues));
            attrGainRatio.add(temp);
        }
        Collections.sort(attrGainRatio, (o1, o2) -> (new Double((double) o2.get(1))).compareTo((double) o1.get(1)));
        return (String) attrGainRatio.get(0).get(0);
    }

    /**
     * 求信息增益率
     * 公式：GainRation（A） = Gain（A） / Info(D)
     * @param attrName   当前属性标签
     * @param attrNames  属性标签集
     * @param attrValues 属性标签值集
     * @return 信息增益率
     */
    private Object getGainRation(String attrName, List<ArrayList<String>> data, List<String> attrNames, List<ArrayList<String>> attrValues) {
        return getInfoGain(attrName, data, attrNames, attrValues) /
                getInfoEntropy(attrName, data, attrNames, attrValues);
    }

    /**
     * 求信息增益
     * 公式: gain(A) = info(D) - info_A(D)
     * @param attrName   当前属性标签
     * @param data       数据集
     * @param attrNames  属性标签集
     * @param attrValues 标签值集
     * @return 信息增益值
     */
    private double getInfoGain(String attrName, List<ArrayList<String>> data, List<String> attrNames, List<ArrayList<String>> attrValues) {
        int index = attrNames.indexOf(attrName);    //获取当前属性的索引值
        int dataSize = data.size();
        double infoEntropyAttrNameDecisionName = 0.0;     //当前属性的信息增益
        Map<String, Integer> attrNum = new HashMap<>();      //当前属性下，各属性值出现的次数
        Map<List<String>, Integer> valueOfAttrDecision = new HashMap<>();    //[[当前属性，决策属性]，属性对出现次数]

        for (ArrayList<String> dataItem : data) {
            //info(Dj)
            attrNum.put(dataItem.get(index), attrNum.getOrDefault(dataItem.get(index), 0) + 1);
            List<String> attrDecision = Arrays.asList(dataItem.get(index), dataItem.get(attrNames.indexOf(decisionName)));
            valueOfAttrDecision.put(attrDecision, valueOfAttrDecision.getOrDefault(attrDecision, 0) + 1);
        }

        //Info(Dj)
        for (Map.Entry<String, Integer> entry : attrNum.entrySet()) {
            //循环[当前属性值，次数]
            Iterator<Map.Entry<List<String>, Integer>> iterator = valueOfAttrDecision.entrySet().iterator();
            double t = 0.0;
            while (iterator.hasNext()) {
                Map.Entry<List<String>, Integer> entry1 = iterator.next();   //获取[属性值,决策值],次数
                if (entry1.getKey().get(0).equals(entry.getKey())) {
                    //如果属性值对中的第一个属性值等于当前属性值
                    double t1 = (double) entry1.getValue() / (double) entry.getValue();
                    t += -t1 * getLog2_N(t1);
                }
                //Info_A(Dj)
                infoEntropyAttrNameDecisionName += ((double) entry.getValue() / (double) dataSize) * t;
            }
        }
        return getInfoEntropy(decisionName, data, attrNames, attrValues) - infoEntropyAttrNameDecisionName;
    }

    /**
     * 求信息熵
     * 公式：+= -((double) num / (double) dataSize) * getLog2_N((double) num / (double) dataSize);
     *
     * @param attrName   当前属性标签
     * @param data       数据集
     * @param attrNames  属性标签集
     * @param attrValues 标签值集
     * @return 信息熵
     */
    private double getInfoEntropy(String attrName, List<ArrayList<String>> data, List<String> attrNames, List<ArrayList<String>> attrValues) {
        int index = attrNames.indexOf(attrName);
        int dataSize = data.size();
        double infoEntropy = 0.0;
        for (String value : attrValues.get(index)) {
            int num = 0;
            for (ArrayList<String> dataItem : data) {
                if (dataItem.get(index).equals(value)) {
                    num += 1;
                }
                if (num != 0) {
                    infoEntropy += -((double) num / (double) dataSize) * getLog2_N((double) num / (double) dataSize);
                }
            }
        }
        return infoEntropy;
    }

    /**
     * 利用换底公式计算2为底，n的对数
     *
     * @param n 概率值
     * @return 对数值
     */
    private double getLog2_N(double n) {
        return (Math.log(n) / Math.log(2));
    }

    //实现预测
    private void prediction(List<String> attrNames,List<ArrayList<String>> data){
        int TP=0,TN=0,FP=0,FN=0;
        int cannot = 0;
        String result;
        for (ArrayList<String> dataItem: data) {
            Node searchNode = node;
            while (searchNode.children.size()!=0) {
                boolean flag = false;
                for (int i = 0; i < searchNode.children.size(); i++) {
                    Node temp = searchNode.children.get(i);
                    int index = attrNames.indexOf(temp.value);
                    if (dataItem.get(index).toString().equals(temp.disvisionValue)) {
                        searchNode = temp;
                        flag=true;
                        break;
                    }
                }
                if (flag==false){
//                    System.out.println("无法分类");
                    break;
                }
            }
            result = searchNode.decision;
            int index = attrNames.indexOf(decisionName);
            if (result==null){
                cannot++;
            } else {
                if (dataItem.get(index).equals("yes")){
                    if (result.equals("yes")) {
                        TP++;
                    } else if (result.equals("no")) {
                        FN++;
                    }
                } else if (dataItem.get(index).equals("no")) {
                    if (result.equals("no")) {
                        TN++;
                    } else if (result.equals("yes")){
                        FP++;
                    }
                }
            }

        }
//        System.out.println(TP+"\t" + FP + "\t" + TN + "\t" + FN + "\t" + cannot);
        //计算分类器准确率
        //总样本数
        float total = TP+FP+TN+FN+cannot;
        //成功分类率
        float classfyRate = (total-cannot)/total;
        //准确率（准确率）
        float accuracy = (float) (TP+TN)/(float) (TP+TN+FP+FN);
        //误分类率（错误率）
        float errorRate = (float)(FP+FN)/(float) (TP+TN+FP+FN);
        //精度
        float precision = (float)TP/(float)(TP+FP);
        System.out.println("总样本数："+total);
        System.out.printf("成功分类率：%.2f",classfyRate*100);
        System.out.printf("\n分类准确率：%.2f",accuracy*100);
        System.out.printf("\n分类错误率：%.2f",errorRate*100);
        System.out.printf("\n精度：%.2f",precision*100);
    }
}