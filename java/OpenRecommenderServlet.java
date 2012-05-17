package org.openrecommender.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openrecommender.OpenRecommender;
import org.w3c.dom.Document;

@WebServlet(urlPatterns = "/OpenRecommenderServlet", loadOnStartup = 1)
public class OpenRecommenderServlet extends HttpServlet {
    
    private final String LOCAL_CACHE = "recommendations.xml";
    private final String FORMAT = "xml";
    private final String ENCODING = "application/xml";
    
    private ServletContext servletContext;
    private Logger log;

    public void init(ServletConfig servletConfig) throws ServletException {
        servletContext = servletConfig.getServletContext();
        log = Logger.getLogger(OpenRecommenderServlet.class.getName());
    }
    
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        BufferedInputStream webToProxyBuf = null;
        BufferedOutputStream proxyToClientBuf = null;
        HttpURLConnection con;
        String recommendationsHTML = "";
        try{
            int statusCode;
            int oneByte;
            String methodName;
            String headerText;
            
            String urlString = request.getRequestURL().toString();
            String queryString = request.getQueryString();
                        
            urlString += queryString==null?"":"?"+queryString;
            URL url = new URL(urlString);
                log.info("Fetching >"+url.toString());

            String filePath = (queryString.indexOf("url=") != -1) ? request.getParameter("url") : LOCAL_CACHE;
            String format = (queryString.indexOf("format=") != -1) ? request.getParameter("format") : FORMAT;
            String encoding = (queryString.indexOf("encoding=") != -1) ? request.getParameter("encoding") : ENCODING;
                
            con =(HttpURLConnection) url.openConnection();
            
            methodName = request.getMethod();
            //con.setRequestProperty("Accept", encoding);
            con.setRequestMethod(methodName);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setFollowRedirects(true); //careful with this as someone could attack with multiple/circular redirects
            con.setUseCaches(true);

            for( Enumeration e = request.getHeaderNames(); e.hasMoreElements();){
                String headerName = e.nextElement().toString();
                con.setRequestProperty(headerName, request.getHeader(headerName));
            }

            con.connect();
            
          //if POST method was used to reach the Proxy, perform Proxy request
            if(methodName.equals("POST")) {
                BufferedInputStream clientToProxyBuf = new BufferedInputStream(request.getInputStream());
                BufferedOutputStream proxyToWebBuf   = new BufferedOutputStream(con.getOutputStream());
                
                while ((oneByte = clientToProxyBuf.read()) != -1) {
                    proxyToWebBuf.write(oneByte);
                }
                
                proxyToWebBuf.flush();
                proxyToWebBuf.close();
                clientToProxyBuf.close();
            }

          //loop through response
            statusCode = con.getResponseCode();
            response.setStatus(statusCode);
            
            for( Iterator i = con.getHeaderFields().entrySet().iterator() ; i.hasNext() ;){
                Map.Entry mapEntry = (Map.Entry)i.next();
                if(mapEntry.getKey()!=null)
                    response.setHeader(mapEntry.getKey().toString(), ((List)mapEntry.getValue()).get(0).toString());
            }
            
          //load into local cache
            webToProxyBuf = new BufferedInputStream(con.getInputStream());
            proxyToClientBuf = new BufferedOutputStream(response.getOutputStream());

            FileWriter fstream = new FileWriter(filePath);
            BufferedWriter fileOut = new BufferedWriter(fstream);
            
            while ((oneByte = webToProxyBuf.read()) != -1) {
                proxyToClientBuf.write(oneByte);
                fileOut.write(oneByte);
            } 
          //DEBUG (output response to screen):  
            proxyToClientBuf.flush();
            
          //OpenRecommender XML parser
            OpenRecommender orec = new OpenRecommender();
            Document doc = orec.load(filePath); //load XML from default location
            recommendationsHTML = orec.toHtmlString(doc);
            
            fileOut.close(); //close the local cache File I/O output stream
            con.disconnect(); //disconnect the HTTP connection
        } catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {            
            proxyToClientBuf.close(); //close the client stream
            webToProxyBuf.close(); //close the HTTP stream            
        }
        
      //output HTML
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Test</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<div>");
            out.println(recommendationsHTML);
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }    
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}