import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.text.html.HTMLDocument.Iterator;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;




public class Controller {
	
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "/Users/ameyruikar/CrawledData";
        int numberOfCrawlers = 4;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(16);
        config.setMaxPagesToFetch(10000);
        config.setIncludeBinaryContentInCrawling(true);
        config.setFollowRedirects(false);
        config.setPolitenessDelay(500);  
        


        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("http://www.usatoday.com/");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
        
        List<Object> crawlersLocalData = controller.getCrawlersLocalData();
        
        long totalAttempt = 0;
        long totalSuccess = 0;
        long totalIn = 0;
        long totalOut = 0;
        long totalAborts = 0;
        long totalFails = 0;
        long[] finalSize = new long[5];
        for(int i=0; i<5; i++){
        	finalSize[i] = 0;
        }
        
        Map<Integer, Integer>	finalCodes = new HashMap<Integer, Integer>();
        Map<String, Integer>	finalFileTypes = new HashMap<String, Integer>();

        for (Object localData : crawlersLocalData) {
          CrawlStat stat = (CrawlStat) localData;
          totalAttempt += stat.getfetchAttempt();
          totalSuccess += stat.getfetchSuccess();
          totalIn += stat.gettotalLinksIn();
          totalOut += stat.gettotalLinksOut();
          
          totalAborts += stat.aborted;
          totalFails += stat.failed;
          
          //inc size counts
          for(int i = 0; i < 5; i++){
        	  finalSize[i] += stat.sizeArray[i];
          }
          System.out.println(stat.statusCodes);
          System.out.println(stat.fileType);
          
          for(Integer i : stat.statusCodes.keySet()){
        	  
        	  if(finalCodes.get(i) == null){
        		  finalCodes.put(i, new Integer(stat.statusCodes.get(i)));
        		  
        	  }
        	  else{
        		  Integer prev = finalCodes.get(i);
        		  prev = prev + stat.statusCodes.get(i);
        		  finalCodes.put(i, prev);
  
        	  }
        	    
          }
          
          for(String key: stat.fileType.keySet()){
        	  
        	  if(finalFileTypes.get(key) == null){
        		  finalFileTypes.put(key, new Integer(stat.fileType.get(key)));
        		  
        	  }
        	  else{
        		  Integer prev = finalFileTypes.get(key);
        		  prev = prev + stat.fileType.get(key);
        		  finalFileTypes.put(key, prev);
        	  }
        	 
          }
        
        }
        
        System.out.println("\n\n\nReports:");
        System.out.println("# fetches attempted: "+totalAttempt);
        System.out.println("# fetches succeeded: "+totalSuccess);
        System.out.println("# Fails: "+totalFails);
        System.out.println("# Aborts: "+totalAborts);
        System.out.println("# Links Inside: "+totalIn);
        System.out.println("# Links Outside: "+totalOut);
        
        System.out.println("\n\nTotal Unique URls: " + (UniqueMap.uniqueUrls.size() + UniqueMap.uniqueUrlsOut.size()));
        System.out.println("# Unimap Urls IN: " + UniqueMap.uniqueUrls.size());
        System.out.println("# Unimap Urls OUT: " + UniqueMap.uniqueUrlsOut.size());
        
        System.out.println("\nStatus Codes:");
        System.out.println(finalCodes + "\n");
        
        
        System.out.println("\nFIle Types:");
        System.out.println(finalFileTypes + "\n");
        
        System.out.println("\nFile Sizes:");
        System.out.println("< 1KB: " + finalSize[0]);
        System.out.println("1KB ~ < 10KB: " + finalSize[1]);
        System.out.println("10KB ~ <100KB: " + finalSize[2]);
        System.out.println("100KB ~ <1MB: " + finalSize[3]);
        System.out.println(">= 1MB: " + finalSize[4]);
        

    }
	

}
