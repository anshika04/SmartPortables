import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.sql.*;

public class AjaxUtility extends HttpServlet {

    private ServletContext context;    


    
    public void init(ServletConfig config) throws ServletException {
        this.context = config.getServletContext();
    }

    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
    {
      try
      {

          HashMap<String, ProductCatalog> products = WfCache.getAllProducts();

          String action = request.getParameter("action");
          String targetId = request.getParameter("id");
          StringBuffer sb = new StringBuffer();

          if (targetId != null) {
            targetId = targetId.trim().toLowerCase();
        } else {
            context.getRequestDispatcher("/error.jsp").forward(request, response);
        }

        boolean namesAdded = false;
        if (action.equals("complete")) {

            // check if user sent empty string
            if (!targetId.equals("")) {

                Iterator it = products.keySet().iterator();

                while (it.hasNext()) {
                    String id = (String) it.next();
                    ProductCatalog product = products.get(id);

                    if (product.getName().toLowerCase().startsWith(targetId)) {

                        sb.append("<product>");
                        sb.append("<productid>" + product.getId() + "</productid>");
                        sb.append("<productname>" + product.getName() + "</productname>");                       
                        sb.append("</product>");
                        namesAdded = true;
                    }
                }
            }

            if (namesAdded) {
                response.setContentType("text/xml");
                response.setHeader("Cache-Control", "no-cache");
                response.getWriter().write("<products>" + sb.toString() + "</products>");
            } else {
                //nothing to show
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }

        if (action.equals("lookup")) {

        //  if(targetId.toLowerCase().startsWith("ph"))
        //  {
        //     response.sendRedirect("phone?id="+targetId); 
        // }
        // else if(targetId.toLowerCase().startsWith("tb"))
        // {
        //     response.sendRedirect("tablet?id="+targetId); 
        // }
        // else if(targetId.toLowerCase().startsWith("lp"))
        // {
        //     response.sendRedirect("laptop?id="+targetId); 
        // }
        // else if(targetId.toLowerCase().startsWith("tv"))
        // {
        //     response.sendRedirect("television?id="+targetId); 
        // }
        // else if(targetId.toLowerCase().startsWith("ac"))
        // {
        //     response.sendRedirect("accessory?id="+targetId); 
        // }

        if(targetId.toLowerCase().startsWith("ph"))
            {
                response.sendRedirect("phone?id="+targetId);

            }
            else if(targetId.toLowerCase().startsWith("sp"))
            {
                response.sendRedirect("speaker?id="+targetId);
                
            }
            else if(targetId.toLowerCase().startsWith("lp"))
            {
                response.sendRedirect("laptop?id="+targetId);
                
            }
            else if(targetId.toLowerCase().startsWith("sm"))
            {
               response.sendRedirect("smartWatch?id="+targetId);
                
            }
            else if(targetId.toLowerCase().startsWith("he"))
            {
                response.sendRedirect("headphone?id="+targetId);
                
            }
            else if(targetId.toLowerCase().startsWith("ex"))
            {
                response.sendRedirect("externalStorage?id="+targetId);
                
            }
            else if(targetId.toLowerCase().startsWith("ac"))
            {
                response.sendRedirect("accessory?id="+targetId);
                
            }          
    }
}
catch(SQLException ex)
{

}

}
}
