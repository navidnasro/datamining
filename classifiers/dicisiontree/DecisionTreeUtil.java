package datamining.classifiers.dicisiontree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Navid
 */
public class DecisionTreeUtil 
{
    public static Map<String,Integer> PositivesAndNegatives;
    
    /**
     * 
     * @param length
     * @return 
     */
    public static char[] generateLabels(int length)
    {
        char[] labels = new char[length];
        
        for(int i=0 ; i<length ; i++)
            labels[i] = (char)(65+i);
        
        return labels;
    }
    
    /**
     * 
     * @param featureValues
     * @param root
     * @return 
     */
    public static double gain(Map<String,Map<String,Integer>> featureValues,TreeNode root)
    {
        double entropy = 0.0;
        int featureProbability;
        //number of negatives
        int no;
        //number of positives
        int yes;
        
        if(root == null)
        {
            int rootNo = 0;
            int rootYes = 0;
            
            for(String feature : featureValues.keySet())
            {                
                no = featureValues.get(feature).get("no");
                yes = featureValues.get(feature).get("yes");
                featureProbability = no + yes;
                
                rootNo += no;
                rootYes += yes;
                
                entropy += featureProbability*entropy(yes,no);
            }
            
            return entropy(rootYes,rootNo)-entropy;
        }
        
        return 0.0;
    }
    
    /**
     * 
     * @param trainSet
     * @param feature1
     * @param value1
     * @param feature2
     * @param value2
     * @return 
     */
    public static double calculateProbability(List<String> trainSet ,int feature1 ,String value1 ,int feature2 ,String value2)
    {
        PositivesAndNegatives = new HashMap<>();
        
        int sv=0 ,s=0;
        int positive=0 , negative=0;
        
        //for each record
        for(String record : trainSet)
        {
            String[] features = record.split(",");
            
            if(features[feature1].equalsIgnoreCase(value1))
            {
                s++;
                if(features[feature2].equalsIgnoreCase(value2))
                {
                    sv++;
                    
                    if(features[0].contains("no"))
                        negative++;
                    else
                        positive++;
                }
            }
        }
        
        PositivesAndNegatives.put("yes", positive);
        PositivesAndNegatives.put("no", negative);
        
        return (sv/s)*entropy(positive, negative);
    }
    
    /**
     * 
     * @param yes
     * @param no
     * @return 
     */
    public static double entropy(int yes,int no)
    {
        if(yes==0 || no==0)
            return 0;
        
        else if((yes-no)==0)
            return 1;
        
        else
            return -1*((yes*log2(yes))+(no*log2(no)));
    }
    
    /**
     * 
     * @param num
     * @return 
     */
    private static double log2(int num)
    {
        return Math.log(num) / Math.log(2);
    }
    
    /**
     * 
     * @param correct
     * @param totalRecords 
     */
    public static void accuracy(int correct,int totalRecords)
    {
        double accuracy;
        accuracy = ((double)correct/(double)totalRecords)*100;
        
        System.out.printf("%.2f\n",accuracy);
    }    
    
    /**
     * 
     * @param featureValues
     * @return 
     */
    public static char findMax(Map<Character,Double> featureValues)
    {
        double max = 0.0;
        char columnLabel = 'A';
        
        for(char label : featureValues.keySet())
        {
            if(featureValues.get(label) > max)
            {
                max = featureValues.get(label);
                columnLabel = label;
            }
        }
        
        return columnLabel;
    }
}
