import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;


public class MyCrawler extends WebCrawler{


    private final static Pattern FILTERS = Pattern.compile(".*");
    CrawlStat localStat;
    
    @Override
    public void onStart() {

 		int id = getMyId();
		try {
			localStat.localcsvFetch = new FileWriter("/Users/ameyruikar/CrawlCSV/fetch_NewsSite_"+String.valueOf(id)+".csv");
			localStat.localcsvVisit = new FileWriter("/Users/ameyruikar/CrawlCSV/visit_NewsSite_"+String.valueOf(id)+".csv");
			localStat.localcsvUrls = new FileWriter("/Users/ameyruikar/CrawlCSV/urls_NewsSite_"+String.valueOf(id)+".csv");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    public MyCrawler(){	
    	localStat = new CrawlStat();
    	localStat.sizeArray = new long[5];
    	
    	for(int i=0; i<5; i++){
    		localStat.sizeArray[i] = 0;
    	}
    	
    	localStat.statusCodes = new HashMap<Integer, Integer>();
    	localStat.fileType = new HashMap<String, Integer>();
    	
    }
    
    
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        //String href = url.getURL();
        
        String anchor = "http://www.usatoday.com/";
        
        //urls_newsites.csv -> [url, {OK, N_OK} ] 
        
        
    	if((href.startsWith(anchor))){
            //System.out.println("\t"+url.getURL() +"->"+ String.valueOf(href.startsWith(anchor)));
            localStat.inctotalLinksIn();
           UniqueMap.uniqueUrls.putIfAbsent(url.getURL(), new Integer(1));
           
           if(UniqueMap.uniqueUrls.size()%500 == 0){
        	   System.out.print("\rIn links: "+ UniqueMap.uniqueUrls.size());
           }
            
        	try {
    			localStat.localcsvUrls.append(url.getURL()+",OK\n");
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            
    	}
    	else{
    		//links pointing outside the anchor
    		localStat.inctotalLinksOut();
    		UniqueMap.uniqueUrlsOut.putIfAbsent(url.getURL(), new Integer(1));
            
        	try {
    			localStat.localcsvUrls.append(url.getURL()+",N_OK\n");
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	/*
        return FILTERS.matcher(href).matches()
               && href.startsWith("http://www.viterbi.usc.edu/");
               */
    	return href.startsWith(anchor);
    }
    
    @Override
    public void visit(Page page) {
    	try{
        	
            String url = page.getWebURL().getURL();
            //System.out.println("\nURL: " + url);
            //System.out.println("StatusCode: " + page.getStatusCode()+"\n");
            //System.out.println(page.getContentType());
            
            // visit_newsite.csv
            // successful downloads -> [url, size, outgoing, content/type]
            localStat.incfetchSuccess();
            

            

            if(page.getContentType().contains("text/html")){
                if (page.getParseData() instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                    String text = htmlParseData.getText();
                    String html = htmlParseData.getHtml(); 
                    Set<WebURL> links = htmlParseData.getOutgoingUrls();
                    
                    //System.out.println("Context/type: "+ page.getContentType());
                    /*
                    System.out.println("Text length: " + text.length());
                    System.out.println("Html length: " + html.length());
                    */
                    //System.out.println("Number of outgoing links: " + links.size()+"\n");
                    bucketSize(html.length());
                    IncFileType(page.getContentType());
                    
                	try {
            			localStat.localcsvVisit.append(url.replace(",", "-")+","+html.length() +","+ links.size()+","+page.getContentType()+"\n");
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
                    
                    
                }
            }
            else {
            	
            	// for images:
            	String content = page.getContentType();
            	if(content.equals("image/jpeg") ||  content.equals("image/png") || content.equals("image/tiff") || content.equals("image/gif")){
                	if (page.getParseData() instanceof BinaryParseData) {
                		BinaryParseData imgData = (BinaryParseData) page.getParseData();;
                		String html = imgData.getHtml(); 
                        Set<WebURL> links = imgData.getOutgoingUrls();
                        
                        //System.out.println("Context/type: "+ page.getContentType());
                        /*
                        System.out.println("Image size: " +  html.length());
                        System.out.println("Number of outgoing links: " + links.size()+"\n");
                        */
                        bucketSize(html.length());
                        IncFileType(page.getContentType());
                        
                        
                    	try {
                			localStat.localcsvVisit.append(url.replace(",", "-")+","+html.length() +","+ links.size()+","+page.getContentType()+"\n");
                		} catch (IOException e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		}
                        
                    }	
            	}
            	else{
            		
            		//doc
            		if(page.getContentType().equals("application/msword")){
                    	if (page.getParseData() instanceof BinaryParseData) {
                    		BinaryParseData imgData = (BinaryParseData) page.getParseData();;
                    		String html = imgData.getHtml(); 
                            Set<WebURL> links = imgData.getOutgoingUrls();
                            
                            //System.out.println("Context/type: "+ page.getContentType());
                            /*
                            System.out.println("Doc size: " +  html.length());
                            System.out.println("Number of outgoing links: " + links.size()+"\n");
                            */
                            bucketSize(html.length());
                            IncFileType(page.getContentType());
                            
                        	try {
                    			localStat.localcsvVisit.append(url.replace(",", "-")+","+html.length() +","+ links.size()+","+page.getContentType()+"\n");
                    		} catch (IOException e) {
                    			// TODO Auto-generated catch block
                    			e.printStackTrace();
                    		}
                            
                        }
            			
            		}
            		//pdf
            		if(page.getContentType().equals("application/pdf")){
                    	if (page.getParseData() instanceof BinaryParseData) {
                    		BinaryParseData imgData = (BinaryParseData) page.getParseData();;
                    		String html = imgData.getHtml(); 
                            Set<WebURL> links = imgData.getOutgoingUrls();
                            
                           // System.out.println("Context/type: "+ page.getContentType());
                            /*
                            System.out.println("PDF size: " +  html.length());
                            System.out.println("Number of outgoing links: " + links.size()+"\n");
                            */
                            bucketSize(html.length());
                            IncFileType(page.getContentType());
                            
                        	try {
                    			localStat.localcsvVisit.append(url.replace(",", "-")+","+html.length() +","+ links.size()+","+page.getContentType()+"\n");
                    		} catch (IOException e) {
                    			// TODO Auto-generated catch block
                    			e.printStackTrace();
                    		}
                            
                        }

            		}
            		
            	}
           }
    
    	}
    	catch(Exception e){
    		System.out.println(e.toString());
    	}

   }
    /*
    @Override
    protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType, String description){
    	
    	//System.out.print("Unexpected Status code -> ");
    	
    	localStat.incfetchAttempt();
    	localStat.aborted++;
    	
    	
    }
	*/
    
    @Override
    protected void onContentFetchError(WebURL webUrl) {
        //logger.warn("Can't fetch content of: {}", webUrl.getURL());
        // Do nothing by default (except basic logging)
        // Sub-classed can override this to add their custom functionality
    	//System.out.println("fetch Error: "+ webUrl.getURL());
    	System.out.println("********************************************************** -> "+ webUrl.getURL());
    	localStat.incfetchAttempt();
    	localStat.aborted++;
      }
    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {

    	//System.out.println(webUrl.getURL() +" -> "+ statusCode +" depth ->"+webUrl.getDepth());
    	UniqueMap.count++;
    	if(UniqueMap.count%200 == 0){
        	System.out.print("\r Approx Crawled: " + UniqueMap.count);
    	}

    	localStat.incfetchAttempt();
    	
    	try {
			localStat.localcsvFetch.append(webUrl.getURL().replace(",","-")+","+String.valueOf(statusCode)+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//failed aborted or what?
    	if( statusCode >= 300 && statusCode < 400){
    		localStat.aborted++;
    	}
    	else{
    		if(statusCode >= 400){
    			localStat.failed++;
    		}
    	}
    	
    	
    	Integer key = new Integer(statusCode);
    	// which code did i get?
    	if(localStat.statusCodes.get(key) == null){
    		localStat.statusCodes.put(key, new Integer(1));
    	}
    	else{
    		Integer prevCount = localStat.statusCodes.get(key);
    		localStat.statusCodes.put(key, ++prevCount);
    		
    	}
    	
    	
      }
    
    @Override
    public Object getMyLocalData() {
      return localStat;
    }

    @Override
    public void onBeforeExit() {
      try {
		localStat.localcsvFetch.close();
		localStat.localcsvVisit.close();
		localStat.localcsvUrls.close();
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      dumpMyData();
    }
    
    
    public void dumpMyData() {
        int id = getMyId();
        
        System.out.println("\nThread ID: " + id);
        System.out.println("TotalAttempts: " + localStat.getfetchAttempt()+"\n");

     }
    
    public void bucketSize(int size){
    	
    	if(size >= 1048576){
    		localStat.sizeArray[4]++;
    	}
    	else if(size >= 102400){
    		localStat.sizeArray[3]++;
    	}
    	else if(size >= 10240){
    		localStat.sizeArray[2]++;
    	}
    	else if(size >= 1024){
    		localStat.sizeArray[1]++;
    	}
    	else{
    		localStat.sizeArray[0]++;
    	}
    	
    }
    
    public void IncFileType(String type){
    	
    	String subType = type.split("/")[1];
    	
    	if(subType.contains("html")){
    		subType = "html";
    	}
    	
    	if(localStat.fileType.get(subType) == null){
    		localStat.fileType.put(subType, new Integer(1));
    		
    	}
    	else{
    		Integer prev = localStat.fileType.get(subType);
    		localStat.fileType.put(subType, ++prev);
    		
    	}
    	
    	
    }
    
}
