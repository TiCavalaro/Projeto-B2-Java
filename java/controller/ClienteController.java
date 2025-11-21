package controller;

import model.Cliente;
import model.ClienteDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

@WebServlet("/clientes")
public class ClienteController extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        ClienteDAO dao = new ClienteDAO();

        try {
            if (action == null || action.equals("listar")) {
                List<Cliente> lista = dao.listar();
                req.setAttribute("clientes", lista);

                // Captura mensagem de sessão (sucesso/erro)
                String msg = (String) req.getSession().getAttribute("msg");
                if(msg != null) {
                    req.setAttribute("msg", msg);
                    req.getSession().removeAttribute("msg");
                }

                req.getRequestDispatcher("/clientes/list.jsp").forward(req, resp);

            } else if (action.equals("editar")) {
                String idStr = req.getParameter("id");
                Cliente c = null;
                if(idStr != null && !idStr.isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    c = dao.getClienteById(id);
                }
                req.setAttribute("cliente", c);
                req.getRequestDispatcher("/clientes/form.jsp").forward(req, resp);

            } else if (action.equals("deletar")) {
                int id = Integer.parseInt(req.getParameter("id"));
                dao.deletar(id);

                req.getSession().setAttribute("msg", "Cliente excluído com sucesso!");
                resp.sendRedirect("clientes?action=listar");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("msg", "Erro: " + e.getMessage());
            resp.sendRedirect("clientes?action=listar");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Cliente c = new Cliente();
        c.setId(req.getParameter("id") != null && !req.getParameter("id").isEmpty() ?
                Integer.parseInt(req.getParameter("id")) : 0);
        c.setNome(req.getParameter("nome"));
        c.setEmail(req.getParameter("email"));
        c.setSenha(req.getParameter("senha"));
        c.setTelefone(req.getParameter("telefone"));

        ClienteDAO dao = new ClienteDAO();

        try {
            if(c.getId() == 0) {
                dao.salvar(c);
                req.getSession().setAttribute("msg", "Cliente cadastrado com sucesso!");
            } else {
                dao.atualizar(c);
                req.getSession().setAttribute("msg", "Cliente atualizado com sucesso!");
            }
            resp.sendRedirect("clientes?action=listar");
        } catch (Exception e) {
            req.getSession().setAttribute("msg", "Erro: " + e.getMessage());
            resp.sendRedirect("clientes?action=listar");
        }
    }
}
