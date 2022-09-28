import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

public class registro extends HttpServlet{
    private Gson objJSON;
    private requestRegister registro;
    private BufferedReader body;
    private PrintWriter salida;
    private Context tablaPool;
    private DataSource pool;
    private Connection conex;
    private PreparedStatement consult;
    private String sql;
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        try {
            tablaPool = new InitialContext();
            pool = (DataSource) tablaPool.lookup("java:comp/env/jdbc/mysql");
        } catch (NamingException e) {}
    }
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
        objJSON = new Gson();
        sql = "insert into usuarios (emailUser, nameUser , passwordUser) values (?, ?, ?)";
        try {
            response.setContentType("application/json");
            salida = response.getWriter();
            body = request.getReader();
            registro = (requestRegister)objJSON.fromJson(body.readLine(), requestRegister.class);
            body.close();
            conex = pool.getConnection();
            conex.setAutoCommit(false);
            try {
                conex.rollback();
            } catch (Exception e) {}
            consult = conex.prepareStatement(sql);
            consult.setString(1, registro.email);
            consult.setString(2, registro.nombre);
            consult.setString(3, registro.password);
            if(consult.executeUpdate() != 0)
            {
                response.setStatus(200);
                salida.write(objJSON.toJson(new error("ok")));
                conex.commit();
            }
            else
            {
                response.setStatus(400);
                salida.write(objJSON.toJson(new error("OPeracion fallida intentar de nuevo")));
                conex.rollback();
            }
            consult.close();
            conex.close();
            salida.close();
        } catch (SQLException e){
            try {
                String error = "";
                if(e.getErrorCode() == 1062)
                {
                    response.setStatus(400);
                    error += "El correo :"+registro.email+" ya existe \n pruebe recuperar contraseña o \n intente registrarse con otra direccion de correo electronico";
                }
                else
                {
                    response.setStatus(500);
                    error += "ERROR INTERNO DE LA APLICACION \n PRUEBE MAS TARDE";
                }
                response.getWriter().write(objJSON.toJson(new error(error)));
            } catch (Exception e1) {
                response.setStatus(500);
            }
        }
        catch (IOException ee){
            response.setStatus(500);
        }
        finally
        {
            try {
                body.close();
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
