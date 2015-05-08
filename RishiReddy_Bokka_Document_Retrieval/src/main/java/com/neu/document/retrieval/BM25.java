package com.neu.document.retrieval;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BM25 {
	public static HashMap<String,LinkedList<Result>> feed=new HashMap();
	LinkedList<Result> resultList=new LinkedList<Result>();
	 public static Map<String, TreeMap<String, Integer>> invertedindex = new TreeMap<String, TreeMap<String, Integer>>();	//total inverted index which is given as input to BM25
	    public Map<String, Integer> tokenCount = new TreeMap<String, Integer>();	//total number of words in a file
	    public static Map<String, TreeMap<String, Integer>> qindex = new TreeMap<String, TreeMap<String, Integer>>(); //total query index in all files
	    public static Map<String, Double> BM25 = new TreeMap<String, Double>();
	    public final Double ri = 0.0;
	    public final Double R = 0.0;
	    public final Double k1 = 1.2;		
	    public final Double k2 = 100.0;
	    public final Double b = 0.75;
	  
	   
	    public LinkedList<Result> rank(String indexfile, String queryfile, String num) throws IOException, ClassNotFoundException {

	    	//reads inverted iindex file and stores in a treemap.
	        int count = Integer.parseInt(num);
	        String cur_dir ="C:\\Users\\user\\Documents\\workspace-sts-3.6.4.RELEASE\\RishiReddy_Bokka_Document_Retrieval";
	        FileInputStream fileIn = new FileInputStream(cur_dir + "\\" + indexfile);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        invertedindex = (TreeMap) in.readObject();
	        System.out.println(invertedindex);
	        if (invertedindex == null) {
	            
	        }
	        tokenCount = (TreeMap) in.readObject();
	        if (tokenCount == null) {
	           
	        }
	        System.out.println(tokenCount);
	        int totalTokenCount = 0;
	        //Calculates total number of tokens in the collection
	        for (Iterator i = tokenCount.entrySet().iterator(); i.hasNext();) {
	            Map.Entry next = (Map.Entry) i.next();
	            totalTokenCount = totalTokenCount + (Integer) next.getValue();
	            
	        }
	        Double avdl = totalTokenCount * 1.0 / tokenCount.size();
	        File qf = new File(cur_dir + "//" + queryfile);
	        BufferedReader bf = new BufferedReader(new FileReader(qf));
	        String querytext;
	        int queryid = 1;
	        while ((querytext = bf.readLine()) != null) {
	            String[] querywords = querytext.split(" ");
	            //Step1: Retrieve all inverted lists corresponding to terms in a query.
	            for (String word1 : querywords) {
	                PorterStemmer ps = new PorterStemmer();
	                String word = ps.stem(word1);
	                //System.out.println("my search word is " + word);
	                word = word.trim();
	                if (!word.equals("") && invertedindex.containsKey(word)) {
	                    
	                    qindex.put(word, invertedindex.get(word));
	                    
	                }
	            }
	            //Calculate BM25 scores for documents in the lists.
	            for (Iterator iterator1 = qindex.entrySet().iterator(); iterator1.hasNext();) {
	                Map.Entry next = (Map.Entry) iterator1.next();
	                TreeMap indexes = (TreeMap) next.getValue();
	                for (Iterator iterator2 = indexes.entrySet().iterator(); iterator2.hasNext();) {
	                    Map.Entry next2 = (Map.Entry) iterator2.next();
	                   
	                    int fi = (Integer) next2.getValue();// frequency of i in the document
	                   
	                    int N = tokenCount.size(); 			//total number of files in corpus
	                    int ni = indexes.size();			//word frequency in a file
	                    Double qfi = 0.0;					//query words frequency in a file
	                    for (int i = 0; i < querywords.length; i++) {
	                        if (querytext.contains(querywords[i])) {
	                            qfi++;
	                        }
	                    }
	                    //Computing K value
	                  //calculates bm score for the given query
	                    Double K = k1 * ((1 - b) + b * (tokenCount.get(next2.getKey()) / avdl));
	                    Double f = (Math.log(((ri + 0.5) / (R - ri + 0.5))
	                            / ((ni - ri + 0.5) / (N - ni - R + ri + 0.5))));
	                    Double f1 = ((k1 + 1) * fi / (K + fi));
	                    Double f2 = ((k2 + 1) * qfi / (k2 + qfi));
	                    Double total = f * f1 * f2;
	                    if (BM25.containsKey((String) next2.getKey())) {								 
	                    Double valueToPut = total + BM25.get((String) next2.getKey());
	                        BM25.put((String) next2.getKey(), valueToPut);	
	                    } else {
	                        BM25.put((String) next2.getKey(), total);
	                    }
	                }
	            }
	            //Sorts the list in desc order to display in such a way that more bm score gets less rank
	            Descending comp = new Descending((TreeMap) BM25);
	            TreeMap<String, Double> list_asc = new TreeMap<String, Double>(comp);
	            list_asc.putAll(BM25);
	            int rank = 1;
	           //display the results in console
	            for (Iterator itr = list_asc.entrySet().iterator(); itr.hasNext() && rank <= count;) {
	                Map.Entry nx = (Map.Entry) itr.next();
	                Double bm25value = (Double) nx.getValue();
	                System.out.println(queryid + " Q0 " + nx.getKey() + " " + rank + " " + bm25value + " RishiReddy");
	               
	                Result r=new Result();
	                r.setQueryId(queryid);
	                r.setFileName((String)nx.getKey());
	                r.setRank(rank);
	                rank++;
	                resultList.add(r);
	                 feed.put(querytext, resultList);          
	                
	            }
	            queryid++;
	            BM25.clear();
	            qindex.clear();
	        }
	        in.close();
	        fileIn.close();
	        bf.close();
	        return resultList;
	    }
	    
	    
	    public class Descending implements Comparator<String> {
	    	
	        Map<String, Double> tmap = new TreeMap<String, Double>();
	        public Descending(TreeMap map) {
	            this.tmap = map;
	        }
	        public Descending() {
	            super();
	        }
	        @Override
	        public int compare(String string1, String string2) {
	            if (tmap.get(string1) >= tmap.get(string2)) {
	                return -1;
	            } else {
	                return 1;
	            }
	        }
	    }

	    public static void main(String[] args) throws IOException, ClassNotFoundException {
	    	InvertedIndex i=new InvertedIndex();
	        BM25 bm25 = new BM25();
	        bm25.rank("\\index.out", "query.txt", "20");
	    }
	

}
