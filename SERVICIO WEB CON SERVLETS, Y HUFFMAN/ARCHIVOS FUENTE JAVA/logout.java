import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class logout extends HttpServlet{
    private HttpSession sesion;
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        procesa(request, response);
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        procesa(request, response);
    }
    public void procesa(HttpServletRequest request, HttpServletResponse response)
    {
        response.setStatus(200);
        sesion = request.getSession(false);
        sesion.invalidate();
    }
}