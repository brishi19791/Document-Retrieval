package com.neu.document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.neu.document.retrieval.BM25;
import com.neu.document.retrieval.InvertedIndex;
import com.neu.document.retrieval.Result;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		
		return "documentSearch";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(Model model,@RequestParam String search) {
		System.out.println("Given Search String"+ search);
		String s=search.toLowerCase();
		try{
			String cur_dir = System.getProperty("user.dir");	
			System.out.println("Directory"+cur_dir);
		File file=new File("C:/Users/user/Documents/workspace-sts-3.6.4.RELEASE/RishiReddy_Bokka_Document_Retrieval" +"\\query.txt");
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(s);
		bw.close();
		
		
		InvertedIndex i=new InvertedIndex();
		
		if(BM25.feed.containsKey(s)){
			LinkedList<Result> results=BM25.feed.get(s);
			model.addAttribute("results", results);
			model.addAttribute("query",search);
		}
		else{
			BM25 bm25=new BM25();
		LinkedList<Result> results=bm25.rank("index.out", "query.txt", "20");
		
		System.out.println("RESULLYTTT"+results.size());
		model.addAttribute("results", results);
		model.addAttribute("query",search);
		
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		return "documentSearch";
	}
	@RequestMapping(value = "/viewResults", method = RequestMethod.GET)
	public String view(Model model,@RequestParam String fileName) {
		try{
		String dir="C:\\Users\\user\\Documents\\workspace-sts-3.6.4.RELEASE\\RishiReddy_Bokka_Document_Retrieval\\Dataset_Algorithms";
		 File file = new File(dir+"\\"+fileName);
		 FileReader myfile = new FileReader(file);
         BufferedReader myreader = new BufferedReader(myfile);
         String text = "";
         String line1 = myreader.readLine();

         while (line1 != null) {
             //System.out.println("lineeee" + line1);
             text += line1.toLowerCase();
             line1 = myreader.readLine();
         }
         model.addAttribute("fileName", fileName);
		model.addAttribute("text",text);		
		}
		catch(Exception e)
		{e.printStackTrace();
			}
		
	return "viewResults";
	}
}
