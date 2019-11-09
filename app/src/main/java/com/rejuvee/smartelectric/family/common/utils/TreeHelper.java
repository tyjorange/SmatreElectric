package com.rejuvee.smartelectric.family.common.utils;

import com.rejuvee.smartelectric.family.annotation.TreeNodeBean;
import com.rejuvee.smartelectric.family.annotation.TreeNodeId;
import com.rejuvee.smartelectric.family.annotation.TreeNodePid;
import com.rejuvee.smartelectric.family.model.bean.Node;
import com.rejuvee.smartelectric.family.model.bean.SwitchStatementBean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TreeHelper {
    /**
     * 传入我们的普通bean，转化为我们排序后的Node
     *
     * @param datas
     * @param defaultExpandLevel
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) {
        List<Node> result = new ArrayList<>();
        // 将用户数据转化为List<Node>
        List<Node> nodes = convetData2Node(datas);
        // 拿到根节点
        List<Node> rootNodes = getRootNodes(nodes);
        // 排序以及设置Node间关系
        for (Node node : rootNodes) {
            addNode(result, node, defaultExpandLevel, 1);
        }
        return result;
    }

    /**
     * 过滤出所有可见的Node
     *
     * @param nodes
     * @return
     */
    public static List<Node> filterVisibleNode(List<Node> nodes) {
        List<Node> result = new ArrayList<>();
        if (nodes == null || nodes.size() == 0) {
            return result;
        }

        Node n = null;
        for (Node node : nodes) {
            // 如果为跟节点，或者上层目录为展开状态
            if (node.isRoot() || node.isParentExpand()) {
                result.add(node);

                if (n != null && node.getLevel() == 0) {
                    n.setLast(true);
                    node.setStart(true);
                } else if (n != null) {
                    n.setLast(false);
                    node.setStart(false);
                }
                n = node;
            }
        }
        result.get(result.size() - 1).setLast(true);
        return result;
    }

    /**
     * 将我们的数据转化为树的节点
     *
     * @param datas
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private static <T> List<Node> convetData2Node(List<T> datas) {
        List<Node> nodes = new ArrayList<>();
        Node node = null;

        for (T t : datas) {
            int id = -1;
            int pId = -1;
            SwitchStatementBean ssb = null;
            Class<?> clazz = t.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.getAnnotation(TreeNodeId.class) != null) {
                    f.setAccessible(true);
                    try {
                        id = f.getInt(t);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (f.getAnnotation(TreeNodePid.class) != null) {
                    f.setAccessible(true);
                    try {
                        pId = f.getInt(t);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (f.getAnnotation(TreeNodeBean.class) != null) {
                    f.setAccessible(true);
                    try {
                        ssb = (SwitchStatementBean) f.get(t);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (id != -1 && pId != -1 && ssb != null) {
                    break;
                }
            }
            node = new Node(id, pId, ssb);
            nodes.add(node);
        }

        /**
         * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
         */
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node m = nodes.get(j);
                if (m.getpId() == n.getId()) {
                    n.getChildren().add(m);
                    m.setParent(n);
                } else if (m.getId() == n.getpId()) {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        return nodes;
    }

    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> root = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot())
                root.add(node);
        }
        return root;
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     */
    private static void addNode(List<Node> nodes, Node node,
                                int defaultExpandLeval, int currentLevel) {

        nodes.add(node);
        if (defaultExpandLeval >= currentLevel) {
            node.setExpand(true);
        }

        if (node.isLeaf())
            return;
        for (int i = 0; i < node.getChildren().size(); i++) {
            addNode(nodes, node.getChildren().get(i), defaultExpandLeval,
                    currentLevel + 1);
        }
    }
}