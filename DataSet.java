package datamining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Navid
 */
public class DataSet 
{
    private List<String> lines;
    private List<String> testSet;
    private List<String> trainSet;
    
    private String source;
    
    private final double trainSize;
    private final double testSize;
    
    public DataSet(String source,int trainSize,int testSize)
    {
        this.trainSize = (double) trainSize/100;
        this.testSize = (double) testSize/100;

        //checking whether the parameter are valid
        if((this.trainSize+this.testSize) != 1)
            throw new InvalidParameterException("The Sum Of Parameters Must be 100!");
        
        //if parameters are valid
        else
        {
            this.source = source;
            lines = new ArrayList<>();
            readLines();
        }
    }
    
    /**
     * Reading the data set file data
     */
    private void readLines()
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(source)))
        {
            String line;
            while((line = reader.readLine())!= null)
            {
                lines.add(line);
            }
        }catch(Exception e){
            System.out.println("Error in reading from dataset file. message:"+e.getMessage());
        }
    }
    
    /**
     * Generating the test and train sets
     */
    public void generateSets()
    {
        int trainSize;
        
        Collections.shuffle(lines);

        trainSize = (int) (this.trainSize * lines.size());

        trainSet = lines.subList(0, trainSize);
        testSet = lines.subList(trainSize, lines.size());
    }
    
    /**
     * 
     * @param ch 
     */
    public void removeRecordContaining(CharSequence ch)
    {
        List<String> temp = new ArrayList<>();
        
        for(String record : lines)
            if(!record.contains(ch))
                temp.add(record);

        lines = temp;
    }
    
    /**
     * Replaces the desired sequence of characters with most repeated value in the same column
     * @param ch
     */
    public void replaceCharInRecords(CharSequence ch)
    {
        //map to store features count in a column
        Map<String,Integer> featuresCount = new HashMap<>();
        
        //index of where question marks are located in a column. contains row,column value pairs
        List<Integer> questionMarkIndices = new ArrayList<>();
        
        int max = 0;
        int featureCount;
        String featureName = "";
        
        //number of features in a record
        int numberOfFeatures = lines.get(0).split(",").length;

        //processing records column by column
        for(int i=1 ; i<numberOfFeatures ; i++)
        {            
            //foreach record
            for(String record : lines)
            {
                String[] features = record.split(",");
                
                //if feature doesn't exist in the map , initialize it
                if(!featuresCount.containsKey(features[i]))
                    featuresCount.put(features[i], 0);
                
                //update features count
                featuresCount.put(features[i], featuresCount.get(features[i])+1);
                
                if(features[i].contains(ch))
                {
                    //row , which record the question mark is located in a column
                    questionMarkIndices.add(lines.indexOf(record));
                    //column , which column of the record the question mark is located
                    questionMarkIndices.add(i);
                }
            }
            
            //finding the max repetition in the map of features in a column
            for(String feature : featuresCount.keySet())
            {
                featureCount = featuresCount.get(feature);
                
                if(featureCount > max)
                {
                    max = featureCount;
                    featureName = feature;
                }
            }
            
            //clearing the map
            featuresCount.clear();
            
            //replacing question marks with the most repeated feature in current column
            for(int q=0 ; q<questionMarkIndices.size() ; q+=2)
            {
                //index of record in lines list
                int row = questionMarkIndices.get(q);
                //index of feature within the record
                int column = questionMarkIndices.get(q+1);
                
                String[] record = lines.get(row).split(",");
                //chaging question mark to most repeated feature in that column
                record[column] = featureName;
                
                lines.remove(row);
                lines.add(row, Arrays.toString(record));
            }
        }
    }
    
    /**
     * Returning the generated Train set
     * @return 
     */
    public List<String> getTrainSet()
    {
        return trainSet;
    }
    
    /**
     * Returning the generated Test set
     * @return 
     */
    public List<String> getTestSet()
    {
        return testSet;
    }
}