package algorithm;
/**
 * 决策树节点
 */

import java.util.ArrayList;

public  class Node{
    String value;                                               //节点值
    String disvisionValue;                                      //节点分裂条件
    String decision;                                            //决策值
    ArrayList<Node> children = new ArrayList<>();               //子节点列表
    //获得节点值
    public String getValue() {
        return value;
    }
    //设置节点值
    public void setValue(String value) {
        this.value = value;
    }
    //获得分裂属性
    public String getDisvisionValue() {
        return disvisionValue;
    }
    //设置节点分裂属性
    public void setDisvisionValue(String disvisionValue) {
        this.disvisionValue = disvisionValue;
    }
    //获得决策属性值
    public String getDecision() {
        return decision;
    }
    //设置决策属性值
    public void setDecision(String decision) {
        this.decision = decision;
    }
    //获得子节点
    public ArrayList<Node> getChildren() {
        return children;
    }
    //设置子节点
    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }
    /**
     *为当前节点添加子节点
     * @param childNode     子节点
     */
    public void addChildNode(Node childNode){
        children.add(childNode);
    }

}

