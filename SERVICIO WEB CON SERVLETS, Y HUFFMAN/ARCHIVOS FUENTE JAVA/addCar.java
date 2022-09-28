import javax.sql.DataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class addCar extends HttpServlet{
    private DataSource pool;
    private Context tablaPool;
    private HttpSession sesion;
    private Gson obJSON;
    private Connection conex;
    private PreparedStatement consult;
    private ResultSet res;
    private PrintWriter salida;
    private BufferedReader lectura;
    private String sql;
    private carrito newCar;
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
    private void procesa(HttpServletRequest request, HttpServletResponse response) 
    {
        sql = "SELECT idArticulo, cantAlmacen FROM articulo WHERE nombre = ?";
        obJSON = new GsonBuilder().registerTypeAdapter(String.class, new formatDateTimeGSON()).create();
        int idArt = -1, cantAlmacen = -1, newCantAlm = -1, idUser = -1;
        boolean validaCommit1 = false, validaCommit2 =  false;
        try {
            salida = response.getWriter();
            sesion = request.getSession();
            lectura = request.getReader();
            newCar = (carrito) obJSON.fromJson(lectura.readLine(), carrito.class);
            lectura.close();
            conex = pool.getConnection();
            conex.setAutoCommit(false);
            try {
                conex.rollback();
            } catch (Exception e) {}
            //QUERY PARA ESTRAER EL IDDE ARTICULO Y LAS EXISTENCIAS DE ALMACEN
            consult = conex.prepareStatement(sql);
            consult.setString(1, newCar.nombreElemtSolictado);
            res = consult.executeQuery();
            if(res.next())
            {
                idArt = res.getInt("idArticulo");
                cantAlmacen = res.getInt("cantAlmacen");
            }
            res.close();
            consult.close();
            //QUERY PARA OBTENER EL ID DEL USUARIO
            sql = "select idUsuario from usuarios where emailUser = ?";
            consult = conex.prepareStatement(sql);
            consult.setString(1, (String) sesion.getAttribute("email"));
            res = consult.executeQuery();
            if(res.next())
                idUser = res.getInt("idUsuario");
            res.close();
            consult.close();
            //INSERTANDO VALORES EN LA TABLA DE CARRITO
            sql = "INSERT INTO carrito_compra (idArticulo, idUsuario, cantCompra) VALUES (?, ?, ?)";
            consult = conex.prepareStatement(sql);
            consult.setInt(1, idArt);
            consult.setInt(2, idUser);
            consult.setInt(3, newCar.precioElemtSolictado);
            if(consult.executeUpdate() != 0)
                validaCommit1 = true;
            consult.close();
            //REALIZANDO UPDATE EN ARTICULO
            sql = "UPDATE articulo SET cantAlmacen = ? WHERE idArticulo = ?";
            newCantAlm = cantAlmacen-newCar.precioElemtSolictado;
            consult = conex.prepareStatement(sql);
            consult.setInt(1, newCantAlm);
            consult.setInt(2, idArt);
            if(consult.executeUpdate() != 0)
                validaCommit2 = true;
            consult.close();
            /*VALIDA EL COMMIT CON LOS BOOLEANOS Y QUE LA RESTA DE ARTICULOS EN 
            ALMACEN CON ARTICULOS SOLICITADOS SEA >= 0*/
            if(validaCommit1 && validaCommit2 && newCantAlm >= 0)
            {
                response.setStatus(200);
                salida.write(obJSON.toJson(new error("ok")));
                conex.commit();
            }
            else if(validaCommit1 && validaCommit2 && newCantAlm < 0)
            {
                response.setStatus(400);
                salida.write(obJSON.toJson(new error("ARTICULOS INSUFICIENTES PARA COMPRA")));
                conex.rollback();
            }
            else
            {
                response.setStatus(400);
                salida.write(obJSON.toJson(new error("ERROR INTERNO")));
                conex.rollback();
            }
            conex.close();
            salida.close();
        } catch (Exception e) {
            response.setStatus(500);
        }
        finally
        {
            try {
                lectura.close();
            } catch (Exception e) {}
            try {
                res.close();
            } catch (Exception e) {}
            try {
                consult.close();
            } catch (Exception e) {}
            try {
                conex.close();
            } catch (Exception e) {}
        }
    }
}