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
import javax.sql.DataSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class captura extends HttpServlet{
    private DataSource pool;
    private Context tablaPool;
    private PrintWriter salida;
    private Gson obJSON;
    private Connection conex;
    private PreparedStatement insertSQL;
    private BufferedReader lectura;
    private articulo newArt;
    private String sql;
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
        sql = "INSERT INTO articulo(nombre, descripcion, precio, cantAlmacen, fechaRegistro, foto) VALUES (?, ?, ?, ?, ?, ?)";
        obJSON = new GsonBuilder().registerTypeAdapter(String.class, new formatDateTimeGSON()).create();
        try {
            salida = response.getWriter();
            lectura = request.getReader();
            newArt = (articulo) obJSON.fromJson(lectura.readLine(), articulo.class);
            lectura.close();
            conex = pool.getConnection();
            conex.setAutoCommit(false);
            try {
                conex.rollback();
            } catch (SQLException e1) {}
            insertSQL = conex.prepareStatement(sql);
            insertSQL.setString(1, newArt.nombre);
            insertSQL.setString(2, newArt.descripcion);
            insertSQL.setFloat(3, newArt.precio);
            insertSQL.setInt(4, newArt.cantidadAlmacen);
            insertSQL.setString(5, newArt.fechaRegistro);
            insertSQL.setString(6, newArt.byteFoto);
            if(insertSQL.executeUpdate() != 0)
            {
                response.setStatus(200);
                salida.write(obJSON.toJson(new error("ok")));
                conex.commit();
            }
            else
            {
                response.setStatus(400);
                salida.write(obJSON.toJson(new error("ERROR INTERNO PRUEBE MAS TARDE")));
                conex.rollback();
            }
            insertSQL.close();
            conex.close();
        } catch (SQLException e){
            try {
                String error = "";
                if(e.getErrorCode() == 1062)
                {
                    response.setStatus(400);
                    error += "El articulo con nombre : "+newArt.nombre+" ya existe \n pruebe ingresando otro nombre";
                }
                else
                {
                    response.setStatus(500);
                    error += "ERROR INTERNO DE LA APLICACION \n PRUEBE MAS TARDE";
                }
                response.getWriter().write(obJSON.toJson(new error(error)));
            } catch (Exception e1) {
                response.setStatus(500);
            }
        }
        catch(IOException e1)
        {
            response.setStatus(500);
        }
        finally
        {
            try {
                salida.close();
            } catch (Exception e) {}
            try {
                lectura.close();
            } catch (Exception e) {}
            try {
                insertSQL.close();
            } catch (Exception e) {}
            try {
                conex.close();
            } catch (Exception e) {}
        }
    }
}
