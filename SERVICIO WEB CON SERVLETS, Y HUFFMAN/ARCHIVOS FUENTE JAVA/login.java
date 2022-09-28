import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;

public class login extends HttpServlet{
    private HttpSession sesion;
    private DataSource pool;
    private Context tablaPool;
    private PrintWriter salida;
    private Gson obJSON;
    private requestInicioUser user;
    private Connection conex;
    private PreparedStatement consult;
    private ResultSet res;
    private BufferedReader lectura;
    private String sql, resPassword, resNombre;
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        procesa(request, response);
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        procesa(request, response);
    }
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        try {
            tablaPool = new InitialContext();
            pool = (DataSource) tablaPool.lookup("java:comp/env/jdbc/mysql");
        } catch (NamingException e) {}
    }
    public void procesa(HttpServletRequest request, HttpServletResponse response)
    {
        sql = "select nameUser, passwordUser from usuarios where emailUser = ?";
        resNombre = "";
        resPassword = "";
        obJSON = new Gson();
        try {
            response.setContentType("application/json");
            sesion = request.getSession(false);
            salida = response.getWriter();
            lectura = request.getReader();
            user = (requestInicioUser)obJSON.fromJson(lectura.readLine(), requestInicioUser.class);
            lectura.close();
            conex = pool.getConnection();
            consult = conex.prepareStatement(sql);
            consult.setString(1, user.email);
            res = consult.executeQuery();
            if(res.next())
            {
                resNombre += res.getString("nameUser");
                resPassword += res.getString("passwordUser");
            }
            res.close();
            consult.close();
            conex.close();
            if(!resNombre.equals("") && resPassword.equals(user.password))
            {
                sesion.setAttribute("email", user.email);
                sesion.setAttribute("nombre", resNombre);
                response.setStatus(200);
                salida.print(obJSON.toJson(new error("ok")));
            }
            else if(!resNombre.equals("") && !resPassword.equals(user.password))
            {
                salida.print(obJSON.toJson(new error("LA CONTRASEÑA NO COINCIDE")));
                response.setStatus(400);
            }
            else
            {
                salida.print(obJSON.toJson(new error("EL USUARIO NO EXISTE")));
                response.setStatus(400);
            }
            salida.close();
        } catch (Exception e) {}
        finally
        {
            try {
                res.close();
            } catch (Exception e) {}
            try {
                consult.close();
            } catch (Exception e) {}
            try {
                conex.close();
            } catch (Exception e) {}
            try {
                salida.close();
            } catch (Exception e) {}
        }
    }
}
