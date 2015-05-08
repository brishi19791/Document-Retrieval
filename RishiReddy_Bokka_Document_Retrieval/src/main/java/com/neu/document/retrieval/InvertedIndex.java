package com.neu.document.retrieval;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class InvertedIndex {
	
    public static Map<String, TreeMap<String, Integer>> inv_index = new TreeMap<String, TreeMap<String, Integer>>();
    public TreeMap<String, Integer> hashmap_inner = new TreeMap();
    public TreeMap<String, Integer> doc_token_count = new TreeMap();
    public  InvertedIndex() throws FileNotFoundException, IOException, ClassNotFoundException {
// code to iterate over all the files in a given folder
        File folder = new File("C:\\Users\\user\\Documents\\workspace-sts-3.6.4.RELEASE\\RishiReddy_Bokka_Document_Retrieval\\Dataset_Algorithms");
        File[] listOfFiles = folder.listFiles();
//code to load 
        for (File file : listOfFiles) {
            if (file.getName().equalsIgnoreCase(".DS_Store")) {
                continue;
            }
            if (file.isFile()) {
                int file_tokencount = 0;
                System.out.println(file.getName());
//read data from file as a line
                FileReader myfile = new FileReader(file);
                BufferedReader myreader = new BufferedReader(myfile);
                String text = "";
                String line1 = myreader.readLine();
//read the data as a line and add the data to the text line.
                while (line1 != null) {
                    //System.out.println("lineeee" + line1);
                    text =text+" "+line1.toLowerCase();
                    line1 = myreader.readLine();
                }
// tokenize the text from the line.
                StringTokenizer stk = new StringTokenizer(text, ". ?,:=;!@#$%^&*()_+{}-/|\"\'~`");
                int i = 0;
                Set<String> stopWords = new LinkedHashSet<String>();
                BufferedReader br = new BufferedReader(new FileReader("C:/Users/user/Documents/workspace-sts-3.6.4.RELEASE/RishiReddy_Bokka_Document_Retrieval/stopwords.txt"));
                for (String line; (line = br.readLine()) != null;) {
                    stopWords.add(line.toLowerCase().trim());
                }
                br.close();
                //when the data for the line 
                while (stk.hasMoreTokens()) {
                    //code for stopword removal
                    String xyzc = stk.nextToken();
                    if (stopWords.contains(xyzc)) // it's a stop word
                    {
                        //System.out.println("stop word deleted" + xyzc);
                        continue;
                    } else {
                        //count for the total tokens in the file
                        file_tokencount++;
                        doc_token_count.put(file.getName(), file_tokencount);
                        //code for stemming the words
                        PorterStemmer ps = new PorterStemmer();
                        String stem = ps.stem(xyzc);
//checking for previously existing stems
                        if (inv_index.containsKey(stem)) {
                            TreeMap<String, Integer> hashmap_inner_contains = inv_index.get(stem);
                            if (hashmap_inner_contains.containsKey(file.getName())) {
                                int count = hashmap_inner_contains.get(file.getName());
                                int countx = count + 1;
                                hashmap_inner_contains.put(file.getName(), countx);
                                inv_index.put(stem, hashmap_inner_contains);
                                
                           } else {
                                int j = 1;
                                hashmap_inner_contains.put(file.getName(), j);

                            }
//calculate the total token count for the 
                        } else {
                            int k = 1;
                            TreeMap<String, Integer> hashmap_inner_1 = new TreeMap();
                            hashmap_inner_1.put(file.getName(), k);
                            inv_index.put(stem, hashmap_inner_1);

                        }
                    }
                }
            }
        }
        diplay2(
                (TreeMap<String, TreeMap<String, Integer>>) inv_index);
        display3();
        String cur_dir ="C:\\Users\\user\\Documents\\workspace-sts-3.6.4.RELEASE\\RishiReddy_Bokka_Document_Retrieval";
        FileOutputStream fileOut = new FileOutputStream(cur_dir + "\\index.out");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(inv_index);
        out.writeObject(doc_token_count);
       // System.out.println(doc_token_count);
        out.close();
        fileOut.close();
        //BM25 bm25=new BM25();
        //LinkedList<Result> results=bm25.computeRank("index.out", "query.txt", "20");
        //return results;
    }
    public void display3() {
//        System.out.println("hi1");
//        System.out.println(doc_token_count);
//        System.out.println("hi2");
    }
    public void diplay2(TreeMap<String, TreeMap<String, Integer>> inv_index) {
        for (Map.Entry<String, TreeMap<String, Integer>> entrySet : inv_index.entrySet()) {
            String key = entrySet.getKey();
            TreeMap<String, Integer> value = entrySet.getValue();
            for (Map.Entry<String, Integer> entrySet1 : value.entrySet()) {
                String key1 = entrySet1.getKey();
                Integer value1 = entrySet1.getValue();
                //System.out.println("key ******" + key + "****fiename***" + key1 + "*****occurance is ****" + value1);
            }
        }
    }

}
