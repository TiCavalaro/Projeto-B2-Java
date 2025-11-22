package controller;

import model.Produto;
import model.ProdutoDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if(req.getSession().getAttribute("usuarioLogado") == null){
            resp.sendRedirect("login.jsp");
            return;
        }

        ProdutoDAO dao = new ProdutoDAO();
        try {
            List<Produto> produtos = dao.listar();
            req.setAttribute("produtos", produtos);
        } catch (Exception e) {
            req.setAttribute("msg", "Erro ao carregar produtos: " + e.getMessage());
        }

        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
