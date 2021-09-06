import com.relevantcodes.extentreports.ExtentReports;


public class extentReport {


    String Path=System.getProperty("user.dir");

    String extentReportPath=Path+"/target/out.html";

    ExtentReports er=new ExtentReports(extentReportPath);

}
