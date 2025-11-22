package controller;

import model.Produto;
import model.ProdutoDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if(req.getSession().getAttribute("usuarioLogado") == null){
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            ProdutoDAO dao = new ProdutoDAO();
            List<Produto> produtos = dao.listar();  // Pega todos os produtos do banco
            req.setAttribute("produtos", produtos);
        } catch (Exception e) {
            req.setAttribute("msg", "Erro ao carregar produtos: " + e.getMessage());
        }

        // Redireciona para index.jsp
        RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
        rd.forward(req, resp);
    }
}
