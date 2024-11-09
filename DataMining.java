package datamining;

import datamining.classifiers.NaiveBayes;
import datamining.classifiers.dicisiontree.DecisionTree;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Navid
 */
public class DataMining {

    public static void main(String[] args) 
    {        
        Scanner scan = new Scanner(System.in);
        
        String source = "./breast-cancer_dataset.csv";
        int trainSize;
        int testSize;
        int choice;
        
        System.out.println("Data set is loaded successfully");
        
        System.out.println("Enter the size of train set : ");
        trainSize = scan.nextInt();
        
        System.out.println("Enter the size of test set : ");
        testSize = scan.nextInt();
        
        DataSet set = new DataSet(source, trainSize, testSize);        
        
        System.out.println("There are some obscure data in your data set.\nwhat do you want to do with them?\n1.remove them\n2.replace them\nyour choice : ");
        choice = scan.nextInt();

        switch (choice) {
            case 1 -> {
                set.removeRecordContaining("?");
                break;
            }
            case 2 -> {
                set.replaceCharInRecords("?");
                break;
            }
            default -> {
                System.out.println("Wrong choice,terminating!");
                System.exit(0);
            }
        }

        set.generateSets();
        
        List<String> trainSet = set.getTrainSet();
        List<String> testSet = set.getTestSet();
        
        System.out.println("Choose the algorithm:\n1.Naive Bayes\n2.Decision Tree\nYour Choice: ");
        choice = scan.nextInt();
        
        switch (choice) {
            case 1 -> {
                NaiveBayes naiveBayes = new NaiveBayes();
                naiveBayes.train(trainSet);
                naiveBayes.predict(testSet);
                break;
            }
            case 2 -> {
                DecisionTree decisionTree = new DecisionTree();
                decisionTree.train(trainSet);
                decisionTree.predict(testSet);
                break;
            }
            default -> {
                System.out.println("Wrong choice,terminating!");
                System.exit(0);
            }
        }
    }
    
}
