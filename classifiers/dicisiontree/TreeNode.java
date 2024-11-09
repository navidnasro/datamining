package datamining.classifiers.dicisiontree;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Navid
 */
public class TreeNode
{
    private final char label;
    private String classValue;
    private final int featureIndex;
    
    public Map<String,TreeNode> linkedNodes;
    public Map<Character,TreeNode> parents;
    private Map<String,Map<String,Integer>> branches;
    
    
    public TreeNode(char label,int index)
    {
        branches = new HashMap<>();
        parents = new HashMap<>();
        linkedNodes = new HashMap<>();
        this.label = label;
        classValue = null;
        featureIndex = index;
    }
    
    /**
     * 
     * @return 
     */
    public char getLabel()
    {
        return this.label;
    }
    
    public int getIndex()
    {
        return this.featureIndex;
    }
    
    /**
     * 
     * @param nodeLabel
     * @param node 
     */
    public void addParent(char nodeLabel,TreeNode node)
    {
        if(node != null)
            this.parents.put(nodeLabel, node);
    }
    
    /**
     * 
     * @return 
     */
    public Set<Character> getParents()
    {
        return this.parents.keySet();
    }
    
    /**
     * 
     * @param classValue 
     */
    public void setClassValue(String classValue)
    {
        this.classValue = classValue;
    }
    
    /**
     * 
     * @return 
     */
    public String getClassValue()
    {
        return this.classValue;
    }
    
    /**
     * 
     * @param branch
     * @param node 
     */
    public void linkTo(String branch,TreeNode node)
    {
        linkedNodes.put(branch, node);
    }
    
    /**
     * 
     * @param branch
     * @return 
     */
    public TreeNode getLinkedNode(String branch)
    {
        return this.linkedNodes.get(branch);
    }
    
    public boolean isLinkedTo(String branch)
    {
        return this.linkedNodes.containsKey(branch);
    }
    
    /**
     * 
     * @return 
     */
    public Map<String,Map<String,Integer>> getBranches()
    {
        return branches;
    }
    
    /**
     * 
     * @return 
     */
    public Set<String> getBranchesNames()
    {
        return branches.keySet();
    }
    
    /**
     * 
     * @param branches
     */
    public void setBranches(Map<String,Map<String,Integer>> branches)
    {
        this.branches = branches;
    }
    
    /**
     * 
     * @param branch
     * @return 
     */
    public int getPositives(String branch)
    {
        return branches.get(branch).get("yes");
    }
    
    /**
     * 
     * @param branch 
     * @param positive 
     */
    public void setPositives(String branch,int positive)
    {
        if(!branches.containsKey(branch))
            branches.put(branch, new HashMap<>());
        
        branches.get(branch).put("yes", positive);
    }
    
    /**
     * 
     * @param branch
     * @return 
     */
    public int getNegatives(String branch)
    {       
        return branches.get(branch).get("no");
    }
    
    /**
     * 
     * @param branch 
     * @param negatives 
     */
    public void setNegatives(String branch,int negatives)
    {
        if(!branches.containsKey(branch))
            branches.put(branch, new HashMap<>());
        
        branches.get(branch).put("no", negatives);
    }
}
