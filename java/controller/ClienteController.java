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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        // --- BLOQUEIA ACESSO SE NÃO ESTIVER LOGADO, EXCETO PARA CADASTRO ---
        if (req.getSession().getAttribute("usuarioLogado") == null
                && (action == null || (!action.equals("editar") && !action.equals("novo")))) {
            resp.sendRedirect("login.jsp");
            return;
        }

        ClienteDAO dao = new ClienteDAO();

        try {
            if (action == null || action.equals("listar")) {
                List<Cliente> lista = dao.listar();
                req.setAttribute("clientes", lista);

                // Mensagem de sucesso/erro da sessão
                String msg = (String) req.getSession().getAttribute("msg");
                if (msg != null) {
                    req.setAttribute("msg", msg);
                    req.getSession().removeAttribute("msg");
                }

                req.getRequestDispatcher("/clientes/list.jsp").forward(req, resp);

            } else if (action.equals("editar") || action.equals("novo")) {
                String idStr = req.getParameter("id");
                Cliente c = null;

                if (idStr != null && !idStr.isEmpty()) {
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Cliente c = new Cliente();
        c.setId(req.getParameter("id") != null && !req.getParameter("id").isEmpty() ?
                Integer.parseInt(req.getParameter("id")) : 0);
        c.setNome(req.getParameter("nome"));
        c.setEmail(req.getParameter("email"));
        c.setSenha(req.getParameter("senha"));
        c.setTelefone(req.getParameter("telefone"));

        // --- SOMENTE BLOQUEIA SE EDITAR CLIENTE EXISTENTE ---
        if (c.getId() != 0 && req.getSession().getAttribute("usuarioLogado") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        ClienteDAO dao = new ClienteDAO();

        try {
            if (c.getId() == 0) {
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
