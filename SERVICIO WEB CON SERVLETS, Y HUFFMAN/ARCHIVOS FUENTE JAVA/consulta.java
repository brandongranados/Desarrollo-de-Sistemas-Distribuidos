import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

public class consulta extends HttpServlet{
    private DataSource pool;
    private Context tablaPool;
    private PrintWriter salida;
    private Gson obJSON;
    private Connection conex;
    private PreparedStatement consultSQL;
    private ResultSet resultSQL;
    private BufferedReader lectura;
    private ArrayList<articulo> artList;
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
        sql = "SELECT nombre, descripcion, precio, cantAlmacen, fechaRegistro, foto FROM articulo WHERE descripcion LIKE ?";
        obJSON = new GsonBuilder().registerTypeAdapter(String.class, new formatDateTimeGSON()).create();
        artList = new ArrayList<articulo>();
        try {
            salida = response.getWriter();
            lectura = request.getReader();
            String coincidencia = "%"+(String)obJSON.fromJson(lectura.readLine(), String.class)+"%";
            lectura.close();
            conex = pool.getConnection();
            consultSQL = conex.prepareStatement(sql);
            consultSQL.setString(1, coincidencia);
            resultSQL = consultSQL.executeQuery();
            while(resultSQL.next())
            {
                articulo art = new articulo();
                art.nombre = resultSQL.getString("nombre");
                art.descripcion = resultSQL.getString("descripcion");
                art.precio = resultSQL.getFloat("precio");
                art.cantidadAlmacen = resultSQL.getInt("cantAlmacen");
                art.fechaRegistro = resultSQL.getString("fechaRegistro");
                art.byteFoto = resultSQL.getString("foto");
                artList.add(art);
            }
            resultSQL.close();
            consultSQL.close();
            conex.close();
            if(artList.size() != 0)
            {
                response.setStatus(200);
                salida.write(obJSON.toJson(artList));
            }
            else
            {
                response.setStatus(400);
                salida.write(obJSON.toJson(new error("NO EXISTEN COINCIDENCIAS")));
            }
            salida.close();
        } catch (SQLException e) {
            response.setStatus(500);
        } catch (IOException e1) {
            response.setStatus(400);
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
                resultSQL.close();
            } catch (Exception e) {}
            try {
                consultSQL.close();
            } catch (Exception e) {}
            try {
                conex.close();
            } catch (Exception e) {}
        }
    }
}
