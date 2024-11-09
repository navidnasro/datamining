package datamining.classifiers.dicisiontree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Navid
 */
public class DecisionTree 
{
    private TreeNode root;
    private List<String> trainSet;
    private Map<Character,TreeNode> nodes;
    
    private int numberOfFeatures;
    private int totalRecords;
    private char[] columns;
    
    public DecisionTree()
    {
        root = null;
        nodes = new HashMap<>();
    }
    
    /**
     * 
     * @param trainSet 
     */
    public void train(List<String> trainSet)
    {
        this.trainSet = trainSet;
        
        //number of columns
        numberOfFeatures = trainSet.get(0).split(",").length;
        //associating each column with a label
        columns = DecisionTreeUtil.generateLabels(numberOfFeatures);      
        //number of all available records
        totalRecords = trainSet.size();        
        
        defineRoot();
        buildTree(root);
    }
    
    private void defineRoot()
    {        
        //chosen node in every iteration
        char chosenNode;
        
        boolean isPositive;
        String status;
        
        //storing feature's value with the number of their positives and negatives
        Map<String,Map<String,Integer>> featureValues = new HashMap<>();
        
        //storing gain value of each column
        Map<Character,Double> gain = new HashMap<>();
        
        //defining the root node
        for(int i=1 ; i<numberOfFeatures ; i++)
        {
            for(String record : trainSet)
            {
                String[] features = record.split(",");
                
                //whether it is negative or positive
                isPositive = !(features[0].contains("no"));
                status = isPositive ? "yes" : "no";
                
                //if feature doesn't exist in the map , initialize it
                if(!featureValues.containsKey(features[i]))
                {
                    featureValues.put(features[i], new HashMap<>());
                    featureValues.get(features[i]).put("no", 0);
                    featureValues.get(features[i]).put("yes", 0);
                }
                
                //updating features positveness or negativeness
                featureValues.get(features[i]).put(status, featureValues.get(features[i]).get(status)+1);
            }
            
            //calculating Gain for each column
            gain.put(columns[i-1], DecisionTreeUtil.gain(featureValues,root));
            
            //assigning tree node's branches
            if(!nodes.containsKey(columns[i-1]))
                nodes.put(columns[i-1], new TreeNode(columns[i-1],i));
            
            nodes.get(columns[i-1]).setBranches(featureValues);
            
            //reinstantiating the map , the old one will be handeled by GC
            featureValues = new HashMap<>();
        }
        
        chosenNode = DecisionTreeUtil.findMax(gain);
        
        root = new TreeNode(chosenNode,nodes.get(chosenNode).getIndex());
        root.setBranches(nodes.get(chosenNode).getBranches());
    }
    
    /**
     * 
     * @param node 
     */
    private void buildTree(TreeNode node)
    {
        double entropy;
        
        double sum;
        double f;
        double featureGain;
        double max = 0.0;
        
        //chosen node in every iteration
        char chosenNode = 'A';
        
        for(String branch : node.getBranchesNames())
        {
            entropy = DecisionTreeUtil.entropy(node.getPositives(branch), node.getNegatives(branch));
            
            //exiting condition
            if(entropy == 0.0 && node.getPositives(branch) == 0)
            {
                node.setClassValue("no-recurrence-events");
                return;
            }
            
            else if(entropy == 0.0 && node.getNegatives(branch) == 0)
            {
                node.setClassValue("recurrence-events");
                return;
            }
            
            //for each feature
            for(int i=1 ; i<numberOfFeatures ; i++)
            {
                //if current feature is not already seen
                if(node.getParents().contains(columns[i-1]) && node.getLabel() == columns[i-1])
                    continue;
                
                sum = 0;
                
                //for each branch of the passed node
                for(String value : nodes.get(columns[i-1]).getBranchesNames())
                {
                    //calculate probability
                    f = DecisionTreeUtil.calculateProbability(trainSet,node.getIndex(),branch,i,value);
                    sum += f;
                }
                
                //calculate gain
                featureGain = entropy - sum;
                
                if(featureGain > max)
                {
                    max = featureGain;
                    chosenNode = columns[i-1];
                }
            }
            
            //selected feature to be a node
            TreeNode newNode = nodes.get(chosenNode);

            newNode.setNegatives(branch, DecisionTreeUtil.PositivesAndNegatives.get("no"));
            newNode.setPositives(branch,DecisionTreeUtil.PositivesAndNegatives.get("yes"));
            
            //adding it's parent
            newNode.addParent(node.getLabel(), node);
            //linking nodes together
            node.linkTo(branch, newNode);
            
            //recursively call the function
            buildTree(newNode);
        }
    }
    
    /**
     * 
     * @param testSet 
     */
    public void predict(List<String> testSet)
    {
        int index;
        String value;
        
        int correct = 0;
        int recordsCount = testSet.size();
        
        for(String record : testSet)
        {
            String[] features = record.split(",");
            
            //index of root feature
            int rootIndex = root.getIndex();
            
            //value of root feature in test set
            String rootValue = features[rootIndex];

            //tree node that root is connected to through it's value
            TreeNode node = root.getLinkedNode(rootValue);

            //while we haven't reached a class name
            while(node.getClassValue() == null)
            {
                //get node index
                index = node.getIndex();
                //get feature value
                value = features[index];

                //get the linked node through value
                node = node.getLinkedNode(value);
            }
            
            if(node.getClassValue().equals(features[0]))
                correct++;
        }
        
        //calculating the accuracy of model
        DecisionTreeUtil.accuracy(correct, recordsCount);
    }
        
}
