package controller;

import model.Cliente;
import model.ClienteDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Apenas redireciona para a página de login
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        try {
            ClienteDAO dao = new ClienteDAO();
            Cliente c = dao.getClienteByEmail(email);

            if(c != null && c.getSenha().equals(senha)) {
                // Login bem-sucedido
                req.getSession().setAttribute("usuarioLogado", c);
                resp.sendRedirect("index.jsp");
            } else {
                req.setAttribute("erro", "Email ou senha inválidos!");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("erro", "Erro no login: " + e.getMessage());
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
