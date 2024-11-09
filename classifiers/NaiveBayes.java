package datamining.classifiers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Navid
 */
public class NaiveBayes 
{
    private final Map<String,Double> classCounts;
    private final Map<String,Double> classProbabilities; //p(yes)
    private final Map<String,Map<String,Double>> likelihood; //p(sunny | yes)
    
    public NaiveBayes()
    {
        classCounts = new HashMap<>();
        likelihood = new HashMap<>();
        classProbabilities = new HashMap<>();
    }
    
    /**
     * 
     * @param trainSet
     * @return 
     */
    public boolean train(List<String> trainSet)
    {
        int totalRecords = trainSet.size();
        //processing train set , line by line
        for(String record : trainSet)
        {
            //attributes
            String[] attributes = record.split(",");
            //class name of this record
            String className = attributes[0];
            
            //if class name doesn't exist in map , add it
            if(!classCounts.containsKey(className))
            {
                classCounts.put(className, 0.0);
                likelihood.put(className, new HashMap<>());
            }
            
            //update class name value in the map
            classCounts.put(className, classCounts.get(className)+1);
            
            //process each record and ignore the class name(optained before)
            for(int i=1 ; i<attributes.length ; i++)
            {
                //if feature doesn't exist in the map , initialize it
                if(!likelihood.get(className).containsKey(attributes[i]))
                    likelihood.get(className).put(attributes[i], 0.0);
                
                //calculating features count
                likelihood.get(className).put(attributes[i], likelihood.get(className).get(attributes[i])+1);
            }
            
            //calculating class probabilities
            classProbabilities.put(className, classCounts.get(className)/totalRecords);
        }
        
        //calculating features probability according to classes
        for(String className : classCounts.keySet())
        {
            double classCount = classCounts.get(className);
            
            if(likelihood.containsKey(className))
                for(String feature : likelihood.get(className).keySet())
                {
                    double featureCount = likelihood.get(className).get(feature);
                    likelihood.get(className).put(feature, featureCount/classCount);
                }
        }
        
        return true;
    }
    
    /**
     * 
     * @param testSet
     */
    public void predict(List<String> testSet)
    {
        //stores record probability according to each class
        Map<String,Double> classPrediction = new HashMap<>();        
        
        int correct = 0;
        int totalRecords = testSet.size();
        
        double max;
        String predictedClass;
                
        for(String record : testSet)
        {
            max = -1.0;
            predictedClass = "";
            //record features
            String[] features = record.split(",");
            
            //for each class
            for(String className : classCounts.keySet())
            {
                double prediction = 1;
                
                //for all features excluding the class
                for(int i=1 ; i<features.length ; i++)
                {
                    try{
                        //calculate the features probabilities
                        prediction *= likelihood.get(className).get(features[i]);
                    }catch(NullPointerException e){
                        prediction *= 0;
                    }
                }
                
                //also consider class probability
                prediction *= classProbabilities.get(className);
                
                //store record probability of processed class
                classPrediction.put(className, prediction);
            }
            
            //finding the maximum predicted value of feature according to classes
            for(String className : classPrediction.keySet())
            {
                double prediction = classPrediction.get(className);

                if(prediction > max)
                {
                    max = prediction;
                    predictedClass = className;
                }
            }
            
            if(predictedClass.equalsIgnoreCase(features[0]))
                correct++;
        }
       
        accuracy(correct,totalRecords);
        
    }
    
    /**
     * 
     * @param correct
     * @param totalRecords 
     */
    private void accuracy(int correct,int totalRecords)
    {
        double accuracy;
        accuracy = ((double)correct/(double)totalRecords)*100;
        
        System.out.printf("%.2f\n",accuracy);
    }
}
