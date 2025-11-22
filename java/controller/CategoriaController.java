package controller;

import model.Categoria;
import model.CategoriaDAO;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

@WebServlet("/categorias")
public class CategoriaController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (req.getSession().getAttribute("usuarioLogado") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String action = req.getParameter("action");
        CategoriaDAO dao = new CategoriaDAO();

        try {
            if (action == null || action.equals("listar")) {
                List<Categoria> lista = dao.listar();
                req.setAttribute("categorias", lista);

                String msg = (String) req.getSession().getAttribute("msg");
                if (msg != null) {
                    req.setAttribute("msg", msg);
                    req.getSession().removeAttribute("msg");
                }

                req.getRequestDispatcher("/categoria/list.jsp").forward(req, resp);

            } else if (action.equals("editar")) {
                String idStr = req.getParameter("id");
                Categoria c = null;
                if (idStr != null && !idStr.isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    c = dao.getCategoriaById(id);
                }
                req.setAttribute("categoria", c);
                req.getRequestDispatcher("/categoria/form.jsp").forward(req, resp);

            } else if (action.equals("deletar")) {
                int id = Integer.parseInt(req.getParameter("id"));
                dao.deletar(id);
                req.getSession().setAttribute("msg", "Categoria excluída com sucesso!");
                resp.sendRedirect("categorias?action=listar");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("msg", "Erro: " + e.getMessage());
            resp.sendRedirect("categorias?action=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        if (req.getSession().getAttribute("usuarioLogado") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Categoria c = new Categoria();
        c.setId(req.getParameter("id") != null && !req.getParameter("id").isEmpty() ?
                Integer.parseInt(req.getParameter("id")) : 0);
        c.setNome(req.getParameter("nome"));

        CategoriaDAO dao = new CategoriaDAO();

        try {
            if (c.getId() == 0) {
                dao.salvar(c);
                req.getSession().setAttribute("msg", "Categoria cadastrada com sucesso!");
            } else {
                dao.atualizar(c);
                req.getSession().setAttribute("msg", "Categoria atualizada com sucesso!");
            }
            resp.sendRedirect("categorias?action=listar");
        } catch (Exception e) {
            req.getSession().setAttribute("msg", "Erro: " + e.getMessage());
            resp.sendRedirect("categorias?action=listar");
        }
    }

}
