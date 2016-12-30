import java.io.FileWriter;
import java.util.Map;


public class CrawlStat {
	  private long fetchAttempt;
	  private long fetchSuccess;
	  private long totalLinksIn;
	  private long totalLinksOut;
	  public FileWriter localcsvFetch;
	  public FileWriter localcsvVisit;
	  public FileWriter localcsvUrls;
	  public long	aborted;
	  public long 	failed;
	  public long[] sizeArray;
	  public Map<Integer, Integer> statusCodes;
	  public Map<String, Integer>	fileType;
	  
	  
	  public CrawlStat(){
		  
		  fetchAttempt = 0;
		  fetchSuccess = 0;
		  totalLinksOut = 0;
		  totalLinksIn = 0;
		  aborted = 0;
		  failed = 0;
		  localcsvFetch = null;
		  localcsvVisit = null;
		  localcsvUrls = null;
		  sizeArray = null;
		  statusCodes = null;
		  fileType = null;
		  
	  }
	  
	  public void incfetchAttempt() {
		    this.fetchAttempt  = this.fetchAttempt + 1;
	  }

	  public void incfetchSuccess() {
		    this.fetchSuccess  = this.fetchSuccess + 1;
	  }
	  
	  public void inctotalLinksIn() {
		    this.totalLinksIn  = this.totalLinksIn + 1;
	  }
	  
	  public void inctotalLinksOut() {
		    this.totalLinksOut  = this.totalLinksOut + 1;
	  }
	  
	  public long getfetchAttempt() {
		    return fetchAttempt;
	  }
	  
	  public long getfetchSuccess() {
		    return fetchSuccess;
	  }
	  
	  public long gettotalLinksIn() {
		    return totalLinksIn;
	  }
	  
	  public long gettotalLinksOut() {
		    return totalLinksOut;
	  }
	  

}
